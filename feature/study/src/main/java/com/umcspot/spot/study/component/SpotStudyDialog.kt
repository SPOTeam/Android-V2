package com.umcspot.spot.study.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.button.SpotActivationButton
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun SpotStudyDialog(
    onDismissRequest: () -> Unit,
    title: String,
    description: String,
    buttonText: String,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismissRequest) {
        SpotStudyDialogContent(
            onDismissRequest = onDismissRequest,
            title = title,
            description = description,
            buttonText = buttonText,
            onButtonClick = onButtonClick,
            modifier = modifier
        )
    }
}

@Composable
private fun SpotStudyDialogContent(
    onDismissRequest: () -> Unit,
    title: String,
    description: String,
    buttonText: String,
    onButtonClick: () -> Unit,
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
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.ic_check),
            contentDescription = "완료",
            modifier = Modifier.size(33.dp),
            tint = SpotTheme.colors.B500
        )

        Spacer(modifier = Modifier.height(screenHeightDp(7.dp)))

        Text(
            text = title,
            style = SpotTheme.typography.h2,
            color = SpotTheme.colors.black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))

        Text(
            text = description,
            style = SpotTheme.typography.regular_500,
            color = SpotTheme.colors.black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))

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