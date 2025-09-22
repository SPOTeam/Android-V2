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
            containerColor = White
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
                        state = ButtonState.B400State,
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
    Surface {
        AcceptDialog(
            visible = true,
            onDismiss = {},
            onClick = {},
            onCancel = {}
        )
    }
}
