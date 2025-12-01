package com.umcspot.spot.post.content

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.umcspot.spot.designsystem.theme.B500
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.feature.board.BoardViewModel
import com.umcspot.spot.post.PostViewModel

@Composable
fun PostContentScreen(
    contentPadding : PaddingValues,
    boardViewModel: BoardViewModel,
    postViewModel : PostViewModel = hiltViewModel(),
) {
    val userInfo = postViewModel.userData.collectAsStateWithLifecycle()

    val topPad = contentPadding.calculateTopPadding()
    val bottomPad = contentPadding.calculateBottomPadding()

//    LaunchedEffect(Unit) {
//        postViewModel.load("postId")
//    }
//    when (val state = postInfo.value) {
//        is UiState.Loading -> {
//            Text(text = "로딩 중...", color = Color.Gray)
//        }
//
//        is UiState.Failure -> {
//            Text(text = "에러: ${state.msg}", color = Color.Red)
//        }
//
//        is UiState.Empty -> {
//            Text(text = "데이터가 없습니다.")
//        }
//
//        is UiState.Success -> {
//            PostContentDetailScreen(state.data.posts.postList)
//        }
//    }

    PostContentDetailScreen(
        modifier = Modifier.padding(top = topPad, bottom = bottomPad)
    )
}

@Composable
fun PostContentDetailScreen(
    modifier : Modifier = Modifier,

) {
    Row (
        modifier = modifier
    ) {

    }
}

//@Composable
//private fun ActionsRow(
//    like: Int,
//    comments: Int,
//    views: Int,
//    modifier: Modifier = Modifier
//) {
//    Row(
//        modifier = modifier,
//        horizontalArrangement = Arrangement.spacedBy(16.dp)
//    ) {
//        Stat()
//        StatChip(
//            icon = { Icon(Icons.Outlined.ChatBubbleOutline, null) },
//            text = comments.toString()
//        )
//        StatChip(icon = { Icon(Icons.Outlined.Visibility, null) }, text = views.toString())
//    }
//}
//
//@Composable
//private fun Stat(
//    @DrawableRes iconRes: Int,
//    count1: Int = 0,
//    count2: Int,
//    likeChecked: Boolean= false
//) {
//    fun cap(n: Int) = if (n >= 1000) "999+" else n.toString()
//    val display = if (count1 != 0) "${cap(count1)}/${cap(count2)}" else cap(count2)
//
//    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
//        Icon(
//            painter = painterResource(iconRes),
//            contentDescription = null,
//            tint = if(likeChecked) SpotTheme.colors.B500 else Color.Unspecified,
//            modifier = Modifier.size(14.dp)
//        )
//
//        Text(text = display, style = SpotTheme.typography.small_400, color = SpotTheme.colors.B500)
//    }
//}
//
//
//@Composable
//private fun StatChip(
//    icon: @Composable () -> Unit,
//    text: String
//) {
//    Row(verticalAlignment = Alignment.CenterVertically) {
//        icon()
//        Spacer(Modifier.width(6.dp))
//        Text(text, fontSize = 13.sp)
//    }
//}
//
//@Composable
//private fun CommentItem(
//    name: String,
//    body: String,
//    modifier: Modifier = Modifier
//) {
//    Row(modifier = modifier) {
//        // 아바타 자리
//        Box(
//            modifier = Modifier
//                .size(36.dp)
//                .clip(RoundedCornerShape(18.dp))
//                .background(MaterialTheme.colorScheme.surfaceVariant)
//        )
//        Spacer(Modifier.width(12.dp))
//        Column(Modifier.weight(1f)) {
//            Text(name, fontSize = 14.sp, fontWeight = FontWeight.Medium)
//            Spacer(Modifier.height(6.dp))
//            Text(body, fontSize = 14.sp, lineHeight = 20.sp)
//        }
//    }
//}
//
//@Composable
//private fun CommentInputBar(
//    value: String,
//    onValueChange: (String) -> Unit,
//    onSend: () -> Unit
//) {
//    Surface(tonalElevation = 2.dp) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .navigationBarsPadding()
//                .imePadding()
//                .padding(horizontal = 12.dp, vertical = 8.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            OutlinedTextField(
//                value = value,
//                onValueChange = onValueChange,
//                placeholder = { Text("Sample Text") },
//                modifier = Modifier.weight(1f),
//                singleLine = true,
//                shape = RoundedCornerShape(12.dp)
//            )
//            Spacer(Modifier.width(8.dp))
//            FilledIconButton(onClick = onSend) {
//                Icon(Icons.Outlined.Send, contentDescription = "전송")
//            }
//        }
//    }
//}
