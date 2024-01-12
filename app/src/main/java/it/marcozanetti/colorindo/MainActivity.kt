package it.marcozanetti.colorindo

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.ColorUtils
import eltos.simpledialogfragment.SimpleDialog
import eltos.simpledialogfragment.color.SimpleColorDialog
import eltos.simpledialogfragment.color.SimpleColorDialog.COLORFUL_COLOR_PALLET
import it.marcozanetti.appelia.R
import java.util.*


class MainActivity : AppCompatActivity(), SimpleDialog.OnDialogResultListener {

    private val background: ConstraintLayout by lazy { findViewById(R.id.backgroundLayout) }
    private val editableText: TextView by lazy { findViewById(R.id.editableText) }
    private val changeBackgroundColorButton: ImageButton by lazy { findViewById(R.id.changeBackgroundColorButton) }
    private val changeTextColorButton: ImageButton by lazy { findViewById(R.id.changeTextColorButton) }

    private val BACKGROUND_COLOR: String = "BACKGROUND_COLOR"
    private val TEXT_COLOR: String = "TEXT_COLOR"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        background.setBackgroundColor(getRandomColor())
        editableText.setTextColor(getRandomColor())

        background.setOnClickListener {
            background.setBackgroundColor(ColorUtils.blendARGB(background.solidColor, getRandomColor(), 1.0f));
        }

        editableText.setOnClickListener {
            editableText.setTextColor(ColorUtils.blendARGB(editableText.solidColor, getRandomColor(), 1.0f));
        }

        changeBackgroundColorButton.setOnClickListener {
            getColor(BACKGROUND_COLOR)
        }

        changeTextColorButton.setOnClickListener {
            getColor(TEXT_COLOR)
        }
    }

    private fun getRandomColor(): Int {
        val rnd = Random()
        val color: Int = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        return color
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

    override fun onResult(dialogTag: String, which: Int, extras: Bundle): Boolean {
        when(dialogTag) {
            BACKGROUND_COLOR -> {
                background.setBackgroundColor(ColorUtils.blendARGB(background.solidColor, extras.getInt(SimpleColorDialog.COLOR), 1.0f));
                return true
            }
            TEXT_COLOR -> {
                editableText.setTextColor(ColorUtils.blendARGB(editableText.solidColor, extras.getInt(SimpleColorDialog.COLOR), 1.0f));
                return true
            }
            else ->
                Log.d("Colorindo", "Didn't expect to end up here")
        }
        return false
    }
}