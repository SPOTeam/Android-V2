package com.umcspot.spot.study.detail.screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.appBar.BackTopBar
import com.umcspot.spot.designsystem.component.button.TextButton
import com.umcspot.spot.designsystem.component.button.TextButtonState
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.G300
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.study.detail.StudyDetailViewModel
import com.umcspot.spot.study.detail.model.StudyDetailSideEffect
import com.umcspot.spot.ui.extension.noRippleClickable
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@Composable
fun StudyBoardPostRoute(
    studyId: Long,
    onBackClick: () -> Unit,
    contentPadding: PaddingValues,
    viewModel: StudyDetailViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            if (effect is StudyDetailSideEffect.BoardPostSuccess) {
                onBackClick()
            }
        }
    }

    StudyBoardPostScreen(
        contentPadding = contentPadding,
        onBackClick = onBackClick,
        onPostSubmit = { title, content,isPrivate ->
            viewModel.postBoard(
                studyId = studyId,
                title = title,
                content = content,
                isPrivate = isPrivate
            )
        }
    )
}
@Composable
fun StudyBoardPostScreen(
    contentPadding: PaddingValues,
    onBackClick: () -> Unit,
    onPostSubmit: (String, String, Boolean) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var isPrivate by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotTheme.colors.white)
            .imePadding()
    ) {
        Spacer(modifier = Modifier.height(contentPadding.calculateTopPadding()))

        BackTopBar(
            title = "글쓰기",
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = screenWidthDp(17.dp), vertical = screenHeightDp(18.dp))
        ) {
            // 제목
            BasicTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = screenHeightDp(12.dp), vertical = screenHeightDp(4.dp)),
                textStyle = SpotTheme.typography.h4,
                decorationBox = { innerTextField ->
                    if (title.isEmpty()) {
                        Text(
                            text = "제목",
                            color = SpotTheme.colors.gray300,
                            style = SpotTheme.typography.h4
                        )
                    }
                    innerTextField()
                }
            )

            Spacer(modifier = Modifier.height(screenHeightDp(7.dp)))

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                color = SpotTheme.colors.gray300
            )

            Spacer(modifier = Modifier.height(screenHeightDp(7.dp)))

            BasicTextField(
                value = content,
                onValueChange = { content = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = screenHeightDp(12.dp), vertical = screenHeightDp(4.dp))
                    .weight(1f),
                textStyle = SpotTheme.typography.medium_400,
                decorationBox = { innerTextField ->
                    if (content.isEmpty()) {
                        Text(
                            text = "내용을 입력해주세요.",
                            color = SpotTheme.colors.gray300,
                            style = SpotTheme.typography.medium_400,
                        )
                    }
                    innerTextField()
                }
            )

            Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = screenWidthDp(17.dp))
                .padding(
                    top = screenHeightDp(4.dp),
                    bottom = if (contentPadding.calculateBottomPadding() > 0.dp) {
                        contentPadding.calculateBottomPadding()
                    } else {
                        screenHeightDp(13.dp)
                    }
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .noRippleClickable { isPrivate = !isPrivate }
                    .padding(vertical = screenHeightDp(2.dp))
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_check),
                    contentDescription = null,
                    modifier = Modifier.size(11.dp),
                    tint = if (isPrivate) SpotTheme.colors.B500 else SpotTheme.colors.G300
                )
                Spacer(modifier = Modifier.width(screenWidthDp(4.dp)))
                Text(
                    text = "클릭하면, 이 글은 스터디원에게만 노출됩니다.",
                    style = SpotTheme.typography.regular_500,
                    color = SpotTheme.colors.black
                )
            }

            Spacer(modifier = Modifier.height(screenHeightDp(13.dp)))

            TextButton (
                modifier = Modifier
                    .width(screenWidthDp(326.dp))
                    .height(screenHeightDp(47.dp)),
                text = "완료",
                enabled = title.isNotBlank() && content.isNotBlank(),
                state = TextButtonState.B500State,
                style = SpotTheme.typography.h3,
                onClick = {
                    onPostSubmit(title, content, isPrivate)
                }
            )
        }
    }
}