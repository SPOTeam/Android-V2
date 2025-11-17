import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.umcspot.spot.common.location.LocationRow
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.B100
import com.umcspot.spot.designsystem.theme.B400
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.SpotTheme
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PreferLocationBottomSheet(
    contentPadding : PaddingValues,
    visible: Boolean,
    query: String,
    results: List<LocationRow>,               // ✅ 결과 리스트 전달받음
    onQueryChange: (String) -> Unit,
    onDismiss: () -> Unit,
    selected: List<String>,                    // ✅ 현재 선택된 지역 칩들
    onAddSelected: (String) -> Unit,          // ✅ 선택 추가
    onRemoveSelected: (String) -> Unit        // ✅ 선택 제거,
) {
    if (!visible) return

    val keyboard = LocalSoftwareKeyboardController.current
    val focusRequester = remember { FocusRequester() }
    val blurFocusRequester = remember { FocusRequester() }    // 🔑 바깥 클릭 시 포커스 이동용
    var isFocused by remember { mutableStateOf(false) }

    val density = LocalDensity.current
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    val sheetHeight = 533.dp
    val scope = rememberCoroutineScope()
    val sheetOffset = remember { Animatable(with(density) { screenHeight.toPx() }) }
    val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()


    LaunchedEffect(Unit) {
        val screenHeightPx = with(density) { screenHeight.toPx() }
        val sheetHeightPx  = with(density) { sheetHeight.toPx() }
        val openY = screenHeightPx - sheetHeightPx           // 👈 바닥에 딱 붙이기
        sheetOffset.animateTo(openY, animationSpec = tween(300))
    }

    fun animateAndDismiss() {
        scope.launch {
            sheetOffset.animateTo(
                targetValue = with(density) { screenHeight.toPx() },
                animationSpec = tween(250)
            )
            onDismiss()
        }
    }

    Dialog(
        onDismissRequest = { animateAndDismiss() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,   // ✅ 가로 전체
            dismissOnBackPress = false,        // 우리가 직접 처리
            dismissOnClickOutside = false      // 우리가 스크림에서 처리
        )
    ) {

        BackHandler(enabled = true) { animateAndDismiss() }

        Box(Modifier.fillMaxSize()) {
            // 스크림 (배경)
            Box(
                Modifier
                    .matchParentSize()
                    .background(SpotTheme.colors.black.copy(alpha = 0.4f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        blurFocusRequester.requestFocus()
                        keyboard?.hide()
                        animateAndDismiss()
                    }
            )

            // 시트 본체 (위 레이어)
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(sheetHeight)
                    .offset { IntOffset(0, sheetOffset.value.roundToInt()) }
                    .clip(SpotShapes.SoftTop)
                    .background(SpotTheme.colors.white)
                    .imePadding()              // 키보드 올라와도 시트 고정, 내부만 패딩
                    .focusRequester(blurFocusRequester)
                    .focusable()
                    // ⬇︎ 시트 빈 공간 탭 -> 포커스 이동 + 키보드 닫기 (dismiss는 안 함)
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {
                        blurFocusRequester.requestFocus()
                        keyboard?.hide()
                    }
            ) {
                // === 네 기존 UI 그대로 ===
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            blurFocusRequester.requestFocus()
                            keyboard?.hide()
                        },
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(Modifier.width(24.dp))
                        Text(
                            text = "스터디 지역",
                            style = SpotTheme.typography.medium_400,
                            color = SpotTheme.colors.black
                        )
                        IconButton(onClick = { animateAndDismiss() }) {
                            Icon(
                                painter = painterResource(R.drawable.dismiss),
                                contentDescription = "닫기",
                                tint = SpotTheme.colors.black
                            )
                        }
                    }

                    Spacer(Modifier.height(6.dp))

                    Text(
                        text = "스터디를 진행하고 싶은 지역을 추가해주세요.",
                        style = SpotTheme.typography.medium_500,
                        color = SpotTheme.colors.gray500
                    )
                    Text(
                        text = "최대 10개까지 추가할 수 있어요",
                        style = SpotTheme.typography.small_300,
                        color = SpotTheme.colors.gray500
                    )

                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = query,
                        onValueChange = onQueryChange,
                        singleLine = true,
                        textStyle = SpotTheme.typography.small_300,
                        placeholder = {
                            Text(
                                text = "OO시, OO구, OO동",
                                style = SpotTheme.typography.small_300,
                                color = SpotTheme.colors.gray400
                            )
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                // 포커스 있으면 키보드 닫기
                                if (isFocused) {
                                    blurFocusRequester.requestFocus() // ← 포커스를 빼앗아온다
                                    keyboard?.hide()
                                } else {
                                    focusRequester.requestFocus()
                                    keyboard?.show()
                                }
                            }) {
                                Icon(
                                    painter = painterResource(com.umcspot.spot.designsystem.R.drawable.search),
                                    contentDescription = "검색",
                                    tint = SpotTheme.colors.gray500,
                                    modifier = Modifier.size(15.dp)
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .onFocusChanged { fs ->
                                isFocused = fs.isFocused
                                if (isFocused) keyboard?.show()
                            }
                    )

                    SelectedChips(
                        items = selected,
                        onRemove = onRemoveSelected
                    )

                    if (results.isNotEmpty()) {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp, bottom = navBarPadding)
                                .background(SpotTheme.colors.white),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(results, key = { it.code }) { row ->
                                ListItem(
                                    headlineContent = {
                                        Text(
                                            text = row.name,
                                            fontSize = 18.sp,
                                            maxLines = 1
                                        )
                                    },
                                    colors = ListItemDefaults.colors(
                                        containerColor = SpotTheme.colors.white
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            onAddSelected(row.name)
                                        }
                                )
                                HorizontalDivider()
                            }
                        }
                    } else {
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

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SelectedChips(
    items: List<String>,
    onRemove: (String) -> Unit
) {
    if (items.isEmpty()) return

    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .horizontalScroll(scrollState),   // 👈 가로 스크롤 추가
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        items.forEach { name ->
            AssistChip(
                onClick = { /* no-op */ },
                label = { Text(name, style = SpotTheme.typography.small_300) },
                trailingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.dismiss),
                        contentDescription = "삭제",
                        tint = SpotTheme.colors.B500,
                        modifier = Modifier
                            .size(14.dp)
                            .clickable { onRemove(name) }

                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = SpotTheme.colors.B100,
                    labelColor = SpotTheme.colors.B500
                ),
            border = BorderStroke(1.dp, SolidColor(SpotTheme.colors.B100)),
            shape = RoundedCornerShape(percent = 50)   // 👈 타원(캡슐) 모양
            )
        }
    }
}

