package com.umcspot.spot.study.detail.component.memoir

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.theme.SpotTheme

@Composable
fun StudyDetailMemoirItem(
    thumbnailUrl: String?,
    description: String,
    writerName: String,
    authorProfileUrl: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .width(96.dp)   
            .height(175.dp) 
            .padding(4.dp)
    ) {
        AsyncImage(
            model = thumbnailUrl,
            contentDescription = null,
            modifier = Modifier
                .size(88.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(SpotTheme.colors.gray100),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.memoir_sample_img),
            error = painterResource(id = R.drawable.memoir_sample_img)
        )
        
        Spacer(modifier = Modifier.height(7.dp))

        Text(
            text = description,
            style = SpotTheme.typography.small_400,
            color = SpotTheme.colors.black,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .width(88.dp)
                .weight(1f)
        )

        Spacer(modifier = Modifier.height(7.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.width(88.dp)
        ) {
            AsyncImage(
                model = authorProfileUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(SpotTheme.colors.gray100),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.profile_sample_png),
                error = painterResource(id = R.drawable.profile_sample_png)
            )
            Text(
                text = writerName,
                style = SpotTheme.typography.regular_500,
                color = SpotTheme.colors.black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
        }
    }
}