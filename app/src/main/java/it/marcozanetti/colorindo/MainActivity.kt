package it.marcozanetti.colorindo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.TextAutoSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FontDownload
import androidx.compose.material.icons.filled.FormatColorText
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.TextFields
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.marcozanetti.colorindo.ui.theme.ColorindoTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.retrieveValuesFromSharedPrefs(this)
        setContent {
            ColorindoTheme {
                val context = LocalContext.current
                val backgroundColor by viewModel.backgroundColor.collectAsState()
                val textColor by viewModel.textColor.collectAsState()
                val textToDisplay by viewModel.textToDisplay.collectAsState()
                val isUppercase by viewModel.isUppercase.collectAsState()

                var showTextDialog by remember { mutableStateOf(false) }
                var showBackgroundColorDialog by remember { mutableStateOf(false) }
                var showTextColorDialog by remember { mutableStateOf(false) }
                var showFontDialog by remember { mutableStateOf(false) }

                val balooTamma = FontFamily(Font(R.font.baloo_tamma, FontWeight.Normal))

                // Vibrant palette for icons
                val iconPalette = listOf(
                    Color(0xFFFFEB3B), // Yellow
                    Color(0xFF00E5FF), // Cyan
                    Color(0xFFD500F9), // Purple
                    Color(0xFFFF3D00), // Orange
                    Color(0xFF00C853), // Green
                    Color(0xFFFF1744), // Red
                    Color(0xFF1A237E), // Indigo
                    Color(0xFF00B8D4), // Light Blue
                    Color(0xFFFFC400), // Amber
                    Color(0xFFAA00FF)  // Deep Purple
                )

                // List of available fonts (assume TTFs are present in res/font/)
                val fontOptions = listOf(
                    "Baloo Tamma" to FontFamily(Font(R.font.baloo_tamma)),
                    "Suez One" to FontFamily(Font(R.font.suez_one)),
                    "Wendy One" to FontFamily(Font(R.font.wendy_one)),
                    "Monoton" to FontFamily(Font(R.font.monoton)),
                    "Kids Magazine" to FontFamily(Font(R.font.kids_magazine)),
                    "Roboto" to FontFamily(Font(R.font.roboto)),
                    "Lato" to FontFamily(Font(R.font.lato)),
                    "Pacifico" to FontFamily(Font(R.font.pacifico)),
                    "Lobster" to FontFamily(Font(R.font.lobster)),
                    "Oswald" to FontFamily(Font(R.font.oswald)),
                    "Indie Flower" to FontFamily(Font(R.font.indie_flower)),
                    "Raleway" to FontFamily(Font(R.font.raleway))
                )
                val selectedFontIndex by viewModel.selectedFontIndex.collectAsState()
                val selectedFontFamily = fontOptions[selectedFontIndex].second

                // Contrast ratio calculation (WCAG)
                fun contrastRatio(c1: Color, c2: Color): Double {
                    fun channel(v: Float): Double {
                        val vNorm = v / 1.0f
                        return if (vNorm <= 0.03928) vNorm / 12.92 else Math.pow(((vNorm + 0.055) / 1.055), 2.4)
                    }
                    val l1 = 0.2126 * channel(c1.red) + 0.7152 * channel(c1.green) + 0.0722 * channel(c1.blue)
                    val l2 = 0.2126 * channel(c2.red) + 0.7152 * channel(c2.green) + 0.0722 * channel(c2.blue)
                    val lighter = maxOf(l1, l2)
                    val darker = minOf(l1, l2)
                    return (lighter + 0.05) / (darker + 0.05)
                }

                val bgColor = Color(backgroundColor)
                val iconTint = remember(backgroundColor) {
                    val accessiblePalette = iconPalette.filter { contrastRatio(it, bgColor) >= 3.0 }
                    if (accessiblePalette.isNotEmpty()) {
                        accessiblePalette[Random.nextInt(accessiblePalette.size)]
                    } else {
                        // Fallback: pick black or white, whichever is more accessible
                        val whiteContrast = contrastRatio(Color.White, bgColor)
                        val blackContrast = contrastRatio(Color.Black, bgColor)
                        if (whiteContrast > blackContrast) Color.White else Color.Black
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(backgroundColor))
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            viewModel.setBackgroundToRandomColor()
                        }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            IconButton(onClick = { showTextDialog = true }, modifier = Modifier.size(64.dp)) {
                                Icon(
                                    imageVector = Icons.Filled.Edit,
                                    contentDescription = "Edit Text",
                                    modifier = Modifier.size(48.dp),
                                    tint = iconTint
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            IconButton(onClick = { showFontDialog = true }, modifier = Modifier.size(64.dp)) {
                                Icon(
                                    imageVector = Icons.Filled.FontDownload,
                                    contentDescription = "Select Font",
                                    modifier = Modifier.size(48.dp),
                                    tint = iconTint
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            IconButton(onClick = { viewModel.setIsUppercase(!isUppercase) }, modifier = Modifier.size(64.dp)) {
                                Icon(
                                    imageVector = Icons.Filled.TextFields,
                                    contentDescription = if (isUppercase) "Switch to lowercase" else "Switch to uppercase",
                                    modifier = Modifier.size(48.dp),
                                    tint = iconTint
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            val minFontSize = 24.sp
                            val maxFontSize = 96.sp
                            val lineHeight = ((minFontSize.value + maxFontSize.value) / 2 * 1.5f).sp
                            Column(
                                modifier = Modifier.verticalScroll(rememberScrollState())
                            ) {
                                BasicText(
                                    text = if (isUppercase) textToDisplay.uppercase() else textToDisplay.lowercase(),
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                        .clickable(
                                            enabled = true,
                                            indication = null,
                                            interactionSource = remember { MutableInteractionSource() }
                                        ) {
                                            viewModel.setTextColorToRandomColor()
                                        },
                                    style = LocalTextStyle.current.copy(
                                        color = Color(textColor),
                                        fontFamily = selectedFontFamily,
                                        textAlign = TextAlign.Center,
                                        platformStyle = PlatformTextStyle(
                                            includeFontPadding = false
                                        ),
                                        lineHeight = lineHeight,
                                    ),
                                    autoSize = TextAutoSize.StepBased(
                                        minFontSize = minFontSize,
                                        maxFontSize = maxFontSize
                                    )
                                )
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            IconButton(onClick = { showBackgroundColorDialog = true }, modifier = Modifier.size(64.dp)) {
                                Icon(
                                    imageVector = Icons.Filled.Palette,
                                    contentDescription = "Change Background Color",
                                    modifier = Modifier.size(48.dp),
                                    tint = iconTint
                                )
                            }
                            Spacer(modifier = Modifier.width(32.dp))
                            IconButton(onClick = { showTextColorDialog = true }, modifier = Modifier.size(64.dp)) {
                                Icon(
                                    imageVector = Icons.Filled.FormatColorText,
                                    contentDescription = "Change Text Color",
                                    modifier = Modifier.size(48.dp),
                                    tint = iconTint
                                )
                            }
                        }
                    }
                }

                if (showTextDialog) {
                    var input by remember { mutableStateOf(textToDisplay) }
                    AlertDialog(
                        onDismissRequest = { showTextDialog = false },
                        title = { Text("SCEGLI LA PAROLA") },
                        text = {
                            OutlinedTextField(
                                value = input,
                                onValueChange = { input = it.uppercase() },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(
                                    capitalization = KeyboardCapitalization.Characters,
                                    imeAction = ImeAction.Done
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        },
                        confirmButton = {
                            TextButton(onClick = {
                                viewModel.changeText(input)
                                showTextDialog = false
                            }) { Text("OK") }
                        },
                        dismissButton = {
                            TextButton(onClick = { showTextDialog = false }) { Text("Annulla") }
                        }
                    )
                }

                if (showBackgroundColorDialog) {
                    ColorPickerDialog(
                        onColorSelected = {
                            viewModel.changeBackgroundColor(it)
                            showBackgroundColorDialog = false
                        },
                        onDismiss = { showBackgroundColorDialog = false }
                    )
                }

                if (showTextColorDialog) {
                    ColorPickerDialog(
                        onColorSelected = {
                            viewModel.changeTextColor(it)
                            showTextColorDialog = false
                        },
                        onDismiss = { showTextColorDialog = false }
                    )
                }

                if (showFontDialog) {
                    AlertDialog(
                        onDismissRequest = { showFontDialog = false },
                        title = { Text("Scegli un font") },
                        text = {
                            Column(
                                modifier = Modifier.verticalScroll(rememberScrollState())
                            ) {
                                fontOptions.forEachIndexed { idx, (name, _) ->
                                    Row(
                                        Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                viewModel.setSelectedFontIndex(idx)
                                                showFontDialog = false
                                            }
                                            .padding(vertical = 0.5.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        RadioButton(
                                            selected = idx == selectedFontIndex,
                                            onClick = {
                                                viewModel.setSelectedFontIndex(idx)
                                                showFontDialog = false
                                            }
                                        )
                                        Text(
                                            name,
                                            modifier = Modifier.padding(start = 8.dp),
                                            fontFamily = fontOptions[idx].second,
                                            fontSize = 22.sp
                                        )
                                    }
                                }
                            }
                        },
                        confirmButton = {},
                        dismissButton = {
                            TextButton(onClick = { showFontDialog = false }) { Text("Annulla") }
                        }
                    )
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.storeValuesToSharedPrefs(this)
    }
}

@Composable
fun ColorPickerDialog(onColorSelected: (Int) -> Unit, onDismiss: () -> Unit) {
    // Expanded palette with more vibrant and varied colors
    val colors = listOf(
        Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Magenta, Color.Cyan, Color.Black, Color.White,
        Color(0xFFFF9800), // Orange
        Color(0xFF4CAF50), // Green
        Color(0xFF2196F3), // Blue
        Color(0xFFFFEB3B), // Yellow
        Color(0xFF9C27B0), // Purple
        Color(0xFFFF5722), // Deep Orange
        Color(0xFF795548), // Brown
        Color(0xFF607D8B), // Blue Grey
        Color(0xFF00BCD4), // Cyan
        Color(0xFF8BC34A), // Light Green
        Color(0xFFFFC107), // Amber
        Color(0xFF3F51B5), // Indigo
        Color(0xFFE91E63), // Pink
        Color(0xFFCDDC39), // Lime
        Color(0xFF673AB7), // Deep Purple
        Color(0xFF009688), // Teal
        Color(0xFFBDBDBD), // Grey
        Color(0xFFFFA726), // Light Orange
        Color(0xFFA1887F), // Light Brown
        Color(0xFFB39DDB), // Light Purple
        Color(0xFF80CBC4), // Light Teal
        Color(0xFFFFCDD2), // Light Red
        Color(0xFFDCEDC8), // Light Green
        Color(0xFFBBDEFB), // Light Blue
        Color(0xFFFFF9C4), // Light Yellow
        Color(0xFFD1C4E9), // Light Violet
        Color(0xFFFFF8E1)  // Light Cream
    )
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Scegli un colore") },
        text = {
            Column(Modifier.fillMaxWidth()) {
                colors.chunked(6).forEach { rowColors ->
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        rowColors.forEach { color ->
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(color, RoundedCornerShape(20.dp))
                                    .clickable { onColorSelected(color.toArgb()) }
                            )
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Annulla") }
        }
    )
}
