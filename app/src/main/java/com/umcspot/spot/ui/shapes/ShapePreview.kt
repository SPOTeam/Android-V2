package com.umcspot.spot.ui.shapes

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.umcspot.spot.ui.theme.*

/** Border Shape **/

@Preview(showBackground = true)
@Composable
fun HardShapePreview() {
    ShapeBox(
        width = 200.dp,
        height = 80.dp,
        shape = SpotShapes.Hard,
        color = B100,
        modifier = Modifier.padding(8.dp),
        borderWidth = 1.dp,
        borderColor = B500
    )
}

@Preview(showBackground = true)
@Composable
fun SoftShapePreview() {
    ShapeBox(
        width = 200.dp,
        height = 80.dp,
        shape = SpotShapes.Soft,
        color = B400,
        modifier = Modifier.padding(8.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun RoundShapePreview() {
    ShapeBox(
        width = 200.dp,
        height = 80.dp,
        shape = SpotShapes.Round,
        color = R500,
        modifier = Modifier.padding(8.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun SoftLeftShapePreview() {
    ShapeBox(
        width = 200.dp,
        height = 80.dp,
        shape = SpotShapes.SoftLeft,
        color = B200,
        modifier = Modifier.padding(8.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun SoftRightShapePreview() {
    ShapeBox(
        width = 200.dp,
        height = 80.dp,
        shape = SpotShapes.SoftRight,
        color = B200,
        modifier = Modifier.padding(8.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun RoundLeftShapePreview() {
    ShapeBox(
        width = 200.dp,
        height = 80.dp,
        shape = SpotShapes.RoundLeft,
        color = G300,
        modifier = Modifier.padding(8.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun RoundRightShapePreview() {
    ShapeBox(
        width = 200.dp,
        height = 80.dp,
        shape = SpotShapes.RoundRight,
        color = G500,
        modifier = Modifier.padding(8.dp)
    )
}

/** Style Shape **/

@Preview(showBackground = true)
@Composable
fun StateActivePreview() {
    StateCardActive(
        width = 160.dp,
        height = 88.dp,
        modifier = Modifier.padding(8.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun StateSuccessPreview() {
    StateCardSuccess(
        width = 160.dp,
        height = 88.dp,
        modifier = Modifier.padding(8.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun StateErrorPreview() {
    StateCardError(
        width = 160.dp,
        height = 88.dp,
        modifier = Modifier.padding(8.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun StateWarningPreview() {
    StateCardWarning(
        width = 160.dp,
        height = 88.dp,
        modifier = Modifier.padding(8.dp)
    )
}