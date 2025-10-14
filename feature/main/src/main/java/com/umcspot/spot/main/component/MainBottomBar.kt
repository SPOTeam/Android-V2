package com.umcspot.spot.main.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.umcspot.spot.designsystem.theme.B400
import com.umcspot.spot.designsystem.theme.Black
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.main.MainNavTab
import com.umcspot.spot.ui.extension.noRippleClickable
import com.umcspot.spot.ui.extension.screenHeightDp
import kotlinx.collections.immutable.ImmutableList

@Composable
fun MainBottomBar(
    visible: Boolean,
    tabs: ImmutableList<MainNavTab>,
    currentTab: MainNavTab?,
    onTabSelected: (MainNavTab) -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = EnterTransition.None,
        exit = ExitTransition.None
    ) {
        Column(
            modifier = Modifier.background(SpotTheme.colors.white)
        ) {
            HorizontalDivider(
                thickness = 1.dp,
                color = SpotTheme.colors.gray300
            )
            Row(
                modifier = Modifier
                    .navigationBarsPadding()
                    .fillMaxWidth()
                    .padding(vertical = screenHeightDp(10.dp)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                tabs.forEach { tab ->
                    key(tab.route) {
                        val selected = currentTab == tab
                        MainBottomBarItem(
                            tab = tab,
                            selected = selected,
                            onClick = { onTabSelected(tab) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RowScope.MainBottomBarItem(
    modifier: Modifier = Modifier,
    tab: MainNavTab,
    selected: Boolean,
    onClick: () -> Unit
) {
    val bottomItemColor = if (selected) B400 else Black
    val bottomTextStyle = if (selected) SpotTheme.typography.bodyRegular400 else SpotTheme.typography.bodyRegular400

    Column(
        modifier = modifier
            .noRippleClickable(onClick = onClick)
            .weight(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        Icon(
            imageVector = ImageVector.vectorResource(tab.icon),
            contentDescription = stringResource(tab.contentDescription),
            tint = bottomItemColor
        )
        Text(
            text = stringResource(tab.contentDescription),
            fontSize = 14.sp,
            style = bottomTextStyle,
            color = bottomItemColor
        )
    }
}
