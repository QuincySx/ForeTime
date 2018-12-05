package com.smallraw.foretime.app.ui.taskInfo

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.smallraw.foretime.app.R
import com.smallraw.foretime.app.repository.db.entity.MemorialEntity

class TaskInfoAdapter(private val mMemorialEntity: MemorialEntity, private val mColors: List<String>?) : RecyclerView.Adapter<TaskInfoAdapter.ViewHolder>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_calendar, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.layoutInfo.setBackgroundColor(Color.parseColor(mColors!![i]))
        viewHolder.tvTaskName.text = mMemorialEntity.name
        viewHolder.tvNote.text = mMemorialEntity.description
    }

    override fun getItemCount(): Int {
        return mColors?.size ?: 0
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var layoutInfo = itemView.findViewById<View>(R.id.layoutInfo)
        var tvShortState = itemView.findViewById<TextView>(R.id.tvShortState)
        var tvTaskName = itemView.findViewById<TextView>(R.id.tvTaskName)
        var tvAlsoState = itemView.findViewById<TextView>(R.id.tvAlsoState)
        var tvNumber = itemView.findViewById<TextView>(R.id.tvNumber)
        var tvUnit = itemView.findViewById<TextView>(R.id.tvUnit)
        var tvNote = itemView.findViewById<TextView>(R.id.tvNote)
    }
}
