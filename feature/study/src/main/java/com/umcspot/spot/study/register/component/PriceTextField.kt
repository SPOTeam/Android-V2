package com.umcspot.spot.study.register.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun PriceTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.height(screenHeightDp(35.dp)),
        textStyle = SpotTheme.typography.medium_500.copy(
            color = SpotTheme.colors.black,
            textAlign = TextAlign.Start
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true,
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = SpotTheme.colors.white,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = SpotTheme.colors.B500,
                        shape = RoundedCornerShape(6.dp)
                    )
                    .padding(end = screenWidthDp(7.dp)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = screenWidthDp(10.dp))
                ) {
                    innerTextField()
                }

                Spacer(modifier = Modifier.width(screenWidthDp(4.dp)))

                Text(
                    text = "원",
                    style = SpotTheme.typography.medium_500,
                    color = SpotTheme.colors.black
                )
            }
        }
    )
}