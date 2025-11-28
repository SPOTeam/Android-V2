package com.umcspot.spot.study.register.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.G400
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp


private val CONSONANTS_VOWELS_ONLY_REGEX = Regex("^[ㄱ-ㅎㅏ-ㅣ]+$")

@Composable
fun StudyNameTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isValid = remember(value) {
        value.isNotBlank() && !CONSONANTS_VOWELS_ONLY_REGEX.matches(value)
    }
    var isFocused by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current
    val shape = RoundedCornerShape(screenWidthDp(6.dp)) 


    val guideColor = SpotTheme.colors.G400
    val validColor = SpotTheme.colors.B500
    val borderColor = if (isFocused) SpotTheme.colors.B500 else SpotTheme.colors.G400

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(screenWidthDp(1.dp), borderColor, shape) 
                .clip(shape)
                .padding(horizontal = screenWidthDp(10.dp), vertical = screenHeightDp(7.dp)), 
            contentAlignment = Alignment.Center
        ) {
            BasicTextField(
                value = value,
                onValueChange = { if (it.length <= 15) onValueChange(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { focusState -> isFocused = focusState.isFocused },
                textStyle = SpotTheme.typography.medium_500,
                singleLine = true,
                cursorBrush = SolidColor(SpotTheme.colors.B500),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(modifier = Modifier.weight(1f)) {
                            innerTextField()
                        }

                        Text(
                            text = "(${value.length}/15)",
                            style = SpotTheme.typography.regular_500,
                            color = guideColor
                        )
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(screenHeightDp(4.dp))) 

        val message = when {
            value.isBlank() -> "스터디 이름을 입력해주세요."
            isValid -> "멋진 스터디 이름이에요!"
            else -> "자음/모음만으로는 이름을 만들 수 없어요."
        }
        val messageColor = if (isValid) validColor else guideColor

        Text(
            text = message,
            style = SpotTheme.typography.regular_500,
            color = messageColor
        )
    }
}
