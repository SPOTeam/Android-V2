package com.umcspot.spot.post.posting

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.button.TextButton
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.G200
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.feature.board.BoardViewModel
import com.umcspot.spot.model.PostType
import com.umcspot.spot.model.korean

@Composable
fun PostingScreen(
    contentPadding: PaddingValues,
    viewModel: BoardViewModel = hiltViewModel(),
    onBackRequest: () -> Unit,
) {
    val topPad = contentPadding.calculateTopPadding()
    val bottomPad = contentPadding.calculateBottomPadding()

    var title by rememberSaveable { mutableStateOf("") }
    var body by rememberSaveable { mutableStateOf("") }

    var selectedBoardType by rememberSaveable { mutableStateOf(PostType.FREE_TALK) }

    val isSubmitEnabled = title.isNotBlank() && body.isNotBlank()

    val focusManager = LocalFocusManager.current      // ✅ 포커스 매니저

    BackHandler {
        onBackRequest()
    }

    Box(
        modifier = Modifier
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
            .background(SpotTheme.colors.white)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            // 제목
            BasicTextField(
                value = title,
                onValueChange = { title = it },
                modifier = Modifier
                    .fillMaxWidth(),
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

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(),
                color = SpotTheme.colors.gray300
            )

            Spacer(modifier = Modifier.height(12.dp))

            BasicTextField(
                value = body,
                onValueChange = { body = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // 위는 내용, 아래는 고정 영역
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

            Spacer(modifier = Modifier.height(16.dp))

            // 하단: 사진 추가 / 게시판 드롭다운 / 완료 버튼
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                BottomToolsRow(
                    selectedBoardType = selectedBoardType,
                    onClickAddPhoto = { /* TODO: 사진 추가 */ },
                    onSelectedBoardType = { selectedBoardType = it }
                )

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    text = "완료",
                    enabled = isSubmitEnabled,
                    onClick = { }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
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
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 사진 추가
        Row(
            modifier = Modifier
                .clickable(
                    onClick = onClickAddPhoto
                )
        ) {
            Image(
                modifier = Modifier.size(20.dp),
                painter = painterResource(R.drawable.camera),
                contentDescription = "사진 추가",
            )
            Spacer(modifier = Modifier.width(4.dp))
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

            Spacer(modifier = Modifier.width(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .width(85.dp)
                    .border(
                        width = 1.dp,
                        color = SpotTheme.colors.G200,
                        shape = SpotShapes.Hard
                    )
                    .clickable { expanded = true }
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Text(
                    text = selected.korean,                 // BoardType.korean 사용
                    color = SpotTheme.colors.black,
                    style = SpotTheme.typography.regular_500
                )

                Spacer(modifier = Modifier.width(4.dp))

                Image(
                    painter = painterResource(R.drawable.arrow_up),
                    contentDescription = "게시판 선택",
                    colorFilter =  ColorFilter.tint(SpotTheme.colors.B500),
                    modifier = Modifier.size(14.dp)
                )

                DropdownMenu(
                    modifier = Modifier
                        .width(85.dp)
                        .background(SpotTheme.colors.white),
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    offset = DpOffset(
                        x = 16.dp,
                        y = 0.dp
                    )
                ) {
                    PostType.entries.forEach { type ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = type.korean,
                                    style = SpotTheme.typography.regular_500,
                                    textAlign = TextAlign.Center
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
