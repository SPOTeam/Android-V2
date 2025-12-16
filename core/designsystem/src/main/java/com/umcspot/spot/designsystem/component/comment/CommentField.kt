package com.umcspot.spot.designsystem.component.comment

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.shapes.ShapeBox
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.B100
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.Black
import com.umcspot.spot.designsystem.theme.G300
import com.umcspot.spot.designsystem.theme.G400
import com.umcspot.spot.designsystem.theme.SpotTheme

data class CommentFieldColors(
    val bg: Color,
    val text: Color,
    val placeholder: Color,
    val border: Color,
    val sendIcon: Color,
)

enum class CommentFieldState(
    val noFocus: CommentFieldColors,
    val focused: CommentFieldColors,
) {
    Default(
        noFocus = CommentFieldColors(
            bg = B100,
            text = G400,
            placeholder = G400,
            border = G300,
            sendIcon = G300,
        ),
        focused = CommentFieldColors(
            bg = B100,
            text = Black,
            placeholder = Black,
            border = B500,
            sendIcon = B500,
        )
    )
}

@Composable
fun CommentField(
    onSendComment: (String) -> Unit,
    comment: String,
    onCommentChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    canWrite: Boolean = true,
    onFocusChanged: (Boolean) -> Unit = {},
    state: CommentFieldState = CommentFieldState.Default,
) {
    var isFocused by remember { mutableStateOf(false) }

    val placeholderText =
        if (canWrite) "댓글을 입력하세요"
        else "댓글 기능은 스터디원만 이용 가능합니다."

    val sendEnabled = canWrite && comment.isNotBlank()

    val colors = when {
        !canWrite -> state.noFocus
        isFocused -> state.focused
        else -> state.noFocus
    }

    ShapeBox(
        shape = SpotShapes.Hard,
        color = colors.bg,
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 44.dp)
            .wrapContentHeight(),
        borderWidth = 1.dp,
        borderColor = colors.border
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 12.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            BasicTextField(
                value = if (canWrite) comment else "",
                onValueChange = { if (canWrite) onCommentChange(it) },
                enabled = canWrite,
                singleLine = false,
                maxLines = 4, // ✅ 줄바꿈 + 최대 줄 수 (원하는 값으로)
                cursorBrush = SolidColor(B500),
                textStyle = SpotTheme.typography.medium_400.copy(color = colors.text),
                modifier = Modifier
                    .weight(1f)
                    .onFocusChanged {
                        isFocused = it.isFocused
                        onFocusChanged(it.isFocused)
                    },
                decorationBox = { innerTextField ->
                    if (!canWrite || comment.isBlank()) {
                        Text(
                            text = placeholderText,
                            style = SpotTheme.typography.medium_400,
                            color = colors.placeholder
                        )
                    }
                    innerTextField()
                }
            )

            Image(
                painter = painterResource(id = R.drawable.send),
                contentDescription = "send",
                modifier = Modifier
                    .size(20.dp)
                    .alpha(if (sendEnabled) 1f else 0.35f)
                    .clickable(enabled = sendEnabled) {
                        val text = comment.trim()
                        onSendComment(text)
                        onCommentChange("")
                    },
                colorFilter = ColorFilter.tint(colors.sendIcon)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun CommentFieldPreview_CanWrite() {
    SpotTheme {
        CommentField(
            onSendComment = {},
            comment = "",
            onCommentChange = {},
            canWrite = false,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CommentFieldPreview_CantWrite() {
    SpotTheme {
        CommentField(
            onSendComment = {},
            comment = "",
            onCommentChange = {},
            canWrite = false,
            modifier = Modifier.padding(16.dp)
        )
    }
}

