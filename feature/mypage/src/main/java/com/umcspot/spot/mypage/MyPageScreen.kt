package com.umcspot.spot.mypage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.SpotSpinner
import com.umcspot.spot.designsystem.component.appBar.BackTopBar
import com.umcspot.spot.designsystem.component.button.BlankButton
import com.umcspot.spot.designsystem.component.button.ImageButtonState
import com.umcspot.spot.designsystem.component.modal.AcceptDialog
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.G300
import com.umcspot.spot.designsystem.theme.G400
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.mypage.component.AppVersion
import com.umcspot.spot.mypage.component.EmailInfo
import com.umcspot.spot.mypage.component.InterestedInfo
import com.umcspot.spot.mypage.component.LoginType
import com.umcspot.spot.mypage.component.Logout
import com.umcspot.spot.mypage.component.StudyInfoFrame
import com.umcspot.spot.mypage.component.TermSection
import com.umcspot.spot.mypage.component.UserProfile
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import com.umcspot.spot.ui.state.UiState
import com.umcspot.spot.user.model.MyPageResult
import kotlinx.coroutines.launch

@Composable
fun MyPageRoute(
    contentPadding: PaddingValues,
    onParticipatingClick: () -> Unit,
    onMyRecruitingClick: () -> Unit,
    onMyAppliedClick: () -> Unit,
    onEditInterestClick: () -> Unit,
    onEditInterestLocationClick: () -> Unit,
    onCancelMemberShipClick: () -> Unit,
    onLogoutClick: () -> Unit,
    viewmodel: MyPageViewModel = hiltViewModel()
) {
    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewmodel.load()
    }

    MyPageScreen(
        contentPadding = contentPadding,
        memberInfo = uiState.memberInfo,
        preferRegion = uiState.preferRegions,
        preferCategory = uiState.preferCategories,
        appVersion = uiState.appVersion,
        onParticipatingClick = onParticipatingClick,
        onMyRecruitingClick = onMyRecruitingClick,
        onMyAppliedClick = onMyAppliedClick,
        onEditInterestClick = onEditInterestClick,
        onEditInterestLocationClick = onEditInterestLocationClick,
        onCancelMemberShipClick = onCancelMemberShipClick,
        onLogoutClick = {
            scope.launch {
                viewmodel.logout()
                onLogoutClick()
            }
        }
    )
}

@Composable
private fun MyPageScreen(
    contentPadding: PaddingValues,
    memberInfo: UiState<MyPageResult>,
    preferRegion: UiState<List<String?>>,
    preferCategory: UiState<List<String?>>,
    appVersion: UiState<String>,
    onParticipatingClick: () -> Unit,
    onMyRecruitingClick: () -> Unit,
    onMyAppliedClick: () -> Unit,
    onEditInterestClick: () -> Unit,
    onEditInterestLocationClick: () -> Unit,
    onCancelMemberShipClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    var logout by remember { mutableStateOf(false) }
    var successLogout by remember { mutableStateOf(false) }

    val isLoading = memberInfo is UiState.Loading ||
            preferRegion is UiState.Loading ||
            preferCategory is UiState.Loading ||
            appVersion is UiState.Loading

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SpotTheme.colors.white)
            .padding(
                top = contentPadding.calculateTopPadding(),
                bottom = contentPadding.calculateBottomPadding()
            )
    ) {
        if (isLoading) {
            SpotSpinner(modifier = Modifier.align(Alignment.Center))
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                BackTopBar(
                    title = "마이페이지",
                    onBackClick = {},
                    modifier = Modifier.fillMaxWidth()
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = screenHeightDp(18.dp))
                        .padding(horizontal = screenWidthDp(17.dp)),
                    contentPadding = PaddingValues(bottom = screenHeightDp(24.dp))
                ) {
                    if (memberInfo is UiState.Success) {
                        item {
                            UserProfile(
                                nickName = memberInfo.data.nickname,
                                profileImageUrl = memberInfo.data.profileImageUrl
                            )
                            Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))
                        }

                        item {
                            StudyInfoFrame(
                                participatingStudyCount = memberInfo.data.participateCount,
                                recruitingStudyCount = memberInfo.data.recruitingCount,
                                appliedStudyCount = memberInfo.data.appliedCount,
                                onParticipatingClick = onParticipatingClick,
                                onRecruitingClick = onMyRecruitingClick,
                                onAppliedClick = onMyAppliedClick
                            )
                            Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))
                        }
                    }

                    if (preferCategory is UiState.Success) {
                        item {
                            InterestedInfo(
                                title = "관심 분야",
                                icon = painterResource(id = R.drawable.search_prefer),
                                interest = preferCategory.data,
                                onClick = onEditInterestClick
                            )
                            Spacer(modifier = Modifier.height(screenHeightDp(7.dp)))
                            HorizontalDivider(
                                color = SpotTheme.colors.G300,
                                thickness = 1.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = screenWidthDp(4.dp))
                            )
                        }
                    }

                    if (preferRegion is UiState.Success) {
                        item {
                            Spacer(modifier = Modifier.height(screenHeightDp(7.dp)))
                            InterestedInfo(
                                title = "관심 지역",
                                icon = painterResource(id = R.drawable.location_outline),
                                interest = preferRegion.data,
                                onClick = onEditInterestLocationClick
                            )
                            Spacer(modifier = Modifier.height(screenHeightDp(7.dp)))
                            HorizontalDivider(
                                color = SpotTheme.colors.G300,
                                thickness = 1.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = screenWidthDp(4.dp))
                            )
                        }
                    }

                    if (memberInfo is UiState.Success) {
                        item {
                            Spacer(modifier = Modifier.height(screenHeightDp(13.dp)))
                            LoginType(type = memberInfo.data.loginType)
                            EmailInfo(email = memberInfo.data.email)
                            Spacer(modifier = Modifier.height(screenHeightDp(13.dp)))
                            HorizontalDivider(
                                color = SpotTheme.colors.G300,
                                thickness = 1.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = screenWidthDp(4.dp))
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(screenHeightDp(13.dp)))
                        TermSection(
                            onCommunityRuleClick = {},
                            onRestrictionHistoryClick = {},
                            onPrivacyPolicyClick = {},
                            onTermsOfServiceClick = {}
                        )
                        Spacer(modifier = Modifier.height(screenHeightDp(13.dp)))
                        HorizontalDivider(
                            color = SpotTheme.colors.G300,
                            thickness = 1.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = screenWidthDp(4.dp))
                        )
                    }

                    if (appVersion is UiState.Success) {
                        item {
                            Spacer(modifier = Modifier.height(screenHeightDp(13.dp)))
                            AppVersion(appVersion = appVersion.data)
                            Spacer(modifier = Modifier.height(screenHeightDp(13.dp)))
                            HorizontalDivider(
                                color = SpotTheme.colors.G300,
                                thickness = 1.dp,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = screenWidthDp(4.dp))
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(screenHeightDp(13.dp)))
                        Logout(onLogOutClick = { logout = true })
                        Spacer(modifier = Modifier.height(screenHeightDp(13.dp)))
                        HorizontalDivider(
                            color = SpotTheme.colors.G300,
                            thickness = 1.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = screenWidthDp(4.dp))
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(screenHeightDp(13.dp)))
                        BlankButton(
                            modifier = Modifier
                                .width(screenWidthDp(60.dp))
                                .wrapContentHeight(),
                            state = ImageButtonState.XOUTLINEB100State,
                            shape = SpotShapes.Hard,
                            onClick = onCancelMemberShipClick
                        ) {
                            Text(
                                text = "회원 탈퇴",
                                style = SpotTheme.typography.regular_500,
                                color = SpotTheme.colors.G400,
                                textDecoration = TextDecoration.Underline,
                                modifier = Modifier.padding(
                                    horizontal = screenWidthDp(8.dp),
                                    vertical = screenHeightDp(4.dp)
                                )
                            )
                        }
                    }
                }
            }

            AcceptDialog(
                visible = logout,
                painter = painterResource(R.drawable.logout),
                painterTint = SpotTheme.colors.G400,
                modalTitle = "로그아웃 할까요?",
                modalDes = null,
                okButtonText = "로그아웃",
                noButtonText = "취소",
                onClick = {
                    successLogout = true
                    logout = false
                },
                onDismiss = { logout = false }
            )

            AcceptDialog(
                visible = successLogout,
                painter = painterResource(R.drawable.ic_check),
                painterTint = SpotTheme.colors.B500,
                modalTitle = "로그아웃 처리 완료",
                modalDes = null,
                okButtonText = "확인",
                noButtonText = null,
                onClick = {
                    successLogout = false
                    onLogoutClick()
                },
                onDismiss = {
                    successLogout = false
                    onLogoutClick()
                }
            )
        }
    }
}