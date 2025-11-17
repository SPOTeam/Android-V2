package com.umcspot.spot.designsystem.component.appBar

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.G300
import com.umcspot.spot.designsystem.theme.G400
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.designsystem.theme.White


@Composable
fun AppBarHome (
    hasAlert: Boolean = false,
    onSearchClick: () -> Unit,
    onAlertClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(SpotTheme.colors.white)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // 로고
        Image(
            painter = painterResource(id = R.drawable.spot_logo),
            contentDescription = "App Logo",
            modifier = Modifier.size(32.dp)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onSearchClick) {
                Icon(
                    painter = painterResource(id = R.drawable.search),
                    contentDescription = "Search",
                    modifier = Modifier.size(32.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = onAlertClick) {
                Icon(
                    painter = painterResource(
                        id = if (hasAlert) R.drawable.alert_noti else R.drawable.alert
                    ),
                    contentDescription = if (hasAlert) "New Notifications" else "Notifications",
                    tint = Color.Unspecified,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview_NoNotification() {
    SpotTheme {
        AppBarHome(
            hasAlert = false,
            onSearchClick = {},
            onAlertClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview_WithNotification() {
    SpotTheme{
        AppBarHome(
            hasAlert = true,
            onSearchClick = {},
            onAlertClick = {}
        )
    }
}

@Composable
fun BackTopBar(
    title: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(SpotTheme.colors.white)
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start // ✅ 왼쪽 정렬 고정verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBackClick,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_left),
                contentDescription = "Back",
                modifier = Modifier.size(25.dp)
            )
        }

        Spacer(Modifier.width(8.dp))
        Text(
            text = title,
            style = SpotTheme.typography.medium_500,
            fontSize = 18.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun BackTopBarPreview_Title() {
    SpotTheme{
        BackTopBar(
            title = "홈",
            onBackClick = {},
        )
    }
}

@Composable
fun SearchTopBar(
    value: String,
    onValueChange: (String) -> Unit,
    onBackClick: () -> Unit = {},
    onSearchIconClick: () -> Unit = {},
    placeholder: String = "Text",
    shape: Shape = SpotShapes.Hard,
    textStyle: TextStyle? = null,
    borderWidth : Dp = 1.dp,
    borderColor: Color = SpotTheme.colors.G300,
    backgroundColor: Color = White,
    modifier: Modifier = Modifier, // ✅ 추가
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(SpotTheme.colors.white)
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back
        IconButton(onClick = onBackClick) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_left),
                contentDescription = "Back",
                modifier = Modifier.size(25.dp)
            )
        }

        Spacer(Modifier.width(8.dp))

        // Search pill
        Box(
            modifier = Modifier.weight(1f)
        ) {
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                singleLine = true,
                textStyle = textStyle ?: TextStyle.Default,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp)
                    .background(backgroundColor, shape)
                    .border(borderWidth, borderColor, shape)
                    .padding(horizontal = 14.dp),
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(Modifier.weight(1f)) {
                            if (value.isEmpty()) {
                                Text(
                                    text = placeholder,
                                    color = SpotTheme.colors.G400,
                                    style = textStyle ?: TextStyle.Default
                                )
                            }
                            innerTextField()
                        }
                        IconButton(
                            onClick = onSearchIconClick,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.search),
                                contentDescription = "Search",
                                tint = Color.Unspecified,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true, name = "SearchTopBar - With Text")
@Composable
fun PreviewSearchTopBarWithText() {
    var text by remember { mutableStateOf("안녕하세요") }

    SpotTheme{
        SearchTopBar(
            value = text,
            onValueChange = { text = it }
        )
    }
}
