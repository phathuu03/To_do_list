package com.example.myapplication.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication.ui.fragment.IntroOneFragment
import com.example.myapplication.ui.fragment.IntroThreeFragment
import com.example.myapplication.ui.fragment.IntroTwoFragment

class ViewPage2IntroAdapter(activity: FragmentActivity?) : FragmentStateAdapter(activity!!) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> IntroOneFragment()
            1 -> IntroTwoFragment()
            2 -> IntroThreeFragment()
            else -> IntroThreeFragment()
        }
    }
}