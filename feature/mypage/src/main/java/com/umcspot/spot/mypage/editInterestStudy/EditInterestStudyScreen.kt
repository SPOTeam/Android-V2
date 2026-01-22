package com.umcspot.spot.mypage.editInterestStudy

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.button.TextButton
import com.umcspot.spot.designsystem.component.button.TextButtonState
import com.umcspot.spot.designsystem.component.modal.RejectDialog
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.G300
import com.umcspot.spot.designsystem.theme.G400
import com.umcspot.spot.designsystem.theme.R500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun EditInterstStudyScreen(
    contentPadding : PaddingValues,
    successCancelMemberShip: () -> Unit,
    moveToParticipatingStudy: () -> Unit,
    viewmodel : EditInterestStudyViewModel = hiltViewModel()
) {
    var showDialog by remember { mutableStateOf(false) }
    var editMode by remember { mutableStateOf(false) }
    val status by viewmodel.leaveStatus.collectAsStateWithLifecycle()

    LaunchedEffect(status) {
        when(status) {
            "MEMBER4006" -> { showFailDialog = true }
            "MEMBER200" -> { showSuccessDialog = true }
        }
    }

    Column(
        modifier = Modifier
            .background(SpotTheme.colors.white)
            .padding(
                top = contentPadding.calculateTopPadding(),
                bottom = contentPadding.calculateBottomPadding()
            )
            .padding(horizontal = screenWidthDp(17.dp), vertical = screenHeightDp(18.dp)),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        Text(text = "SPOT 탈퇴 전 확인하세요.", style = SpotTheme.typography.h3)

        Spacer(modifier = Modifier.height(screenHeightDp(10.dp)))

        Text(text = "탈퇴하시면 아래 정보들은 모두 파기되며 모든 데이터들은", style = SpotTheme.typography.regular_500)

        Spacer(modifier = Modifier.height(screenHeightDp(4.dp)))

        Row {
            Text(text = "복구가", style = SpotTheme.typography.regular_500)

            Spacer(modifier = Modifier.width(screenWidthDp(8.dp)))

            Text(
                text = "불가능",
                style = SpotTheme.typography.regular_500,
                color = SpotTheme.colors.R500
            )
            Text(text = "합니다.", style = SpotTheme.typography.regular_500)
        }

        Spacer(modifier = Modifier.height(screenHeightDp(40.dp)))

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = screenWidthDp(4.dp)),
            color = SpotTheme.colors.G300,
            thickness = 1.dp,
        )

        Spacer(modifier = Modifier.height(screenHeightDp(40.dp)))

        Text(text = "회원 정보", style = SpotTheme.typography.large_500)

        Spacer(modifier = Modifier.height(screenHeightDp(7.dp)))

        Text(text = "로그인 이메일, 성명, 생년월일 정보가 모두 삭제됩니다.", style = SpotTheme.typography.regular_500)

        Spacer(modifier = Modifier.height(screenHeightDp(40.dp)))

        Text(text = "스터디 정보", style = SpotTheme.typography.large_500)

        Spacer(modifier = Modifier.height(screenHeightDp(7.dp)))

        Text(text = "진행 중 스터디, 모집 중 스터디, 스터디 찜 정보, 모든 게시글, 댓글, 사진,", style = SpotTheme.typography.regular_500)

        Spacer(modifier = Modifier.height(screenHeightDp(4.dp)))

        Text(text = "개인 관심사/관심지역 정보 등이 모두 삭제됩니다.", style = SpotTheme.typography.regular_500)

        Spacer(modifier = Modifier.weight(1f))

        TextButton(
            modifier = Modifier
                .width(screenWidthDp(326.dp))
                .height(screenHeightDp(47.dp)),
            text = "탈퇴하기",
            style = SpotTheme.typography.h3,
            state = TextButtonState.R500State,
            shape = SpotShapes.Soft,
            onClick = { showDialog = true }
        )

        RejectDialog(
            visible = showDialog,
            painter = painterResource(R.drawable.cancel_membership),
            painterTint = SpotTheme.colors.G400,
            modalTitle = "정말 탈퇴할까요?",
            modalDes = null,
            okButtonText = "탈퇴",
            noButtonText = "취소",
            onDismiss = { showDialog = false },
            onClick = {
                viewmodel.leaveSpot()
                showDialog = false
            },
            onCancel = { showDialog = false }
        )

        RejectDialog(
            visible = showFailDialog,
            painter = painterResource(R.drawable.error),
            painterTint = SpotTheme.colors.R500,
            modalTitle = "호스트로 운영중인 스터디가 있어요.",
            modalDes = "호스트로 운영중인 스터디를 먼저 나가야\n스팟 서비스를 탈퇴할 수 있어요.",
            okButtonText = "스터디 나가기",
            noButtonText = "취소",
            onDismiss = { showFailDialog = false },
            onClick = moveToParticipatingStudy,
            onCancel = { showFailDialog = false }
        )

        RejectDialog(
            visible = showSuccessDialog,
            painter = painterResource(R.drawable.success_default),
            painterTint = SpotTheme.colors.R500,
            modalTitle = "탈퇴 처리 완료",
            modalDes = null,
            okButtonText = "확인",
            noButtonText = "취소",
            onDismiss = {
                showSuccessDialog = false
                successCancelMemberShip()
            },
            onClick = {
                showSuccessDialog = false
                successCancelMemberShip()
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CancelMemberShipScreenPreview() {
    SpotTheme {
        EditInterstStudyScreen(
            contentPadding = PaddingValues(0.dp),
            successCancelMemberShip = {},
            moveToParticipatingStudy = {}
        )
    }
}