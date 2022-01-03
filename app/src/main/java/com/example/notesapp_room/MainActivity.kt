package com.example.notesapp_room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private val noteDao by lazy { noteDB.getDatabase(this).noteDao() }

    lateinit var submitButton: Button
    lateinit var refreshButton: FloatingActionButton
    lateinit var noteText: EditText

    lateinit var rvMain: RecyclerView
    lateinit var rvAdapter: RecyclerViewAdapter
    lateinit var notes: List<Notes>

    //hold instance of the Notes class , used for update and delete
    var selectedNote: Notes? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notes = arrayListOf()

        submitButton = findViewById(R.id.submitButton)
        refreshButton = findViewById(R.id.refreshButton)
        noteText = findViewById(R.id.noteEditText)
        rvMain = findViewById(R.id.rvMain)

        rvMain.layoutManager = LinearLayoutManager(this)
        rvAdapter = RecyclerViewAdapter(this)


        submitButton.setOnClickListener {

            val note = noteText.text.toString()
            CoroutineScope(IO).launch {
                noteDao.addNote(Notes(0,note))
            }
            noteText.text = null
            Toast.makeText(this, "Note added", Toast.LENGTH_LONG).show()
        }

        refreshButton.setOnClickListener{

            CoroutineScope(IO).launch {
                val data = async {
                    noteDao.getNote()
                }.await()
                if (data.isNotEmpty()){
                    notes = data
                    withContext(Main){
                        rvAdapter.updateData(notes)
                        rvMain.adapter = rvAdapter
                    }
                }else{
                    Log.e("MainActivity", "Unable to get data")
                }
            }
        }

    }
    fun delete(note: Notes){
        CoroutineScope(IO).launch {
            noteDao.deleteNote(note)
        }
        Toast.makeText(this, "Deleted successfully", Toast.LENGTH_LONG).show()
    }

    fun updateFields(){
        noteText.setText(selectedNote!!.note)
    }
}