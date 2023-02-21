package com.example.aa_bssd_5250_51

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import com.example.aa_bssd_5250_51.NotesData

class NoteEditorDialog: DialogFragment() {
    var targetResultKey: String = ""
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val editName = EditText(requireContext()).apply {
            setHint(R.string.name_place_holder)
            setText(NotesData.getNoteList()[indexVal].name)
        }
        val editDate = EditText(requireContext()).apply {
            setHint(R.string.date_place_holder)
            setText(NotesData.getNoteList()[indexVal].date)
        }
        val editDesc = EditText(requireContext()).apply {
            setHint(R.string.desc_place_holder)
            setText(NotesData.getNoteList()[indexVal].desc)
        }
        val linearLayout = LinearLayoutCompat(requireContext()).apply {
            orientation = LinearLayoutCompat.VERTICAL
            addView(editName)
            addView(editDate)
            addView(editDesc)
        }
        val ad = AlertDialog.Builder(requireContext()).apply {
            setTitle("Note Editor")
            setMessage("Edit Content")
            setView(linearLayout)
            setPositiveButton("Save") {_,_ ->
                NotesData.getNoteList()[indexVal].name = editName.text.toString()
                NotesData.getNoteList()[indexVal].date = editDate.text.toString()
                NotesData.getNoteList()[indexVal].desc = editDesc.text.toString()
                setFragmentResult(targetResultKey, Bundle.EMPTY)
            }
            setNegativeButton("Cancel") {_,_ ->}
        }
        return   ad.create()
    }

    companion object{
        const val TAG = "NoteEditorDialog"
        private var existingData: Bundle = Bundle.EMPTY
        var indexVal:Int = -1
        @JvmStatic
        fun newInstance(target:String, existing:Bundle, unique:Int)=
            NoteEditorDialog().apply {
                indexVal = unique
                targetResultKey = target
                existingData = existing
            }
    }
}