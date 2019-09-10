package com.smallraw.foretime.app.common.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import java.util.*

class ViewPagerAdapter(manager: androidx.fragment.app.FragmentManager) : androidx.fragment.app.FragmentPagerAdapter(manager) {

    private val mFragmentList = ArrayList<androidx.fragment.app.Fragment>()

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        return mFragmentList[position]
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

    fun addFragment(fragment: androidx.fragment.app.Fragment) {
        mFragmentList.add(fragment)
    }

    fun clear() {
        mFragmentList.clear()
        notifyDataSetChanged()
    }

}
