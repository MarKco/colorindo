package it.marcozanetti.colorindo

import android.graphics.Color
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.AllCaps
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.ColorUtils
import androidx.lifecycle.ViewModelProvider
import eltos.simpledialogfragment.SimpleDialog
import eltos.simpledialogfragment.color.SimpleColorDialog
import eltos.simpledialogfragment.color.SimpleColorDialog.COLORFUL_COLOR_PALLET


class MainActivity : AppCompatActivity(), SimpleDialog.OnDialogResultListener {

    private val background: ConstraintLayout by lazy { findViewById(R.id.backgroundLayout) }
    private val editableText: TextView by lazy { findViewById(R.id.editableText) }
    private val changeBackgroundColorButton: ImageButton by lazy { findViewById(R.id.changeBackgroundColorButton) }
    private val changeTextColorButton: ImageButton by lazy { findViewById(R.id.changeTextColorButton) }
    private val changeTextButton: ImageButton by lazy { findViewById(R.id.changeTextButton) }

    private val BACKGROUND_COLOR: String = "BACKGROUND_COLOR"
    private val TEXT_COLOR: String = "TEXT_COLOR"
    private val TEXT_CONTENT: String = "TEXT_CONTENT"

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.retrieveValuesFromSharedPrefs(this)

        viewModel.backgroundColor.observeForever {
            background.setBackgroundColor(ColorUtils.blendARGB(editableText.solidColor, it, 1.0f))
        }

        viewModel.textColor.observeForever {
            editableText.setTextColor(ColorUtils.blendARGB(editableText.solidColor, it, 1.0f))
        }

        background.setBackgroundColor(viewModel.backgroundColor.value!!)
        editableText.setTextColor(viewModel.textColor.value!!)
        editableText.text = viewModel.textToDisplay.value

        background.setOnClickListener {
            viewModel.setBackgroundToRandomColor()
        }

        editableText.setOnClickListener {
            viewModel.setTextColorToRandomColor()
        }

        changeBackgroundColorButton.setOnClickListener {
            getColor(BACKGROUND_COLOR)
        }

        changeTextColorButton.setOnClickListener {
            getColor(TEXT_COLOR)
        }

        changeTextButton.setOnClickListener {
            //Shows a dialog with a text field pre-filled with the current text and allowing the user to change it
            showTextChangeDialog {
                viewModel.changeText(it)
                editableText.text = it
            }
        }
    }

    private fun getColor(tag: String) {
        SimpleColorDialog.build()
            .choiceMode(SimpleColorDialog.SINGLE_CHOICE_DIRECT)
            .colorPreset(COLORFUL_COLOR_PALLET)
            .title(R.string.pick_a_color)
            .colorPreset(Color.RED)
            .allowCustom(true)
            .show(this, tag)
    }

    private fun showTextChangeDialog(updateText: (String) -> Unit) {
        val updatedText = EditText(this).apply {
            filters = arrayOf<InputFilter>(AllCaps())
            //select all on focus
            setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    selectAll()
                }
            }
            setSelectAllOnFocus(true)
            setLines(1)
            maxLines = 1
            setSingleLine()
            setText(viewModel.textToDisplay.value)
        }

        AlertDialog.Builder(this)
            .setMessage("SCEGLI LA PAROLA")
            .setView(updatedText)
            .setPositiveButton("OK") { dialog, whichButton ->
                val updatedTextFromDialog: String = updatedText.text.toString()
                updateText(updatedTextFromDialog)
            }
            .show()

    }

    override fun onResult(dialogTag: String, which: Int, extras: Bundle): Boolean {
        when(dialogTag) {
            BACKGROUND_COLOR -> {
                viewModel.changeBackgroundColor(extras.getInt(SimpleColorDialog.COLOR))
                return true
            }
            TEXT_COLOR -> {
                viewModel.changeTextColor(extras.getInt(SimpleColorDialog.COLOR))
                return true
            }
            else ->
                Log.d("Colorindo", "Didn't expect to end up here")
        }
        return false
    }

    override fun onStop() {
        super.onStop()
        viewModel.storeValuesToSharedPrefs(this)
    }
}
