package com.umcspot.spot.designsystem.component.button

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.theme.SpotTheme

@Preview(showBackground = true, widthDp = 650)
@Composable
fun TextButtonXL_AssetsPreview() {
    SpotTheme {
        Row(Modifier.padding(12.dp)) {
            TextButton(text = "Button Text", onClick = {}, state = TextButtonState.B500State)
            Spacer(Modifier.width(8.dp))
            TextButton(text = "Button Text", onClick = {}, state = TextButtonState.B500State, enabled = false)
            Spacer(Modifier.width(8.dp))
            TextButton(text = "Button Text", onClick = {}, state = TextButtonState.B500State, checked = true)
        }
    }
}

@Preview(showBackground = true, widthDp = 650)
@Composable
fun TextButtonL_AssetsPreview() {
    SpotTheme {
        Row(Modifier.padding(12.dp)) {
            TextButton(text = "Button Text", onClick = {}, state = TextButtonState.R500State)
            Spacer(Modifier.width(8.dp))
            TextButton(text = "Button Text", onClick = {}, state = TextButtonState.R500State, enabled = false)
            Spacer(Modifier.width(8.dp))
            TextButton(text = "Button Text", onClick = {}, state = TextButtonState.R500State, checked = true)
        }
    }
}

@Preview(showBackground = true, widthDp = 650)
@Composable
fun TextButtonM_AssetsPreview() {
    SpotTheme {
        Row(Modifier.padding(12.dp)) {
            TextButton(text = "Button Text", onClick = {}, state = TextButtonState.G500State)
            Spacer(Modifier.width(8.dp))
            TextButton(text = "Button Text", onClick = {}, state = TextButtonState.G500State, enabled = false)
            Spacer(Modifier.width(8.dp))
            TextButton(text = "Button Text", onClick = {}, state = TextButtonState.G500State, checked = true)
        }
    }
}

@Preview(showBackground = true, widthDp = 650)
@Composable
fun TextButtonS_AssetsPreview() {
    SpotTheme {
        Row(Modifier.padding(12.dp)) {
            TextButton(text = "Button Text", onClick = {}, state = TextButtonState.B500State)
            Spacer(Modifier.width(8.dp))
            TextButton(text = "Button Text", onClick = {}, state = TextButtonState.B500State, enabled = false)
            Spacer(Modifier.width(8.dp))
            TextButton(text = "Button Text", onClick = {}, state = TextButtonState.B500State, checked = true)
        }
    }
}

@Preview(showBackground = true, widthDp = 650)
@Composable
fun TextButtonXS_AssetsPreview() {
    SpotTheme {
        Row(Modifier.padding(12.dp)) {
            TextButton(text = "Btn", onClick = {}, state = TextButtonState.R500State)
            Spacer(Modifier.width(8.dp))
            TextButton(text = "Btn", onClick = {}, state = TextButtonState.R500State, enabled = false)
            Spacer(Modifier.width(8.dp))
            TextButton(text = "Btn", onClick = {}, state = TextButtonState.R500State, checked = true)
        }
    }
}

@Preview(showBackground = true, widthDp = 650)
@Composable
fun TextButtonToggle_AssetsPreview() {
    SpotTheme {
        Row(Modifier.padding(12.dp)) {
            TextButton(text = "Btn",  state = TextButtonState.Toggle, onClick = {})
            Spacer(Modifier.width(8.dp))
            TextButton(text = "Btn", onClick = {}, state = TextButtonState.Toggle, enabled = false)
            Spacer(Modifier.width(8.dp))
            TextButton(text = "", onClick = {}, state = TextButtonState.Toggle, checked = true)
        }
    }
}