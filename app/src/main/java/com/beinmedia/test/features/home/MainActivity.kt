package com.beinmedia.test.features.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.beinmedia.test.App
import com.beinmedia.test.R
import com.beinmedia.test.base.BaseActivity
import com.beinmedia.test.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : BaseActivity() {

    private val viewModel: MainActivityViewModel by viewModels { viewModelFactory }
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        App.currentDisplayedActivity = this
        setUpViews()
    }

    private fun setUpViews() {
        binding.viewPager.adapter = MainPagerAdapter(this)
        TabLayoutMediator(binding.mainTabs, binding.viewPager) { tab, position ->
            if (position == 0)
                tab.text = getString(R.string.news)
            else
                tab.text = getString(R.string.trash)

        }.attach()
    }


}