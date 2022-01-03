package com.example.notesapp_room

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch

class UpdateActivity : AppCompatActivity() {

    private val noteDao by lazy { noteDB.getDatabase(this).noteDao() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        val pk = intent.getIntExtra("pk", 0)
        val note = intent.getStringExtra("note")

        val updateButton = findViewById<Button>(R.id.btUpdate)
        val updateText = findViewById<EditText>(R.id.tvUpdate)
        updateText.hint = note

        updateButton.setOnClickListener {
            if (updateText.text.isNotEmpty()){
                CoroutineScope(IO).launch {
                    val newNote = updateText.text.toString()
                    noteDao.updateNote(Notes(pk,newNote))
                }
            }

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }
    }
}