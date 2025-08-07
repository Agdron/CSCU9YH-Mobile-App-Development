package com.example.noteapp

import MyViewModel
import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import java.text.SimpleDateFormat
import java.util.GregorianCalendar
import java.util.Locale

/**
 * Fragment for displaying the list of saved notes.
 */
class Page3Fragment : Fragment() {

    // ViewModel to share data between fragments
    lateinit var viewModel: MyViewModel

    // Adapter for the ListView
    private lateinit var adapter: ArrayAdapter<String>

    private var fullNotesList: MutableList<String> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize the ViewModel
        // Initialize the ViewModel
        viewModel = ViewModelProvider(requireActivity())[MyViewModel::class.java]

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_note_list, container, false)

        // UI Components
        val datePicker = view.findViewById<DatePicker>(R.id.datePickerFilter)
        val filterButton = view.findViewById<Button>(R.id.filterButton)
        val searchInput = view.findViewById<EditText>(R.id.searchInput)
        val noteListView = view.findViewById<ListView>(R.id.noteListView)

        // Initialize the adapter with the mutable list
        adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            fullNotesList
        )
        noteListView.adapter = adapter

        // Observe LiveData for notes
        viewModel.notesLiveData.observe(viewLifecycleOwner) { notes ->
            fullNotesList.clear() // Clear the existing list
            fullNotesList.addAll(notes) // Add the updated notes
            adapter.notifyDataSetChanged() // Refresh the ListView
        }

        // Filter notes by selected date
        filterButton.setOnClickListener {
            val day = datePicker.dayOfMonth
            val month = datePicker.month
            val year = datePicker.year
            val selectedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                .format(GregorianCalendar(year, month, day).time)

            // Filter notes that match the selected date
            val filteredNotes = viewModel.getNotes().filter { it.startsWith(selectedDate) }
            adapter.clear()
            adapter.addAll(filteredNotes)
        }

        // Handle long clicks to edit notes
        noteListView.setOnItemLongClickListener { _, _, position, _ ->
            val selectedNote = adapter.getItem(position)

            selectedNote?.let { note ->
                // Show an edit dialog
                AlertDialog.Builder(requireContext())
                    .setTitle("Edit Note")
                    .setMessage("Do you want to edit this note?")
                    .setPositiveButton("Edit") { _, _ ->
                        // Navigate to Page2Fragment for editing
                        navigateToEditNote(note, position)
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
            true
        }

        return view
    }

    /**
     * Navigates to Page2Fragment with the selected note for editing.
     */
    private fun navigateToEditNote(note: String, position: Int) {
        val viewPager = requireActivity().findViewById<androidx.viewpager2.widget.ViewPager2>(R.id.pager)

        // Pass the note data and position to Page2Fragment using a Bundle
        val bundle = Bundle().apply {
            putString("note_text", note)
            putInt("note_position", position)
        }

        // Use the FragmentManager to set arguments on the fragment directly in the adapter
        val adapter = (viewPager.adapter as PageAdapter)
        val page2Fragment = adapter.createFragment(1) as Page2Fragment // Assuming Page2Fragment is at position 1
        page2Fragment.arguments = bundle

        // Navigate to the Page2Fragment (Edit screen) by changing the ViewPager2's current item
        viewPager.currentItem = 1
    }
}