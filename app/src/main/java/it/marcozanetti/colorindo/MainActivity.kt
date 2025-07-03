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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import it.marcozanetti.colorindo.ui.theme.ColorindoTheme

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

                var showTextDialog by remember { mutableStateOf(false) }
                var showBackgroundColorDialog by remember { mutableStateOf(false) }
                var showTextColorDialog by remember { mutableStateOf(false) }

                val balooTamma = FontFamily(Font(R.font.baloo_tamma, FontWeight.Normal))

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
                            .padding(bottom = 24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Spacer(modifier = Modifier.height(24.dp))
                        IconButton(onClick = { showTextDialog = true }, modifier = Modifier.size(64.dp)) {
                            Icon(
                                imageVector = Icons.Filled.FormatSize,
                                contentDescription = "Change Text",
                                modifier = Modifier.size(48.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = textToDisplay,
                                color = Color(textColor),
                                fontSize = 48.sp,
                                fontFamily = balooTamma,
                                modifier = Modifier
                                    .clickable(
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ) {
                                        viewModel.setTextColorToRandomColor()
                                    },
                                maxLines = 3
                            )
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            IconButton(onClick = { showBackgroundColorDialog = true }, modifier = Modifier.size(64.dp)) {
                                Icon(
                                    painter = painterResource(id = R.mipmap.background_color),
                                    contentDescription = "Change Background Color",
                                    modifier = Modifier.size(48.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(32.dp))
                            IconButton(onClick = { showTextColorDialog = true }, modifier = Modifier.size(64.dp)) {
                                Icon(
                                    painter = painterResource(id = R.mipmap.text_color),
                                    contentDescription = "Change Text Color",
                                    modifier = Modifier.size(48.dp)
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
    // For simplicity, show a few preset colors. You can expand this as needed.
    val colors = listOf(
        Color.Red, Color.Green, Color.Blue, Color.Yellow, Color.Magenta, Color.Cyan, Color.Black, Color.White
    )
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Scegli un colore") },
        text = {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                colors.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(color, RoundedCornerShape(20.dp))
                            .clickable { onColorSelected(color.toArgb()) }
                    )
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Annulla") }
        }
    )
}
