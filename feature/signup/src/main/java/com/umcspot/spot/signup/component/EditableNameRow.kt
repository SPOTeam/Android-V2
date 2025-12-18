package com.umcspot.spot.signup.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun EditableNameRow(
    modifier: Modifier = Modifier,
    name: String,
    onNameChange: (String) -> Unit,
) {
    var editing by rememberSaveable { mutableStateOf(false) }
    var draft by rememberSaveable { mutableStateOf(name) }
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val keyboard = LocalSoftwareKeyboardController.current
    var hadFocus by remember { mutableStateOf(false) }

    LaunchedEffect(editing) {
        if (editing) {
            hadFocus = false
            focusRequester.requestFocus()
            keyboard?.show()
        }
    }
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        if (editing) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BasicTextField(
                    value = draft,
                    onValueChange = { new ->
                        when {
                            new.length <= 15 -> draft = new
                            new.length > draft.length -> {
                                val last = new.last()
                                draft = draft.take(14) + last
                            }

                            else -> draft = new.take(15)
                        }
                    },
                    singleLine = true,
                    textStyle = SpotTheme.typography.h2.copy(
                        color = SpotTheme.colors.black
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus(force = true)
                    }),
                    modifier = Modifier
                        .weight(1f)
                        .focusRequester(focusRequester)
                        .onFocusChanged { state ->
                            if (hadFocus && !state.isFocused && editing) {
                                val final = draft.trim()
                                if (final.isNotEmpty()) {
                                    onNameChange(final)
                                } else {
                                    draft = name
                                }
                                editing = false
                            }
                            hadFocus = state.isFocused
                        },
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .background(SpotTheme.colors.white, RoundedCornerShape(6.dp))
                                .border(
                                    width = 1.dp,
                                    color = SpotTheme.colors.B500,
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .padding(
                                    horizontal = screenWidthDp(7.dp),
                                    vertical = screenHeightDp(1.dp)
                                ),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (draft.isEmpty()) {
                                Text(
                                    text = "이름을 입력하세요",
                                    style = SpotTheme.typography.h2,
                                    color = SpotTheme.colors.default
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
            Spacer(Modifier.height(screenHeightDp(8.dp)))

            Text(
                text = "공백 포함 15자까지 입력 가능해요.",
                style = SpotTheme.typography.regular_500,
                color = SpotTheme.colors.B500,
                modifier = Modifier.padding(start = screenWidthDp(4.dp))
            )
        } else {
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = name,
                    style = SpotTheme.typography.h2,
                    modifier = Modifier.padding(start = screenWidthDp(7.dp))
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            draft = name
                            editing = true
                        }
                        .padding(vertical = screenHeightDp(4.dp))
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_write),
                        contentDescription = "이름 수정",
                        modifier = Modifier.size(screenWidthDp(14.dp))
                    )

                    Spacer(modifier = Modifier.width(screenWidthDp(4.dp)))

                    Text(
                        text = "수정",
                        style = SpotTheme.typography.small_400,
                        color = SpotTheme.colors.black
                    )
                }
            }

            Spacer(Modifier.height(screenHeightDp(8.dp)))

            Text(
                text = "실명이 맞나요? 이름을 확인해주세요.",
                style = SpotTheme.typography.regular_500,
                color = SpotTheme.colors.gray400,
                modifier = Modifier.padding(start = screenWidthDp(4.dp))
            )
        }
    }
}