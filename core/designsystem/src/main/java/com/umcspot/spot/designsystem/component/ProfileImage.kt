package com.umcspot.spot.designsystem.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.model.ImageRef
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun ProfileImage(
    imageRef: ImageRef,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    val painter: Painter? = when (imageRef) {
        ImageRef.None -> painterResource(R.drawable.spot_logo)

        is ImageRef.Name -> {
            val resId = remember(imageRef.name) {
                context.resources.getIdentifier(
                    imageRef.name,
                    "drawable",
                    context.packageName
                )
            }
            if (resId != 0) painterResource(id = resId) else null
        }

        is ImageRef.Url -> {
            rememberAsyncImagePainter(model = imageRef.url)
        }

        is ImageRef.LocalUri ->
            rememberAsyncImagePainter(model = imageRef.uri)

    }

    if (painter != null) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = modifier
                .clip(CircleShape)
        )
    }
}

@Preview(showBackground = false)
@Composable
private fun preview() {
    SpotTheme {
        ProfileImage(
            imageRef = ImageRef.Name("sample"),
            modifier = Modifier.size(screenWidthDp(33.dp))
        )
    }
}
