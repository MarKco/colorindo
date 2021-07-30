package it.marcozanetti.appelia

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import java.util.*
import android.content.DialogInterface
import android.util.Log
import eltos.simpledialogfragment.SimpleDialog
import eltos.simpledialogfragment.color.SimpleColorDialog


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
            background.setBackgroundColor(getRandomColor())
        }

        eliaText.setOnClickListener {
            eliaText.setTextColor(getRandomColor())
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
            .title(R.string.pick_a_color)
            .colorPreset(Color.RED)
            .allowCustom(true)
            .show(this, tag);
    }

    override fun onResult(dialogTag: String, which: Int, extras: Bundle): Boolean {
        when(dialogTag) {
            BACKGROUND_COLOR -> {
                background.setBackgroundColor(extras.getInt(SimpleColorDialog.COLOR))
                return true
            }
            TEXT_COLOR -> {
                eliaText.setTextColor(extras.getInt(SimpleColorDialog.COLOR))
                return true
            }
            else ->
                Log.d("EliApp", "Didn't expect to end up here")
        }
        return false
    }
}