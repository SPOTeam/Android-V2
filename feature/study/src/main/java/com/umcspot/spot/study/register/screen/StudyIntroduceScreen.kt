package com.umcspot.spot.study.register.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.Default
import com.umcspot.spot.designsystem.theme.G100
import com.umcspot.spot.designsystem.theme.G400
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.noRippleClickable
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun StudyIntroduceScreen(
    description: String,
    onDescriptionChange: (String) -> Unit,
    onIntroduceValid: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    var isImageSelected by remember { mutableStateOf(false) }

    LaunchedEffect(description) {
        onIntroduceValid(description.isNotEmpty())
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = screenHeightDp(65.dp))
    ) {
        Text(
            text = "마지막으로. 이 스터디에 대해\n자세히 소개할 것이 있다면 작성해주세요.",
            style = SpotTheme.typography.h3
        )
        Spacer(modifier = Modifier.height(screenHeightDp(4.dp)))

        Text(
            text = "자세히 적을수록, 모집 확률은 올라가요.",
            color = SpotTheme.colors.G400,
            style = SpotTheme.typography.regular_500
        )

        Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))

        BasicTextField(
            value = description,
            onValueChange = onDescriptionChange,
            modifier = Modifier.fillMaxWidth(),
            textStyle = SpotTheme.typography.medium_500.copy(
                color = SpotTheme.colors.black
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = screenHeightDp(134.dp))
                        .border(
                            width = 1.dp,
                            color = if (description.isEmpty()) SpotTheme.colors.Default else SpotTheme.colors.B500,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(
                            horizontal = screenWidthDp(10.dp),
                            vertical = screenHeightDp(7.dp)
                        )
                ) {
                    if (description.isEmpty()) {
                        Text(
                            text = "이 스터디의 목표, 선호하는 스터디원, 앞으로의 진행 방식 등 ",
                            style = SpotTheme.typography.medium_500,
                            color = SpotTheme.colors.Default
                        )
                    }
                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "스터디 대표 이미지",
                style = SpotTheme.typography.h5,
                color = SpotTheme.colors.black
            )
            Spacer(modifier = Modifier.width(screenWidthDp(4.dp)))
            Text(
                text = "(선택)",
                style = SpotTheme.typography.h5,
                color = SpotTheme.colors.G400
            )
        }

        Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

        Box(
            modifier = Modifier
                .size(width = screenWidthDp(80.dp), height = screenHeightDp(80.dp))
                .clip(RoundedCornerShape(6.dp))
                .background(if (isImageSelected) SpotTheme.colors.black else SpotTheme.colors.G100) // 이미지가 없을 때 배경색 지정 (G100)
                .noRippleClickable {
                    isImageSelected = !isImageSelected
                },
            contentAlignment = Alignment.Center
        ) {
            if (isImageSelected) {
                Image(
                    painter = painterResource(id = R.drawable.image),
                    contentDescription = "Selected Study Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.image),
                    contentDescription = "Upload Image",
                    tint = SpotTheme.colors.G400,
                    modifier = Modifier.size(screenWidthDp(24.dp))
                )
            }
        }
    }
}