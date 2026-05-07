package com.umcspot.spot.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umcspot.spot.designsystem.component.button.TextButton
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.signup.component.ConsentItem
import com.umcspot.spot.signup.component.EditableNameRow
import com.umcspot.spot.signup.component.PrivacyConsentDialog
import com.umcspot.spot.signup.component.UniqueConsentDialog
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import kotlinx.coroutines.flow.collectLatest


@Composable
fun SignUpRoute(
    contentPadding: PaddingValues,
    navigateToCheckList: () -> Unit,
    viewModel: SignUpViewModel = hiltViewModel(),
    snackBarHostState: SnackbarHostState? = null
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collectLatest { effect ->
            when (effect) {
                is SignupSideEffect.NavigateToCheckList -> navigateToCheckList()
                is SignupSideEffect.ShowSnackBar -> {
                    snackBarHostState?.showSnackbar(effect.message)
                }
            }
        }
    }

    SignUpScreen(
        contentPadding = contentPadding,
        uiState = uiState,
        onNameChange = viewModel::onNameChange,
        onPrivacyCheckChange = viewModel::onPrivacyCheckChange,
        onUniqueCheckChange = viewModel::onUniqueCheckChange,
        onNextClick = viewModel::onNextClick
    )
}


@Composable
fun SignUpScreen(
    contentPadding: PaddingValues,
    uiState: SignupState,
    onNameChange: (String) -> Unit,
    onPrivacyCheckChange: (Boolean) -> Unit,
    onUniqueCheckChange: (Boolean) -> Unit,
    onNextClick: () -> Unit
) {
    val focusManager = LocalFocusManager.current
    val topPad = contentPadding.calculateTopPadding()
    val bottomPad = contentPadding.calculateBottomPadding()

    var showPrivacyDialog by rememberSaveable { mutableStateOf(false) }
    var showUniqueDialog by rememberSaveable { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotTheme.colors.white)
            .padding(
                top = topPad,
                start = screenWidthDp(17.dp),
                end = screenWidthDp(17.dp),
                bottom = bottomPad
            )
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
                .clip(RoundedCornerShape(6.dp))
                .padding(horizontal = 4.dp, vertical = 8.dp)
        ) {
            EditableNameRow(
                name = uiState.currentName ?: "",
                onNameChange = onNameChange
            )
        }
        Spacer(Modifier.weight(1f))

        AgreementConfirm(
            privacyChecked = uiState.isPrivacyChecked,
            uniqueChecked = uiState.isUniqueChecked,
            onOpenPrivacyDialog = {
                if (uiState.isPrivacyChecked) onPrivacyCheckChange(false)
                else showPrivacyDialog = true
            },
            onOpenUniqueDialog = {
                if (uiState.isUniqueChecked) onUniqueCheckChange(false)
                else showUniqueDialog = true
            },
        )

        Spacer(Modifier.height(screenHeightDp(24.dp)))

        TextButton(
            modifier = Modifier
                .padding(vertical = screenHeightDp(10.dp))
                .width(screenWidthDp(326.dp))
                .heightIn(screenHeightDp(47.dp)),
            text = "다음",
            style = SpotTheme.typography.h3,
            enabled = uiState.isPrivacyChecked && uiState.isUniqueChecked,
            onClick = onNextClick,
            shape = SpotShapes.Soft
        )

        Spacer(Modifier.height(screenHeightDp(13.dp)))
    }

    if (showPrivacyDialog) {
        PrivacyConsentDialog(
            open = true,
            onAgree = {
                onPrivacyCheckChange(true)
                showPrivacyDialog = false
            },
            onDismiss = { showPrivacyDialog = false }
        )
    }

    if (showUniqueDialog) {
        UniqueConsentDialog(
            open = true,
            onAgree = {
                onUniqueCheckChange(true)
                showUniqueDialog = false
            },
            onDismiss = { showUniqueDialog = false }
        )
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