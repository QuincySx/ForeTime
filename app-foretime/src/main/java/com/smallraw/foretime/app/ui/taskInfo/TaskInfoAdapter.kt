package com.smallraw.foretime.app.ui.taskInfo

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.smallraw.foretime.app.R
import com.smallraw.foretime.app.repository.db.entity.MemorialDO
import com.smallraw.time.utils.dateFormat
import com.smallraw.time.utils.dateParse
import com.smallraw.time.utils.differentDays
import java.util.*

class TaskInfoAdapter(private var item: MemorialDO, private val mColors: List<String>?) : RecyclerView.Adapter<TaskInfoAdapter.ViewHolder>() {
    private val mCurrentDate = dateParse(dateFormat(Date()))

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_task_info, viewGroup, false)
        return ViewHolder(view)
    }

    fun setData(item: MemorialDO) {
        this.item = item
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        holder.layoutInfo.setBackgroundColor(Color.parseColor(mColors!![i]))
        holder.tvTaskName.text = item.name
        holder.tvNote.text = item.description
        holder.tvUnit.text = "天"

        if (item.type == 0) {
            val days = differentDays(Date(), item.targetTime)
            holder.tvNumber.text = "${Math.abs(days)}"
            holder.tvShortState.text = "根据"
            holder.tvAlsoState.text = "累计"
        } else {
            holder.tvShortState.text = "距离"
            when {
                mCurrentDate < item.targetTime -> {
                    val days = differentDays(Date(), item.targetTime)
                    holder.tvNumber.text = "${Math.abs(days)}"
                    holder.tvAlsoState.text = "还有"
                }
                mCurrentDate == item.targetTime -> {
                    val days = differentDays(Date(), item.targetTime)
                    holder.tvNumber.text = "${Math.abs(days + 1)}"
                    holder.tvAlsoState.text = "活动中"
                }
                else -> {
                    val days = differentDays(item.targetTime, Date())
                    holder.tvNumber.text = "${Math.abs(days)}"
                    holder.tvAlsoState.text = "已过"
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return mColors?.size ?: 0
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var layoutInfo = itemView.findViewById<View>(R.id.layoutInfo)!!
        var tvShortState = itemView.findViewById<TextView>(R.id.tvShortState)!!
        var tvTaskName = itemView.findViewById<TextView>(R.id.tvTaskName)!!
        var tvAlsoState = itemView.findViewById<TextView>(R.id.tvAlsoState)!!
        var tvNumber = itemView.findViewById<TextView>(R.id.tvNumber)!!
        var tvUnit = itemView.findViewById<TextView>(R.id.tvUnit)!!
        var tvNote = itemView.findViewById<TextView>(R.id.tvNote)!!
    }
}
