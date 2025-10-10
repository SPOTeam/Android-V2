package com.example.core.ui.component.button

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true, widthDp = 650)
@Composable
fun TextButtonXL_AssetsPreview() {
    Row(Modifier.padding(12.dp)) {
        TextButtonXL(text = "Button Text", onClick = {}, state = ButtonState.B400State)
        Spacer(Modifier.width(8.dp))
        TextButtonXL(text = "Button Text", onClick = {}, state = ButtonState.B400State, enabled = false)
    }
}

@Preview(showBackground = true, widthDp = 650)
@Composable
fun TextButtonL_AssetsPreview() {
    Row(Modifier.padding(12.dp)) {
        TextButtonL(text = "Button Text", onClick = {}, state = ButtonState.R500State)
        Spacer(Modifier.width(8.dp))
        TextButtonL(text = "Button Text", onClick = {}, state = ButtonState.R500State, enabled = false)
    }
}

@Preview(showBackground = true, widthDp = 650)
@Composable
fun TextButtonM_AssetsPreview() {
    Row(Modifier.padding(12.dp)) {
        TextButtonM(text = "Button Text", onClick = {}, state = ButtonState.G500State)
        Spacer(Modifier.width(8.dp))
        TextButtonM(text = "Button Text", onClick = {}, state = ButtonState.G500State, enabled = false)
    }
}

@Preview(showBackground = true, widthDp = 650)
@Composable
fun TextButtonS_AssetsPreview() {
    Row(Modifier.padding(12.dp)) {
        TextButtonS(text = "Button Text", onClick = {}, state = ButtonState.B400State)
        Spacer(Modifier.width(8.dp))
        TextButtonS(text = "Button Text", onClick = {}, state = ButtonState.B400State, enabled = false)
    }
}

@Preview(showBackground = true, widthDp = 650)
@Composable
fun TextButtonXS_AssetsPreview() {
    Row(Modifier.padding(12.dp)) {
        TextButtonXS(text = "Btn", onClick = {}, state = ButtonState.R500State)
        Spacer(Modifier.width(8.dp))
        TextButtonXS(text = "Btn", onClick = {}, state = ButtonState.R500State, enabled = false)
    }
}