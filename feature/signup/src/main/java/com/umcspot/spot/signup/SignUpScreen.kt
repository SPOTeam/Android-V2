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

    // ✅ 동의 체크 상태 (초기 false 권장)
    var privacyChecked by rememberSaveable { mutableStateOf(false) }
    var uniqueChecked by rememberSaveable { mutableStateOf(false) }

    // ✅ 모달 표시 상태
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

                Spacer(Modifier.height(70.dp))
                Text(
                    text = "스팟에서는 안전한 스터디 매칭을 위해\n실명 활동제를 도입하고 있어요.",
                    style = SpotTheme.typography.bodyMedium500.copy(fontSize = 16.sp),
                    color = SpotTheme.colors.B500
                )

                Spacer(Modifier.height(24.dp))

                // 이름 섹션
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



                // ✅ 항목 클릭 시 모달만 연다 (체크는 모달의 동의에서 처리)
                AgreementConfirm(
                    privacyChecked = privacyChecked,
                    uniqueChecked = uniqueChecked,
                    onOpenPrivacyDialog = { if (privacyChecked) privacyChecked = false else showPrivacyDialog = true },
                    onOpenUniqueDialog = { if (uniqueChecked) uniqueChecked = false else showUniqueDialog = true },
                )

                Spacer(Modifier.height(10.dp))

                TextButton(
                    text = "다음",
                    enabled = privacyChecked && uniqueChecked,
                    onClick = onNextClick
                )
            }

            // ✅ 모달들
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
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp)
        )
        Spacer(Modifier.height(12.dp))

        ConsentItem(
            title = "개인정보 이용 및 활용 동의",
            checked = privacyChecked,
            onClick = onOpenPrivacyDialog
        )
        Spacer(Modifier.height(8.dp))
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
                        focusedBorderColor = SpotTheme.colors.B500,      // 포커스 시 테두리
                        unfocusedBorderColor = Color.Transparent,               // 비활성 테두리
                        focusedLabelColor = SpotTheme.colors.B500,       // 포커스 시 라벨
                        unfocusedLabelColor = Color.Transparent,                // 비활성 라벨
                        cursorColor = SpotTheme.colors.B500,             // 커서 색상
                        focusedTextColor = SpotTheme.colors.black,       // 포커스 시 텍스트
                        unfocusedTextColor = SpotTheme.colors.black,
                        focusedContainerColor = SpotTheme.colors.white,  // 내부 배경색
                        unfocusedContainerColor = SpotTheme.colors.white
                    ),
                    value = draft,
                    onValueChange = { new ->
                        when {
                            new.length <= 15 -> {
                                // 15자 이하면 그대로 반영
                                draft = new
                            }
                            // 15자 초과: 기존보다 늘어난 타이핑이라면 끝 글자만 교체
                            new.length > draft.length -> {
                                val last = new.last()              // 마지막에 입력된 글자(간단 버전)
                                draft = draft.take(14) + last      // 14 + 새 글자 = 15자 유지
                            }
                            else -> {
                                // 그 외(중간 수정/붙여넣기 등)는 안전하게 15자 컷
                                draft = new.take(15)
                            }
                        }
                    },
                    singleLine = true,
                    shape = SpotShapes.Hard,
                    textStyle = SpotTheme.typography.bodySmall400.copy(fontSize = 20.sp),
                    placeholder = { Text("이름을 입력하세요", style = SpotTheme.typography.bodySmall400) },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        // IME Done도 포커스 아웃과 동일 처리
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
            Spacer(Modifier.height(8.dp))
            Text(
                text = "공백 포함 15자까지 입력 가능해요.",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 12.sp, color = SpotTheme.colors.B500
                ),
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
                    style = SpotTheme.typography.bodySmall400.copy(fontSize = 20.sp)
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
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 12.sp
                ),
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
    val background = if(checked) SpotTheme.colors.B100 else SpotTheme.colors.white

    Surface(
        shape = SpotShapes.Soft,
        color = background,
        border = BorderStroke(1.dp, border),
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 52.dp)
            .clip(RoundedCornerShape(12.dp))
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
                style = SpotTheme.typography.bodySmall400.copy(fontSize = 14.sp),
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
