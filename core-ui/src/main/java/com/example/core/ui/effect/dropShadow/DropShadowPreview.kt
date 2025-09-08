package com.example.core.ui.effect.dropShadow

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.ui.shapes.SpotShapes
import com.example.core.ui.theme.B500
import com.example.core.ui.theme.BlueGradient
import com.example.core.ui.theme.G100
import com.example.core.ui.theme.GrayGradient
import com.example.core.ui.theme.R500

@Preview(name = "Hard", showBackground = true)
@Composable
fun Preview_DS100() {
    Box(
        modifier = Modifier.padding(10.dp)
    ) {
        DS100(
            width = 200.dp,
            height = 100.dp,
            shape = SpotShapes.Hard,
            backgroundColor = B500,
            borderWidth = 3.dp,
            borderColor = Color.Black
        )
    }
}

@Preview(name = "Soft", showBackground = true)
@Composable
fun Preview_DS100_Y() {
    Box(
        modifier = Modifier.padding(10.dp)
    ) {
        DS100_Y(
            width = 200.dp,
            height = 100.dp,
            shape = SpotShapes.Soft,
            brush = BlueGradient
        )
    }
}

@Preview(name = "Round", showBackground = true)
@Composable
fun Preview_DS200() {
    Box(
        modifier = Modifier.padding(10.dp)
    ) {
        DS200(
            width = 200.dp,
            height = 100.dp,
            shape = SpotShapes.Round,
            brush = GrayGradient
        )
    }
}

@Preview(name = "SoftLeft", showBackground = true)
@Composable
fun Preview_DS300() {
    Box(
        modifier = Modifier.padding(10.dp)
    ) {
        DS300(
            width = 200.dp,
            height = 100.dp,
            shape = SpotShapes.SoftLeft,
            backgroundColor = R500
        )
    }
}

@Preview(name = "RoundRight", showBackground = true)
@Composable
fun Preview_DS400() {
    Box(
        modifier = Modifier.padding(10.dp)
    ) {
        DS400(
            width = 200.dp,
            height = 100.dp,
            shape = SpotShapes.RoundRight,
            backgroundColor = G100
        )
    }
}