package com.umcspot.spot.designsystem.component.modal

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.button.TextButton
import com.umcspot.spot.designsystem.component.button.TextButtonState
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.SpotTheme

@Composable
fun AcceptModal(
    modalTitle : String,
    modalDes : String,
    buttonText : String,
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
        Column(
            modifier = Modifier.padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.success_default),
                colorFilter = ColorFilter.tint(B500),
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
            )

            // 텍스트 + 통계
            Column(
                verticalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = modalTitle,
                    style = SpotTheme.typography.medium_500.copy(fontSize = 16.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = modalDes,
                    style = SpotTheme.typography.small_500.copy(fontSize = 12.sp)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(
                        text = buttonText,
                        onClick = onClick,
                        state = TextButtonState.B500State,
                        modifier = Modifier.weight(1f)
                    )

                    TextButton(
                        text = "취소",
                        onClick = onCancel,
                        state = TextButtonState.G500State,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun AcceptDialog(
    visible: Boolean,
    onDismiss: () -> Unit,
    onClick: () -> Unit,
    onCancel: () -> Unit
) {
    if (!visible) return
    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        AcceptModal(
            modalTitle = "참여 확정",
            modalDes = "호스트가 참여를 승인했어요.",
            buttonText = "확정",
            onClick = onClick,
            onCancel = onCancel
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun AcceptDialog_Preview() {
    SpotTheme {
        AcceptDialog(
            visible = true,
            onDismiss = {},
            onClick = {},
            onCancel = {}
        )
    }
}
