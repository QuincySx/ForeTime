package com.smallraw.foretime.app.ui.musicListActivity

import android.os.Bundle
import android.view.View
import com.smallraw.foretime.app.R
import com.smallraw.foretime.app.base.BaseTitleBarActivity
import kotlinx.android.synthetic.main.activity_music_list.*
import kotlinx.android.synthetic.main.fragment_calendar.*
import me.jessyan.autosize.utils.AutoSizeUtils

class MusicListActivity : BaseTitleBarActivity() {
    private val mMusicList = ArrayList<MusicBean>()
    private val mAdapter = MusicAdapter(mMusicList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_list)
        setTitleBarLeftImage(R.drawable.ic_back_black)
        addRightView(newTitleRightView())

        mMusicList.add(MusicBean("","","测试","我是测试啊"))
        mMusicList.add(MusicBean("","","测试","我是测试啊"))

        initRecyclerView()
    }

    private fun initRecyclerView() {
        recyclerViewMusic.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerViewMusic.adapter = mAdapter
        recyclerView.setHasFixedSize(true)
    }

    private fun newTitleRightView(): View {
        val inflate = layoutInflater.inflate(R.layout.layout_harvest_number, null)
        inflate.setPadding(0, 0, AutoSizeUtils.dp2px(this, 12F), 0)
        return inflate
    }
}
