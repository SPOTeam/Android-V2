package com.umcspot.spot.ui.component.button.g500

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true, widthDp = 650)
@Composable
fun TextButtonG500XL_AssetsPreview() {
    Row(Modifier.padding(12.dp)) {

        TextButtonG500XL(text = "Button Text", onClick = {})

        Spacer(Modifier.padding(4.dp))

        TextButtonG500XL(text = "Button Text", onClick = {}, checked = true)

        Spacer(Modifier.padding(4.dp))

        TextButtonG500XL(text = "Button Text", onClick = {}, enabled = false)
    }
}

@Preview(showBackground = true, widthDp = 650)
@Composable
fun TextButtonG500L_AssetsPreview() {
    Row(Modifier.padding(12.dp)) {

        TextButtonG500L(text = "Button Text", onClick = {})

        Spacer(Modifier.padding(4.dp))

        TextButtonG500L(text = "Button Text", onClick = {}, checked = true)

        Spacer(Modifier.padding(4.dp))

        TextButtonG500L(text = "Button Text", onClick = {}, enabled = false)
    }
}

@Preview(showBackground = true, widthDp = 650)
@Composable
fun TextButtonG500M_AssetsPreview() {
    Row(Modifier.padding(12.dp)) {

        TextButtonG500M(text = "Button Text", onClick = {})

        Spacer(Modifier.padding(4.dp))

        TextButtonG500M(text = "Button Text", onClick = {}, checked = true)

        Spacer(Modifier.padding(4.dp))

        TextButtonG500M(text = "Button Text", onClick = {}, enabled = false)
    }
}

@Preview(showBackground = true, widthDp = 650)
@Composable
fun TextButtonG500S_AssetsPreview() {
    Row(Modifier.padding(12.dp)) {

        TextButtonG500S(text = "Button Text", onClick = {})

        Spacer(Modifier.padding(4.dp))

        TextButtonG500S(text = "Button Text", onClick = {}, checked = true)

        Spacer(Modifier.padding(4.dp))

        TextButtonG500S(text = "Button Text", onClick = {}, enabled = false)
    }
}

@Preview(showBackground = true, widthDp = 650)
@Composable
fun TextButtonG500XS_AssetsPreview() {
    Row(Modifier.padding(12.dp)) {

        TextButtonG500XS(text = "Btn", onClick = {})

        Spacer(Modifier.padding(4.dp))

        TextButtonG500XS(text = "Btn", onClick = {}, checked = true)

        Spacer(Modifier.padding(4.dp))

        TextButtonG500XS(text = "Btn", onClick = {}, enabled = false)
    }
}