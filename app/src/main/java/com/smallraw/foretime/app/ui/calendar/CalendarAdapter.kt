package com.smallraw.foretime.app.ui.calendar

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.smallraw.foretime.app.R
import org.jetbrains.annotations.NotNull
import java.util.ArrayList

class CalendarAdapter(@NotNull val mCalendars: List<String>) : RecyclerView.Adapter<CalendarAdapter.CalenderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalenderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar, parent, false)
        return CalenderViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalenderViewHolder, position: Int) {
        val calender = mCalendars[position]
        holder.tvCalendarTitle.text = calender
    }

    override fun getItemCount(): Int {
        return mCalendars.size
    }

    class CalenderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val viewStatus: View
        val tvCalendarTitle: TextView
        val tvTypeData: TextView
        val tvData: TextView
        val tvWeek: TextView
        val tvStatus: TextView
        val tvTimeState: TextView
        val tvTimeNumber: TextView
        val tvTimeUnit: TextView

        init {
            viewStatus = itemView.findViewById(R.id.tvCalendarTitle)
            tvTypeData = itemView.findViewById(R.id.tvTypeData)
            tvCalendarTitle = itemView.findViewById(R.id.tvCalendarTitle)
            tvData = itemView.findViewById(R.id.tvData)
            tvWeek = itemView.findViewById(R.id.tvWeek)
            tvStatus = itemView.findViewById(R.id.tvStatus)
            tvTimeState = itemView.findViewById(R.id.tvTimeState)
            tvTimeUnit = itemView.findViewById(R.id.tvTimeUnit)
            tvTimeNumber = itemView.findViewById(R.id.tvTimeNumber)
        }
    }
}
