package com.example.kotlinstart.fragments

import android.icu.util.Calendar
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.kotlinstart.MyApp
import com.example.kotlinstart.R
import com.example.kotlinstart.database.Race
import kotlin.random.Random
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class RaceFragment : Fragment() {
    private lateinit var horse1: ImageView // 1 лашадка
    private lateinit var horse2: ImageView // 2 лашадка
    private lateinit var startButton: Button // кнопка старта
    private lateinit var overlay : View // Оверлей (затемнение)
    private lateinit var winner : TextView // Текст с победителем

    private var raceFinished = false // флаг на проверку запущена ли гоночка
    private val handler = Handler(Looper.getMainLooper()) // привязываемся к потоку лол
    private val db = MyApp.database

    private val calendar = Calendar.getInstance()

    private var startTime: Long = 0
    private var raceDuration: Double = 0.0 // В секундах с сотыми долями

    private val timerRunnable = object : Runnable {
        override fun run() {
            val currentTime = SystemClock.elapsedRealtime()
            raceDuration = (currentTime - startTime) / 1000.0

            handler.postDelayed(this, 10)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.race_fragment, container, false)

        horse1 = view.findViewById(R.id.iv_horse1)
        horse2 = view.findViewById(R.id.iv_horse2)
        startButton = view.findViewById(R.id.startButton)
        overlay = view.findViewById(R.id.overlay)
        winner = view.findViewById(R.id.tv_winner)

        startButton.setOnClickListener {
            startRace()
        }

        return view
    }

    private fun startRace() {
        winner.visibility = View.GONE
        overlay.visibility = View.GONE
        startButton.visibility = View.GONE
        raceFinished = false

        // Лашадок в начало
        horse1.translationY = 0f
        horse2.translationY = 0f

        startTime = SystemClock.elapsedRealtime() // Фиксируем время старта
        handler.post(timerRunnable) // Запускаем таймер

        // Побежали кони
        // айди чтобы определять какая лашадка какая
        moveHorse(horse1, 1)
        moveHorse(horse2, 2)
    }

    private fun finishRace(winner: Int) {
        val today: Int = calendar.get(Calendar.DAY_OF_MONTH)
        val month: Int = calendar.get(Calendar.MONTH) + 1
        val hours = calendar.get(Calendar.HOUR_OF_DAY)
        val minutes = calendar.get(Calendar.MINUTE)

        handler.removeCallbacks(timerRunnable) // Останавливаем таймер

        val race = Race(
            date = today * 100 + month,
            time = hours * 100 + minutes,
            winner = winner,
            duration = raceDuration
        )

        val raceDao = db.raceDao()

        lifecycleScope.launch {
            db.raceDao().insertRace(race) // Безопасный вызов без блока основного потока
        }

        showWinner(winner)
    }

    private fun moveHorse(horse: ImageView, horseNumber: Int) {
        if (raceFinished) return

        // Рандомное время за которое обновляется положение какой то из лашадок
        val randomSpeed = Random.nextLong(25, 50)
        // Расстояние на которое смещаются лошадки за рандомное время
        val distance = 10f

        // Наш запрос в поток с рандомной задержкой
        handler.postDelayed({
            if (!raceFinished) {
                horse.translationY -= distance

                if (horse.top + horse.translationY <= getFinishLinePosition()) {
                    raceFinished = true

                    finishRace(horseNumber)
                } else {
                    moveHorse(horse, horseNumber)
                }
            }
        }, randomSpeed)
    }

    private fun getFinishLinePosition(): Float {
        val finishLine = view?.findViewById<View>(R.id.finishLine)
        // если finishline = null, то возвращается 0 f
        return finishLine?.top?.toFloat() ?: 0f // у финиша
    }

    private fun showWinner(horseNumber: Int) {
        overlay.visibility = View.VISIBLE
        winner.visibility = View.VISIBLE
        winner.text = "Лошадь $horseNumber победила!"

        // Через 2 секунды показываем кнопку старта снова
        handler.postDelayed({
            startButton.visibility = View.VISIBLE
        }, 2000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Очищаем все обработчики при уничтожении View
        handler.removeCallbacksAndMessages(null)
    }
}