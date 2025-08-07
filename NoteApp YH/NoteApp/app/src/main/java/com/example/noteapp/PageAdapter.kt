package com.example.noteapp

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PageAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> Page1Fragment() // Date Picker
            1 -> Page2Fragment() // Note Entry
            2 -> Page3Fragment() // Note List
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}