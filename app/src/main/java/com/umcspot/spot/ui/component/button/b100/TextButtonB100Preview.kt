package com.umcspot.spot.ui.component.button.b100

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true, widthDp = 650)
@Composable
fun TextButtonB100XL_AssetsPreview() {
    Row(Modifier.padding(12.dp)) {

        TextButtonB100XL(text = "Button Text", onClick = {})

        Spacer(Modifier.padding(4.dp))

        TextButtonB100XL(text = "Button Text", onClick = {}, checked = true)

        Spacer(Modifier.padding(4.dp))

        TextButtonB100XL(text = "Button Text", onClick = {}, enabled = false)
    }
}

@Preview(showBackground = true, widthDp = 650)
@Composable
fun TextButtonB100L_AssetsPreview() {
    Row(Modifier.padding(12.dp)) {

        TextButtonB100L(text = "Button Text", onClick = {})

        Spacer(Modifier.padding(4.dp))

        TextButtonB100L(text = "Button Text", onClick = {}, checked = true)

        Spacer(Modifier.padding(4.dp))

        TextButtonB100L(text = "Button Text", onClick = {}, enabled = false)
    }
}

@Preview(showBackground = true, widthDp = 650)
@Composable
fun TextButtonB100M_AssetsPreview() {
    Row(Modifier.padding(12.dp)) {

        TextButtonB100M(text = "Button Text", onClick = {})

        Spacer(Modifier.padding(4.dp))

        TextButtonB100M(text = "Button Text", onClick = {}, checked = true)

        Spacer(Modifier.padding(4.dp))

        TextButtonB100M(text = "Button Text", onClick = {}, enabled = false)
    }
}

@Preview(showBackground = true, widthDp = 650)
@Composable
fun TextButtonB100S_AssetsPreview() {
    Row(Modifier.padding(12.dp)) {

        TextButtonB100S(text = "Button Text", onClick = {})

        Spacer(Modifier.padding(4.dp))

        TextButtonB100S(text = "Button Text", onClick = {}, checked = true)

        Spacer(Modifier.padding(4.dp))

        TextButtonB100S(text = "Button Text", onClick = {}, enabled = false)
    }
}

@Preview(showBackground = true, widthDp = 650)
@Composable
fun TextButtonB100XS_AssetsPreview() {
    Row(Modifier.padding(12.dp)) {

        TextButtonB100XS(text = "Btn", onClick = {})

        Spacer(Modifier.padding(4.dp))

        TextButtonB100XS(text = "Btn", onClick = {}, checked = true)

        Spacer(Modifier.padding(4.dp))

        TextButtonB100XS(text = "Btn", onClick = {}, enabled = false)
    }
}