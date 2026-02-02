package com.umcspot.spot.mypage.editInterestStudy

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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umcspot.spot.designsystem.component.button.TextButton
import com.umcspot.spot.designsystem.component.button.TextButtonState
import com.umcspot.spot.designsystem.component.modal.AcceptDialog
import com.umcspot.spot.designsystem.component.study.section.ActivityThemeSection
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun EditInterestStudyScreen(
    contentPadding : PaddingValues,
    moveToMyInterestStudy: () -> Unit,
    moveToMyPage: () -> Unit,
    viewmodel : EditInterestStudyViewModel = hiltViewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    var editMode by remember { mutableStateOf(false) }
    val selectedThemes by viewmodel.preferCategories.collectAsStateWithLifecycle()
    var draftThemes by remember { mutableStateOf(selectedThemes) }

    val topPad = contentPadding.calculateTopPadding()
    val bottomPad = contentPadding.calculateBottomPadding()

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
            .padding(top = topPad, bottom = bottomPad)
            .padding(horizontal = screenWidthDp(17.dp), vertical = screenHeightDp(30.dp))
    ) {
        Spacer(Modifier.height(screenHeightDp(68.dp)))

        Text(
            text = "내가 원하는 스터디를 선택해주세요!",
            style = SpotTheme.typography.h3,
            color = SpotTheme.colors.black
        )

        Spacer(Modifier.height(screenHeightDp(40.dp)))

        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            ActivityThemeSection(
                selectedThemes = if (editMode) draftThemes else selectedThemes,
                onSelect = { theme ->
                    if (!editMode) return@ActivityThemeSection

                    draftThemes =
                        if (draftThemes.contains(theme)) draftThemes - theme else draftThemes + theme
                },
                modifier = Modifier.fillMaxWidth(),
                maxSelection = 10,
            )
        }

        if(editMode) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TextButton(
                    modifier = Modifier
                        .width(screenWidthDp(156.dp))
                        .height(screenHeightDp(47.dp)),
                    text = "취소",
                    style = SpotTheme.typography.h3,
                    onClick = {
                        draftThemes = selectedThemes
                        editMode = false
                    },
                    shape = SpotShapes.Soft,
                    state = TextButtonState.G500State,
                )

                TextButton(
                    modifier = Modifier
                        .width(screenWidthDp(156.dp))
                        .height(screenHeightDp(47.dp)),
                    text = "완료",
                    style = SpotTheme.typography.h3,
                    onClick = {
                        viewmodel.updatePreferCategories(draftThemes)
                        showDialog = true
                    },
                    shape = SpotShapes.Soft,
                    state = TextButtonState.B500State,
                )
            }
        } else {
            TextButton(
                modifier = Modifier
                    .width(screenWidthDp(326.dp))
                    .height(screenHeightDp(47.dp)),
                text = "수정",
                style = SpotTheme.typography.h3,
                onClick = {
                    draftThemes = selectedThemes
                    editMode = true
                },
                shape = SpotShapes.Soft,
                state = TextButtonState.B500State,
            )
        }

        AcceptDialog(
            visible = showDialog,
            modalTitle = "",
            modalDes = "수정이 완료되었어요.\n새로운 관심 분야에 맞는 스터디를 확인해보세요.",
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
}