package com.umcspot.spot.designsystem.shapes

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.ui.theme.B100
import com.example.core.ui.theme.B200
import com.example.core.ui.theme.B400
import com.example.core.ui.theme.B500
import com.example.core.ui.theme.G300
import com.example.core.ui.theme.G500
import com.example.core.ui.theme.R500

/** Border Shape **/

@Preview(showBackground = true)
@Composable
fun HardShapePreview() {
    ShapeBox(
        shape = SpotShapes.Hard,
        color = B100,
        modifier = Modifier
            .padding(8.dp)
            .width(200.dp)
            .height(80.dp),
        borderWidth = 1.dp,
        borderColor = B500
    )
}

@Preview(showBackground = true)
@Composable
fun SoftShapePreview() {
    ShapeBox(
        shape = SpotShapes.Soft,
        color = B400,
        modifier = Modifier
            .padding(8.dp)
            .width(200.dp)
            .height(80.dp),
    )
}

@Preview(showBackground = true)
@Composable
fun ShapeImageWithBadgePreview() {
    ShapeImageWithBadge(
        shape = SpotShapes.Hard,
        modifier = Modifier.padding(8.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun RoundShapePreview() {
    ShapeBox(
        shape = SpotShapes.Round,
        color = R500,
        modifier = Modifier
            .padding(8.dp)
            .width(200.dp)
            .height(80.dp),
    )
}

@Preview(showBackground = true)
@Composable
fun SoftLeftShapePreview() {
    ShapeBox(

        shape = SpotShapes.SoftLeft,
        color = B200,
        modifier = Modifier
            .padding(8.dp)
            .width(200.dp)
            .height(80.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun SoftRightShapePreview() {
    ShapeBox(
        shape = SpotShapes.SoftRight,
        color = B200,
        modifier = Modifier
            .padding(8.dp)
            .width(200.dp)
            .height(80.dp),
    )
}

@Preview(showBackground = true)
@Composable
fun RoundLeftShapePreview() {
    ShapeBox(
        shape = SpotShapes.RoundLeft,
        color = G300,
        modifier = Modifier
            .padding(8.dp)
            .width(200.dp)
            .height(80.dp),
    )
}

@Preview(showBackground = true)
@Composable
fun RoundRightShapePreview() {
    ShapeBox(
        shape = SpotShapes.RoundRight,
        color = G500,
        modifier = Modifier
            .padding(8.dp)
            .width(200.dp)
            .height(80.dp),
    )
}

/** Style Shape **/

@Preview(showBackground = true)
@Composable
fun StateActivePreview() {
    StateCardActive(
        modifier = Modifier
            .padding(8.dp)
            .width(200.dp)
            .height(80.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun StateSuccessPreview() {
    StateCardSuccess(
        modifier = Modifier
            .padding(8.dp)
            .width(200.dp)
            .height(80.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun StateErrorPreview() {
    StateCardError(
        modifier = Modifier
            .padding(8.dp)
            .width(200.dp)
            .height(80.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun StateWarningPreview() {
    StateCardWarning(
        modifier = Modifier
            .padding(8.dp)
            .width(200.dp)
            .height(80.dp)
    )
}