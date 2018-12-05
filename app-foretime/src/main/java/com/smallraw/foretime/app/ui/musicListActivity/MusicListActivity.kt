package com.smallraw.foretime.app.ui.musicListActivity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.smallraw.foretime.app.App
import com.smallraw.foretime.app.R
import com.smallraw.time.base.BaseTitleBarActivity
import com.smallraw.time.model.thoroughDeleteTaskAll
import kotlinx.android.synthetic.main.activity_music_list.*
import me.jessyan.autosize.AutoSizeConfig
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
        recyclerViewMusic.layoutManager = LinearLayoutManager(this)
        recyclerViewMusic.adapter = mAdapter
    }

    private fun newTitleRightView(): View {
        val inflate = layoutInflater.inflate(R.layout.layout_harvest_number, null)
        inflate.setPadding(0, 0, AutoSizeUtils.dp2px(this, 12F), 0)
        return inflate
    }
}
