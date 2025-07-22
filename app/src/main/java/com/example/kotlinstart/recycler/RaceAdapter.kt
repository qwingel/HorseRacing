package com.example.kotlinstart.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinstart.R
import com.example.kotlinstart.database.Race

class RaceAdapter(private val races: List<Race>) :
    RecyclerView.Adapter<RaceAdapter.RaceViewHolder>() {
    class RaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvDate: TextView = itemView.findViewById(R.id.tv_date)
        val tvWinner: TextView = itemView.findViewById(R.id.tv_winner_id)
        val tvTime: TextView = itemView.findViewById(R.id.tv_time)
        val tvDuration: TextView = itemView.findViewById(R.id.tv_duration)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RaceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_race, parent, false)
        return RaceViewHolder(view)
    }

    private fun FormatDate(date: Int): CharSequence {
        return if(date % 100 < 10) {
            "${date / 100}.0${date % 100}"
        } else {
            "${date / 100}.${date % 100}"
        }
    }

    private fun FormatTime(time: Int): CharSequence {
        return if(time % 100 < 10) {
            "${time / 100}:0${time % 100}"
        } else {
            "${time / 100}:${time % 100}"
        }
    }

    override fun onBindViewHolder(holder: RaceViewHolder, position: Int) {
        val race = races[position]
        holder.tvDate.text = FormatDate(race.date)
        holder.tvWinner.text = "${race.winner}"
        holder.tvTime.text = FormatTime(race.time)
        holder.tvDuration.text = "${race.duration}"
    }

    override fun getItemCount() = races.size
}