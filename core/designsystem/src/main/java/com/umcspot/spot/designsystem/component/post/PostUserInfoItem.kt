package com.umcspot.spot.designsystem.component.post

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.component.ProfileImage
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.model.ImageRef

@Composable
fun UserInfo(
    modifier : Modifier = Modifier,
    postWriterName : String,
    postWriterImage : ImageRef,
    postWriteAt : String,
) {
    Row(
        modifier = modifier
            .wrapContentSize()
    ) {
        ProfileImage(
            imageRef = postWriterImage,
            modifier = Modifier.size(44.dp)
        )
        Spacer(Modifier.width(10.dp))
        Column (

        ) {
            Text(
                text = postWriterName,
                style = SpotTheme.typography.medium_400
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = postWriteAt,
                style = SpotTheme.typography.regular_400
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun preview() {
    SpotTheme {
        UserInfo(
            postWriterName = "사용자",
            postWriterImage = ImageRef.Name("sample"),
            postWriteAt = "25.44.44  44:44"
        )
    }
}

