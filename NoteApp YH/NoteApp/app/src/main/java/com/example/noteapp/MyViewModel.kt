import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

/**
 * ViewModel to share data between fragments and manage app state.
 */

class MyViewModel(application: Application) : AndroidViewModel(application) {


    // SharedPreferences for persistent storage of notes
    private val sharedPreferences = application.getSharedPreferences("notes_prefs", Context.MODE_PRIVATE)

    // Key for accessing notes in SharedPreferences
    private val notesKey = "notes"

    // LiveData for tracking the selected date
    val selectedDate = MutableLiveData<String>()

    // LiveData for tracking notes in memory
    val notesLiveData = MutableLiveData<MutableList<String>>(mutableListOf())

    init {
        // Load notes from SharedPreferences on initialization
        val notesSet = sharedPreferences.getStringSet(notesKey, emptySet())
        notesLiveData.value = notesSet?.toMutableList() ?: mutableListOf()
    }

    /**
     * Save a new note to LiveData and SharedPreferences.
     * @param note The note to be saved.
     */
    fun saveNote(note: String) {
        // Update LiveData
        val currentNotes = notesLiveData.value ?: mutableListOf()
        currentNotes.add(note)
        notesLiveData.value = currentNotes

        // Save to SharedPreferences
        saveToPreferences(currentNotes)
    }
    /**
     * Save the current list of notes to SharedPreferences.
     * @param notes List of notes to be stored.
     */
    fun saveToPreferences(notes: List<String>) {
        val notesSet = notes.toSet()
        sharedPreferences.edit().putStringSet(notesKey, notesSet).apply()
    }

    // Retrieve all notes
    fun getNotes(): List<String> {
        return notesLiveData.value ?: emptyList()
    }

}