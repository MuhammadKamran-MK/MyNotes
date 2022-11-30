package com.example.mynotes.UI.Activities

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.example.mynotes.Room.Entities.Notes
import com.example.mynotes.ViewModel.NotesViewModel
import com.example.mynotes.R
import com.example.mynotes.databinding.ActivityNoteDetailBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton
import java.util.*

class NoteDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoteDetailBinding
    private val viewModel: NotesViewModel by viewModels()

    private var id: Int? = null

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_note_detail
        )

        setSupportActionBar(binding.include.toolbar)

        intent.extras?.apply {

            id = getInt("id")
            val title = getString("title")
            val desc = getString("description")

            binding.detailTitle.setText(title)
            binding.detailDesc.setText(desc)

            binding.btnUpdateNote.setOnClickListener {

                // Get system current date
                val sdf = SimpleDateFormat("EEE, MMM d, yyyy, hh:mm:a")
                val currentDate = sdf.format(Date()).toString()

                viewModel.updateNotes(
                    Notes(
                        id = id,
                        title = binding.detailTitle.text.toString(),
                        description = binding.detailDesc.text.toString(),
                        date = currentDate
                    )
                )

                Toast.makeText(applicationContext, "Note updated successfully!", Toast.LENGTH_SHORT)
                    .show()

                startActivity(Intent(this@NoteDetailActivity, MainActivity::class.java))
                finish()

            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.delete_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menuDelete) {

            val bottomSheet = BottomSheetDialog(this@NoteDetailActivity, R.style.BottomSheetStyle)
            bottomSheet.setContentView(R.layout.dialog_bottom_sheet)
            bottomSheet.show()

            bottomSheet.findViewById<MaterialButton>(R.id.btnNo)?.setOnClickListener {
                bottomSheet.dismiss()
            }

            bottomSheet.findViewById<MaterialButton>(R.id.btnYes)?.setOnClickListener {

                id?.let { it1 -> viewModel.deleteNotes(it1) }

                bottomSheet.dismiss()

                Toast.makeText(
                    applicationContext, "Note deleted successfully!", Toast.LENGTH_SHORT
                ).show()

                startActivity(Intent(this@NoteDetailActivity, MainActivity::class.java))
                finish()
            }

        }

        return super.onOptionsItemSelected(item)
    }
}