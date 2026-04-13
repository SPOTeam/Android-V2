package com.umcspot.spot.mypage.edit.editInterestStudy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.appBar.BackTopBar
import com.umcspot.spot.designsystem.component.button.SpotActivationButton
import com.umcspot.spot.designsystem.component.button.SpotCancelButton
import com.umcspot.spot.designsystem.component.modal.AcceptDialog
import com.umcspot.spot.designsystem.component.study.section.ActivityThemeSection
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun EditInterestStudyScreen(
    contentPadding: PaddingValues,
    moveToMyInterestStudy: () -> Unit,
    moveToMyPage: () -> Unit,
    viewmodel: EditInterestStudyViewModel = hiltViewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    var editMode by remember { mutableStateOf(false) }
    val selectedThemes by viewmodel.preferCategories.collectAsStateWithLifecycle()
    var draftThemes by remember { mutableStateOf(selectedThemes) }

    LaunchedEffect(Unit) {
        viewmodel.loadPreferCategories()
    }

    LaunchedEffect(selectedThemes) {
        if (!editMode) draftThemes = selectedThemes
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotTheme.colors.white)
            .padding(top = contentPadding.calculateTopPadding())
    ) {
        BackTopBar(
            title = "관심 분야",
            onBackClick = moveToMyPage,
            modifier = Modifier.fillMaxWidth()
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = screenWidthDp(17.dp))
        ) {
            Spacer(Modifier.height(screenHeightDp(18.dp)))

            Text(
                text = "내가 원하는 스터디를 선택해주세요!",
                style = SpotTheme.typography.h3,
                color = SpotTheme.colors.black
            )

            Spacer(Modifier.height(screenHeightDp(40.dp)))

            Column(modifier = Modifier.weight(1f)) {
                ActivityThemeSection(
                    selectedThemes = if (editMode) draftThemes else selectedThemes,
                    onSelect = { theme ->
                        if (!editMode) return@ActivityThemeSection
                        draftThemes = if (draftThemes.contains(theme)) {
                            draftThemes - theme
                        } else {
                            draftThemes + theme
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    maxSelection = 10
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = screenHeightDp(12.dp))
                    .padding(bottom = contentPadding.calculateBottomPadding()),
                horizontalArrangement = Arrangement.spacedBy(screenWidthDp(12.dp))
            ) {
                if (!editMode) {
                    SpotActivationButton(
                        modifier = Modifier.fillMaxWidth(),
                        buttonText = "수정",
                        isEnabled = true,
                        onClick = {
                            draftThemes = selectedThemes
                            editMode = true
                        }
                    )
                } else {
                    SpotCancelButton(
                        modifier = Modifier.weight(1f),
                        buttonText = "취소",
                        onClick = {
                            draftThemes = selectedThemes
                            editMode = false
                        }
                    )

                    SpotActivationButton(
                        modifier = Modifier.weight(1f),
                        buttonText = "완료",
                        isEnabled = draftThemes.isNotEmpty(),
                        onClick = {
                            viewmodel.updatePreferCategories(draftThemes)
                            showDialog = true
                        }
                    )
                }
            }
        }
    }

    AcceptDialog(
        visible = showDialog,
        painter = painterResource(id = R.drawable.ic_check),
        painterTint = SpotTheme.colors.B500,
        modalTitle = "수정 완료",
        modalDes = "수정이 완료되었어요.\n새로운 관심 분야에 맞는 스터디를 확인해보세요!",
        okButtonText = "내 관심 스터디 보기",
        noButtonText = null,
        onClick = {
            showDialog = false
            moveToMyInterestStudy()
        },
        onDismiss = {
            showDialog = false
            moveToMyPage()
        }
    )
}