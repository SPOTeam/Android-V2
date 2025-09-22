package com.example.core.ui.component.modal

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.data.study.StudyItem
import com.example.core.ui.R
import com.example.core.ui.component.button.ButtonState
import com.example.core.ui.component.button.TextButtonM
import com.example.core.ui.shapes.SpotShapes
import com.example.core.ui.theme.B500
import com.example.core.ui.theme.G300
import com.example.core.ui.theme.SpotTypography
import com.example.core.ui.theme.White

@Composable
fun RejectModal(
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
            containerColor = White
        )
    ) {
        Column(
            modifier = Modifier.padding(15.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.emoji_sad),
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
                    style = SpotTypography.bodyMedium500.copy(fontSize = 16.sp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = modalDes,
                    style = SpotTypography.bodySmall500.copy(fontSize = 12.sp)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButtonM(
                        text = buttonText,
                        onClick = onClick,
                        state = ButtonState.R500State,
                        modifier = Modifier.weight(1f)
                    )

                    TextButtonM(
                        text = "취소",
                        onClick = onCancel,
                        state = ButtonState.G500State,
                        modifier = Modifier.weight(1f)
                    )
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
    buttonText : String,
    onDismiss: () -> Unit,
    onClick: () -> Unit,
    onCancel: () -> Unit
) {
    if (!visible) return
    androidx.compose.ui.window.Dialog(onDismissRequest = onDismiss) {
        RejectModal(
            modalTitle = modalTitle,
            modalDes = modalDes,
            buttonText = buttonText,
            onClick = onClick,
            onCancel = onCancel
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
fun ReDialog_Preview() {
    Surface {
        RejectDialog(
            visible = true,
            modalTitle = "불참하시겠습니까?",
            modalDes = "신청했던 스터디를 참여 취소하면\n다시 결정을 번복할 수 없어요.",
            buttonText = "불참",
            onDismiss = {},
            onClick = {},
            onCancel = {}
        )
    }
}
