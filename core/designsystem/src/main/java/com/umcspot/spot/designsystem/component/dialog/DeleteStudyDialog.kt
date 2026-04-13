package com.umcspot.spot.designsystem.component.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.G500
import com.umcspot.spot.designsystem.theme.R500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.noRippleClickable
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun DeleteStudyDialog(
    visible: Boolean,
    onDelete: () -> Unit,
    onDismiss: () -> Unit,
) {
    if (!visible) return

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
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
                    .padding(17.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        painter = painterResource(R.drawable.dismiss),
                        contentDescription = null,
                        modifier = Modifier
                            .width(screenWidthDp(26.dp))
                            .noRippleClickable { onDismiss() }
                    )
                }

                Text(
                    text = "스터디를 삭제하시겠습니까?",
                    style = SpotTheme.typography.h2,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(screenHeightDp(10.dp)))

                Text(
                    text = "삭제한 스터디는 복구 불가합니다.",
                    style = SpotTheme.typography.regular_500,
                    color = SpotTheme.colors.black,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(screenHeightDp(20.dp)))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(screenWidthDp(12.dp))
                ) {
                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = onDelete,
                        shape = SpotShapes.Soft,
                        border = BorderStroke(1.dp, SpotTheme.colors.R500),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = SpotTheme.colors.R500,
                            containerColor = Color.Transparent
                        )
                    ) {
                        Text(text = "삭제하기", style = SpotTheme.typography.h5)
                    }

                    OutlinedButton(
                        modifier = Modifier.weight(1f),
                        onClick = onDismiss,
                        shape = SpotShapes.Soft,
                        border = BorderStroke(1.dp, SpotTheme.colors.G500),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = SpotTheme.colors.G500,
                            containerColor = Color.Transparent
                        )
                    ) {
                        Text(text = "취소", style = SpotTheme.typography.h5)
                    }
                }
            }
        }
    }
}