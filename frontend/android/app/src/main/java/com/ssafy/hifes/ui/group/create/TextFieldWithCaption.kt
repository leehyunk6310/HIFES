package com.ssafy.hifes.ui.group.create

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.hifes.ui.theme.Purple40

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextFieldWithCaption(caption: String) {
    var text by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }

    val borderStroke = if (isFocused) {
        BorderStroke(2.dp, Purple40)    // 선택된 보더의 색 변경해야함
    } else {
        BorderStroke(1.dp, Color.Black)
    }

    Column() {

        Text(text = caption, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = text,
            onValueChange = { newText ->
                text = newText
            },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .border(borderStroke, MaterialTheme.shapes.small)
                .onFocusChanged {
                    isFocused = it.isFocused
                },
            textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownWithCaption(caption: String) {
    val options = (1..10).toList()

    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf(options[0].toString()) }

    var isFocused by remember { mutableStateOf(false) }

    val borderStroke = if (isFocused) {
        BorderStroke(2.dp, Purple40)    // 선택된 보더의 색 변경해야함
    } else {
        BorderStroke(1.dp, Color.Black)
    }

    Column() {

        Text(text = caption, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        // We want to react on tap/press on TextField to show menu
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            TextField(
                // The `menuAnchor` modifier must be passed to the text field for correctness.
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .border(borderStroke, MaterialTheme.shapes.small)
                    .onFocusChanged {
                        isFocused = it.isFocused
                    },

                readOnly = true,
                value = selectedOptionText,
                onValueChange = {},
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
//            colors = ExposedDropdownMenuDefaults.textFieldColors(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent
                ),
                textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                options.forEach { selectionOption ->
                    DropdownMenuItem(
                        text = { Text(selectionOption.toString()) },
                        onClick = {
                            selectedOptionText = selectionOption.toString()
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun EditPrev() {
    TextFieldWithCaption(caption = "caption")
    DropdownWithCaption(caption = "caption")
}


