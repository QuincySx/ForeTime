package com.smallraw.foretime.app.ui.musicListActivity

import android.os.Bundle
import android.view.View
import com.smallraw.foretime.app.R
import com.smallraw.foretime.app.base.BaseTitleBarActivity
import com.smallraw.foretime.app.base.databinding.DataBindingConfig
import com.smallraw.foretime.app.databinding.ActivityMusicListBinding
import me.jessyan.autosize.utils.AutoSizeUtils

class MusicListActivity : BaseTitleBarActivity() {
    private val mBinding by lazy {
        ActivityMusicListBinding.inflate(layoutInflater)
    }
    private val mMusicList = ArrayList<MusicBean>()
    private val mAdapter = MusicAdapter(mMusicList)

    override fun initViewModel() {
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_music_list)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        setTitleBarLeftImage(R.drawable.ic_back_black)
        addRightView(newTitleRightView())

        mMusicList.add(MusicBean("","","测试","我是测试啊"))
        mMusicList.add(MusicBean("","","测试","我是测试啊"))

        initRecyclerView()
    }

    private fun initRecyclerView() {
        mBinding.recyclerViewMusic.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        mBinding.recyclerViewMusic.adapter = mAdapter
        mBinding.recyclerViewMusic.setHasFixedSize(true)
    }

    private fun newTitleRightView(): View {
        val inflate = layoutInflater.inflate(R.layout.layout_harvest_number, null)
        inflate.setPadding(0, 0, AutoSizeUtils.dp2px(this, 12F), 0)
        return inflate
    }
}
