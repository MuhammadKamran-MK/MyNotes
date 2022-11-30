package com.example.mynotes.UI.Adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.Room.Entities.Notes
import com.example.mynotes.R
import com.example.mynotes.UI.Activities.MainActivity
import com.example.mynotes.UI.Activities.NoteDetailActivity

class NotesAdapter(private val mainActivity: MainActivity, var notes: List<Notes>) :
    RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    @SuppressLint("NotifyDataSetChanged")
    fun searchFiltering(filterList: ArrayList<Notes>) {
        notes = filterList
        notifyDataSetChanged()
    }

    class NotesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val noteTitle: TextView = itemView.findViewById(R.id.noteTitle)
        val noteDesc: TextView = itemView.findViewById(R.id.noteDescription)
        val noteDate: TextView = itemView.findViewById(R.id.noteDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(LayoutInflater.from(mainActivity).inflate(R.layout.recycler_all_notes_item, parent, false))
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.noteTitle.text = notes[position].title
        holder.noteDesc.text = notes[position].description
        holder.noteDate.text = notes[position].date

        holder.itemView.setOnClickListener {
            val intent = Intent(mainActivity, NoteDetailActivity::class.java)
            intent.putExtra("id", notes[position].id)
            intent.putExtra("title", notes[position].title)
            intent.putExtra("description", notes[position].description)
            intent.putExtra("date", notes[position].date)
            mainActivity.startActivity(intent)
        }
    }

    override fun getItemCount() = notes.size
}