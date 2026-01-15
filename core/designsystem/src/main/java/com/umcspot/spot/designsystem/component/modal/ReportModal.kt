package com.umcspot.spot.designsystem.component.modal

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.button.TextButton
import com.umcspot.spot.designsystem.component.button.TextButtonState
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.G200
import com.umcspot.spot.designsystem.theme.G400
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp


@Composable
fun ReportModal(
    modalTitle : String,
    modalDes : String,
    reason: String,
    onReasonChange: (String) -> Unit,
    okButtonText : String,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit = {},
    onDismiss:() -> Unit= {}
) {
    Card(
        modifier = modifier
            .width(screenWidthDp(326.dp))
            .heightIn(min = screenHeightDp(282.dp), max = screenHeightDp(367.dp)),
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
                    modifier = Modifier
                        .size(screenWidthDp(26.dp))
                        .clickable { onDismiss() }
                )
            }
            Text(
                modifier = Modifier.height(screenHeightDp(30.dp)),
                text = modalTitle,
                style = SpotTheme.typography.h2,
                textAlign = TextAlign.Center
            )

            Spacer(Modifier.height(screenHeightDp(10.dp)))

            Text(
                text = modalDes,
                style = SpotTheme.typography.regular_500,
                textAlign = TextAlign.Center,
            )

            Spacer(Modifier.height(screenHeightDp(20.dp)))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "신고 이유",
                    style = SpotTheme.typography.h5,
                )

                Spacer(modifier = Modifier.width(screenWidthDp(12.dp)))

                Text(
                    text = "(선택)",
                    style = SpotTheme.typography.h5,
                    color = SpotTheme.colors.G400
                )
            }

            Spacer(modifier = Modifier.height(screenHeightDp(7.dp)))

            OutlinedTextField(
                value = reason,
                onValueChange = onReasonChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = screenHeightDp(35.dp), max = screenHeightDp(120.dp))
                    .clip(SpotShapes.Hard),
                placeholder = {

                },
                textStyle = SpotTheme.typography.regular_500,
                singleLine = false,
                minLines = 1,
                maxLines = Int.MAX_VALUE,
                shape = SpotShapes.Hard,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = SpotTheme.colors.white,
                    unfocusedContainerColor = SpotTheme.colors.white,
                    disabledContainerColor = SpotTheme.colors.white,
                    focusedBorderColor = SpotTheme.colors.B500,
                    unfocusedBorderColor = SpotTheme.colors.G200,
                    cursorColor = SpotTheme.colors.B500
                )
            )

            Spacer(Modifier.height(screenHeightDp(16.dp)))

            TextButton(
                modifier = Modifier
                    .width(screenWidthDp(156.dp))
                    .height(screenHeightDp(39.dp)),
                text = okButtonText,
                style = SpotTheme.typography.h5,
                onClick = { onClick(reason) },
                shape = SpotShapes.Soft,
            )
        }
    }
}

@Composable
fun ReportDialog(
    visible: Boolean,
    modalTitle: String,
    modalDes: String,
    reason: String,
    onReasonChange: (String) -> Unit,
    okButtonText: String,
    onClick: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    if (!visible) return
    Dialog(onDismissRequest = onDismiss) {
        ReportModal(
            modalTitle = modalTitle,
            modalDes = modalDes,
            reason = reason,
            onReasonChange = onReasonChange,
            okButtonText = okButtonText,
            onClick = onClick,
            onDismiss = onDismiss
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 640)
@Composable
private fun ReportDialog_Preview() {
    SpotTheme {
        var reason by rememberSaveable { mutableStateOf("") }
        ReportDialog(
            visible = true,
            modalTitle = "스터디원을 신고하시겠습니까?",
            modalDes = "신고 이유를 작성해주세요.\nSPOT 내부 검토 후, 탈퇴 신청을 용인합니다.",
            reason = reason,
            onReasonChange = { reason = it },
            okButtonText = "완료",
            onClick = {  },
            onDismiss = {}
        )
    }
}

@Preview(
    name = "ReportDialog – Long Text (Max Height Test)",
    showBackground = true,
    widthDp = 360,
    heightDp = 640,
)
@Composable
private fun ReportDialog_LongText_InteractivePreview() {
    SpotTheme {
        var visible by rememberSaveable { mutableStateOf(true) }

        val longText = buildString {
            append("아래는 긴 문장 예시입니다. 입력 줄 수가 늘어날 때 카드가 최대 높이(367dp)까지만 커지고, 그 이후는 스크롤됩니다. ")
            append("서비스 운영 정책에 따라 사실 확인 및 내부 검토 절차를 거치며, 반복 위반 시 이용 제한이 적용될 수 있습니다. ")
            append("더 자세한 사항은 공지사항을 참고해주세요. ")
        }

        var reason by rememberSaveable { mutableStateOf(longText) }

        ReportDialog(
            visible = visible,
            modalTitle = "스터디원을 신고하시겠습니까?",
            modalDes = "신고 이유를 작성해주세요.\nSPOT 내부 검토 후, 탈퇴 신청을 용인합니다.",
            reason = reason,
            onReasonChange = { reason = it },
            okButtonText = "완료",
            onClick = { typed ->
                visible = false
            },
            onDismiss = { visible = false }
        )
    }
}