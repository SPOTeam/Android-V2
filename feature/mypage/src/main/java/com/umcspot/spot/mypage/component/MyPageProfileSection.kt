package com.umcspot.spot.mypage.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.component.ProfileImage
import com.umcspot.spot.designsystem.component.button.BlankButton
import com.umcspot.spot.designsystem.component.button.ImageButtonState
import com.umcspot.spot.designsystem.shapes.ShapeBox
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.designsystem.theme.B100
import com.umcspot.spot.designsystem.theme.B200
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.model.ImageRef
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun UserProfile(
    nickName: String,
    profileImageUrl: ImageRef
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
            modifier = Modifier.size(screenWidthDp(55.dp))
        )
        Spacer(Modifier.width(screenWidthDp(13.dp)))
        Text(text = nickName, style = SpotTheme.typography.h2)
        Spacer(Modifier.width(screenWidthDp(13.dp)))
        Text(text = "님", style = SpotTheme.typography.h3)
    }
}

@Composable
fun StudyInfoFrame(
    participatingStudyCount: Int,
    recruitingStudyCount: Int,
    appliedStudyCount: Int,
    onParticipatingClick: () -> Unit,
    onRecruitingClick: () -> Unit,
    onAppliedClick: () -> Unit
) {
    ShapeBox(
        shape = SpotShapes.Round,
        color = SpotTheme.colors.B100,
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
                modifier = Modifier.height(screenHeightDp(50.dp))
            )
            StudyInfo(
                title = "모집 중",
                studyCount = recruitingStudyCount,
                onClick = onRecruitingClick
            )
            VerticalDivider(
                color = SpotTheme.colors.B200,
                thickness = 1.dp,
                modifier = Modifier.height(screenHeightDp(50.dp))
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
    title: String,
    studyCount: Int,
    onClick: () -> Unit
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