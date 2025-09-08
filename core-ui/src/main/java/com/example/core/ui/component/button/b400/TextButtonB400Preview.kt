package com.example.core.ui.component.button.b400

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true, widthDp = 650)
@Composable
fun TextButtonB400XL_AssetsPreview() {
    Row(Modifier.padding(12.dp)) {

        TextButtonB400XL(text = "Button Text", onClick = {})

        Spacer(Modifier.padding(4.dp))

        TextButtonB400XL(text = "Button Text", onClick = {}, checked = true)

        Spacer(Modifier.padding(4.dp))

        TextButtonB400XL(text = "Button Text", onClick = {}, enabled = false)
    }
}

@Preview(showBackground = true, widthDp = 650)
@Composable
fun TextButtonB400L_AssetsPreview() {
    Row(Modifier.padding(12.dp)) {

        TextButtonB400L(text = "Button Text", onClick = {})

        Spacer(Modifier.padding(4.dp))

        TextButtonB400L(text = "Button Text", onClick = {}, checked = true)

        Spacer(Modifier.padding(4.dp))

        TextButtonB400L(text = "Button Text", onClick = {}, enabled = false)
    }
}

@Preview(showBackground = true, widthDp = 650)
@Composable
fun TextButtonB400M_AssetsPreview() {
    Row(Modifier.padding(12.dp)) {

        TextButtonB400M(text = "Button Text", onClick = {})

        Spacer(Modifier.padding(4.dp))

        TextButtonB400M(text = "Button Text", onClick = {}, checked = true)

        Spacer(Modifier.padding(4.dp))

        TextButtonB400M(text = "Button Text", onClick = {}, enabled = false)
    }
}

@Preview(showBackground = true, widthDp = 650)
@Composable
fun TextButtonB400S_AssetsPreview() {
    Row(Modifier.padding(12.dp)) {

        TextButtonB400S(text = "Button Text", onClick = {})

        Spacer(Modifier.padding(4.dp))

        TextButtonB400S(text = "Button Text", onClick = {}, checked = true)

        Spacer(Modifier.padding(4.dp))

        TextButtonB400S(text = "Button Text", onClick = {}, enabled = false)
    }
}

@Preview(showBackground = true, widthDp = 650)
@Composable
fun TextButtonB400XS_AssetsPreview() {
    Row(Modifier.padding(12.dp)) {

        TextButtonB400XS(text = "Btn", onClick = {})

        Spacer(Modifier.padding(4.dp))

        TextButtonB400XS(text = "Btn", onClick = {}, checked = true)

        Spacer(Modifier.padding(4.dp))

        TextButtonB400XS(text = "Btn", onClick = {}, enabled = false)
    }
}