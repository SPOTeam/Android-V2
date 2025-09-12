package com.example.feature.alert

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.example.core.data.alert.AlertItem
import com.example.core.data.global.AlertKind
import com.example.core.ui.R
import com.example.core.ui.component.appBar.BackTopBar
import com.example.core.ui.shapes.SpotShapes
import com.example.core.ui.theme.B500
import com.example.core.ui.theme.G300
import com.example.core.ui.theme.SpotTypography

@DrawableRes
fun AlertKind.iconRes(): Int = when (this) {
    AlertKind.POPULAR_POST -> R.drawable.fire
    AlertKind.STUDY_NOTICE,
    AlertKind.STUDY_SCHEDULE,
    AlertKind.TODO_DONE -> R.drawable.announce
}

fun AlertKind.needsStudyImage(): Boolean = this != AlertKind.POPULAR_POST

@Composable
fun AlertScreen(
    navController: NavController,
    viewModel: AlertViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            BackTopBar(
                title = "알림",
                onBackClick = { navController.popBackStack() }
            )
        },
    ) { innerPadding ->
        if (uiState.alerts.isEmpty() && !uiState.showAppliedStudyCard) {
            // 완전 빈 상태
            EmptyNotification(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                if (uiState.showAppliedStudyCard) {
                    item(key = "applied_card") {
                        EnrollStudyCard(
                            isAvailable = true,
                            onClick = { viewModel.onClickAppliedStudyCard() }
                        )
                        Spacer(Modifier.height(8.dp))
                    }
                }


                    items(items = uiState.alerts, key = { it.id }) { item ->
                        AlertRow(
                            data = item,
                            onClick = { viewModel.onClickAlert(item) }
                        )
                    }

            }
        }
    }
}

@Composable
private fun EnrollStudyCard(
    isAvailable : Boolean = false,
    onClick: () -> Unit
) {
    if (!isAvailable) return

    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Image(
                painter = painterResource(R.drawable.success_default),
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    text = "신청 스터디",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = "신청 스터디의 수락 알림입니다.\n클릭하여 스터디 참여를 확인해주세요.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun EmptyNotification(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.alert),
            contentDescription = null,
            modifier = modifier.size(30.dp)
        )
        Spacer(Modifier.height(12.dp))
        Text(
            text = "아직 알림이 없어요.",
            style = SpotTypography.bodyMedium500,
            color = B500
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = "스팟에서 내 목표를 이뤄봐요.",
            style = SpotTypography.bodyMedium500,
            color = G300,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun AlertRow(
    data: AlertItem,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // fire 또는 announce 아이콘 (enum → 리소스 매핑)
            Icon(
                painter = painterResource(id = data.kind.iconRes()),
                contentDescription = null,
                tint = if (data.kind == AlertKind.POPULAR_POST)
                    MaterialTheme.colorScheme.error
                else
                    MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )

            if (data.kind.needsStudyImage()) {
                Spacer(Modifier.width(4.dp))
                Image(
                    painter = painterResource(
                        id = data.studyImageRes ?: R.drawable.spot_logo // 기본 이미지 fallback
                    ),
                    contentDescription = null,
                    modifier = Modifier
                        .size(28.dp)
                        .clip(SpotShapes.Hard),
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        Column(Modifier.weight(1f)) {
            Text(
                text = data.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = data.subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
