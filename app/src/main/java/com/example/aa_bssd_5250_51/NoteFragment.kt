package com.example.aa_bssd_5250_51


import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResultListener
import com.example.aa_bssd_5250_51.MainActivity
import com.example.aa_bssd_5250_51.NoteEditorDialog
import com.google.android.material.button.MaterialButton


class NoteFragment : Fragment() {
    // TODO: Rename and change types of parameters

    private var resultKey: String = ""
    private var indexVal:Int = -1
    private var fragBgColor: String = ""

    private lateinit var nameView: TextView
    private lateinit var dateView: TextView
    private lateinit var descView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setFragmentResultListener(resultKey){ _, _ ->
            nameView.text = NotesData.getNoteList()[indexVal].name
            dateView.text = NotesData.getNoteList()[indexVal].date
            descView.text = NotesData.getNoteList()[indexVal].desc
        }
    }

    private fun sizeText(size: Float): Float {
        val metrics: DisplayMetrics = Resources.getSystem().displayMetrics
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, size, metrics)
    }
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val editButton = MaterialButton(requireContext()).apply {
            text = "Edit"
            setBackgroundColor(Color.BLUE)
            setTextColor(Color.WHITE)
            id = View.generateViewId()
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            (layoutParams as RelativeLayout.LayoutParams).addRule(
                RelativeLayout.ALIGN_PARENT_RIGHT
            )
            setOnClickListener{
//                val noteEditorDialog = NoteEditorDialog()
                val currentData = bundleOf(
                    "name" to nameView.text,
                    "date" to dateView.text,
                    "desc" to descView.text
                )

                NoteEditorDialog.newInstance(resultKey, currentData, indexVal)
                    .show(parentFragmentManager, NoteEditorDialog.TAG)

            }
        }
        val deleteButton = MaterialButton(requireContext()).apply {
            text = "Delete"
            setBackgroundColor(Color.WHITE)
            setTextColor(Color.RED)
            id = View.generateViewId()
            layoutParams = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            )
            (layoutParams as RelativeLayout.LayoutParams).addRule(
                RelativeLayout.RIGHT_OF, editButton.id
            )
            setOnClickListener{

                AlertDialog.Builder(requireContext()).apply {
                    setTitle("Delete Note")
                    setPositiveButton("Yes") { _, _ ->
                        activity?.supportFragmentManager?.commit {
//                            remove(this@NoteFragment)
                            NotesData.deleteNote(indexVal)

                        }
//
                        val parentActivity = activity as MainActivity
                        parentActivity.noteCount--
//                        NotesData.deleteNote(indexVal)
                    }
                    setNegativeButton("No", null) // do nothing if they say no
                    create()
                    show()
                }
            }
        }
        nameView = TextView(context).apply{
            setHint(R.string.name_place_holder)
            textSize = sizeText(6f)
            text = NotesData.getNoteList()[indexVal].name
        }
        dateView = TextView(context).apply{
            text = NotesData.getNoteList()[indexVal].date
            textSize = sizeText(6f)

        }
        descView = TextView(context).apply{
            setHint(R.string.desc_place_holder)
            textSize = sizeText(6f)
            text = NotesData.getNoteList()[indexVal].desc
        }
        val nameLinearLayout = LinearLayoutCompat(requireContext()).apply {

            layoutParams = LinearLayoutCompat.LayoutParams(
                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                100,
                0.2f
            )
            // Add the ImageView to the layout.
            addView(nameView)
        }
        val dateLinearLayout = LinearLayoutCompat(requireContext()).apply {

            layoutParams = LinearLayoutCompat.LayoutParams(
                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                100,
                0.2f
            )
            // Add the ImageView to the layout.
            addView(dateView)
        }
        val descLinearLayout = LinearLayoutCompat(requireContext()).apply {

            layoutParams = LinearLayoutCompat.LayoutParams(

                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                100,
                0.2f
            )
            // Add the ImageView to the layout.
            addView(descView)
        }
        val leftWrapper = LinearLayoutCompat(requireContext()).apply {
//            setBackgroundColor(Color.WHITE)
            orientation = LinearLayoutCompat.VERTICAL
            layoutParams = LinearLayoutCompat.LayoutParams(
                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                LinearLayoutCompat.LayoutParams.WRAP_CONTENT,
                .6f

            )

            addView(nameLinearLayout)
            addView(dateLinearLayout)
            addView(descLinearLayout)
        }
        val rightWrapper = LinearLayoutCompat(requireContext()).apply {
//            setBackgroundColor(Color.YELLOW)
            orientation = LinearLayoutCompat.VERTICAL
            layoutParams = LinearLayoutCompat.LayoutParams(
                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                1.2f
            )
            layoutParams = (layoutParams as ViewGroup.MarginLayoutParams).apply {
                setPadding( 50, 30, 50, 10)

            }
            addView(deleteButton)
            addView(editButton)
        }

        val linearLayout = LinearLayout(requireContext()).apply {

            setBackgroundColor(Color.parseColor(fragBgColor))
            layoutParams = LinearLayoutCompat.LayoutParams(
                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
                LinearLayoutCompat.LayoutParams.MATCH_PARENT,
            )
            layoutParams = (layoutParams as ViewGroup.MarginLayoutParams).apply {
                setMargins(10, top, 10, 20)
            }
            weightSum = 2f
            addView(leftWrapper)
            addView(rightWrapper)
        }

        return linearLayout
    }

    companion object {

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment NoteFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(unique:Int) =
            NoteFragment().apply {
                indexVal = unique
               resultKey = "NoteDataChange$unique"
                val importance = NotesData.getNoteList()[indexVal].important
                fragBgColor = if(importance == 1){
                    "RED"
                }else{
                    "WHITE"
                }

            }
    }
}