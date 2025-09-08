package com.example.core.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.ui.R
import com.example.core.ui.theme.G100
import com.example.core.ui.theme.SpotTypography

@Composable
fun SheetTopBar(
    title: String,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
    titleStyle: TextStyle = SpotTypography.header05,
    backgroundColor: androidx.compose.ui.graphics.Color = G100,
    @DrawableRes closeIconRes: Int = R.drawable.dismiss
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .height(56.dp)
            .padding(horizontal = 16.dp)
    ) {
        // 중앙 타이틀 (오른쪽 아이콘의 폭과 무관하게 정확히 중앙)
        Text(
            text = title,
            style = titleStyle,
            modifier = Modifier.align(Alignment.Center)
        )

        // 닫기 아이콘 (오른쪽 정렬)
        IconButton(
            onClick = onCloseClick,
            modifier = Modifier.align(Alignment.CenterEnd)
        ) {
            Icon(
                painter = painterResource(id = closeIconRes),
                contentDescription = "Close"
            )
        }
    }
}

/* =================== Previews =================== */

@Preview(showBackground = true, name = "SheetTopBar - Default")
@Composable
fun Preview_SheetTopBar_Default() {
    SheetTopBar(
        title = "Sheet Title",
        onCloseClick = {}
    )
}
