package com.smallraw.foretime.app.ui.calendar

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.smallraw.foretime.app.R
import com.smallraw.foretime.app.repository.db.entity.MemorialEntity
import com.smallraw.time.utils.dateFormat
import com.smallraw.time.utils.dateParse
import com.smallraw.time.utils.differentDays
import com.smallraw.time.utils.getWeekOfDate
import org.jetbrains.annotations.NotNull
import java.util.*

class CalendarAdapter(@NotNull val mCalendars: List<MemorialEntity>) : RecyclerView.Adapter<CalendarAdapter.CalenderViewHolder>() {
    val mCurrentDate = dateParse(dateFormat(Date()))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalenderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar, parent, false)
        return CalenderViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalenderViewHolder, position: Int) {
        val item = mCalendars[position]
        holder.tvCalendarTitle.text = item.description


        val context = holder.itemView.context;

        if (item.type == 0) {
            holder.tvWeek.text = getWeekOfDate(context, item.beginTime)
        } else {
            holder.tvWeek.text = "至"
        }
        holder.tvData.text = dateFormat(item.beginTime)

        holder.tvTimeUnit.text = "天"

        if (item.type == 0) {
            val days = differentDays(Date(), item.beginTime)
            holder.tvTimeNumber.text = "${Math.abs(days)}"
            holder.tvTypeData.text = "累计日"
            holder.tvTimeState.text = "累计"
            holder.tvStatus.text = "起"
        } else {
            holder.tvTypeData.text = "倒数日"
            holder.tvStatus.text = "止"
            if (mCurrentDate < item.beginTime) {
                val days = differentDays(Date(), item.beginTime)
                holder.tvTimeNumber.text = "${Math.abs(days)}"
                holder.tvTimeState.text = "剩余"
            } else if (mCurrentDate <= item.endTime || (item.endTime == item.beginTime && mCurrentDate == item.beginTime)) {
                val days = differentDays(Date(), item.beginTime)
                holder.tvTimeNumber.text = "${Math.abs(days + 1)}"
                holder.tvTimeState.text = "活动中"
            } else {
                val days = differentDays(item.endTime, Date())
                holder.tvTimeNumber.text = "${Math.abs(days)}"
                holder.tvTimeState.text = "已过"
            }
        }

        holder.viewStatus.setBackgroundColor(Color.parseColor(item.color))
        holder.tvStatus.setTextColor(Color.parseColor(item.color))
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
            viewStatus = itemView.findViewById(R.id.viewStatus)
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