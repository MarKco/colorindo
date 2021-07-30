package it.marcozanetti.appelia

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.ColorUtils
import androidx.window.WindowManager
import eltos.simpledialogfragment.SimpleDialog
import eltos.simpledialogfragment.color.SimpleColorDialog
import eltos.simpledialogfragment.color.SimpleColorDialog.COLORFUL_COLOR_PALLET
import eltos.simpledialogfragment.color.SimpleColorDialog.MATERIAL_COLOR_PALLET_LIGHT
import java.util.*


class MainActivity : AppCompatActivity(), SimpleDialog.OnDialogResultListener {

    val background: ConstraintLayout by lazy { findViewById(R.id.backgroundLayout) }
    val eliaText: TextView by lazy { findViewById(R.id.eliaText) }
    val changeBackgroundColorButton: ImageButton by lazy { findViewById(R.id.changeBackgroundColorButton) }
    val changeTextColorButton: ImageButton by lazy { findViewById(R.id.changeTextColorButton) }

    val BACKGROUND_COLOR: String = "BACKGROUND_COLOR"
    val TEXT_COLOR: String = "TEXT_COLOR"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        background.setBackgroundColor(getRandomColor())
        eliaText.setTextColor(getRandomColor())

        background.setOnClickListener {
            background.setBackgroundColor(ColorUtils.blendARGB(background.solidColor, getRandomColor(), 1.0f));
        }

        eliaText.setOnClickListener {
            eliaText.setTextColor(ColorUtils.blendARGB(eliaText.solidColor, getRandomColor(), 1.0f));
        }

        changeBackgroundColorButton.setOnClickListener {
            getColor(BACKGROUND_COLOR)
        }

        changeTextColorButton.setOnClickListener {
            getColor(TEXT_COLOR)
        }
    }

    fun getRandomColor(): Int {
        val rnd = Random()
        val color: Int = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        return color
    }

    fun getColor(tag: String) {
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
                eliaText.setTextColor(ColorUtils.blendARGB(eliaText.solidColor, extras.getInt(SimpleColorDialog.COLOR), 1.0f));
                return true
            }
            else ->
                Log.d("EliApp", "Didn't expect to end up here")
        }
        return false
    }
}