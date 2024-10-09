package com.sifat.mydiary

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.sifat.mydiary.databinding.FragmentAddNoteBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Add_note_Fragment : Fragment() {

    lateinit var binding: FragmentAddNoteBinding
    var showTime: String? = null
    var showDate: String? = null
    lateinit var database: NoteDatabase
    var noteId = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAddNoteBinding.inflate(inflater, container, false)

        arguments?.let {
            noteId = it.getInt("note", 0)
        }

        database = NoteDatabase.getDB(requireContext())

        if (noteId != 0) {
            loadNoteData()
        }

        binding.dateBtn.setOnClickListener {
            pickADate()
        }

        binding.timeBtn.setOnClickListener {
            pickATime()
        }

        binding.done.setOnClickListener {
            saveNote()
        }

        return binding.root
    }

    private fun loadNoteData() {
        CoroutineScope(Dispatchers.IO).launch {
            val notes = database.getNoteDao().loadAllByIds(listOf(noteId))
            withContext(Dispatchers.Main) {
                if (notes.isNotEmpty()) {
                    val note = notes[0]
                    binding.apply {
                        title.setText(note.title)
                        timeBtn.setText(note.time)
                        dateBtn.setText(note.date)
                        mainBox.setText(note.desription)
                    }
                }
            }
        }
    }

    private fun saveNote() {
        val descriptionStr = binding.mainBox.text.toString()
        val titleStr = binding.title.text.toString()
        val timeStr = showTime ?: "00:00"
        val dateStr = showDate ?: "00/00/0000"

        val note = Note(id = noteId, title = titleStr, time = timeStr, date = dateStr, desription = descriptionStr)

        CoroutineScope(Dispatchers.IO).launch {
            if (noteId == 0) {
                database.getNoteDao().insertData(note)
            } else {
                database.getNoteDao().UpdateData(note)
            }

            withContext(Dispatchers.Main) {
                findNavController().navigate(R.id.action_add_note_Fragment_to_homeFragment)
            }
        }
    }

    private fun pickATime() {
        val calendar = java.util.Calendar.getInstance()
        val hour = calendar.get(java.util.Calendar.HOUR_OF_DAY)
        val minute = calendar.get(java.util.Calendar.MINUTE)

        TimePickerDialog(
            requireActivity(),
            { _, hourOfDay, minute ->
                showTime = String.format("%02d:%02d", hourOfDay, minute)
                binding.timeBtn.text = showTime
            },
            hour, minute, false
        ).show()
    }

    private fun pickADate() {
        val calendar = Calendar.getInstance()
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH)
        val year = calendar.get(Calendar.YEAR)

        DatePickerDialog(
            requireActivity(),
            { _, year, month, dayOfMonth ->
                showDate = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                binding.dateBtn.text = showDate
            },
            year, month, day
        ).show()
    }
}
