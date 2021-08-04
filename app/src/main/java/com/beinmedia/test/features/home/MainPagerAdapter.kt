package com.beinmedia.test.features.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.beinmedia.test.features.newsTab.NewsFragment
import com.beinmedia.test.features.trashTab.TrashFragment


class MainPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NewsFragment()
            else -> TrashFragment()
        }
    }

    override fun getItemCount(): Int = 2

}