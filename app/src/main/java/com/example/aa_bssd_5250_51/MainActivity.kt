package com.example.aa_bssd_5250_51

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.commit
import com.example.aa_bssd_5250_51.NoteFragment
import com.example.aa_bssd_5250_51.NotesData
import com.google.android.material.button.MaterialButton
import org.json.JSONArray
import java.io.*

class MainActivity : NotesData.NotesDataUpdateListener, AppCompatActivity() {
    override fun updateNotesDependents() {
        createNoteFragments()
    }
    private fun removeExistingNotes(){
        for(noteF in supportFragmentManager.fragments){
            supportFragmentManager.commit {
                remove(noteF)
            }
        }
    }

    private fun createNoteFragments(){
        removeExistingNotes()
        for (i in 0 until NotesData.getNoteList().size){
            supportFragmentManager.commit{
                add(fid, NoteFragment.newInstance(i),  null)
            }
        }
    }
    var noteCount = 0
    private var fid = 0
    override fun onPause() {
        super.onPause()
        writeDataToFile()
    }

    override fun onResume() {
        super.onResume()
        val jsonResult = readDataAsJSON()
        if (jsonResult != null){
            loadJSONNotes(jsonResult)
            createNoteFragments()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NotesData.registerListener(this)

        val addButton = Button(this).apply {
            text = "+"
            id = View.generateViewId()
            setOnClickListener{
                supportFragmentManager.commit {

                    if(noteCount < 10){
                        NotesData.addNote(Note("", "", null, 0))
                        val uniqueID = NotesData.getNoteList().size-1
                        add(fid, NoteFragment.newInstance(uniqueID),
                            null)
                        noteCount ++
                    }
                }
            }
        }
        val redAddButton = MaterialButton(this).apply {
            text = "+"
            id = View.generateViewId()
            setOnClickListener{
                supportFragmentManager.commit {

                    if(noteCount < 10){
                        NotesData.addNote(Note("", "", null, 1))
                        val uniqueID = NotesData.getNoteList().size-1
                        add(fid, NoteFragment.newInstance(uniqueID),
                            null)
                        noteCount ++
                    }
                }
            }
        }
        val buttonsContainer = LinearLayoutCompat(this).apply {
            orientation = LinearLayoutCompat.HORIZONTAL
            id = View.generateViewId()
            redAddButton.apply {
                setBackgroundColor(Color.RED)

            }
            addView(addButton)
            addView(redAddButton)
        }
        val fragmentLinearLayout = LinearLayoutCompat(this).apply {
            id = View.generateViewId()
            fid = id
            orientation = LinearLayoutCompat.VERTICAL
            setBackgroundColor(Color.LTGRAY)
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )

            (layoutParams as RelativeLayout.LayoutParams).addRule(
                RelativeLayout.BELOW, buttonsContainer.id)
        }

        val relativeLayout = RelativeLayout(this).apply {
            setBackgroundColor(Color.WHITE)
            addView(buttonsContainer)
            addView(fragmentLinearLayout)

        }
        setContentView(relativeLayout)

    }

    private fun readDataAsJSON(): JSONArray?{
        try {
            val fileInputStream: FileInputStream? = openFileInput("notes.json")
            val inputStreamReader= InputStreamReader(fileInputStream)
            val bufferedReader= BufferedReader(inputStreamReader)
            val stringBuilder: StringBuilder = StringBuilder()
            var text: String?
            while (run {
                    text = bufferedReader.readLine()
                    text
                } != null) {
                stringBuilder.append(text)
            }
            fileInputStream?.close()
            return JSONArray(stringBuilder.toString())
        } catch(e: FileNotFoundException){
            return null
        }

    }

    private fun loadJSONNotes(data:JSONArray){
        NotesData.loadNotes(data)
//        Log.d("MainActivity result", notesData2.toString())
    }

    private fun writeDataToFile(){
        val file = "notes.json"
        val data:String = NotesData.toJSON().toString()

        try {
            val fileOutputStream = openFileOutput(file, Context.MODE_PRIVATE)
            fileOutputStream.write(data.toByteArray())
            fileOutputStream.close()
        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}