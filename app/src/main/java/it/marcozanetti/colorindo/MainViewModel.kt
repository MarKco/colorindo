package it.marcozanetti.colorindo

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Random

class MainViewModel(
    private val savedStateHandle: SavedStateHandle = SavedStateHandle()
) : ViewModel() {

  private val _textToDisplay = MutableStateFlow("COLORINDO")
  val textToDisplay: StateFlow<String> = _textToDisplay.asStateFlow()

  private val _backgroundColor = MutableStateFlow(getRandomColor())
  val backgroundColor: StateFlow<Int> = _backgroundColor.asStateFlow()

  private val _textColor = MutableStateFlow(getRandomColor())
  val textColor: StateFlow<Int> = _textColor.asStateFlow()

  private val FONT_INDEX_KEY = "font_index"
  private val _selectedFontIndex = MutableStateFlow(savedStateHandle.get<Int>(FONT_INDEX_KEY) ?: 0)
  val selectedFontIndex: StateFlow<Int> = _selectedFontIndex.asStateFlow()

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

  fun setSelectedFontIndex(index: Int) {
    _selectedFontIndex.value = index
    savedStateHandle[FONT_INDEX_KEY] = index
  }

  private fun getRandomColor(): Int {
    val rnd = Random()
    return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
  }

  fun storeValuesToSharedPrefs(context: Context) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("ColorindoSharedPrefs", MODE_PRIVATE)
    val myEdit = sharedPreferences.edit()

    myEdit.putString("textToDisplay", textToDisplay.value)
    myEdit.putInt("backgroundColor", backgroundColor.value)
    myEdit.putInt("textColor", textColor.value)

    myEdit.apply()
  }

  fun retrieveValuesFromSharedPrefs(context: Context) {
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("ColorindoSharedPrefs", MODE_PRIVATE)
    val textToDisplay = sharedPreferences.getString("textToDisplay", "COLORINDO") ?: "COLORINDO"
    val backgroundColor = sharedPreferences.getInt("backgroundColor", getRandomColor())
    val textColor = sharedPreferences.getInt("textColor", getRandomColor())

    _textToDisplay.value = textToDisplay
    _backgroundColor.value = backgroundColor
    _textColor.value = textColor
  }
}

