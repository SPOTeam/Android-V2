package com.umcspot.spot.designsystem.component.modal

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.button.TextButtonState
import com.umcspot.spot.designsystem.component.button.TextButtonM
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.designsystem.theme.White

@Composable
fun RejectModal(
    modalTitle : String,
    modalDes : String,
    buttonOKText : String,
    buttonNOText : String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    onCancel:() -> Unit= {}
) {
    Card(
        modifier = modifier,
        shape = SpotShapes.Hard,
        colors = CardDefaults.elevatedCardColors(
            containerColor = SpotTheme.colors.white
        )
    ) {
        Box(
            modifier = Modifier.fillMaxWidth().padding(15.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.dismiss),
                contentDescription = null,
                modifier = Modifier
                    .size(25.dp)
                    .align(Alignment.TopEnd)
                    .clickable(
                        onClick = {onCancel}
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top =  25.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(R.drawable.emoji_sad),
                    contentDescription = null,
                    modifier = Modifier.size(40.dp)
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = modalTitle,
                        style = SpotTheme.typography.medium_500,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = modalDes,
                        style = SpotTheme.typography.medium_500
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        TextButtonM(
                            text = buttonOKText,
                            onClick = onClick,
                            state = TextButtonState.R500State,
                            modifier = Modifier.weight(1f)
                        )
                        TextButtonM(
                            text = buttonNOText,
                            onClick = onCancel,
                            state = TextButtonState.G500State,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun RejectDialog(
    visible: Boolean,
    modalTitle : String,
    modalDes : String,
    buttonOKText : String,
    buttonNOText : String,
    onDismiss: () -> Unit,
    onClick: () -> Unit,
    onCancel: () -> Unit
) {
    if (!visible) return
    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        RejectModal(
            modalTitle = modalTitle,
            modalDes = modalDes,
            buttonOKText = buttonOKText,
            buttonNOText = buttonNOText,
            onClick = onClick,
            onCancel = onCancel
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun ReDialog_Preview() {
    SpotTheme {
        RejectDialog(
            visible = true,
            modalTitle = "불참하시겠습니까?",
            modalDes = "신청했던 스터디를 참여 취소하면\n다시 결정을 번복할 수 없어요.",
            buttonOKText = "불참",
            buttonNOText = "아니요",
            onDismiss = {},
            onClick = {},
            onCancel = {}
        )
    }
}
