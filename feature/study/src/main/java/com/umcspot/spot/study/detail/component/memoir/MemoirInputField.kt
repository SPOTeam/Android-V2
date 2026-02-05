package com.umcspot.spot.study.detail.component.memoir

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.theme.G200
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun MemoirInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 24.dp)
    ) {
        Text(
            text = label,
            style = SpotTheme.typography.medium_500,
            color = SpotTheme.colors.black
        )

        Spacer(modifier = Modifier.height(screenHeightDp(7.dp)))

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = screenHeightDp(98.dp)),
            textStyle = SpotTheme.typography.medium_500.copy(
                color = SpotTheme.colors.black
            ),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                        .border(
                            width = 1.dp,
                            color = SpotTheme.colors.G200,
                            shape = RoundedCornerShape(6.dp)
                        )
                        .padding(vertical = screenHeightDp(7.dp), horizontal = screenWidthDp(10.dp))
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = SpotTheme.typography.medium_500,
                            color = SpotTheme.colors.default
                        )
                    }
                    innerTextField()
                }
            }
        )
    }
}