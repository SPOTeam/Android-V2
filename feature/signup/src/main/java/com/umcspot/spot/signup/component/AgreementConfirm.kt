package com.umcspot.spot.signup.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.screenHeightDp

@Composable
fun AgreementConfirm(
    privacyChecked: Boolean,
    uniqueChecked: Boolean,
    onOpenPrivacyDialog: () -> Unit,
    onOpenUniqueDialog: () -> Unit,
) {
    Column {
        Text(
            text = "약관 동의",
            style = SpotTheme.typography.h3
        )
        Spacer(Modifier.height(screenHeightDp(7.dp)))

        ConsentItem(
            title = "개인정보 이용 및 활용 동의",
            checked = privacyChecked,
            onClick = onOpenPrivacyDialog
        )
        Spacer(Modifier.height(screenHeightDp(4.dp)))

        ConsentItem(
            title = "고유식별정보 처리 동의",
            checked = uniqueChecked,
            onClick = onOpenUniqueDialog
        )
    }
}