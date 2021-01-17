/*
 * Copyright 2021 Smallraw Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.smallraw.foretime.app.ui.main.calendar

import android.graphics.Color
import android.util.Log
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.smallraw.foretime.app.R
import com.smallraw.foretime.app.common.adapter.OnItemClickListener
import com.smallraw.foretime.app.repository.database.entity.MemorialDO
import com.smallraw.foretime.app.utils.dateFormat
import com.smallraw.foretime.app.utils.dateParse
import com.smallraw.foretime.app.utils.differentDays
import com.smallraw.time.utils.getWeekOfDate
import java.util.Collections
import java.util.Date
import org.jetbrains.annotations.NotNull

class CalendarAdapter(@NotNull val mCalendars: List<MemorialDO>) :
    RecyclerView.Adapter<CalendarAdapter.CalenderViewHolder>() {
    private val mCurrentDate = dateParse(dateFormat(Date()))
    private var mOnItemClickListener: OnItemClickListener? = null
    var oldPosition: Int? = null
    var preOutPosition: Int? = null

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        mOnItemClickListener = onItemClickListener
    }

    fun getOnItemClickListener(): OnItemClickListener? {
        return mOnItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalenderViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_calendar, parent, false)
        val viewHolder = CalenderViewHolder(view)
        viewHolder.itemView.setOnClickListener {
            getOnItemClickListener()?.onClick(it, viewHolder.layoutPosition)
        }
//        viewHolder.itemView.setOnLongClickListener {
//            val item = mCalendars[viewHolder.adapterPosition]
//            val state = DragData(item, it.width, it.height)
//            ViewCompat.startDragAndDrop(it, null, View.DragShadowBuilder(it), state, 0)
//        }

        viewHolder.itemView.setOnDragListener(object : View.OnDragListener {
            override fun onDrag(v: View?, event: DragEvent?): Boolean {
                if (v == null) {
                    return false
                }
                val dragViewHolder =
                    (parent as androidx.recyclerview.widget.RecyclerView).findContainingViewHolder(v)
                val currentPosition = dragViewHolder?.adapterPosition
                when (event?.action) {
                    DragEvent.ACTION_DRAG_ENTERED -> {
                        Log.e(
                            "=====",
                            """== ACTION_DRAG_ENTERED ==
                                |oldPosition:$oldPosition,
                                |position:$currentPosition,
                                |preOutPosition:$preOutPosition,  ${v.hashCode()}""".trimMargin()
                        )
                        if (null != preOutPosition && preOutPosition == currentPosition) {
                            Log.e("=====", "== preOut ==")
                            preOutPosition = null
                            return true
                        }
                        if (null != currentPosition) {
                            if (null != oldPosition && oldPosition != currentPosition) {
                                Collections.swap(mCalendars, oldPosition!!, currentPosition)
                                notifyItemMoved(oldPosition!!, currentPosition)
                                Log.e(
                                    "=====",
                                    """== Swap List ${null != oldPosition} == oldPosition:$oldPosition
                                        |To Position:$currentPosition""".trimMargin()
                                )
                            }
                            oldPosition = currentPosition
                        }
                    }
                    DragEvent.ACTION_DRAG_EXITED -> {
                        preOutPosition = oldPosition
                        Log.e(
                            "=====",
                            "== ACTION_DRAG_EXITED == oldPosition:$oldPosition  Position:$currentPosition  ${v.hashCode()}"
                        )
                        oldPosition = currentPosition
                        Log.e(
                            "=====",
                            "==============================================================="
                        )
                    }
                    DragEvent.ACTION_DRAG_ENDED -> {
                        oldPosition = null
                        preOutPosition = null
                        Log.e(
                            "=====",
                            "== ACTION_DRAG_ENDED == Position:$currentPosition  ${v.hashCode()}"
                        )
                    }
                }
                return true
            }
        })
        return viewHolder
    }

    override fun onBindViewHolder(holder: CalenderViewHolder, position: Int) {
        val item = mCalendars[position]
        holder.tvCalendarTitle.text = item.name

        val context = holder.itemView.context

        if (item.type == 0) {
            holder.tvWeek.text = getWeekOfDate(context, item.targetTime!!)
        } else {
            holder.tvWeek.text = "至"
        }
        holder.tvData.text = dateFormat(item.targetTime!!)

        holder.tvTimeUnit.text = "天"

        if (item.type == 0) {
            val days = differentDays(Date(), item.targetTime!!)
            holder.tvTimeNumber.text = "${Math.abs(days)}"
            holder.tvTypeData.text = "累计日"
            holder.tvTimeState.text = "累计"
            holder.tvStatus.text = "起"
        } else {
            holder.tvTypeData.text = "倒数日"
            holder.tvStatus.text = "止"
            when {
                mCurrentDate < item.targetTime -> {
                    val days = differentDays(Date(), item.targetTime!!)
                    holder.tvTimeNumber.text = "${Math.abs(days)}"
                    holder.tvTimeState.text = "剩余"
                }
                mCurrentDate == item.targetTime -> {
                    val days = differentDays(Date(), item.targetTime!!)
                    holder.tvTimeNumber.text = "${Math.abs(days + 1)}"
                    holder.tvTimeState.text = "活动中"
                }
                else -> {
                    val days = differentDays(item.targetTime!!, Date())
                    holder.tvTimeNumber.text = "${Math.abs(days)}"
                    holder.tvTimeState.text = "已过"
                }
            }
        }

        holder.viewStatus.setBackgroundColor(Color.parseColor(item.color))
        holder.tvStatus.setTextColor(Color.parseColor(item.color))
    }

    override fun getItemCount(): Int {
        return mCalendars.size
    }

    class CalenderViewHolder(itemView: View) :
        androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val viewStatus: View = itemView.findViewById(R.id.viewStatus)
        val tvCalendarTitle: TextView = itemView.findViewById(R.id.tvCalendarTitle)
        val tvTypeData: TextView = itemView.findViewById(R.id.tvTypeData)
        val tvData: TextView = itemView.findViewById(R.id.tvData)
        val tvWeek: TextView = itemView.findViewById(R.id.tvWeek)
        val tvStatus: TextView = itemView.findViewById(R.id.tvStatus)
        val tvTimeState: TextView = itemView.findViewById(R.id.tvTimeState)
        val tvTimeNumber: TextView = itemView.findViewById(R.id.tvTimeNumber)
        val tvTimeUnit: TextView = itemView.findViewById(R.id.tvTimeUnit)
    }

    inner class DragData(val item: MemorialDO, val width: Int, val height: Int)
}
