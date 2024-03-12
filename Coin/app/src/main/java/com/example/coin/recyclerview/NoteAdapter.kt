package com.example.coin.recyclerview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.example.coin.COLOR_ATTR_EXPENSES_CARD
import com.example.coin.COLOR_ATTR_INCOMES_CARD
import com.example.coin.R
import com.example.coin.data.Note
import com.example.coin.databinding.RvHistoryNoteBinding
import com.example.coin.paintCardViews
import com.example.coin.repository.sharedprefs.spGetCurrencyName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class NoteAdapter : RecyclerView.Adapter<NoteAdapter.NoteHolder>() {

    private val _ldDeleteNoteFromRoom = MutableLiveData<Note>()
    val ldDeleteNoteFromRoom: LiveData<Note> = _ldDeleteNoteFromRoom

    private val noteArray = ArrayList<Note>()

    class NoteHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = RvHistoryNoteBinding.bind(view)

        fun bind(note: Note) = with(binding) {
            val resourceId = binding.root.resources.getIdentifier(
                note.imageName, "drawable", binding.root.context.packageName
            )
            val date = LocalDate.ofEpochDay(note.epochDay!!)

            tvDate.text = "${date.dayOfMonth}.${date.monthValue}.${date.year}"
            imageView.setImageResource(resourceId)
            tvCategoryName.text = note.categoryName
            tvAmount.text = "${note.amount.toString()}${spGetCurrencyName(binding.root.context)}"
            paintCardViews(
                listOf(cardviewColor),
                note.color!!
            )
            paintCardViews(
                listOf(cardviewIncExp),
                if (note.isIncomes!!) COLOR_ATTR_INCOMES_CARD else COLOR_ATTR_EXPENSES_CARD,
                binding.root.context
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.rv_history_note, parent, false)
        return NoteHolder(view)
    }

    override fun onBindViewHolder(holder: NoteHolder, position: Int) {
        holder.bind(noteArray[position])
    }

    override fun getItemCount(): Int {
        return noteArray.size
    }

    fun removeAt(position: Int) {
        _ldDeleteNoteFromRoom.value = noteArray[position]
        noteArray.removeAt(position)
        notifyItemRemoved(position)
    }

    fun addNotes(notes: List<Note>) {
        CoroutineScope(Dispatchers.Main).launch {
            noteArray.clear()
            noteArray.addAll(notes.reversed())
            notifyDataSetChanged()
        }
    }

}