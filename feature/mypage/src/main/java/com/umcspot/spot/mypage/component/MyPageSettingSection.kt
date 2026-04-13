package com.umcspot.spot.mypage.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.component.button.BlankButton
import com.umcspot.spot.designsystem.component.button.ImageButtonState
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun TermSection(
    onCommunityRuleClick: () -> Unit,
    onRestrictionHistoryClick: () -> Unit,
    onPrivacyPolicyClick: () -> Unit,
    onTermsOfServiceClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        listOf(
            "커뮤니티 이용 규칙" to onCommunityRuleClick,
            "이용 제한 내역" to onRestrictionHistoryClick,
            "개인정보 처리 방침" to onPrivacyPolicyClick,
            "스팟 이용 약관" to onTermsOfServiceClick
        ).forEach { (title, onClick) ->
            BlankButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                state = ImageButtonState.XOUTLINEB100State,
                shape = SpotShapes.Hard,
                align = Alignment.CenterStart,
                onClick = onClick
            ) {
                Row(
                    modifier = Modifier.padding(screenWidthDp(10.dp)),
                    horizontalArrangement = Arrangement.Start
                ) {
                    Text(text = title, style = SpotTheme.typography.medium_500)
                }
            }
        }
    }
}

@Composable
fun Logout(onLogOutClick: () -> Unit) {
    BlankButton(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        state = ImageButtonState.XOUTLINEB100State,
        shape = SpotShapes.Hard,
        align = Alignment.CenterStart,
        onClick = onLogOutClick
    ) {
        Row(
            modifier = Modifier.padding(screenWidthDp(10.dp)),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(text = "로그아웃", style = SpotTheme.typography.medium_500)
        }
    }
}