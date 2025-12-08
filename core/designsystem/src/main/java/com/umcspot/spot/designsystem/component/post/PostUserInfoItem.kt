package com.umcspot.spot.designsystem.component.post

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.model.ImageRef
import com.umcspot.spot.model.PostType

data class UserInfo(
    val userName : String = "추연우",
    val userImg : ImageRef = ImageRef.Name("R.drawable.spot_logo"),
    val date : String = "25.11.11",
    val time : String = "11:11"
)

@Composable
fun PostUserInfoItem(
    userInfo: UserInfo,
    onMoreClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        // 프로필 이미지
        Image(
            painter = painterResource(R.drawable.spot_logo),
            contentDescription = null,
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        // 이름 + 날짜/시간
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = userInfo.userName,
                style = SpotTheme.typography.medium_500
            )

            Spacer(modifier = Modifier.height(2.dp))

            Row {
                Text(
                    text = userInfo.date,
                    style = SpotTheme.typography.medium_500,
                    color = SpotTheme.colors.gray300
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = userInfo.time,
                    style = SpotTheme.typography.medium_500,
                    color = SpotTheme.colors.gray300
                )
            }
        }

        // 더보기 버튼 (...)
        IconButton(onClick = onMoreClick) {
            Icon(
                painter = painterResource(R.drawable.meetball),
                contentDescription = "더 보기"
            )
        }
    }
}


data class PostInfo(
    val id: Int = 1,
    val label: PostType = PostType.FREE_TALK,
    val title: String = "테스트 게시글 제목",
    val content : String = "이것은 임시로 채워 넣은 게시글 내용입니다.",
    val likeNum: Int = 12,
    val commentNum: Int = 3,
    val viewNum: Int = 57,
    val date : String = "25.11.11",
    val time : String = "11:11"
)

