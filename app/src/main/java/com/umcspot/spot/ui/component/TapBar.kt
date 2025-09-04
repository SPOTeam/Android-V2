package com.umcspot.spot.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.umcspot.spot.R
import com.umcspot.spot.ui.shapes.SpotShapes
import com.umcspot.spot.ui.theme.*

data class MainTab(
    val route: String,
    val label: String,
    @DrawableRes val icon: Int,
    @DrawableRes val iconSelected: Int
)

private val defaultTabs = listOf(
    MainTab("home",      "홈",       R.drawable.home_default,     R.drawable.home_filled),
    MainTab("category",  "카테고리", R.drawable.category_default, R.drawable.category_filled),
    MainTab("mystudy",   "내 스터디", R.drawable.study_default,    R.drawable.study_filled),
    MainTab("favorite",  "찜",       R.drawable.like_default,     R.drawable.like_filled_b400),
    MainTab("mypage",    "마이페이지", R.drawable.mypage_default,   R.drawable.mypage_filled),
)

@Composable
fun TapBar(
    currentRoute: String,
    onTabSelected: (String) -> Unit,
    tabs: List<MainTab> = defaultTabs,
    // ↓ 커스터마이즈 포인트
    itemShape: Shape = SpotShapes.Hard, // 배경 모양
    selectedContainerColor: Color = Color.Transparent, // 선택된 애 배경 색상
    pressedContainerColor: Color = Color.Transparent // 눌릴 때 배경 색상
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            tabs.forEach { tab ->
                val selected = tab.route == currentRoute
                BottomTabItem(
                    tab = tab,
                    selected = selected,
                    onClick = { onTabSelected(tab.route) },
                    itemShape = itemShape,
                    selectedContainerColor = selectedContainerColor,
                    pressedContainerColor = pressedContainerColor,
                )
            }
        }
    }
}

@Composable
private fun RowScope.BottomTabItem(
    tab: MainTab,
    selected: Boolean,
    onClick: () -> Unit,
    itemShape: Shape,
    selectedContainerColor: Color,
    pressedContainerColor: Color
) {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()

    val bgColor by animateColorAsState(
        targetValue = when {
            pressed  -> pressedContainerColor
            selected -> selectedContainerColor
            else     -> Color.Transparent
        }, label = "tabItemBg"
    )
    val labelColor by animateColorAsState(
        targetValue = if (selected) B500 else Black, label = "tabLabelColor"
    )
    Box(
        modifier = Modifier
            .weight(1f)
            .height(64.dp)
            .padding(vertical = 4.dp)
            .background(bgColor, itemShape)
            .clip(itemShape)
            .clickable(
                interactionSource = interaction,
                indication = null,            // ripple은 위 indication에서 처리
                role = Role.Tab,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(id = if (selected) tab.iconSelected else tab.icon),
                contentDescription = tab.label,
                tint = Color.Unspecified,
                modifier = Modifier.size(26.dp)
            )
            Spacer(Modifier.height(2.dp))
            Text(text = tab.label, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = labelColor)
        }
    }
}

/* =================== Previews =================== */

@Preview(showBackground = true, name = "TapBar – Press/Selected BG")
@Composable
fun Preview_TapBar_BG() {
    var route by remember { mutableStateOf("home") }
    TapBar(
        currentRoute = route,
        onTabSelected = { route = it },
        // 예: 선택 시 연한 파랑 배경, 눌림 시 더 연한 파랑
//        selectedContainerColor = B500.copy(alpha = 0f),
//        pressedContainerColor = B500.copy(alpha = 0f),
//        itemShape = SpotShapes.Hard
    )
}
