package com.umcspot.spot.study.detail.screen.post

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.umcspot.spot.designsystem.component.button.SpotActivationButton
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.G300
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.study.detail.StudyDetailViewModel
import com.umcspot.spot.study.detail.component.memoir.MemoirImageSection
import com.umcspot.spot.study.detail.component.memoir.MemoirInputField
import com.umcspot.spot.study.detail.model.StudyDetailSideEffect
import com.umcspot.spot.ui.extension.noRippleClickable
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import com.umcspot.spot.ui.extension.toFile
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList

@Composable
fun StudyMemoirPostRoute(
    studyId: Long,
    onBackClick: () -> Unit,
    contentPadding: PaddingValues,
    viewModel: StudyDetailViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            if (effect is StudyDetailSideEffect.MemoirPostSuccess) {
                onBackClick()
            }
        }
    }

    StudyMemoirPostScreen(
        studyId = studyId,
        isLoading = uiState.isLoading,
        contentPadding = contentPadding,
        onBackClick = onBackClick,
        onPostSubmit = { activity, learned, encouragement, isPrivate, images ->
            val imageFiles = images.mapNotNull { it.toFile(context) }
            viewModel.postMemoir(
                studyId = studyId,
                activity = activity,
                learned = learned,
                encouragement = encouragement,
                isPrivate = isPrivate,
                imageFiles = imageFiles
            )
        }
    )
}
@Composable
fun StudyMemoirPostScreen(
    studyId: Long,
    isLoading: Boolean,
    contentPadding: PaddingValues,
    onBackClick: () -> Unit,
    onPostSubmit: (String, String, String, Boolean, List<Uri>) -> Unit
) {
    var selectedImages by remember { mutableStateOf(persistentListOf<Uri>()) }
    var content1 by remember { mutableStateOf("") }
    var content2 by remember { mutableStateOf("") }
    var content3 by remember { mutableStateOf("") }
    var isPrivate by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris ->
        val remainingSlot = 3 - selectedImages.size
        if (remainingSlot > 0) {
            selectedImages = (selectedImages + uris.take(remainingSlot)).toPersistentList()
        }
    }

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
                .padding(horizontal = screenWidthDp(17.dp))
        ) {
            Spacer(modifier = Modifier.height(screenHeightDp(24.dp)))

            MemoirImageSection(
                images = selectedImages,
                onAddClick = { launcher.launch("image/*") },
                onRemoveClick = { index -> selectedImages = selectedImages.removeAt(index) }
            )

            Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))

            MemoirInputField(
                label = "오늘은 무엇을 했나요? (필수)",
                value = content1,
                onValueChange = { content1 = it },
                placeholder = "내용을 입력해주세요."
            )

            MemoirInputField(
                label = "오늘 새롭게 배운 점은 무엇인가요?",
                value = content2,
                onValueChange = { content2 = it },
                placeholder = "내용을 입력해주세요."
            )

            MemoirInputField(
                label = "고생한 나에게 한마디!",
                value = content3,
                onValueChange = { content3 = it },
                placeholder = "내용을 입력해주세요."
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

            SpotActivationButton(
                modifier = Modifier.fillMaxWidth(),
                buttonText = "완료",
                isEnabled = content1.isNotBlank() && !isLoading,
                onClick = {
                    onPostSubmit(content1, content2, content3, isPrivate, selectedImages)
                }
            )
        }
    }
}