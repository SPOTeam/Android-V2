package com.umcspot.spot.designsystem.shapes

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.theme.B100
import com.umcspot.spot.designsystem.theme.B200
import com.umcspot.spot.designsystem.theme.B400
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.G300
import com.umcspot.spot.designsystem.theme.G500
import com.umcspot.spot.designsystem.theme.R500
import com.umcspot.spot.designsystem.theme.SpotTheme


/** Border Shape **/

@Preview(showBackground = true)
@Composable
fun HardShapePreview() {
    SpotTheme {
        ShapeBox(
            shape = SpotShapes.Hard,
            color = SpotTheme.colors.B100,
            modifier = Modifier
                .padding(8.dp)
                .width(200.dp)
                .height(80.dp),
            borderWidth = 1.dp,
            borderColor = B500
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SoftShapePreview() {
    SpotTheme {
        ShapeBox(
            shape = SpotShapes.Soft,
            color = SpotTheme.colors.B400,
            modifier = Modifier
                .padding(8.dp)
                .width(200.dp)
                .height(80.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ShapeImageWithBadgePreview() {
    SpotTheme {
        ShapeImageWithBadge(
            shape = SpotShapes.Hard,
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RoundShapePreview() {
    SpotTheme {
        ShapeBox(
            shape = SpotShapes.Round,
            color = SpotTheme.colors.R500,
            modifier = Modifier
                .padding(8.dp)
                .width(200.dp)
                .height(80.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SoftLeftShapePreview() {
    SpotTheme {
        ShapeBox(
            shape = SpotShapes.SoftLeft,
            color = SpotTheme.colors.B200,
            modifier = Modifier
                .padding(8.dp)
                .width(200.dp)
                .height(80.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SoftRightShapePreview() {
    SpotTheme {
        ShapeBox(
            shape = SpotShapes.SoftRight,
            color = SpotTheme.colors.B200,
            modifier = Modifier
                .padding(8.dp)
                .width(200.dp)
                .height(80.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RoundLeftShapePreview() {
    SpotTheme {
        ShapeBox(
            shape = SpotShapes.RoundLeft,
            color = SpotTheme.colors.G300,
            modifier = Modifier
                .padding(8.dp)
                .width(200.dp)
                .height(80.dp),
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RoundRightShapePreview() {
    SpotTheme {
        ShapeBox(
            shape = SpotShapes.RoundRight,
            color = SpotTheme.colors.G500,
            modifier = Modifier
                .padding(8.dp)
                .width(200.dp)
                .height(80.dp),
        )
    }
}

/** Style Shape **/

@Preview(showBackground = true)
@Composable
fun StateActivePreview() {
    SpotTheme {
        StateCardActive(
            modifier = Modifier
                .padding(8.dp)
                .width(200.dp)
                .height(80.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StateSuccessPreview() {
    SpotTheme {
        StateCardSuccess(
            modifier = Modifier
                .padding(8.dp)
                .width(200.dp)
                .height(80.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StateErrorPreview() {
    SpotTheme {
        StateCardError(
            modifier = Modifier
                .padding(8.dp)
                .width(200.dp)
                .height(80.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun StateWarningPreview() {
    SpotTheme {
        StateCardWarning(
            modifier = Modifier
                .padding(8.dp)
                .width(200.dp)
                .height(80.dp)
        )
    }
}