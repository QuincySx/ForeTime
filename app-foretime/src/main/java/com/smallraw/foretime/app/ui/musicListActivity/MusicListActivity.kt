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
package com.smallraw.foretime.app.ui.musicListActivity

import android.os.Bundle
import android.view.View
import com.smallraw.foretime.app.R
import com.smallraw.foretime.app.base.BaseTitleBarActivity
import com.smallraw.foretime.app.base.databinding.DataBindingConfig
import com.smallraw.foretime.app.databinding.ActivityMusicListBinding
import me.jessyan.autosize.utils.AutoSizeUtils

class MusicListActivity : BaseTitleBarActivity() {
    private val mMusicList = ArrayList<MusicBean>()
    private val mAdapter = MusicAdapter(mMusicList)

    override fun initViewModel() {
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_music_list)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitleBarLeftImage(R.drawable.ic_back_black)
        addRightView(newTitleRightView())

        mMusicList.add(MusicBean("", "", "测试", "我是测试啊"))
        mMusicList.add(MusicBean("", "", "测试", "我是测试啊"))

        initRecyclerView()
    }

    private fun initRecyclerView() {
        val dataBinding = getBinding() as ActivityMusicListBinding
        dataBinding.recyclerViewMusic.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        dataBinding.recyclerViewMusic.adapter = mAdapter
        dataBinding.recyclerViewMusic.setHasFixedSize(true)
    }

    private fun newTitleRightView(): View {
        val inflate = layoutInflater.inflate(R.layout.layout_harvest_number, null)
        inflate.setPadding(0, 0, AutoSizeUtils.dp2px(this, 12F), 0)
        return inflate
    }
}
