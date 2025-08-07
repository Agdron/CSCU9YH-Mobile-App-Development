package com.example.noteapp

import MyViewModel
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2

/**
 * Fragment for entering a note.
 */

class Page2Fragment : Fragment() {

    // ViewModel to share data between fragments
    lateinit var viewModel: MyViewModel

    private var selectedPhotoUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize the ViewModel
        viewModel = ViewModelProvider(requireActivity())[MyViewModel::class.java]
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_note_entry, container, false)
        val selectedDateView = view.findViewById<TextView>(R.id.selectedDate)
        val noteInput = view.findViewById<EditText>(R.id.noteInput)
        val saveButton = view.findViewById<Button>(R.id.saveButton)

        val addPhotoButton = view.findViewById<Button>(R.id.addPhotoButton)

        // Add mood selection buttons
        val redButton = view.findViewById<Button>(R.id.redButton)
        val yellowButton = view.findViewById<Button>(R.id.yellowButton)
        val greenButton = view.findViewById<Button>(R.id.greenButton)

        val selectedMoodView = view.findViewById<TextView>(R.id.selectedMoodTrack)

        var selectedMood = "Neutral" // Default mood


        // Set mood based on button clicks
        redButton.setOnClickListener { selectedMood = "Bad"
            selectedMoodView.text = "Mood: $selectedMood"
        }
        yellowButton.setOnClickListener { selectedMood = "Neutral"
            selectedMoodView.text = "Mood: $selectedMood"
        }
        greenButton.setOnClickListener { selectedMood = "Good"
            selectedMoodView.text = "Mood: $selectedMood"
        }

        // Display the selected date
        viewModel.selectedDate.observe(viewLifecycleOwner) { date ->
            selectedDateView.text = "Date: $date"

        }
        // Handle the "Save" button click
        saveButton.setOnClickListener {
            // Combine the date and note text
            val note = "${viewModel.selectedDate.value}: ${noteInput.text} [Mood: $selectedMood]"
            if (noteInput.text.isNotEmpty()) {
                // Save the note in ViewModel and SharedPreferences
                viewModel.saveNote(note)
                noteInput.text.clear()

                // Navigate to the next fragment (Page3Fragment)
                val viewPager = requireActivity().findViewById<ViewPager2>(R.id.pager)
                viewPager.currentItem = 2
            }
        }
        // Add Photo
        addPhotoButton.setOnClickListener {
            pickPhotoFromGallery()
        }


        return view
    }
    private fun pickPhotoFromGallery() {
        val PHOTO_PICKER_REQUEST = 1001
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PHOTO_PICKER_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val PHOTO_PICKER_REQUEST = 1001
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PHOTO_PICKER_REQUEST && resultCode == android.app.Activity.RESULT_OK) {
            selectedPhotoUri = data?.data
        }
    }


}