package com.umcspot.spot.mypage.editInterestRegion

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umcspot.spot.common.location.LocationRow
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.bottomsheet.SelectedChips
import com.umcspot.spot.designsystem.component.location.EditRegionBottomButtons
import com.umcspot.spot.designsystem.component.location.RegionItem
import com.umcspot.spot.designsystem.component.location.RegionSearchTextField
import com.umcspot.spot.designsystem.component.location.SearchResultItem
import com.umcspot.spot.designsystem.component.modal.AcceptDialog
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.G200
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.mypage.editInterestRegion.screen.EmptyRegionScreen
import com.umcspot.spot.mypage.editInterestRegion.screen.ErrorRegionScreen
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import com.umcspot.spot.ui.state.UiState

@Composable
fun EditInterestRegionScreen(
    contentPadding: PaddingValues,
    moveToMyInterestStudy: () -> Unit,
    moveToMyPage: () -> Unit,
    viewModel: EditInterestRegionViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()

    var draftRegions by remember { mutableStateOf<List<LocationRow>?>(null) }
    var editMode by remember { mutableStateOf(false) }
    var isSearching by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current
    var isFocused by remember { mutableStateOf(false) }

    LaunchedEffect(uiState) {
        when (uiState) {
            is UiState.Success -> {
                draftRegions = (uiState as UiState.Success<List<LocationRow>>).data
            }
            is UiState.Empty -> {
                draftRegions = emptyList()
            }
            else -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotTheme.colors.white)
            .padding(top = contentPadding.calculateTopPadding())
            .padding(horizontal = screenWidthDp(17.dp))
    ) {
        Spacer(Modifier.height(screenHeightDp(68.dp)))

        Text(
            text = "내가 스터디하고 싶은 지역은?",
            style = SpotTheme.typography.h3,
            color = Color.Black
        )

        if (!isSearching) {
            Spacer(Modifier.height(screenHeightDp(4.dp)))
            Text(
                text = "관심 지역은 최대 10개까지 설정할 수 있어요.",
                style = SpotTheme.typography.regular_500,
                color = SpotTheme.colors.gray400
            )
        }

        if (isSearching) {
            Spacer(Modifier.height(screenHeightDp(40.dp)))
            RegionSearchTextField(
                query = query,
                onQueryChange = {
                    query = it
                    viewModel.search(it)
                },
                focusRequester = focusRequester,
                isFocused = isFocused,
                onFocusChanged = {
                    isFocused = it
                    if (it) keyboard?.show()
                },
                onSearchIconClick = {
                    if (isFocused) keyboard?.hide() else focusRequester.requestFocus()
                }
            )
            Spacer(Modifier.height(screenHeightDp(13.dp)))
            SelectedChips(
                items = draftRegions ?: emptyList(), // null-safe
                onRemove = { item -> draftRegions = draftRegions?.minus(item) }
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(
                        color = SpotTheme.colors.B500,
                        strokeWidth = 3.dp
                    )
                }

                is UiState.Failure -> {
                    ErrorRegionScreen(
                        msg = state.msg,
                        onRetry = { viewModel.loadPreferRegions() }
                    )
                }

                else -> {
                    val regions = draftRegions

                    when {
                        regions == null -> Unit

                        !isSearching && regions.isEmpty() -> EmptyRegionScreen()

                        else -> {
                            LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                verticalArrangement = Arrangement.spacedBy(screenHeightDp(12.dp)),
                                contentPadding = PaddingValues(
                                    top = if (isSearching) screenHeightDp(13.dp) else screenHeightDp(40.dp),
                                    bottom = screenHeightDp(20.dp)
                                )
                            ) {
                                if (isSearching && query.isNotBlank()) {
                                    itemsIndexed(searchResults, key = { _, row -> row.code }) { _, row ->
                                        val isAlreadySelected = regions.any { it.code == row.code }
                                        val isMaxCount = regions.size >= 10
                                        SearchResultItem(
                                            name = row.fullName,
                                            isEnabled = !isAlreadySelected && !isMaxCount,
                                            isAlreadySelected = isAlreadySelected,
                                            onClick = {
                                                draftRegions = regions + row
                                                query = ""
                                            }
                                        )
                                        HorizontalDivider(thickness = 0.5.dp, color = SpotTheme.colors.G200)
                                    }
                                } else if (!isSearching) {
                                    itemsIndexed(regions, key = { _, row -> row.code }) { _, row ->
                                        RegionItem(
                                            regionName = row.fullName,
                                            isReadOnly = !editMode,
                                            onRemoveClick = { draftRegions = regions - row }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        EditRegionBottomButtons(
            editMode = editMode,
            isSearching = isSearching,
            regionCount = draftRegions?.size ?: 0,
            onEditClick = { editMode = true },
            onAddClick = { isSearching = true },
            onCompleteClick = {
                if (isSearching) {
                    isSearching = false
                    query = ""
                } else {
                    viewModel.updatePreferRegions(draftRegions ?: emptyList())
                    showSuccessDialog = true
                }
            }
        )
    }

    if (showSuccessDialog) {
        AcceptDialog(
            visible = true,
            painter = painterResource(id = R.drawable.ic_check),
            painterTint = SpotTheme.colors.B500,
            modalTitle = "",
            modalDes = "수정이 완료되었어요.\n새로운 관심 지역의 스터디를 확인하세요!",
            okButtonText = "내 지역 스터디 보기",
            noButtonText = null,
            onClick = { moveToMyInterestStudy() },
            onDismiss = { moveToMyPage() }
        )
    }
}