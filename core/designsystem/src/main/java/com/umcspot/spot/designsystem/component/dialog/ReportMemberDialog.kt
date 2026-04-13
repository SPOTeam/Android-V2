package com.umcspot.spot.designsystem.component.dialog

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.G200
import com.umcspot.spot.designsystem.theme.G300
import com.umcspot.spot.designsystem.theme.G400
import com.umcspot.spot.designsystem.theme.G500
import com.umcspot.spot.designsystem.theme.R500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.study.model.StudyMemberModel
import com.umcspot.spot.ui.extension.noRippleClickable
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun ReportMemberDialog(
    visible: Boolean,
    members: List<StudyMemberModel>,
    isMemberLoading: Boolean,
    selectedMemberId: Long?,
    isReportStep: Boolean,
    reportReason: String,
    onMemberSelect: (Long) -> Unit,
    onReasonChange: (String) -> Unit,
    onNext: () -> Unit,
    onSubmit: () -> Unit,
    onDismiss: () -> Unit,
) {
    if (!visible) return

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .width(screenWidthDp(326.dp))
                .wrapContentHeight(),
            shape = SpotShapes.Round,
            colors = CardDefaults.elevatedCardColors(
                containerColor = SpotTheme.colors.white
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(17.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Icon(
                        painter = painterResource(R.drawable.dismiss),
                        contentDescription = null,
                        modifier = Modifier
                            .width(screenWidthDp(26.dp))
                            .aspectRatio(1f)
                            .noRippleClickable { onDismiss() }
                    )
                }

                Text(
                    text = "스터디원을 신고하시겠습니까?",
                    style = SpotTheme.typography.h2,
                    textAlign = TextAlign.Center
                )

                Spacer(Modifier.height(screenHeightDp(10.dp)))

                if (!isReportStep) {
                    Text(
                        text = "탈퇴의 이유가 충분한 스터디원이 있나요?",
                        style = SpotTheme.typography.regular_500,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(screenHeightDp(30.dp)))

                    if (isMemberLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = screenHeightDp(100.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(
                                color = SpotTheme.colors.B500
                            )
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(5),
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = screenHeightDp(200.dp))
                                .padding(horizontal = screenWidthDp(23.5.dp)),
                            horizontalArrangement = Arrangement.spacedBy(screenWidthDp(20.dp)),
                            verticalArrangement = Arrangement.spacedBy(screenHeightDp(20.dp))
                        ) {
                            items(members) { member ->
                                val isSelected = selectedMemberId == member.id
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.noRippleClickable { onMemberSelect(member.id) }
                                ) {
                                    Box(contentAlignment = Alignment.BottomEnd) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .aspectRatio(1f)
                                                .then(
                                                    if (isSelected) Modifier.border(
                                                        width = 1.dp,
                                                        color = SpotTheme.colors.R500,
                                                        shape = CircleShape
                                                    ) else Modifier
                                                )
                                                .clip(CircleShape)
                                        ) {
                                            AsyncImage(
                                                model = member.profileUrl,
                                                contentDescription = member.name,
                                                modifier = Modifier.fillMaxSize(),
                                                contentScale = ContentScale.Crop
                                            )
                                        }

                                        if (member.isLeader) {
                                            Icon(
                                                painter = painterResource(id = R.drawable.ic_leader),
                                                contentDescription = null,
                                                modifier = Modifier
                                                    .width(screenWidthDp(10.dp))
                                                    .aspectRatio(1f),
                                                tint = Color.Unspecified
                                            )
                                        }
                                    }

                                    Spacer(Modifier.height(8.dp))

                                    Text(
                                        text = member.name,
                                        style = SpotTheme.typography.small_500,
                                        color = if (isSelected) SpotTheme.colors.R500 else SpotTheme.colors.black,
                                        maxLines = 1,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(screenHeightDp(30.dp)))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(screenWidthDp(10.dp))
                    ) {
                        OutlinedButton(
                            modifier = Modifier.weight(1f),
                            onClick = onNext,
                            enabled = selectedMemberId != null,
                            shape = SpotShapes.Soft,
                            border = BorderStroke(
                                width = 1.dp,
                                color = if (selectedMemberId != null) SpotTheme.colors.R500 else SpotTheme.colors.G300
                            ),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = if (selectedMemberId != null) SpotTheme.colors.R500 else SpotTheme.colors.G400,
                                disabledContentColor = SpotTheme.colors.G400,
                                containerColor = Color.Transparent,
                                disabledContainerColor = Color.Transparent
                            )
                        ) {
                            Text(text = "신고하기", style = SpotTheme.typography.h5)
                        }

                        OutlinedButton(
                            modifier = Modifier.weight(1f),
                            onClick = onDismiss,
                            shape = SpotShapes.Soft,
                            border = BorderStroke(1.dp, SpotTheme.colors.G500),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = SpotTheme.colors.G500,
                                containerColor = Color.Transparent
                            )
                        ) {
                            Text(text = "취소", style = SpotTheme.typography.h5)
                        }
                    }

                } else {

                    Text(
                        text = "신고 이유를 작성해주세요.\nSPOT 내부 검토 후, 탈퇴 신청을 용인합니다.",
                        style = SpotTheme.typography.regular_500,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(screenHeightDp(20.dp)))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Text(
                            text = "신고 이유",
                            style = SpotTheme.typography.h5
                        )
                        Spacer(Modifier.width(screenWidthDp(12.dp)))
                        Text(
                            text = "(선택)",
                            style = SpotTheme.typography.h5,
                            color = SpotTheme.colors.G400
                        )
                    }

                    Spacer(Modifier.height(screenHeightDp(7.dp)))

                    OutlinedTextField(
                        value = reportReason,
                        onValueChange = onReasonChange,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = screenHeightDp(120.dp))
                            .clip(SpotShapes.Hard),
                        singleLine = false,
                        minLines = 3,
                        maxLines = Int.MAX_VALUE,
                        shape = SpotShapes.Hard,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = SpotTheme.colors.white,
                            unfocusedContainerColor = SpotTheme.colors.white,
                            focusedBorderColor = SpotTheme.colors.B500,
                            unfocusedBorderColor = SpotTheme.colors.G200,
                            cursorColor = SpotTheme.colors.B500
                        )
                    )

                    Spacer(Modifier.height(screenHeightDp(16.dp)))

                    OutlinedButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = screenWidthDp(68.dp)),
                        onClick = onSubmit,
                        shape = SpotShapes.Soft,
                        border = BorderStroke(
                            width = 1.dp,
                            color = SpotTheme.colors.B500
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = SpotTheme.colors.B500,
                            containerColor = Color.Transparent
                        )
                    ) {
                        Text(text = "완료", style = SpotTheme.typography.h5)
                    }
                }
            }
        }
    }
}