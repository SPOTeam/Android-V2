package com.umcspot.spot.ui.component.button.r500

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true, widthDp = 650)
@Composable
fun TextButtonR500XL_AssetsPreview() {
    Row(Modifier.padding(12.dp)) {

        TextButtonR500XL(text = "Button Text", onClick = {})

        Spacer(Modifier.padding(4.dp))

        TextButtonR500XL(text = "Button Text", onClick = {}, checked = true)

        Spacer(Modifier.padding(4.dp))

        TextButtonR500XL(text = "Button Text", onClick = {}, enabled = false)
    }
}

@Preview(showBackground = true, widthDp = 650)
@Composable
fun TextButtonR500L_AssetsPreview() {
    Row(Modifier.padding(12.dp)) {

        TextButtonR500L(text = "Button Text", onClick = {})

        Spacer(Modifier.padding(4.dp))

        TextButtonR500L(text = "Button Text", onClick = {}, checked = true)

        Spacer(Modifier.padding(4.dp))

        TextButtonR500L(text = "Button Text", onClick = {}, enabled = false)
    }
}

@Preview(showBackground = true, widthDp = 650)
@Composable
fun TextButtonR500M_AssetsPreview() {
    Row(Modifier.padding(12.dp)) {

        TextButtonR500M(text = "Button Text", onClick = {})

        Spacer(Modifier.padding(4.dp))

        TextButtonR500M(text = "Button Text", onClick = {}, checked = true)

        Spacer(Modifier.padding(4.dp))

        TextButtonR500M(text = "Button Text", onClick = {}, enabled = false)
    }
}

@Preview(showBackground = true, widthDp = 650)
@Composable
fun TextButtonR500S_AssetsPreview() {
    Row(Modifier.padding(12.dp)) {

        TextButtonR500S(text = "Button Text", onClick = {})

        Spacer(Modifier.padding(4.dp))

        TextButtonR500S(text = "Button Text", onClick = {}, checked = true)

        Spacer(Modifier.padding(4.dp))

        TextButtonR500S(text = "Button Text", onClick = {}, enabled = false)
    }
}

@Preview(showBackground = true, widthDp = 650)
@Composable
fun TextButtonR500XS_AssetsPreview() {
    Row(Modifier.padding(12.dp)) {

        TextButtonR500XS(text = "Btn", onClick = {})

        Spacer(Modifier.padding(4.dp))

        TextButtonR500XS(text = "Btn", onClick = {}, checked = true)

        Spacer(Modifier.padding(4.dp))

        TextButtonR500XS(text = "Btn", onClick = {}, enabled = false)
    }
}