package com.umcspot.spot.signup

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.HorizontalAlignmentLine
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.button.TextButton
import com.umcspot.spot.designsystem.component.button.TextButtonM
import com.umcspot.spot.designsystem.component.button.TextButtonS
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.screenHeightDp

@Composable
fun PrivacyConsentDialog(
    open: Boolean,
    onAgree: () -> Unit,
    onDismiss: () -> Unit,
) {
    if (!open) return

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = SpotShapes.Round,
            shape = RoundedCornerShape(14.dp),
            tonalElevation = 2.dp,
            color = SpotTheme.colors.white
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        vertical = screenHeightDp(17.dp),
                        horizontal = screenWidthDp(17.dp)
                    )
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Spacer(Modifier.weight(1f))

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(20.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.dismiss),
                            contentDescription = "닫기",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "개인정보 이용 및 활용 동의",
                        style = SpotTheme.typography.h2,
                        color = SpotTheme.colors.black,
                    )
                }

                Spacer(Modifier.height(screenHeightDp(20.dp)))

                Surface(
                    shape = SpotShapes.Soft,
                    border = BorderStroke(1.dp, SpotTheme.colors.gray300),
                    color = SpotTheme.colors.white,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    val scroll = rememberScrollState()
                    Column(
                        modifier = Modifier
                            .padding(
                                vertical = screenHeightDp(10.dp),
                                horizontal = screenWidthDp(10.dp)
                            )
                            .verticalScroll(scroll)
                    ) {

                        Text(
                            text = "제1조 (개인정보 수집 및 이용 목적)",
                            style = SpotTheme.typography.regular_500,
                            color = SpotTheme.colors.black
                        )
                        Spacer(Modifier.height(screenHeightDp(4.dp)))
                        val bullet = SpotTheme.typography.small_500
                        Text(
                            text = "SPOT은 이용자의 개인정보를 다음 목적을 위해\n수집 및 이용합니다.",
                            style = bullet,
                            modifier = Modifier.padding(start = screenWidthDp(34.dp))
                        )
                        NumberedLine(1, "회원 가입 및 관리: 본인 확인, 회원 서비스 제공", bullet)
                        NumberedLine(2, "서비스 제공 및 운영: 커뮤니티 기능 제공, 맞춤형 \n콘텐츠 추천", bullet)
                        NumberedLine(3, "고객지원: 문의사항 응대 및 서비스 개선", bullet)
                        NumberedLine(4, "서비스 개선 및 분석: 이용 통계 분석, 부정 이용 방지", bullet)

                        Spacer(Modifier.height(screenHeightDp(7.dp)))


                        Text(
                            text = "제2조 (수집하는 개인정보 항목)",
                            style = SpotTheme.typography.regular_500,
                            color = SpotTheme.colors.black
                        )
                        Spacer(Modifier.height(screenHeightDp(4.dp)))

                        NumberedLine(1, "필수항목: 이름, 이메일, 생년월일, 성별", bullet)
                        NumberedLine(2, "자동 수집 항목: 접속 로그, 서비스 이용 기록, 기기", bullet)

                    }
                }

                Spacer(Modifier.height(screenHeightDp(20.dp)))


                SpotActivationButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = screenWidthDp(51.dp)),
                    buttonText = "동의",
                    isEnabled = true,
                    onClick = onAgree
                )
            }
        }
    }
}

@Composable
fun UniqueConsentDialog(
    open: Boolean,
    onAgree: () -> Unit,
    onDismiss: () -> Unit,
) {
    if (!open) return

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = SpotShapes.Round,
            tonalElevation = 2.dp,
            color = SpotTheme.colors.white
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        vertical = screenHeightDp(17.dp),
                        horizontal = screenWidthDp(17.dp)
                    )
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.Top
                ) {
                    Spacer(Modifier.weight(1f))

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .size(20.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.dismiss),
                            contentDescription = "닫기",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "고유식별정보 처리 동의",
                        style = SpotTheme.typography.h2,
                        color = SpotTheme.colors.black,
                    )
                }

                Spacer(Modifier.height(screenHeightDp(20.dp)))

                Surface(
                    shape = SpotShapes.Soft,
                    border = BorderStroke(1.dp, SpotTheme.colors.gray300),
                    color = SpotTheme.colors.white,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    val scroll = rememberScrollState()
                    Column(
                        modifier = Modifier
                            .padding(
                                vertical = screenHeightDp(10.dp),
                                horizontal = screenWidthDp(10.dp)
                            )
                            .verticalScroll(scroll)
                    ) {

                        Text(
                            text = "제1조 (개인정보 수집 및 이용 목적)",
                            style = SpotTheme.typography.regular_500,
                            color = SpotTheme.colors.black
                        )
                        Spacer(Modifier.height(screenHeightDp(4.dp)))
                        val bullet = SpotTheme.typography.small_500
                        Text(
                            text = "SPOT은 이용자의 개인정보를 다음 목적을 위해\n수집 및 이용합니다.",
                            style = bullet,
                            modifier = Modifier.padding(start = screenWidthDp(34.dp))
                        )
                        NumberedLine(1, "회원 가입 및 관리: 본인 확인, 회원 서비스 제공", bullet)
                        NumberedLine(2, "서비스 제공 및 운영: 커뮤니티 기능 제공, 맞춤형 \n콘텐츠 추천", bullet)
                        NumberedLine(3, "고객지원: 문의사항 응대 및 서비스 개선", bullet)
                        NumberedLine(4, "서비스 개선 및 분석: 이용 통계 분석, 부정 이용 방지", bullet)

                        Spacer(Modifier.height(screenHeightDp(7.dp)))


                        Text(
                            text = "제2조 (수집하는 개인정보 항목)",
                            style = SpotTheme.typography.regular_500,
                            color = SpotTheme.colors.black
                        )
                        Spacer(Modifier.height(screenHeightDp(4.dp)))

                        NumberedLine(1, "필수항목: 이름, 이메일, 생년월일, 성별", bullet)
                        NumberedLine(2, "자동 수집 항목: 접속 로그, 서비스 이용 기록, 기기", bullet)

                    }
                }

                Spacer(Modifier.height(screenHeightDp(20.dp)))


                SpotActivationButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = screenWidthDp(51.dp)),
                    buttonText = "동의",
                    isEnabled = true,
                    onClick = onAgree
                )
            }
        }
    }
}

@Composable
private fun NumberedLine(
    number: Int,
    text: String,
    style: TextStyle
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = screenWidthDp(34.dp)),
        verticalAlignment = Alignment.Top
    ) {
        Text(text = "$number. ", style = style)
        Text(text = text, style = style)
    }
}