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
            shape = SpotShapes.Hard,
            tonalElevation = 2.dp,
            color = SpotTheme.colors.white
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
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
                            .size(16.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.dismiss), 
                            contentDescription = "닫기",
                            modifier = Modifier.size(16.dp)
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
                    shape = SpotShapes.Hard,
                    border = BorderStroke(1.dp, SpotTheme.colors.gray300),
                    color = SpotTheme.colors.white,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 160.dp, max = 380.dp), 
                ) {
                    val scroll = rememberScrollState()
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .verticalScroll(scroll)
                    ) {
                        
                        Text(
                            text = "제1조 (개인정보 수집 및 이용 목적)",
                            style = SpotTheme.typography.regular_500,
                            color = SpotTheme.colors.black
                        )
                        Spacer(Modifier.height(6.dp))
                        val bullet = SpotTheme.typography.small_500
                        NumberedLine(1, "회원 가입 및 관리: 본인 확인, 회원 서비스 제공", bullet)
                        NumberedLine(2, "서비스 제공 및 운영: 커뮤니티 기능 제공, 맞춤형 콘텐츠 추천", bullet)
                        NumberedLine(3, "고객지원: 문의사항 응대 및 서비스 개선", bullet)
                        NumberedLine(4, "서비스 개선 및 분석: 이용 통계 분석, 부정 이용 방지", bullet)

                        Spacer(Modifier.height(12.dp))

                        
                        Text(
                            text = "제2조 (수집하는 개인정보 항목)",
                            style = SpotTheme.typography.regular_500,
                            color = SpotTheme.colors.black
                        )
                        Spacer(Modifier.height(6.dp))
                        BulletLine("필수항목: 이름, 이메일, 생년월일, 성별", bullet)
                        BulletLine("자동 수집 항목: 접속 로그, 서비스 이용 기록, 기기 정보 등", bullet)

                        Spacer(Modifier.height(8.dp))
                    }
                }

                Spacer(Modifier.height(16.dp))

                
                TextButtonM(
                    text = "동의",
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
            shape = SpotShapes.Hard,
            tonalElevation = 2.dp,
            color = SpotTheme.colors.white
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
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
                            .size(16.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.dismiss), 
                            contentDescription = "닫기",
                            modifier = Modifier.size(16.dp)
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
                    shape = SpotShapes.Hard,
                    border = BorderStroke(1.dp, SpotTheme.colors.gray300),
                    color = SpotTheme.colors.white,
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 160.dp, max = 380.dp), 
                ) {
                    val scroll = rememberScrollState()
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .verticalScroll(scroll)
                    ) {
                        
                        Text(
                            text = "제1조 (개인정보 수집 및 이용 목적)",
                            style = SpotTheme.typography.regular_500,
                            color = SpotTheme.colors.black
                        )
                        Spacer(Modifier.height(6.dp))
                        val bullet = SpotTheme.typography.small_500
                        NumberedLine(1, "회원 가입 및 관리: 본인 확인, 회원 서비스 제공", bullet)
                        NumberedLine(2, "서비스 제공 및 운영: 커뮤니티 기능 제공, 맞춤형 콘텐츠 추천", bullet)
                        NumberedLine(3, "고객지원: 문의사항 응대 및 서비스 개선", bullet)
                        NumberedLine(4, "서비스 개선 및 분석: 이용 통계 분석, 부정 이용 방지", bullet)

                        Spacer(Modifier.height(12.dp))

                        
                        Text(
                            text = "제2조 (수집하는 개인정보 항목)",
                            style = SpotTheme.typography.regular_500,
                            color = SpotTheme.colors.black
                        )
                        Spacer(Modifier.height(6.dp))
                        BulletLine("필수항목: 이름, 이메일, 생년월일, 성별", bullet)
                        BulletLine("자동 수집 항목: 접속 로그, 서비스 이용 기록, 기기 정보 등", bullet)

                        Spacer(Modifier.height(8.dp))
                    }
                }

                Spacer(Modifier.height(16.dp))

                
                TextButtonM(
                    text = "동의",
                    onClick = onAgree
                )
            }
        }
    }
}

@Composable
private fun NumberedLine(n: Int, text: String, style: androidx.compose.ui.text.TextStyle) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        Text("$n.", style = style, color = SpotTheme.colors.black)
        Spacer(Modifier.width(6.dp))
        Text(text, style = style, color = SpotTheme.colors.black)
    }
}

@Composable
private fun BulletLine(text: String, style: androidx.compose.ui.text.TextStyle) {
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
        Text("•", style = style, color = SpotTheme.colors.black)
        Spacer(Modifier.width(6.dp))
        Text(text, style = style, color = SpotTheme.colors.black)
    }
}
