package com.umcspot.spot.mypage.main

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.ProfileImage
import com.umcspot.spot.designsystem.component.SpotSpinner
import com.umcspot.spot.designsystem.component.button.BlankButton
import com.umcspot.spot.designsystem.component.button.ImageButtonState
import com.umcspot.spot.designsystem.shapes.ShapeBox
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.B100
import com.umcspot.spot.designsystem.theme.B200
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.G300
import com.umcspot.spot.designsystem.theme.G400
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.model.ImageRef
import com.umcspot.spot.model.SocialLoginType
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import com.umcspot.spot.ui.state.UiState
import com.umcspot.spot.user.model.MyPageResult

@Composable
fun MyPageScreen(
    contentPadding : PaddingValues,
    onParticipatingClick : () -> Unit,
    onMyRecruitingClick : () -> Unit,
    onMyAppliedClick : () -> Unit,
    onEditInterestClick : () -> Unit,
    onEditInterestLocationClick : () -> Unit,
    onCancelMemberShipClick: () -> Unit,
    viewmodel : MyPageViewModel = hiltViewModel()
) {
    val uiState by viewmodel.uiState.collectAsStateWithLifecycle()


    LaunchedEffect(Unit) {
        viewmodel.load()
    }

    MyPageScreenContent(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(top = contentPadding.calculateTopPadding(), bottom = contentPadding.calculateBottomPadding()),
        memberInfo = uiState.memberInfo,
        preferRegion = uiState.preferRegions,
        preferCategory = uiState.preferCategories,
        appVersion = uiState.appVersion,
        onParticipatingClick = onParticipatingClick,
        onMyRecruitingClick = onMyRecruitingClick,
        onMyAppliedClick = onMyAppliedClick,
        onEditInterestClick = onEditInterestClick,
        onEditInterestLocationClick = onEditInterestLocationClick,
        onCancelMemberShipClick = onCancelMemberShipClick
    )
}

@Composable
fun MyPageScreenContent(
    modifier: Modifier = Modifier,
    memberInfo: UiState<MyPageResult>,
    preferRegion: UiState<List<String?>>,
    preferCategory: UiState<List<String?>>,
    appVersion : UiState<String>,
    onParticipatingClick: () -> Unit,
    onMyRecruitingClick: () -> Unit,
    onMyAppliedClick: () -> Unit,
    onEditInterestClick: () -> Unit,
    onEditInterestLocationClick: () -> Unit,
    onCancelMemberShipClick: () -> Unit
) {
    LazyColumn(
        modifier = modifier
            .padding(top = screenHeightDp(18.dp))
            .padding(horizontal = screenWidthDp(17.dp)),
        contentPadding = PaddingValues(bottom = screenHeightDp(24.dp))
    ) {
        item {
            when (memberInfo) {
                is UiState.Loading, is UiState.Empty, is UiState.Failure -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        SpotSpinner()
                    }

                    Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))
                }

                is UiState.Success -> {
                    val data = memberInfo.data

                    UserProfile(
                        nickName = data.nickname,
                        profileImageUrl = data.profileImageUrl
                    )

                    Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))
                }
            }
        }

        item {
            when(memberInfo) {
                is UiState.Loading, is UiState.Empty, is UiState.Failure -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        SpotSpinner()
                    }

                    Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))
                }

                is UiState.Success -> {
                    val data = memberInfo.data

                    StudyInfoFrame(
                        participatingStudyCount = data.participateCount,
                        recruitingStudyCount = data.recruitingCount,
                        appliedStudyCount = data.appliedCount,
                        onParticipatingClick = onParticipatingClick,
                        onRecruitingClick = onMyRecruitingClick,
                        onAppliedClick = onMyAppliedClick
                    )

                    Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))
                }
            }
        }


        item {
            when(preferCategory) {
                is UiState.Loading, is UiState.Empty, is UiState.Failure -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        SpotSpinner()
                    }

                    Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))
                }
                is UiState.Success -> {
                    val data = preferCategory.data

                    InterestedInfo(
                        title = "관심 분야",
                        icon = painterResource(id = R.drawable.search_prefer),
                        interest = data,
                        onClick = {}
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
        }

        item {
            when(preferRegion) {
                is UiState.Loading, is UiState.Empty, is UiState.Failure -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        SpotSpinner()
                    }

                    Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))
                }

                is UiState.Success -> {
                    val data = preferRegion.data

                    Spacer(modifier = Modifier.height(screenHeightDp(7.dp)))

                    InterestedInfo(
                        title = "관심 지역",
                        icon = painterResource(id = R.drawable.location_outline),
                        interest = data,
                        onClick = {}
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
        }


        item {
            when(memberInfo) {
                is UiState.Loading, is UiState.Empty, is UiState.Failure -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        SpotSpinner()
                    }

                    Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))
                }

                is UiState.Success -> {
                    val data = memberInfo.data

                    Spacer(modifier = Modifier.height(screenHeightDp(13.dp)))

                    LoginType(type = data.loginType)

                    EmailInfo(email = data.email)

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

        item {
            when(appVersion) {
                is UiState.Loading, is UiState.Empty, is UiState.Failure -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        contentAlignment = Alignment.Center
                    ) {
                        SpotSpinner()
                    }

                    Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))
                }

                is UiState.Success -> {
                    val version = appVersion.data

                    Spacer(modifier = Modifier.height(screenHeightDp(13.dp)))

                    AppVersion(appVersion = version)

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
        }

        item {
            Spacer(modifier = Modifier.height(screenHeightDp(13.dp)))

            Logout(
                onLogOutClick = {}
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

        item {
            Spacer(modifier = Modifier.height(screenHeightDp(13.dp)))

            BlankButton(
                modifier = Modifier
                    .width(screenWidthDp(60.dp))
                    .height(screenHeightDp(26.dp)),
                state = ImageButtonState.XOUTLINEB100State,
                shape = SpotShapes.Hard,
                onClick = onCancelMemberShipClick
            ) {
                Text(
                    text = "회원 탈퇴",
                    style = SpotTheme.typography.regular_500,
                    color = SpotTheme.colors.G400,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier
                        .padding(horizontal = screenWidthDp(8.dp), vertical = screenHeightDp(4.dp))
                )
            }
        }
    }
}

@Composable
fun UserProfile(
    nickName : String,
    profileImageUrl : ImageRef
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeightDp(55.dp)),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        ProfileImage(
            imageRef = profileImageUrl,
            modifier = Modifier
                .size(screenWidthDp(55.dp))
                .padding(start = screenWidthDp(3.dp), top = screenHeightDp(1.dp)) // 이미지 잘림 보정
        )

        Spacer(Modifier.width(screenWidthDp(13.dp)))

        Text(
            text = nickName,
            style = SpotTheme.typography.h2
        )

        Spacer(Modifier.width(screenWidthDp(13.dp)))

        Text(
            text = "님",
            style = SpotTheme.typography.h3
        )
    }
}

@Composable
fun StudyInfoFrame(
    participatingStudyCount : Int,
    recruitingStudyCount : Int,
    appliedStudyCount : Int,
    onParticipatingClick: () -> Unit,
    onRecruitingClick: () -> Unit,
    onAppliedClick: () -> Unit,
) {
    ShapeBox(
        shape = SpotShapes.Round,
        color = SpotTheme.colors.B100,
        modifier = Modifier
            .width(screenWidthDp(326.dp))
            .height(screenHeightDp(104.dp)),
        borderWidth = 1.dp,
        borderColor = SpotTheme.colors.B200
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(screenWidthDp(7.dp)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StudyInfo(
                title = "참여 중",
                studyCount = participatingStudyCount,
                onClick = onParticipatingClick
            )

            VerticalDivider(
                color = SpotTheme.colors.B200,
                thickness = 1.dp,
                modifier = Modifier
                    .height(screenHeightDp(50.dp))
            )

            StudyInfo(
                title = "모집 중",
                studyCount = recruitingStudyCount,
                onClick = onRecruitingClick
            )

            VerticalDivider(
                color = SpotTheme.colors.B200,
                thickness = 1.dp,
                modifier = Modifier
                    .height(screenHeightDp(50.dp))
            )

            StudyInfo(
                title = "신청한",
                studyCount = appliedStudyCount,
                onClick = onAppliedClick
            )
        }
    }
}

@Composable
fun StudyInfo(
    title : String,
    studyCount : Int,
    onClick : () -> Unit
) {
    BlankButton(
        modifier = Modifier
            .width(screenWidthDp(95.dp))
            .height(screenHeightDp(90.dp)),
        state = ImageButtonState.XOUTLINETransparentState,
        shape = SpotShapes.Soft,
        onClick = onClick
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                color = SpotTheme.colors.B500,
                style = SpotTheme.typography.medium_400
            )

            Spacer(modifier = Modifier.height(screenHeightDp(8.dp)))

            Text(
                text = studyCount.toString(),
                style = SpotTheme.typography.medium_500
            )
        }
    }
}

@Composable
fun InterestedInfo(
    title : String,
    icon : Painter,
    interest : List<String?>,
    onClick : () -> Unit
) {
    val interestText = remember(interest) {
        interest.joinToString(separator = " ") { "#$it" }
    }

    BlankButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeightDp(41.dp)),
        state = ImageButtonState.XOUTLINEB100State,
        shape = SpotShapes.Hard,
        align = Alignment.CenterStart,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .padding(screenWidthDp(10.dp))
        ) {
            Image(
                painter = icon,
                contentDescription = null,
                modifier = Modifier.size(screenWidthDp(18.dp)),
                colorFilter = ColorFilter.tint(SpotTheme.colors.B500)
            )

            Spacer(modifier = Modifier.width(screenWidthDp(7.dp)))

            Text(
                text = title,
                style = SpotTheme.typography.medium_500
            )

            Spacer(modifier = Modifier.width(screenWidthDp(7.dp)))

            Text(
                text = interestText,
                modifier = Modifier.weight(1f),
                color = SpotTheme.colors.G400,
                maxLines = 1,
                style = SpotTheme.typography.medium_500,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun LoginType(
    type : SocialLoginType,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeightDp(63.dp))
            .padding(screenWidthDp(10.dp))
    ) {
        Text(
            text = "소셜로그인",
            style = SpotTheme.typography.medium_500
        )

        Spacer(modifier = Modifier.height(screenHeightDp(1.dp)))

        Text(
            text = type.toMainPageText(),
            color = SpotTheme.colors.G400,
            style = SpotTheme.typography.medium_500,
        )
    }
}

@Composable
fun EmailInfo(
    email : String,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeightDp(63.dp))
            .padding(screenWidthDp(10.dp))
    ) {
        Text(
            text = "이메일",
            style = SpotTheme.typography.medium_500
        )
        Spacer(modifier = Modifier.height(screenHeightDp(1.dp)))
        Text(
            text = email,
            color = SpotTheme.colors.G400,
            style = SpotTheme.typography.medium_500,
        )
    }
}

@Composable
fun TermSection(
    onCommunityRuleClick : () -> Unit,
    onRestrictionHistoryClick : () -> Unit,
    onPrivacyPolicyClick : () -> Unit,
    onTermsOfServiceClick : () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        BlankButton(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            state = ImageButtonState.XOUTLINEB100State,
            shape = SpotShapes.Hard,
            align = Alignment.CenterStart,
            onClick = onCommunityRuleClick
        ) {
            Row(
                modifier = Modifier
                    .padding(screenWidthDp(10.dp)),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "커뮤니티 이용 규칙",
                    style = SpotTheme.typography.medium_500,
                )
            }
        }

        BlankButton(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            state = ImageButtonState.XOUTLINEB100State,
            shape = SpotShapes.Hard,
            align = Alignment.CenterStart,
            onClick = onRestrictionHistoryClick,
        ) {
            Row(
                modifier = Modifier
                    .padding(screenWidthDp(10.dp)),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "이용 제한 내역",
                    style = SpotTheme.typography.medium_500,
                )
            }
        }

        BlankButton(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            state = ImageButtonState.XOUTLINEB100State,
            shape = SpotShapes.Hard,
            align = Alignment.CenterStart,
            onClick = onPrivacyPolicyClick
        ) {
            Row(
                modifier = Modifier
                    .padding(screenWidthDp(10.dp)),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "개인정보 처리 방침",
                    style = SpotTheme.typography.medium_500,
                )
            }
        }

        BlankButton(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            state = ImageButtonState.XOUTLINEB100State,
            shape = SpotShapes.Hard,
            align = Alignment.CenterStart,
            onClick = onTermsOfServiceClick
        ) {
            Row(
                modifier = Modifier
                    .padding(screenWidthDp(10.dp)),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "스팟 이용 약관",
                    style = SpotTheme.typography.medium_500,
                )
            }
        }
    }
}

@Composable
fun AppVersion(
    appVersion : String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(screenHeightDp(63.dp))
            .padding(screenWidthDp(10.dp))
    ) {
        Text(
            text = "앱 버전",
            style = SpotTheme.typography.medium_500
        )

        Spacer(modifier = Modifier.height(screenHeightDp(1.dp)))

        Text(
            text = "v${appVersion}",
            color = SpotTheme.colors.G400,
            style = SpotTheme.typography.medium_500,
        )
    }
}

@Composable
fun Logout(
    onLogOutClick:() -> Unit
) {
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
            modifier = Modifier
                .padding(screenWidthDp(10.dp)),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = "로그아웃",
                style = SpotTheme.typography.medium_500,
            )
        }
    }
}


@Composable
private fun SocialLoginType.toMainPageText() : String =
    when(this) {
        SocialLoginType.KAKAO -> "KakaoTalk"
        SocialLoginType.NAVER -> "Naver"
    }