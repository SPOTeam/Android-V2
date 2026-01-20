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
fun RejectModal(
    modalTitle : String,
    modalDes : String,
    okButtonText : String,
    noButtonText : String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onCancel:() -> Unit= {},
    onDismiss:() -> Unit= {}
) {
    Card(
        modifier = modifier
            .width(screenWidthDp(326.dp))
            .height(screenHeightDp(227.dp)),
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
                painter = painterResource(R.drawable.emoji_sad),
                contentDescription = null,
                modifier = Modifier
                    .size(screenWidthDp(33.dp))
            )

            Spacer(Modifier.height(screenHeightDp(7.dp)))

            Text(
                modifier = Modifier.height(screenHeightDp(30.dp)),
                text = modalTitle,
                style = SpotTheme.typography.h2,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(screenHeightDp(20.dp)))

            Text(
                text = modalDes,
                style = SpotTheme.typography.regular_500,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(screenHeightDp(20.dp)))

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                TextButton(
                    modifier = Modifier
                        .width(screenWidthDp(141.dp))
                        .height(screenHeightDp(39.dp)),
                    text = okButtonText,
                    style = SpotTheme.typography.h5,
                    onClick = onClick,
                    shape = SpotShapes.Soft,
                    state = TextButtonState.R500State,
                )

                TextButton(
                    modifier = Modifier
                        .width(screenWidthDp(141.dp))
                        .height(screenHeightDp(39.dp)),
                    text = noButtonText,
                    style = SpotTheme.typography.h5,
                    onClick = onCancel,
                    shape = SpotShapes.Soft,
                    state = TextButtonState.G500State,
                )
            }
        }
    }
}

@Composable
fun RejectDialog(
    visible: Boolean,
    modalTitle : String,
    modalDes : String,
    okButtonText : String,
    noButtonText : String,
    onDismiss: () -> Unit,
    onClick: () -> Unit,
    onCancel: () -> Unit
) {
    if (!visible) return
    Dialog(onDismissRequest = onDismiss) {
        RejectModal(
            modalTitle = modalTitle,
            modalDes = modalDes,
            okButtonText = okButtonText,
            noButtonText = noButtonText,
            onClick = onClick,
            onCancel = onCancel,
            onDismiss = onDismiss
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun RejectDialog_Preview() {
    SpotTheme {
        RejectDialog(
            visible = true,
            modalTitle = "나가시겠어요?",
            modalDes = "지금 나가면, 쓰던 글은 저장되지 않아요.",
            okButtonText = "네",
            noButtonText = "아니요",
            onDismiss = {},
            onClick = {},
            onCancel = {}
        )
    }
}
