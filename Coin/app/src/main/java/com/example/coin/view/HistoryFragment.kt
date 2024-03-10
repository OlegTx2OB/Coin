package com.example.coin.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coin.R
import com.example.coin.databinding.FragmentHistoryBinding
import com.example.coin.recyclerview.CategoryAdapter
import com.example.coin.recyclerview.NoteAdapter
import com.example.coin.recyclerview.SwipeToDeleteCallback
import com.example.coin.repository.room.NoteRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HistoryFragment : Fragment(R.layout.fragment_history) {
    @Inject
    lateinit var mNoteRepository: NoteRepository

    private val mAdapter: NoteAdapter = NoteAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        val binding: FragmentHistoryBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false)

        setViewsPresets(binding)
        setupObservers(binding)
        setupClickListeners(binding)

        return binding.root
    }


    private fun setupClickListeners(
        binding: FragmentHistoryBinding,
    ) {
        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.recyclerView.adapter as NoteAdapter
                adapter.removeAt(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    private fun setupObservers(binding: FragmentHistoryBinding) {

        mNoteRepository.getAllNotes().observe(viewLifecycleOwner) {
            mAdapter.addNotes(it)
        }

        mAdapter.ldDeleteNoteFromRoom.observe(viewLifecycleOwner) {
            CoroutineScope(Dispatchers.IO).launch {
                mNoteRepository.deleteNote(it)
            }
        }
    }

    private fun setViewsPresets(binding: FragmentHistoryBinding) {
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = mAdapter
    }
}