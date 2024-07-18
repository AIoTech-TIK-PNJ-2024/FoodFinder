package com.aiotechpnj.foodfinder.Items

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aiotechpnj.foodfinder.utils.ARG_POSITION

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        val fragment = ItemsFragment()
        fragment.arguments = Bundle().apply {
            putInt(ARG_POSITION, position + 1)
        }
        return fragment
    }
}