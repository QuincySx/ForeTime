package com.smallraw.foretime.app.ui.calendar

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.smallraw.foretime.app.R
import com.smallraw.foretime.app.base.BaseDialogView
import com.smallraw.foretime.app.constant.TaskTypeConsts
import com.smallraw.foretime.app.ui.calendarSetting.CalendarSettingActivity
import com.smallraw.foretime.app.ui.decoration.SpacesItemDecoration
import me.jessyan.autosize.utils.AutoSizeUtils
import java.util.*

class CalendarSettingDialog : BaseDialogView {

    private var mLayoutMoreDetting: View? = null

    private var mCalendarHint: TextView? = null
    private var mCalendarText: TextView? = null
    private var mCalendarArrow: ImageView? = null
    private var mCalendarType: androidx.recyclerview.widget.RecyclerView? = null
    private var mMyAdapter: MyAdapter? = null

    private var isShowType = false

    private var mSelectType = 2

    private var mOnShowTypeListener: OnShowTypeListener? = null

    val selectTypeText: String
        get() = mArrayShowTypeText[mArrayShowType.indexOf(mSelectType)]

    constructor(context: Context) : super(context) {}

    constructor(builder: Builder) : super(builder) {
        mOnShowTypeListener = builder.mOnShowTypeListener
        mSelectType = builder.mSelectType
        setSelect(builder.mSelectType)
    }

    private fun setSelect(selectType: Int) {
        mMyAdapter!!.select(mArrayShowType.indexOf(selectType))
        mCalendarText!!.text = selectTypeText
    }

    override fun initView() {
        mLayoutMoreDetting = findViewById(R.id.layout_more_setting)
        mCalendarArrow = findViewById(R.id.iv_calendar_arrow)
        mCalendarHint = findViewById(R.id.tv_calendar_hint)
        mCalendarText = findViewById(R.id.tv_calendar_text)
        mCalendarType = findViewById(R.id.recycler_calendar_type)
        mCalendarType!!.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        mCalendarType!!.addItemDecoration(SpacesItemDecoration(AutoSizeUtils.dp2px(context, 50f)))

        mMyAdapter = MyAdapter(mArrayShowTypeText, object : MyAdapter.OnItemClickListener {
            override fun onItemClick(view: View, type: String, position: Int) {
                mSelectType = position
                mMyAdapter!!.select(position)
                mCalendarText!!.text = mArrayShowTypeText[position]
                mCalendarHint!!.visibility = View.VISIBLE
                mCalendarText!!.visibility = View.VISIBLE
                mCalendarArrow!!.visibility = View.VISIBLE
                mCalendarType!!.visibility = View.GONE
                isShowType = false
                mOnShowTypeListener?.onChange(this@CalendarSettingDialog, mArrayShowType.get(position))
            }
        })
        mCalendarType!!.adapter = mMyAdapter

        mMyAdapter!!.select(mSelectType)

        mCalendarText!!.setOnClickListener {
            mCalendarHint!!.visibility = View.GONE
            mCalendarText!!.visibility = View.GONE
            mCalendarArrow!!.visibility = View.GONE
            mCalendarType!!.visibility = View.VISIBLE
            isShowType = true
        }

        mLayoutMoreDetting!!.setOnClickListener {
            val intent = Intent(context, CalendarSettingActivity::class.java)
            context.startActivity(intent)
            dismiss()
        }
    }

    override fun setRootView(): View {
        return layoutInflater.inflate(R.layout.dialog_calendar_setting, null, false)
    }

    class Builder(val mContext: Context) : BaseDialogView.Builder(mContext) {
        var mSelectType: Int = 0
        var mOnShowTypeListener: OnShowTypeListener? = null

        fun setSelectType(type: Int): Builder {
            this.mSelectType = type
            return this
        }

        fun setOnChangeSelectListener(onChangeSelectListener: OnShowTypeListener): Builder {
            this.mOnShowTypeListener = onChangeSelectListener
            return this
        }

        override fun build(): CalendarSettingDialog {
            return CalendarSettingDialog(this)
        }
    }

    private class MyAdapter(private val arrayShowType: Array<String>, private val mOnItemClickListener: MyAdapter.OnItemClickListener?) : androidx.recyclerview.widget.RecyclerView.Adapter<ViewHolder>() {
        private var mSelectPosition = -1

        fun select(position: Int) {
            mSelectPosition = position
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
            val view = View.inflate(viewGroup.context, R.layout.item_select_time, null)
            val viewHolder = ViewHolder(view)
            viewHolder.itemView.setOnClickListener { v ->
                mOnItemClickListener?.onItemClick(v, arrayShowType[i], viewHolder.adapterPosition)
            }
            return viewHolder
        }

        override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
            if (viewHolder.adapterPosition == mSelectPosition) {
                viewHolder.mContent.isSelected = true
            } else {
                viewHolder.mContent.isSelected = false
            }

            viewHolder.mContent.text = arrayShowType[i]
        }

        override fun getItemCount(): Int {
            return if (mArrayShowType == null) 0 else mArrayShowTypeText.size
        }

        interface OnItemClickListener {
            fun onItemClick(view: View, type: String, position: Int)
        }
    }

    private class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
        val mContent: TextView = itemView.findViewById(R.id.tv_time_content)
    }

    interface OnShowTypeListener {
        fun onChange(dialog: BaseDialogView, type: Int)
    }

    companion object {
        private val mArrayShowTypeText = arrayOf("仅倒数日", "仅累计日", "全部展示")
        private val mArrayShowType = ArrayList<Int>(3)

        init {
            mArrayShowType.add(TaskTypeConsts.COUNTDOWN_DAY)
            mArrayShowType.add(TaskTypeConsts.CUMULATIVE_DAY)
            mArrayShowType.add(TaskTypeConsts.ALL)
        }
    }
}
