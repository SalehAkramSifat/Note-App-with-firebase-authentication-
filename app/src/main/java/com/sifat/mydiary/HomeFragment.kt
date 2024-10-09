package com.sifat.mydiary

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.sifat.mydiary.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeFragment : Fragment(), NoteAdapter.NoteEdit {

    private lateinit var binding: FragmentHomeBinding
    lateinit var database: NoteDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        database = Room.databaseBuilder(requireContext(), NoteDatabase::class.java, "Note-DB").build()

        val adapter = NoteAdapter(this)

        CoroutineScope(Dispatchers.IO).launch {
            val notes: List<Note> = database.getNoteDao().getalldata()
            withContext(Dispatchers.Main) {
                adapter.submitList(notes)
                binding.recyclerView.adapter = adapter
            }
        }

        binding.addNote.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_add_note_Fragment)
        }

        return binding.root
    }

    override fun onNoteEdit(note: Note) {
        val bundle = Bundle().apply {
            putInt("note", note.id)
        }
        findNavController().navigate(R.id.action_homeFragment_to_add_note_Fragment, bundle)
    }
}
