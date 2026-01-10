package com.umcspot.spot.designsystem.component.bottomsheet

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.umcspot.spot.common.location.LocationRow
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.B100
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.G200
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationBottomSheet(
    visible: Boolean,
    query: String,
    results: List<LocationRow>,
    onQueryChange: (String) -> Unit,
    onDismiss: () -> Unit,
    selected: List<LocationRow>,
    onAddSelected: (LocationRow) -> Unit,
    onRemoveSelected: (LocationRow) -> Unit
) {
    if (!visible) return

    val keyboard = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val blurFocusRequester = remember { FocusRequester() }
    var isFocused by remember { mutableStateOf(false) }

    val density = LocalDensity.current
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val sheetHeight = screenHeightDp(533.dp)

    val scope = rememberCoroutineScope()
    val sheetOffset = remember { Animatable(with(density) { screenHeight.toPx() }) }
    val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()


    fun animateAndDismiss() {
        scope.launch {
            blurFocusRequester.requestFocus()
            keyboard?.hide()
            sheetOffset.animateTo(
                targetValue = with(density) { screenHeight.toPx() },
                animationSpec = tween(250)
            )
            onDismiss()
        }
    }

    LaunchedEffect(visible) {
        if (visible) {
            val screenHeightPx = with(density) { screenHeight.toPx() }
            val sheetHeightPx = with(density) { sheetHeight.toPx() }
            val openY = screenHeightPx - sheetHeightPx
            sheetOffset.snapTo(screenHeightPx)
            sheetOffset.animateTo(openY, animationSpec = tween(300))
        }
    }

    if (visible) {
        Dialog(
            onDismissRequest = { animateAndDismiss() },
            properties = DialogProperties(
                usePlatformDefaultWidth = false,
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            BackHandler(enabled = true) { animateAndDismiss() }

            Box(Modifier.fillMaxSize()) {
                Box(
                    Modifier
                        .matchParentSize()
                        .background(SpotTheme.colors.black.copy(alpha = 0.4f))
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            animateAndDismiss()
                        }
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(sheetHeight)
                        .offset { IntOffset(0, sheetOffset.value.roundToInt()) }
                        .clip(SpotShapes.RoundTop)
                        .background(SpotTheme.colors.white)
                        .imePadding()
                        .focusRequester(blurFocusRequester)
                        .focusable()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            blurFocusRequester.requestFocus()
                            keyboard?.hide()
                        }
                ) {
                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(horizontal = screenWidthDp(17.dp))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = screenHeightDp(16.dp)),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.size(screenWidthDp(20.dp)))

                            Text(
                                text = "스터디 지역",
                                style = SpotTheme.typography.h5,
                                color = SpotTheme.colors.black
                            )

                            IconButton(
                                modifier = Modifier.size(screenWidthDp(20.dp)),
                                onClick = { animateAndDismiss() }) {
                                Icon(
                                    painter = painterResource(R.drawable.dismiss),
                                    contentDescription = "닫기",
                                    modifier = Modifier.size(screenWidthDp(20.dp))
                                )
                            }
                        }

                        Spacer(Modifier.height(screenHeightDp(18.dp)))

                        Text(
                            text = "스터디를 진행하고 싶은 지역을 추가해주세요.",
                            style = SpotTheme.typography.h3,
                            color = SpotTheme.colors.black
                        )

                        Spacer(Modifier.height(screenHeightDp(4.dp)))

                        Text(
                            text = "최대 10개까지 추가할 수 있어요",
                            style = SpotTheme.typography.h5,
                            color = SpotTheme.colors.gray400
                        )

                        Spacer(Modifier.height(screenHeightDp(20.dp)))

                        OutlinedTextField(
                            value = query,
                            onValueChange = onQueryChange,
                            textStyle = SpotTheme.typography.h5,
                            placeholder = {
                                Text(
                                    text = "OO시, OO구, OO동",
                                    style = SpotTheme.typography.h5,
                                    color = SpotTheme.colors.gray300
                                )
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        if (isFocused) {
                                            blurFocusRequester.requestFocus()
                                            keyboard?.hide()
                                        } else {
                                            focusRequester.requestFocus()
                                            keyboard?.show()
                                        }
                                    }) {
                                    Icon(
                                        painter = painterResource(R.drawable.search),
                                        contentDescription = "검색",
                                        modifier = Modifier.size(screenWidthDp(18.dp)),
                                        tint = SpotTheme.colors.gray400
                                    )
                                }
                            },
                            singleLine = true,
                            shape = SpotShapes.Soft,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SpotTheme.colors.B500,
                                unfocusedBorderColor = SpotTheme.colors.gray300,
                                cursorColor = SpotTheme.colors.B500
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(screenHeightDp(51.dp))
                                .focusRequester(focusRequester)
                                .onFocusChanged { fs ->
                                    isFocused = fs.isFocused
                                    if (isFocused) keyboard?.show()
                                },
                        )

                        Spacer(modifier = Modifier.height(screenHeightDp(13.dp)))

                        SelectedChips(
                            items = selected,
                            onRemove = onRemoveSelected
                        )

                        Spacer(modifier = Modifier.height(screenHeightDp(13.dp)))

                        if (results.isNotEmpty()) {
                            val isMaxSelected = selected.size >= 10

                            HorizontalDivider(thickness = 0.5.dp, color = SpotTheme.colors.G200)
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = navBarPadding)
                                    .background(SpotTheme.colors.white),
                            ) {
                                itemsIndexed(results, key = { _, row -> row.code }) { index, row ->
                                    val isAlreadySelected = selected.any { it.code == row.code }
                                    ListItem(
                                        headlineContent = {
                                            Text(
                                                text = row.fullName,
                                                style = SpotTheme.typography.h5,
                                                maxLines = 1,
                                                color = if (isMaxSelected && !isAlreadySelected) {
                                                    SpotTheme.colors.gray400
                                                } else {
                                                    LocalContentColor.current
                                                }
                                            )
                                        },
                                        colors = ListItemDefaults.colors(
                                            containerColor = SpotTheme.colors.white
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable(
                                                enabled = !isMaxSelected || isAlreadySelected,
                                                onClick = {
                                                    if (!isAlreadySelected) {
                                                        onAddSelected(row)
                                                    }
                                                }
                                            )
                                    )

                                    HorizontalDivider(
                                        thickness = 0.5.dp,
                                        color = SpotTheme.colors.G200
                                    )
                                }
                            }
                        } else {
                            if (query.isNotBlank()) {
                                Text(

                                    text = "검색 결과가 없습니다.",
                                    color = SpotTheme.colors.gray400,
                                    style = SpotTheme.typography.small_300,
                                    modifier = Modifier.padding(top = 14.dp),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SelectedChips(
    items: List<LocationRow>,
    onRemove: (LocationRow) -> Unit
) {
    if (items.isEmpty()) return

    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeightDp(23.dp))
            .horizontalScroll(scrollState),
        horizontalArrangement = Arrangement.spacedBy(screenWidthDp(7.dp))
    ) {
        items.forEach { item ->
            Box(
                modifier = Modifier
                    .wrapContentWidth()
                    .height(screenHeightDp(17.dp))
                    .clip(SpotShapes.Hard)
                    .background(SpotTheme.colors.B100)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        onRemove(item)
                    }
                    .padding(
                        start = screenWidthDp(7.dp),
                        end = screenWidthDp(4.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(screenWidthDp(4.dp))
                ) {
                    Text(
                        text = item.neighborhood,
                        style = SpotTheme.typography.small_400,
                        color = SpotTheme.colors.B500,
                        maxLines = 1
                    )

                    Icon(
                        painter = painterResource(R.drawable.dismiss),
                        contentDescription = "삭제",
                        tint = SpotTheme.colors.B500,
                        modifier = Modifier
                            .size(screenWidthDp(14.dp))
                    )
                }
            }
        }
    }
}