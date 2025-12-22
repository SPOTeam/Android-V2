package com.umcspot.spot.designsystem.component.modal

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.button.TextButton
import com.umcspot.spot.designsystem.component.button.TextButtonSize
import com.umcspot.spot.designsystem.component.button.TextButtonState
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun DeleteModal(
    modalTitle : String,
    modalDes : String,
    okButtonText : String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onDismiss:() -> Unit= {}
) {
    Card(
        modifier = modifier
            .width(screenWidthDp(326.dp))
            .height(screenHeightDp(227.dp)),
        shape = SpotShapes.Hard,
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
                painter = painterResource(R.drawable.delete),
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

            TextButton(
                modifier = Modifier
                    .width(screenWidthDp(156.dp))
                    .height(screenHeightDp(39.dp)),
                text = okButtonText,
                style = SpotTheme.typography.h5,
                onClick = onClick,
                shape = SpotShapes.Soft,
                state = TextButtonState.G500State,
            )

        }
    }
}

@Composable
fun DeleteDialog(
    visible: Boolean,
    modalTitle : String,
    modalDes : String,
    okButtonText : String,
    onClick: () -> Unit,
    onDismiss: () -> Unit,
) {
    if (!visible) return
    Dialog(onDismissRequest = onDismiss) {
        DeleteModal(
            modifier = Modifier.fillMaxWidth(),
            modalTitle = modalTitle,
            modalDes = modalDes,
            okButtonText = okButtonText,
            onClick = onClick,
            onDismiss = onDismiss
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun DeleteDialog_Preview() {
    SpotTheme {
        DeleteDialog(
            visible = true,
            modalTitle = "이 글을 삭제하시겠어요?",
            modalDes = "한번 삭제한 글은 되돌릴 수 없어요.",
            okButtonText = "삭제",
            onClick = {},
            onDismiss = {}
        )
    }
}
