package com.umcspot.spot.designsystem.component.modal

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.button.TextButton
import com.umcspot.spot.designsystem.component.button.TextButtonState
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun AcceptModal(
    painter: Painter,
    painterTint: Color,
    modalTitle : String,
    modalDes : String?,
    okButtonText : String,
    noButtonText: String?,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onDismiss:() -> Unit = {}
) {
    Card(
        modifier = modifier
            .width(screenWidthDp(326.dp))
            .wrapContentHeight(),
        shape = SpotShapes.Round,
        colors = CardDefaults.elevatedCardColors(
            containerColor = SpotTheme.colors.white
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(17.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Image(
                    painter = painterResource(R.drawable.dismiss),
                    contentDescription = null,
                    modifier = Modifier.clickable { onDismiss() }
                )
            }

            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .size(screenWidthDp(33.dp)),
                colorFilter = ColorFilter.tint(painterTint)
            )

            Spacer(Modifier.height(screenHeightDp(7.dp)))

            Text(
                modifier = Modifier.height(screenHeightDp(30.dp)),
                text = modalTitle,
                style = SpotTheme.typography.h2,
                textAlign = TextAlign.Center

            )

            Spacer(Modifier.height(screenHeightDp(20.dp)))

            if(modalDes != null) {
                Text(
                    text = modalDes,
                    style = SpotTheme.typography.regular_500,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(screenHeightDp(20.dp)))
            }

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                TextButton(
                    modifier = Modifier
                        .width(screenWidthDp(141.dp))
                        .height(screenHeightDp(39.dp)),
                    text = okButtonText,
                    style = SpotTheme.typography.h5,
                    onClick = onClick,
                    shape = SpotShapes.Soft,
                    state = TextButtonState.B500State,
                )

                if(noButtonText != null) {
                    TextButton(
                        modifier = Modifier
                            .width(screenWidthDp(141.dp))
                            .height(screenHeightDp(39.dp)),
                        text = noButtonText,
                        style = SpotTheme.typography.h5,
                        onClick = onDismiss,
                        shape = SpotShapes.Soft,
                        state = TextButtonState.G500State,
                    )
                }
            }
        }
    }
}

@Composable
fun AcceptDialog(
    visible: Boolean,
    painter: Painter = painterResource(R.drawable.ic_check),
    painterTint: Color = Color.Unspecified,
    modalTitle : String,
    modalDes : String?,
    okButtonText : String,
    noButtonText: String?,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
) {
    if (!visible) return
    Dialog(onDismissRequest = onDismiss) {
        AcceptModal(
            painter = painter,
            painterTint = painterTint,
            modalTitle = modalTitle,
            modalDes = modalDes,
            okButtonText = okButtonText,
            noButtonText = noButtonText,
            onClick = onClick,
            onDismiss = onDismiss,
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun AcceptDialog_Preview() {
    SpotTheme {
        AcceptDialog(
            visible = true,
            modalTitle = "스터디원 신고 완료",
            modalDes = "스터디원 신고가 완료되었어요.\n쾌적한 서비스 이용을 위해 항상 노력하겠습니다.",
            noButtonText = "취소",
            okButtonText = "확인",
            onClick = {},
            onDismiss = {}
        )
    }
}
