package com.umcspot.spot.signup.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.button.TextButton
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun PrivacyConsentDialog(
    open: Boolean,
    onAgree: () -> Unit,
    onDismiss: () -> Unit,
) {
    if (!open) return

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier.width(screenWidthDp(326.dp)),
            shape = SpotShapes.Round,
            tonalElevation = 2.dp,
            color = SpotTheme.colors.white
        ) {
            Column(
                modifier = Modifier
                    .padding(
                        vertical = screenHeightDp(17.dp),
                        horizontal = screenWidthDp(17.dp)
                    ),
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
                        .width(screenWidthDp(292.dp))
                        .height(screenHeightDp(200.dp))
                ) {
                    val scroll = rememberScrollState()
                    Column(
                        modifier = Modifier
                            .verticalScroll(scroll)
                            .padding(
                                vertical = screenHeightDp(10.dp),
                                horizontal = screenWidthDp(10.dp)
                            )
                    ) {
                        TitleLine(1,"(개인정보 수집 및 이용 목적)",SpotTheme.typography.regular_500)

                        Spacer(Modifier.height(screenHeightDp(4.dp)))
                        val bullet = SpotTheme.typography.small_500
                        Text(
                            text = "SPOT은 이용자의 개인정보를 다음 목적을 위해\n수집 및 이용합니다.",
                            style = bullet,
                            modifier = Modifier.padding(start = screenWidthDp(34.dp))
                        )
                        NumberedLine(1, "회원 가입 및 관리: 본인 확인, 회원 서비스 제공", bullet)
                        NumberedLine(2, "서비스 제공 및 운영: 커뮤니티 기능 제공, 맞춤형\n콘텐츠 추천", bullet)
                        NumberedLine(3, "고객지원: 문의사항 응대 및 서비스 개선", bullet)
                        NumberedLine(4, "서비스 개선 및 분석: 이용 통계 분석, 부정 이용 방지", bullet)

                        Spacer(Modifier.height(screenHeightDp(7.dp)))

                        TitleLine(2,"(수집하는 개인정보 항목)",SpotTheme.typography.regular_500)

                        Spacer(Modifier.height(screenHeightDp(4.dp)))

                        NumberedLine(1, "필수항목: 이름, 이메일, 생년월일, 성별", bullet)
                        NumberedLine(2, "자동 수집 항목: 접속 로그, 서비스 이용 기록, 기기", bullet)

                        Spacer(Modifier.height(screenHeightDp(7.dp)))

                        TitleLine(3,"(개인정보의 보유 및 이용 기간)",SpotTheme.typography.regular_500)

                        Spacer(Modifier.height(screenHeightDp(4.dp)))

                        NumberedLine(1, "원칙적으로 이용자가 서비스 탈퇴 시 즉시 개인정보를\n파기합니다. ", bullet)
                        NumberedLine(2, "단, 법령에 따른 요청이 있을 경우 예외적으로 제공될\n수 있습니다. ", bullet)

                        Spacer(Modifier.height(screenHeightDp(7.dp)))

                        TitleLine(4,"(수집하는 개인정보 항목)",SpotTheme.typography.regular_500)

                        Spacer(Modifier.height(screenHeightDp(4.dp)))

                        NumberedLine(1, "필수항목: 이름, 이메일, 생년월일, 성별", bullet)
                        NumberedLine(2, "자동 수집 항목: 접속 로그, 서비스 이용 기록, 기기", bullet)

                        Spacer(Modifier.height(screenHeightDp(7.dp)))

                        TitleLine(5,"(동의 거부 권리 및 불이익 안내)",SpotTheme.typography.regular_500)

                        Spacer(Modifier.height(screenHeightDp(4.dp)))

                        Text(
                            text = "이용자는 개인정보 수집·이용에 대한 동의를 거부할\n" +
                                    "권리가 있으며, 다만 필수 항목에 대한 동의를 거부할 경우\n" +
                                    "서비스 이용이 제한될 수 있습니다.",
                            style = bullet,
                            modifier = Modifier.padding(start = screenWidthDp(34.dp))
                        )
                    }
                }

                Spacer(Modifier.height(screenHeightDp(20.dp)))

                TextButton(
                    modifier = Modifier
                        .padding(vertical = screenHeightDp(9.dp))
                        .width(screenWidthDp(156.dp))
                        .heightIn(screenHeightDp(39.dp)),
                    text = "동의",
                    style = SpotTheme.typography.h5,
                    enabled = true,
                    onClick = onAgree,
                    shape = SpotShapes.Soft
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
            modifier = Modifier.width(screenWidthDp(326.dp)),
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
                    .width(screenWidthDp(326.dp)),
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
                        .width(screenWidthDp(292.dp))
                        .height(screenHeightDp(200.dp))
                ) {
                    val scroll = rememberScrollState()
                    Column(
                        modifier = Modifier
                            .verticalScroll(scroll)
                            .padding(
                                vertical = screenHeightDp(10.dp),
                                horizontal = screenWidthDp(10.dp)
                            )
                    ) {
                        TitleLine(1,"(고유식별정보의 수집 및 이용 목적)",SpotTheme.typography.regular_500)

                        Spacer(Modifier.height(screenHeightDp(4.dp)))
                        val bullet = SpotTheme.typography.small_500
                        Text(
                            text = "SPOT은 아래의 목적을 위해 이용자의 고유식별정보를 수집 및 이용할 수 있습니다.",
                            style = bullet,
                            modifier = Modifier.padding(start = screenWidthDp(34.dp))
                        )
                        NumberedLine(1, "본인 확인 및 인증", bullet)
                        NumberedLine(2, "법적 의무 이행 (예: 연령 검증)", bullet)

                        Spacer(Modifier.height(screenHeightDp(7.dp)))

                        TitleLine(2,"(수집하는 고유식별정보 항목)",SpotTheme.typography.regular_500)

                        Spacer(Modifier.height(screenHeightDp(4.dp)))

                        Text(
                            text = "SPOT은 다음과 같은 고유식별정보를 수집할 수 있습니다.",
                            style = bullet,
                            modifier = Modifier.padding(start = screenWidthDp(34.dp))
                        )
                        NumberedLine(1, "주민등록록번호, 외국인등록번호 (필요 시)", bullet)

                        Spacer(Modifier.height(screenHeightDp(7.dp)))

                        TitleLine(3,"(고유식별정보의 보유 및 이용 기간)",SpotTheme.typography.regular_500)

                        Spacer(Modifier.height(screenHeightDp(4.dp)))

                        NumberedLine(1, "원칙적으로 목적 달성 후 즉시 파기합니다.", bullet)
                        NumberedLine(2, "단, 관련 법령에 따라 일정 기간 보관이 필요한 경우 해당\n기간 동안 보관 후 파기합니다.", bullet)

                        Spacer(Modifier.height(screenHeightDp(7.dp)))

                        TitleLine(4,"(고유식별정보의 제3자 제공 및 위탁)",SpotTheme.typography.regular_500)

                        Spacer(Modifier.height(screenHeightDp(4.dp)))

                        NumberedLine(1, "SPOT은 이용자의 동의 없이 고유식별정보를 제3자에게\n제공하지 않습니다.", bullet)
                        NumberedLine(2, "단, 법령에 따른 요청이 있을 경우 예외적으로 제공될\n수 있습니다.", bullet)

                        Spacer(Modifier.height(screenHeightDp(7.dp)))

                        TitleLine(5,"(동의 거부 권리 및 불이익 안내)",SpotTheme.typography.regular_500)

                        Spacer(Modifier.height(screenHeightDp(4.dp)))

                        Text(
                            text = "이용자는 고유식별정보 제공에 대한 동의를 거부할 권리가\n" +
                                    "있으며, 다만 동의를 거부할 경우 특정 서비스 이용이 제한될 수 있습니다.",
                            style = bullet,
                            modifier = Modifier.padding(start = screenWidthDp(34.dp))
                        )
                    }
                }

                Spacer(Modifier.height(screenHeightDp(20.dp)))


                TextButton(
                    modifier = Modifier
                        .padding(vertical = screenHeightDp(9.dp))
                        .width(screenWidthDp(156.dp))
                        .heightIn(screenHeightDp(39.dp)),
                    text = "동의",
                    style = SpotTheme.typography.h5,
                    enabled = true,
                    onClick = onAgree,
                    shape = SpotShapes.Soft
                )
            }
        }
    }
}

@Composable
private fun TitleLine(
    number: Int,
    text: String,
    style: TextStyle
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "제${number}조",
            style = style,
            modifier = Modifier.width(screenWidthDp(30.dp)) // 제##조 영역 고정
        )

        Spacer(modifier = Modifier.width(screenWidthDp(4.dp)))

        Text(
            text = text,
            style = style,
            modifier = Modifier.weight(1f) // 본문 영역
        )
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
        Text(
            text = "$number.",
            style = style,
            modifier = Modifier.width(screenWidthDp(12.dp)) // 숫자 영역 고정
        )
        Text(
            text = text,
            style = style,
            modifier = Modifier.weight(1f) // 본문 영역
        )
    }
}