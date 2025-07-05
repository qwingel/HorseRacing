package com.example.kotlinstart.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kotlinstart.R
import kotlin.random.Random

class RaceFragment : Fragment() {
    private lateinit var horse1: ImageView // 1 лашадка
    private lateinit var horse2: ImageView // 2 лашадка
    private lateinit var startButton: Button // кнопка старта
    private var raceFinished = false // флаг на проверку запущена ли гоночка
    private val handler = Handler(Looper.getMainLooper()) // привязываемся к потоку лол

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.race_fragment, container, false)

        horse1 = view.findViewById(R.id.iv_horse1)
        horse2 = view.findViewById(R.id.iv_horse2)
        startButton = view.findViewById(R.id.startButton)

        startButton.setOnClickListener {
            startRace()
        }

        return view
    }

    private fun startRace() {
        startButton.visibility = View.GONE
        raceFinished = false

        // Лашадок в начало
        horse1.translationY = 0f
        horse2.translationY = 0f

        // Побежали кони
        // айди чтобы определять какая лашадка какая
        moveHorse(horse1, 1)
        moveHorse(horse2, 2)
    }

    private fun moveHorse(horse: ImageView, horseNumber: Int) {
        if (raceFinished) return

        // Рандомное время за которое обновляется положение какой то из лашадок
        val randomSpeed = Random.nextLong(25, 50)
        // Расстояние на которое смещаются лошадки за рандомное время
        val distance = 10f

        // Наш запрос в потом с рандомной задержкой
        handler.postDelayed({
            horse.translationY -= distance

            // Добежала ли лашадка
            if (horse.top + horse.translationY <= getFinishLinePosition()) {
                raceFinished = true
                showWinner(horseNumber)
            } else {
                moveHorse(horse, horseNumber)
            }
        }, randomSpeed)
    }

    private fun getFinishLinePosition(): Float {
        val finishLine = view?.findViewById<View>(R.id.finishLine)
        // если finishline = null, то возвращается 0f
        return finishLine?.top?.toFloat() ?: 0f // у финиша
    }

    private fun showWinner(horseNumber: Int) {
        Toast.makeText(context, "Лошадь $horseNumber победила!", Toast.LENGTH_SHORT).show()

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