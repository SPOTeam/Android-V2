package com.umcspot.spot.study.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.button.SpotActivationButton
import com.umcspot.spot.designsystem.theme.B200
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.R500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun SpotStudyApplyDialog(
    onDismissRequest: () -> Unit,
    onApplySubmit: (String) -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        SpotStudyApplyDialogContent(
            onDismissRequest = onDismissRequest,
            onApplySubmit = onApplySubmit
        )
    }
}

@Composable
private fun SpotStudyApplyDialogContent(
    onDismissRequest: () -> Unit,
    onApplySubmit: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var text by remember { mutableStateOf("") }
    val maxLength = 100

    Column(
        modifier = modifier
            .width(screenWidthDp(335.dp))
            .background(
                color = SpotTheme.colors.white,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(17.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = onDismissRequest,
            modifier = Modifier
                .size(20.dp)
                .align(Alignment.End)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.dismiss),
                contentDescription = "닫기",
                tint = SpotTheme.colors.black
            )
        }

        Text(
            text = "스터디 참여하기",
            style = SpotTheme.typography.h2,
            color = SpotTheme.colors.black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

        Text(
            text = "한 번 신청한 스터디는 취소가 불가하니\n신중하게 신청해주세요.",
            style = SpotTheme.typography.regular_500,
            color = SpotTheme.colors.black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeightDp(141.dp))
                .background(
                    color = SpotTheme.colors.white,
                    shape = RoundedCornerShape(6.dp)
                )
                .border(
                    width = 1.dp,
                    color = SpotTheme.colors.B200,
                    shape = RoundedCornerShape(6.dp)
                )
                .padding(vertical = screenHeightDp(7.dp), horizontal = screenWidthDp(10.dp))
        ) {
            BasicTextField(
                value = text,
                onValueChange = { if (it.length <= maxLength) text = it },
                modifier = Modifier.fillMaxSize(),
                textStyle = SpotTheme.typography.medium_500.copy(color = SpotTheme.colors.black),
                cursorBrush = SolidColor(SpotTheme.colors.black)
            )

            Text(
                text = "(${text.length}/$maxLength)",
                modifier = Modifier.align(Alignment.BottomEnd),
                style = SpotTheme.typography.regular_500,
                color = SpotTheme.colors.gray400
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            modifier = Modifier.fillMaxWidth(),
            text = "스터디에 참여하고자 하는 이유, 자기소개 등 간단하게\n 작성하면 돼요.",
            style = SpotTheme.typography.regular_500,
            color = SpotTheme.colors.gray400,
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))

        SpotActivationButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = screenWidthDp(68.dp)),
            buttonText = "신청",
            isEnabled = text.isNotBlank(),
            onClick = { onApplySubmit(text) },
            style = SpotTheme.typography.h5
        )
    }
}

@Composable
fun SpotStudyDialog(
    onDismissRequest: () -> Unit,
    title: String,
    description: String,
    buttonText: String,
    onButtonClick: () -> Unit,
    showCheckIcon: Boolean = true,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismissRequest) {
        SpotStudyDialogContent(
            onDismissRequest = onDismissRequest,
            title = title,
            description = description,
            buttonText = buttonText,
            onButtonClick = onButtonClick,
            showCheckIcon = showCheckIcon,
            modifier = modifier
        )
    }
}

@Composable
fun LeaveStudyDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!visible) return

    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(
                    color = SpotTheme.colors.white,
                    shape = RoundedCornerShape(14.dp)
                )
                .padding(17.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            IconButton(
                onClick = onDismissRequest,
                modifier = Modifier
                    .size(20.dp)
                    .align(Alignment.End)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.dismiss),
                    contentDescription = "닫기",
                    tint = SpotTheme.colors.black
                )
            }

            Spacer(modifier = Modifier.height(screenHeightDp(4.dp)))

            Icon(
                painter = painterResource(id = R.drawable.ic_sad),
                contentDescription = null,
                modifier = Modifier.size(33.dp),
                tint = SpotTheme.colors.R500
            )

            Spacer(modifier = Modifier.height(screenHeightDp(7.dp)))

            Text(
                text = "정말 나가시겠어요?",
                style = SpotTheme.typography.h2,
                color = SpotTheme.colors.black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))

            SpotActivationButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = screenWidthDp(68.dp)),
                buttonText = "스터디 나가기",
                isEnabled = true,
                onClick = onButtonClick,
                style = SpotTheme.typography.h5
            )

            Spacer(modifier = Modifier.height(screenHeightDp(4.dp)))
        }
    }
}
@Composable
private fun SpotStudyDialogContent(
    onDismissRequest: () -> Unit,
    title: String,
    description: String,
    buttonText: String,
    onButtonClick: () -> Unit,
    showCheckIcon: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = SpotTheme.colors.white,
                shape = RoundedCornerShape(14.dp)
            )
            .padding(17.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = onDismissRequest,
            modifier = Modifier
                .size(20.dp)
                .align(Alignment.End)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.dismiss),
                contentDescription = "닫기",
                tint = SpotTheme.colors.black
            )
        }

        if (showCheckIcon) {
            Icon(
                painter = painterResource(id = R.drawable.ic_check),
                contentDescription = "완료",
                modifier = Modifier.size(33.dp),
                tint = SpotTheme.colors.B500
            )
            Spacer(modifier = Modifier.height(screenHeightDp(7.dp)))
        }

        Text(
            text = title,
            style = SpotTheme.typography.h2,
            color = SpotTheme.colors.black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))

        if (description.isNotBlank()) {
            Text(
                text = description,
                style = SpotTheme.typography.regular_500,
                color = SpotTheme.colors.black,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))
        }



        SpotActivationButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = screenWidthDp(68.dp)),
            buttonText = buttonText,
            isEnabled = true,
            onClick = onButtonClick,
            style = SpotTheme.typography.h5
        )
    }
}