package com.umcspot.spot.study.detail.component.planner

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.study.detail.component.common.DeleteMenuPopup
import com.umcspot.spot.ui.extension.noRippleClickable
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun StudyDetailToDoItem(
    text: String,
    isCompleted: Boolean,
    isMyToDo: Boolean,
    isEditing: Boolean = false,
    onTextChange: (String) -> Unit = {},
    onEnterPressed: () -> Unit = {},
    onCheckedChange: (Boolean) -> Unit = {},
    onDeleteClick: () -> Unit = {}
) {
    var showMenu by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val bringIntoViewRequester = remember { BringIntoViewRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val scope = rememberCoroutineScope()

    LaunchedEffect(isEditing) {
        if (isEditing) {
            delay(100)
            focusRequester.requestFocus()
            bringIntoViewRequester.bringIntoView()
            keyboardController?.show()
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = screenHeightDp(6.dp), horizontal = screenWidthDp(7.dp))
            .bringIntoViewRequester(bringIntoViewRequester),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            painter = painterResource(
                id = if (isCompleted) R.drawable.ic_todo_check_filled else R.drawable.ic_todo_check_unfilled
            ),
            contentDescription = null,
            tint = if (isCompleted) SpotTheme.colors.B500 else SpotTheme.colors.gray300,
            modifier = Modifier
                .size(screenWidthDp(18.dp))
                .noRippleClickable {
                    if (isMyToDo && !isEditing) onCheckedChange(!isCompleted)
                }
        )

        Spacer(modifier = Modifier.width(screenWidthDp(7.dp)))

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            if (isEditing) {
                BasicTextField(
                    value = text,
                    onValueChange = onTextChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .onFocusChanged {
                            if (it.isFocused) {
                                scope.launch {
                                    delay(200)
                                    bringIntoViewRequester.bringIntoView()
                                }
                            }
                        },
                    textStyle = SpotTheme.typography.regular_500.copy(
                        color = SpotTheme.colors.black
                    ),
                    cursorBrush = SolidColor(SpotTheme.colors.black),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { onEnterPressed() }),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.CenterStart
                        ) {
                            if (text.isEmpty()) {
                                Text(
                                    text = "할 일을 입력해주세요",
                                    style = SpotTheme.typography.regular_500,
                                    color = SpotTheme.colors.gray400
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            } else {
                Text(
                    text = text,
                    style = SpotTheme.typography.regular_500,
                    color = if (isCompleted) SpotTheme.colors.gray400 else SpotTheme.colors.black,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        if (isMyToDo && !isEditing) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_meetball),
                    contentDescription = null,
                    tint = SpotTheme.colors.gray400,
                    modifier = Modifier
                        .size(screenWidthDp(14.dp))
                        .noRippleClickable { showMenu = true }
                )
                if (showMenu) {
                    Popup(
                        alignment = Alignment.TopEnd,
                        offset = IntOffset(0, 80),
                        onDismissRequest = { showMenu = false }
                    ) {
                        DeleteMenuPopup(onDelete = {
                            onDeleteClick()
                            showMenu = false
                        })
                    }
                }
            }
        }
    }
}