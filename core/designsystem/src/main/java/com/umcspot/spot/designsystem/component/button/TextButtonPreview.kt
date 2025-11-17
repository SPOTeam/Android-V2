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
            TextButtonXL(text = "Button Text", onClick = {}, state = TextButtonState.B400State)
            Spacer(Modifier.width(8.dp))
            TextButtonXL(text = "Button Text", onClick = {}, state = TextButtonState.B400State, enabled = false)
            Spacer(Modifier.width(8.dp))
            TextButtonXL(text = "Button Text", onClick = {}, state = TextButtonState.B400State, checked = true)
        }
    }
}

@Preview(showBackground = true, widthDp = 650)
@Composable
fun TextButtonL_AssetsPreview() {
    SpotTheme {
        Row(Modifier.padding(12.dp)) {
            TextButtonL(text = "Button Text", onClick = {}, state = TextButtonState.R500State)
            Spacer(Modifier.width(8.dp))
            TextButtonL(text = "Button Text", onClick = {}, state = TextButtonState.R500State, enabled = false)
            Spacer(Modifier.width(8.dp))
            TextButtonL(text = "Button Text", onClick = {}, state = TextButtonState.R500State, checked = true)
        }
    }
}

@Preview(showBackground = true, widthDp = 650)
@Composable
fun TextButtonM_AssetsPreview() {
    SpotTheme {
        Row(Modifier.padding(12.dp)) {
            TextButtonM(text = "Button Text", onClick = {}, state = TextButtonState.G500State)
            Spacer(Modifier.width(8.dp))
            TextButtonM(text = "Button Text", onClick = {}, state = TextButtonState.G500State, enabled = false)
            Spacer(Modifier.width(8.dp))
            TextButtonM(text = "Button Text", onClick = {}, state = TextButtonState.G500State, checked = true)
        }
    }
}

@Preview(showBackground = true, widthDp = 650)
@Composable
fun TextButtonS_AssetsPreview() {
    SpotTheme {
        Row(Modifier.padding(12.dp)) {
            TextButtonS(text = "Button Text", onClick = {}, state = TextButtonState.B400State)
            Spacer(Modifier.width(8.dp))
            TextButtonS(text = "Button Text", onClick = {}, state = TextButtonState.B400State, enabled = false)
            Spacer(Modifier.width(8.dp))
            TextButtonS(text = "Button Text", onClick = {}, state = TextButtonState.B400State, checked = true)
        }
    }
}

@Preview(showBackground = true, widthDp = 650)
@Composable
fun TextButtonXS_AssetsPreview() {
    SpotTheme {
        Row(Modifier.padding(12.dp)) {
            TextButtonXS(text = "Btn", onClick = {}, state = TextButtonState.R500State)
            Spacer(Modifier.width(8.dp))
            TextButtonXS(text = "Btn", onClick = {}, state = TextButtonState.R500State, enabled = false)
            Spacer(Modifier.width(8.dp))
            TextButtonXS(text = "Btn", onClick = {}, state = TextButtonState.R500State, checked = true)
        }
    }
}