package com.example.noteapp

import MyViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import java.text.SimpleDateFormat
import java.util.*

/**
 * Fragment for selecting a date.
 */

class Page1Fragment : Fragment() {

    // ViewModel to share data between fragments
    lateinit var viewModel: MyViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize the ViewModel
        viewModel = ViewModelProvider(requireActivity())[MyViewModel::class.java]

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_date_picker, container, false)
        val datePicker = view.findViewById<DatePicker>(R.id.datePicker)
        val nextButton = view.findViewById<Button>(R.id.nextButton)

        // Handle the "Next" button click
        nextButton.setOnClickListener {
            // Get the selected date from the DatePicker
            val day = datePicker.dayOfMonth
            val month = datePicker.month
            val year = datePicker.year


            // Format the date and store it in ViewModel
            val selectedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(GregorianCalendar(year, month, day).time)
            viewModel.selectedDate.value = selectedDate

            // Navigate to the next fragment (Page2Fragment)
            val viewPager = requireActivity().findViewById<androidx.viewpager2.widget.ViewPager2>(R.id.pager)
            viewPager.currentItem = 1
        }

        return view
    }
}