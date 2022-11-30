package com.example.mynotes.UI.Activities

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.DialogInterface
import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mynotes.Room.Entities.Notes
import com.example.mynotes.ViewModel.NotesViewModel
import com.example.mynotes.R
import com.example.mynotes.UI.Adapters.NotesAdapter
import com.example.mynotes.databinding.ActivityMainBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding   // Data binding declaration

    private lateinit var dialog: Dialog
    private lateinit var parentTitle: TextInputLayout
    private lateinit var dialogNoteTitle: TextInputEditText
    private lateinit var parentDesc: TextInputLayout
    private lateinit var dialogNoteDesc: TextInputEditText
    private lateinit var btnAddNote: MaterialButton
    private lateinit var closeNoteDialog: ImageView

    private val viewModel: NotesViewModel by viewModels()

    var myNotesList = arrayListOf<Notes>()
    private lateinit var adapter: NotesAdapter

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Data binding initialization
        binding = DataBindingUtil.setContentView(
            this, R.layout.activity_main
        )

        setSupportActionBar(binding.include.toolbar)

        // Set up recycler view
        viewModel.getNotes().observe(this) {

            myNotesList = it as ArrayList<Notes>

            if (it.isEmpty()) {
                binding.noNotes.visibility = View.VISIBLE
                binding.notesRecyclerView.visibility = View.GONE
            } else {
                binding.noNotes.visibility = View.GONE
                binding.notesRecyclerView.visibility = View.VISIBLE
//                binding.notesRecyclerView.layoutManager = LinearLayoutManager(this)

                val staggeredGridLayoutManager =
                    StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
                binding.notesRecyclerView.layoutManager = staggeredGridLayoutManager

                adapter = NotesAdapter(this, it)
                binding.notesRecyclerView.adapter = adapter
            }

        }

        binding.allNoteFloatBtn.setOnClickListener {
            dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog_all_notes)
            dialog.window!!.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
            )
            dialog.setCancelable(false)
            dialog.show()

            parentTitle = dialog.findViewById(R.id.parentTitle)
            dialogNoteTitle = dialog.findViewById(R.id.dialogNoteTitle)
            parentDesc = dialog.findViewById(R.id.parentDesc)
            dialogNoteDesc = dialog.findViewById(R.id.dialogNoteDesc)
            btnAddNote = dialog.findViewById(R.id.btnAddNote)
            closeNoteDialog = dialog.findViewById(R.id.closeNoteDialog)

            closeNoteDialog.setOnClickListener {
                dialog.dismiss()
            }

            btnAddNote.setOnClickListener {
                if (validation(dialogNoteTitle, dialogNoteDesc)) {

                    val title = dialogNoteTitle.text.toString()
                    val desc = dialogNoteDesc.text.toString()

                    // Get system current date
                    val sdf = SimpleDateFormat("EEE, MMM d, yyyy, hh:mm:a")
                    val currentDate = sdf.format(Date()).toString()

                    Log.e("CurrentDate : ", "onViewCreated: $currentDate")

                    val addNotes = Notes(
                        id = null, title = title, description = desc, date = currentDate
                    )

                    viewModel.insertNotes(addNotes)

                    Toast.makeText(this, "Note added successfully!", Toast.LENGTH_SHORT).show()

                    dialog.dismiss()
                }
            }
        }

        // Handle back press button
        onBackPressedDispatcher.addCallback(this /* lifecycle owner */,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    // build alert dialog
                    val dialogBuilder = AlertDialog.Builder(this@MainActivity, R.style.AlertDialogTheme)

                    // set message of alert dialog
                    dialogBuilder.setTitle("Exit")
                        .setMessage("Do you want to close this application ?")
                        // if the dialog is cancelable
                        .setCancelable(false)
                        // positive button text and action
                        .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                            finish()
                        })
                        // negative button text and action
                        .setNegativeButton("No", DialogInterface.OnClickListener { dialog, id ->
                            dialog.cancel()
                        })

                    // create dialog box
                    val alert = dialogBuilder.create()
                    // set title for alert dialog box
                    alert.setTitle("Exit")
                    // show alert dialog
                    alert.show()

                }
            })

    }

    private fun validation(
        textInputEditText: TextInputEditText, textInputEditText2: TextInputEditText
    ): Boolean {

        return if (textInputEditText.toString().trim() == "" || textInputEditText.length() == 0) {
            textInputEditText.error = "Required"
//            parentTitle.helperText = "Required"
//            parentTitle.setHelperTextColor(ColorStateList.valueOf(Color.RED))
            parentTitle.requestFocus()
            false
        } else if (textInputEditText2.toString().trim() == "" || textInputEditText2.length() == 0) {
            textInputEditText2.error = "Field Required"
//            parentDesc.helperText = "Required"
//            parentDesc.setHelperTextColor(ColorStateList.valueOf(Color.RED))
            parentDesc.requestFocus()
            false
        } else {
            true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)

        val item = menu?.findItem(R.id.menuSearch)
        val searchView = item?.actionView as SearchView
        searchView.queryHint = "Search here"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            // If you complete text and press enter this function will show you your search
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            // If you write something this function will suggest you the text with respect to your text
            override fun onQueryTextChange(p0: String?): Boolean {
                NotesFiltering(p0)
                return true
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

    private fun NotesFiltering(p0: String?) {

        val filterList = arrayListOf<Notes>()

        for (i in myNotesList) {

            if (i.title.contains(p0!!) || i.title.lowercase()
                    .contains(p0) || i.description.contains(p0) || i.description.lowercase()
                    .contains(p0)
            ) {
                filterList.add(i)
            }

        }

        adapter.searchFiltering(filterList)

    }

}