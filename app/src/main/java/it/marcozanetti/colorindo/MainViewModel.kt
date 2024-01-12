package it.marcozanetti.colorindo

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Random


class MainViewModel : ViewModel() {

  private val _textToDisplay = MutableLiveData<String>()
  val textToDisplay: LiveData<String> = _textToDisplay

  private val _backgroundColor = MutableLiveData<Int>()
  val backgroundColor: LiveData<Int> = _backgroundColor

  private val _textColor = MutableLiveData<Int>()
  val textColor: LiveData<Int> = _textColor

  init {
    _textToDisplay.value = "COLORINDO"
    _backgroundColor.value = getRandomColor()
    _textColor.value = getRandomColor()
  }

  fun changeText(text: String) {
    _textToDisplay.value = text
  }

  fun changeBackgroundColor(color: Int) {
    _backgroundColor.value = color
  }

  fun changeTextColor(color: Int) {
    _textColor.value = color
  }

  fun setBackgroundToRandomColor() {
    _backgroundColor.value = getRandomColor()
  }

  fun setTextColorToRandomColor() {
    _textColor.value = getRandomColor()
  }

  private fun getRandomColor(): Int {
    val rnd = Random()
    val color: Int = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    return color
  }

  fun storeValuesToSharedPrefs(context: Context) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("ColorindoSharedPrefs", MODE_PRIVATE)
    val myEdit = sharedPreferences.edit()

    myEdit.putString("textToDisplay", textToDisplay.value)
    backgroundColor.value?.let { myEdit.putInt("backgroundColor", it) }
    textColor.value?.let { myEdit.putInt("textColor", it) }

    myEdit.apply()
  }

  fun retrieveValuesFromSharedPrefs(context: Context) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("ColorindoSharedPrefs", MODE_PRIVATE)
    val textToDisplay = sharedPreferences.getString("textToDisplay", "COLORINDO")
    val backgroundColor = sharedPreferences.getInt("backgroundColor", getRandomColor())
    val textColor = sharedPreferences.getInt("textColor", getRandomColor())

    _textToDisplay.value = textToDisplay
    _backgroundColor.value = backgroundColor
    _textColor.value = textColor
  }

}

