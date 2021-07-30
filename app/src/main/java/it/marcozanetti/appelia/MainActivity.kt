package it.marcozanetti.appelia

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import java.util.*

class MainActivity : AppCompatActivity() {

    val background: ConstraintLayout by lazy { findViewById(R.id.backgroundLayout) }
    val eliaText: TextView by lazy { findViewById(R.id.eliaText) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        background.setOnClickListener {
            background.setBackgroundColor(getRandomColor())
        }

        eliaText.setOnClickListener {
            eliaText.setTextColor(getRandomColor())
        }
    }

    fun getRandomColor(): Int {
        val rnd = Random()
        val color: Int = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        return color
    }
}