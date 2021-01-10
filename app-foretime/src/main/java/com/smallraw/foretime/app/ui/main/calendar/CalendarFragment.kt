package com.smallraw.foretime.app.ui.main.calendar

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import com.smallraw.foretime.app.R
import com.smallraw.foretime.app.base.BaseDialogView
import com.smallraw.foretime.app.base.BaseFragment
import com.smallraw.foretime.app.base.databinding.DataBindingConfig
import com.smallraw.foretime.app.common.adapter.OnItemClickListener
import com.smallraw.foretime.app.common.widget.dialog.SelectDayTypeDialog
import com.smallraw.foretime.app.databinding.FragmentCalendarBinding
import com.smallraw.foretime.app.entity.Weather
import com.smallraw.foretime.app.repository.database.entity.MemorialDO
import com.smallraw.foretime.app.ui.addTaskDay.AddTaskDayActivity
import com.smallraw.foretime.app.ui.main.MainScreenViewModel
import com.smallraw.foretime.app.ui.main.OnMainCalendarFragmentCallback
import com.smallraw.foretime.app.ui.main.calendar.vm.CalendarVewModel
import com.smallraw.foretime.app.ui.taskInfo.TaskInfoActivity
import com.smallraw.foretime.app.utils.monthDayFormat
import com.smallraw.time.model.BaseCallback
import com.smallraw.time.model.WeatherModel
import com.smallraw.time.utils.getWeekOfDate
import me.jessyan.autosize.utils.AutoSizeUtils
import java.util.*


class CalendarFragment : BaseFragment() {
    companion object {
        @JvmStatic
        fun newInstance(onMainCalendarFragmentCallback: OnMainCalendarFragmentCallback): CalendarFragment {
            val fragment = CalendarFragment()
            fragment.onMainCalendarFragmentCallback = onMainCalendarFragmentCallback
            return fragment
        }
    }

    private lateinit var mBinding: FragmentCalendarBinding
    private var onMainCalendarFragmentCallback: OnMainCalendarFragmentCallback? = null
    private val mCalendarList = ArrayList<MemorialDO>()
    private val mCalendarAdapter = CalendarAdapter(mCalendarList)

    private val mMainScreenViewModel by lazy {
        ViewModelProvider(requireActivity()).get(MainScreenViewModel::class.java)
    }
    private val mCalendarVewModel by lazy {
        ViewModelProvider(this).get(CalendarVewModel::class.java)
    }

    private val mItemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
        override fun getMovementFlags(
            p0: androidx.recyclerview.widget.RecyclerView,
            p1: androidx.recyclerview.widget.RecyclerView.ViewHolder
        ): Int {
            val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or
                    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            val swipeFlags = 0
            return makeMovementFlags(dragFlags, swipeFlags)
        }

        override fun onMove(
            p0: androidx.recyclerview.widget.RecyclerView,
            viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder,
            target: androidx.recyclerview.widget.RecyclerView.ViewHolder
        ): Boolean {
            val fromPosition = viewHolder.adapterPosition
            //拿到当前拖拽到的item的viewHolder
            val toPosition = target.adapterPosition
            if (fromPosition < toPosition) {
                for (i in toPosition..fromPosition) {
                    Collections.swap(mCalendarList, i, i + 1)
                }
            } else {
                for (i in fromPosition..toPosition) {
                    Collections.swap(mCalendarList, i, i - 1)
                }
            }
            mCalendarAdapter.notifyItemMoved(fromPosition, toPosition)

            return true
        }

        override fun onSwiped(p0: androidx.recyclerview.widget.RecyclerView.ViewHolder, p1: Int) {
            if (p1 != ItemTouchHelper.ACTION_STATE_IDLE) {
                p0.itemView.visibility = View.GONE
            }
        }

        override fun clearView(
            recyclerView: androidx.recyclerview.widget.RecyclerView,
            viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder
        ) {
            super.clearView(recyclerView, viewHolder)
            viewHolder.itemView.visibility = View.VISIBLE
        }

    })

    override fun initViewModel() {
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.fragment_calendar)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentCalendarBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        mCalendarVewModel.mActiveTaskListLiveData.observe(viewLifecycleOwner, {
            Log.e("LiveData", "任务卡列表收到变化")
            mCalendarList.clear()
            mCalendarList.addAll(it!!.distinct())
            mCalendarAdapter.notifyDataSetChanged()
        })
        mCalendarVewModel.queryActiveTask(-1, 0)
        mCalendarAdapter.setOnItemClickListener(object : OnItemClickListener {
            override fun onClick(view: View, position: Int) {
                TaskInfoActivity.start(activity, mCalendarList[position].id!!)
            }
        })
        setDateTime()
        initWeatherNow()

        mBinding.ivSetting.setOnClickListener {
            context?.let { context ->
                CalendarSettingDialog.Builder(context)
                    .setOnChangeSelectListener(object : CalendarSettingDialog.OnShowTypeListener {
                        override fun onChange(dialog: BaseDialogView, type: Int) {
                            mCalendarVewModel.queryActiveTask(type, 0)
                            dialog.dismiss()
                        }
                    })
                    .setSelectType(mCalendarVewModel.getDisplay())
                    .atViewAuto(it)
                    .build()
                    .show()
            }
        }
    }

    private fun initView() {
        mBinding.recyclerView.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(activity?.applicationContext)
        mBinding.recyclerView.adapter = mCalendarAdapter
        mBinding.recyclerView.setHasFixedSize(true)
        mCalendarAdapter.notifyDataSetChanged()
//        mItemTouchHelper.attachToRecyclerView(recyclerView)
    }

    override fun onResume() {
        super.onResume()
        showViewAction()
    }

    override fun onPause() {
        hiddenViewAction()
        super.onPause()
    }

    private fun showViewAction() {
        mMainScreenViewModel.mCalendarSuspensionButtonResource.value =
            R.drawable.ic_tab_suspension_add
        onMainCalendarFragmentCallback?.setOnClickListener(View.OnClickListener { v ->
            context?.let { context ->
                SelectDayTypeDialog.Builder(context)
                    .setOnClickCallback { view, index ->
                        if (index == 0) {
                            AddTaskDayActivity.startAdd(context, AddTaskDayActivity.DaysMatter)
                        } else {
                            AddTaskDayActivity.startAdd(context, AddTaskDayActivity.DaysCumulative)
                        }
                        view?.dismiss()
                    }
                    .atViewAuto(v, AutoSizeUtils.dp2px(context, 14F))
                    .setWidth(300.toFloat())
                    .build()
                    .show()
            }
        })
    }

    private fun initWeatherNow() {
        WeatherModel().getWeatherCache(object : BaseCallback<Weather> {
            override fun onSuccess(data: Weather) {
                setWeatherData(data)
            }

            override fun onError(e: Exception) {
                setWeatherData(null)
            }
        })
        WeatherModel().getWeatherNow(object : BaseCallback<Weather> {
            override fun onSuccess(data: Weather) {
                setWeatherData(data)
            }

            override fun onError(e: Exception) {
                setWeatherData(null)
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setWeatherData(data: Weather?) {
        try {
            if (data == null) {
                mBinding.ivWeather.setBackgroundResource(R.drawable.ic_weather_qing)
                mBinding.tvWeather.text = "暂无 · 0°C"
            } else {
                val weatherImage = WeatherModel.getWeatherImage(data.cond_code!!)
                mBinding.ivWeather.setBackgroundResource(weatherImage)
                mBinding.tvWeather.text = "${data.cond_txt} · ${data.tmp}°C"
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setDateTime() {
        mBinding.tvDate.text = monthDayFormat(Date())
        mBinding.tvWeek.text = getWeekOfDate(requireActivity().applicationContext, Date())
    }

    fun hiddenViewAction() {

    }
}
