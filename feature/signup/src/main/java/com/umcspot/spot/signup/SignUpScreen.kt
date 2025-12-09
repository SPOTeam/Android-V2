@file:OptIn(ExperimentalMaterial3Api::class)

package com.umcspot.spot.signup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults.outlinedTextFieldColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.button.MultiButton
import com.umcspot.spot.designsystem.component.button.TextButton
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.B100
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.G300
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.state.UiState


@Composable
fun SignUpScreen(
    contentPadding: PaddingValues,
    onNextClick: () -> Unit,
    viewmodel: SignUpViewModel = hiltViewModel(),
) {
    val uiState by viewmodel.name.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current
    val topPad = contentPadding.calculateTopPadding()
    val bottomPad = contentPadding.calculateBottomPadding()

    var privacyChecked by rememberSaveable { mutableStateOf(false) }
    var uniqueChecked by rememberSaveable { mutableStateOf(false) }

    var showPrivacyDialog by rememberSaveable { mutableStateOf(false) }
    var showUniqueDialog by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(uiState.user) {
        if (uiState.user is UiState.Empty) {
            viewmodel.load()
        }
    }

    when (val state = uiState.user) {
        is UiState.Loading -> {
            Text(text = "로딩 중...", color = Color.Gray)
        }

        is UiState.Failure -> {
            Text(text = "에러: ${state.msg}", color = Color.Red)
        }

        is UiState.Empty -> {
            Text(text = "데이터가 없습니다.")
        }

        is UiState.Success -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SpotTheme.colors.white)
                    .padding(top = topPad, start = 14.dp, end = 14.dp, bottom = bottomPad)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) { focusManager.clearFocus(force = true) }
            ) {

                Spacer(Modifier.height(screenHeightDp(68.dp)))
                Text(
                    text = "스팟에서는 안전한 스터디 매칭을 위해\n실명 활동제를 도입하고 있어요.",
                    style = SpotTheme.typography.h3,
                    color = SpotTheme.colors.B500
                )

                Spacer(Modifier.height(screenHeightDp(33.dp)))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .padding(horizontal = 4.dp, vertical = 8.dp)
                ) {
                    EditableNameRow(
                        name = state.data,
                        onNameChange = { viewmodel.setName(it) }
                    )
                }

                Spacer(Modifier.weight(1f))


                AgreementConfirm(
                    privacyChecked = privacyChecked,
                    uniqueChecked = uniqueChecked,
                    onOpenPrivacyDialog = {
                        if (privacyChecked) privacyChecked = false else showPrivacyDialog = true
                    },
                    onOpenUniqueDialog = {
                        if (uniqueChecked) uniqueChecked = false else showUniqueDialog = true
                    },
                )

                Spacer(Modifier.height(screenHeightDp(24.dp)))

                TextButton(
                    text = "다음",
                    shape = SpotShapes.Soft,
                    enabled = privacyChecked && uniqueChecked,
                    onClick = onNextClick
                )
            }

            PrivacyConsentDialog(
                open = showPrivacyDialog,
                onAgree = {
                    privacyChecked = true
                    showPrivacyDialog = false
                },
                onDismiss = { showPrivacyDialog = false }
            )

            UniqueConsentDialog(
                open = showUniqueDialog,
                onAgree = {
                    uniqueChecked = true
                    showUniqueDialog = false
                },
                onDismiss = { showUniqueDialog = false }
            )
        }
    }
}

@Composable
fun AgreementConfirm(
    privacyChecked: Boolean,
    uniqueChecked: Boolean,
    onOpenPrivacyDialog: () -> Unit,
    onOpenUniqueDialog: () -> Unit,
) {
    Column {
        Text(
            text = "약관 동의",
            style = SpotTheme.typography.h3
        )
        Spacer(Modifier.height(screenHeightDp(7.dp)))

        ConsentItem(
            title = "개인정보 이용 및 활용 동의",
            checked = privacyChecked,
            onClick = onOpenPrivacyDialog
        )
        Spacer(Modifier.height(screenHeightDp(4.dp)))

        ConsentItem(
            title = "고유식별정보 처리 동의",
            checked = uniqueChecked,
            onClick = onOpenUniqueDialog
        )
    }
}

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
        modifier = modifier
            .fillMaxWidth()
    ) {
        if (editing) {
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                OutlinedTextField(
                    colors = outlinedTextFieldColors(
                        focusedBorderColor = SpotTheme.colors.B500,
                        unfocusedBorderColor = Color.Transparent,
                        focusedLabelColor = SpotTheme.colors.B500,
                        unfocusedLabelColor = Color.Transparent,
                        cursorColor = SpotTheme.colors.B500,
                        focusedTextColor = SpotTheme.colors.black,
                        unfocusedTextColor = SpotTheme.colors.black,
                        focusedContainerColor = SpotTheme.colors.white,
                        unfocusedContainerColor = SpotTheme.colors.white
                    ),
                    value = draft,
                    onValueChange = { new ->
                        when {
                            new.length <= 15 -> {
                                draft = new
                            }

                            new.length > draft.length -> {
                                val last = new.last()
                                draft = draft.take(14) + last
                            }

                            else -> {
                                draft = new.take(15)
                            }
                        }
                    },
                    singleLine = true,
                    shape = SpotShapes.Hard,
                    textStyle = SpotTheme.typography.h2,
                    placeholder = { Text("이름을 입력하세요", style = SpotTheme.typography.h2) },
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
                                keyboard?.hide()
                            }
                            hadFocus = state.isFocused
                        }
                )
            }
            Spacer(Modifier.height(screenHeightDp(8.dp)))

            Text(
                text = "공백 포함 15자까지 입력 가능해요.",
                style = SpotTheme.typography.regular_500,
                color = SpotTheme.colors.B500,
                modifier = Modifier.padding(start = 10.dp)
            )
        } else {
            Row(
                modifier = modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = name,
                    style = SpotTheme.typography.h2
                )

                MultiButton(
                    text = "수정",
                    painter = painterResource(R.drawable.write),
                    modifier = Modifier.width(85.dp),
                    onClick = {
                        draft = name
                        editing = true
                    }
                )

            }
            Spacer(Modifier.height(8.dp))
            Text(
                text = "실명이 맞나요? 이름을 확인해주세요.",
                style = SpotTheme.typography.regular_500,
                color = SpotTheme.colors.gray400
            )
        }
    }
}

@Composable
private fun ConsentItem(
    title: String,
    checked: Boolean,
    onClick: () -> Unit,
) {
    val border = if (checked) SpotTheme.colors.B500 else SpotTheme.colors.G300
    val checkTint = if (checked) SpotTheme.colors.B500 else SpotTheme.colors.black
    val background = if (checked) SpotTheme.colors.B100 else SpotTheme.colors.white

    Surface(
        shape = SpotShapes.Hard,
        color = background,
        border = BorderStroke(1.dp, border),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current,
                role = Role.Checkbox,
                onClick = onClick
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = SpotTheme.typography.regular_500,
                color = SpotTheme.colors.black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Icon(
                painter = painterResource(R.drawable.success_default),
                modifier = Modifier.size(15.dp),
                contentDescription = if (checked) "동의됨" else "미동의",
                tint = checkTint
            )
        }
    }
}
