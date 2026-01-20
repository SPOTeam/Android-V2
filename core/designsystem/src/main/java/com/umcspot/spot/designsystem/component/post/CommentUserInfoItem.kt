package com.umcspot.spot.designsystem.component.post

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.component.ProfileImage
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.model.ImageRef
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun CommentUserInfo(
    modifier : Modifier = Modifier,
    commentWriterName : String,
    commentWriterImage : ImageRef,
) {
    Row(
        modifier = modifier
            .wrapContentSize(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ProfileImage(
            imageRef = commentWriterImage,
            modifier = Modifier.size(screenWidthDp(33.dp))
        )
        Spacer(Modifier.width(screenWidthDp(7.dp)))

        Text(
            text = commentWriterName,
            style = SpotTheme.typography.medium_400
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun preview() {
    SpotTheme {
        CommentUserInfo(
            commentWriterName = "사용자",
            commentWriterImage = ImageRef.Name("sample"),
        )
    }
}

