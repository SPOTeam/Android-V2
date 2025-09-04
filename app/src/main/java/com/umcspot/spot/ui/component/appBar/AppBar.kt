package com.umcspot.spot.ui.component.appBar

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
import com.umcspot.spot.R
import com.umcspot.spot.ui.component.shape.SpotShapes
import com.umcspot.spot.ui.theme.G300
import com.umcspot.spot.ui.theme.G400
import com.umcspot.spot.ui.theme.SpotTypography
import com.umcspot.spot.ui.theme.White


@Composable
fun AppBarHome (
    hasNotification: Boolean = false,
    onSearchClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
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
                    contentDescription = "Search"
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = onNotificationClick) {
                Icon(
                    painter = painterResource(
                        id = if (hasNotification) R.drawable.alert_noti else R.drawable.alert
                    ),
                    contentDescription = if (hasNotification) "New Notifications" else "Notifications",
                    tint = Color.Unspecified
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview_NoNotification() {
    AppBarHome(
        hasNotification = false,
        onSearchClick = {},
        onNotificationClick = {}
    )
}

@Preview(showBackground = true)
@Composable
fun TopBarPreview_WithNotification() {
    AppBarHome(
        hasNotification = true,
        onSearchClick = {},
        onNotificationClick = {}
    )
}

@Composable
fun BackTopBar(
    title: String,
    onBackClick: () -> Unit,
    textStyle: TextStyle? = null // 기본값
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_left),
                contentDescription = "Back"
            )
        }

        Spacer(modifier = Modifier.width(4.dp))

        if (textStyle != null) {
            Text(
                text = title,
                style = textStyle // ← 폰트 스타일 적용
            )
        } else {
            Text(
                text = title
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun BackTopBarPreview_Title() {
    BackTopBar(
        title = "홈",
        onBackClick = {},
        textStyle = SpotTypography.bodySmall500
    )
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
    borderColor: Color = G300,
    backgroundColor: Color = White,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Back
        IconButton(onClick = onBackClick) {
            Icon(
                painter = painterResource(id = R.drawable.arrow_left),
                contentDescription = "Back"
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
                                    color = G400,
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
                                tint = Color.Unspecified
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
    SearchTopBar(
        value = text,
        onValueChange = { text = it }
    )
}
