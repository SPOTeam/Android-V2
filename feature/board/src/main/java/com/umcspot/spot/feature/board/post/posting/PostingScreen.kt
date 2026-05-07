package com.umcspot.spot.feature.board.post.posting

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.button.TextButton
import com.umcspot.spot.designsystem.component.button.TextButtonState
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.G200
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.model.ImageRef
import com.umcspot.spot.model.PostType
import com.umcspot.spot.model.korean
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import com.umcspot.spot.ui.state.UiState

@Composable
fun PostingScreen(
    contentPadding: PaddingValues,
    onBackRequest: () -> Unit,
    onSubmitSuccess: () -> Unit,
    postingViewModel: PostingViewModel = hiltViewModel()
) {
    val topPad = contentPadding.calculateTopPadding()
    val bottomPad = contentPadding.calculateBottomPadding()

    val title by postingViewModel.title.collectAsStateWithLifecycle()
    val body by postingViewModel.body.collectAsStateWithLifecycle()
    val selectedBoardType by postingViewModel.postType.collectAsStateWithLifecycle()
    val image by postingViewModel.image.collectAsStateWithLifecycle()
    val editingPostId by postingViewModel.editingPostId.collectAsStateWithLifecycle()
    val submitState by postingViewModel.submitState.collectAsStateWithLifecycle()

    val isSubmitEnabled = title.isNotBlank() && body.isNotBlank()

    val focusManager = LocalFocusManager.current

    val pickSingleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        uri ?: return@rememberLauncherForActivityResult
        postingViewModel.setImage(ImageRef.LocalUri(uri.toString()))
    }

    LaunchedEffect(submitState) {
        when (submitState) {
            is UiState.Success -> {
                postingViewModel.consumeSubmitResult()
                onSubmitSuccess()
            }

            else -> Unit
        }
    }

    BackHandler {
        onBackRequest()
    }

    Box(
        modifier = Modifier
            .background(SpotTheme.colors.white)
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus()
            }
            .padding(
                top = topPad,
                bottom = bottomPad
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = screenWidthDp(17.dp)),
        ) {
            Spacer(modifier = Modifier.height(screenHeightDp(24.dp)))

            // 제목
            BasicTextField(
                value = title,
                onValueChange = postingViewModel::onTitleChange,
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
                value = body,
                onValueChange = postingViewModel::onBodyChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = screenHeightDp(12.dp), vertical = screenHeightDp(4.dp))
                    .weight(1f),
                textStyle = SpotTheme.typography.medium_400,
                decorationBox = { innerTextField ->
                    if (body.isEmpty()) {
                        Text(
                            text = "내용을 입력해주세요.",
                            color = SpotTheme.colors.gray300,
                            style = SpotTheme.typography.medium_400,
                        )
                    }
                    innerTextField()
                }
            )

            val previewModel: Any? = when (image) {
                ImageRef.None -> null
                is ImageRef.LocalUri -> (image as ImageRef.LocalUri).uri
                is ImageRef.Url -> (image as ImageRef.Url).url
                is ImageRef.Name -> (image as ImageRef.Name).name
            }

            if (previewModel != null) {
                Spacer(Modifier.height(screenHeightDp(15.dp)))

                val painter = rememberAsyncImagePainter(model = previewModel)
                val dismissTint = rememberDismissTintFromPainter(
                    painterState = painter.state,
                    sampleCorner = Corner.TopEnd
                )

                Box(modifier = Modifier.size(screenWidthDp(80.dp))) {
                    Image(
                        painter = painter,
                        contentDescription = "선택한 이미지",
                        modifier = Modifier
                            .matchParentSize()
                            .border(1.dp, SpotTheme.colors.gray200, SpotShapes.Hard)
                            .clip(SpotShapes.Hard)
                    )

                    Image(
                        modifier = Modifier
                            .size(screenWidthDp(16.dp))
                            .align(Alignment.TopEnd)
                            .clickable { postingViewModel.clearImage() },
                        painter = painterResource(R.drawable.dismiss),
                        contentDescription = "이미지 삭제",
                        colorFilter = ColorFilter.tint(dismissTint)
                    )
                }
            }

            Spacer(modifier = Modifier.height(screenHeightDp(15.dp)))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                BottomToolsRow(
                    selectedBoardType = selectedBoardType,
                    onClickAddPhoto = {
                        pickSingleLauncher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    onSelectedBoardType = postingViewModel::onSelectPostType
                )

                Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))

                TextButton(
                    modifier = Modifier
                        .width(screenWidthDp(326.dp))
                        .height(screenHeightDp(47.dp)),
                    text = "완료",
                    style = SpotTheme.typography.h3,
                    enabled = isSubmitEnabled,
                    shape = SpotShapes.Soft,
                    state = TextButtonState.B500State,
                    onClick = {
                        postingViewModel.submit()
                    }
                )
            }

            Spacer(modifier = Modifier.height(screenHeightDp(8.dp)))
        }
    }
}

@Composable
private fun BottomToolsRow(
    selectedBoardType: PostType,
    onClickAddPhoto: () -> Unit,
    onSelectedBoardType: (PostType) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .clip(SpotShapes.Hard)
                .clickable(onClick = onClickAddPhoto)
                .padding(horizontal = screenWidthDp(5.dp), vertical = screenHeightDp(2.dp)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Image(
                modifier = Modifier.size(screenWidthDp(14.dp)),
                painter = painterResource(R.drawable.camera),
                contentDescription = "사진 추가",
            )
            Spacer(modifier = Modifier.width(screenWidthDp(4.dp)))
            Text(
                text = "사진 추가",
                color = SpotTheme.colors.black,
                style = SpotTheme.typography.regular_500
            )
        }

        BoardCategorySelector(
            selected = selectedBoardType,
            onSelected = onSelectedBoardType
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardCategorySelector(
    selected: PostType,
    onSelected: (PostType) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
        ) {
            Text(
                text = "게시판",
                color = SpotTheme.colors.black,
                style = SpotTheme.typography.regular_500
            )

            Spacer(modifier = Modifier.width(screenWidthDp(7.dp)))

            ExposedDropdownMenuBox (
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .menuAnchor() // ★ anchor 지정
                        .width(screenWidthDp(85.dp))
                        .border(1.dp, SpotTheme.colors.G200, SpotShapes.Hard)
                        .clip(SpotShapes.Hard)
                        .clickable { expanded = true }
                        .padding(
                            horizontal = screenWidthDp(10.dp),
                            vertical = screenHeightDp(6.dp)
                        )
                ) {
                    Text(
                        text = selected.korean,
                        color = SpotTheme.colors.black,
                        style = SpotTheme.typography.regular_500
                    )

                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        painter = painterResource(R.drawable.arrow_up),
                        contentDescription = "게시판 선택",
                        colorFilter = ColorFilter.tint(SpotTheme.colors.B500),
                        modifier = Modifier.size(screenWidthDp(14.dp))
                    )
                }

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .background(SpotTheme.colors.white)
                        .width(screenWidthDp(85.dp))
                ) {
                    PostType.entries.forEach { type ->
                        DropdownMenuItem(
                            modifier = Modifier
                                .height(screenHeightDp(30.dp))
                                .wrapContentWidth(),
                            text = {
                                Text(
                                    text = type.korean,
                                    style = SpotTheme.typography.regular_500,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            },
                            onClick = {
                                onSelected(type)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}
