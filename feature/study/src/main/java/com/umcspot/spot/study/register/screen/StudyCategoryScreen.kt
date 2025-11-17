package com.umcspot.spot.study.register.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.study.register.component.StudyNameTextField

@Composable
fun StudyCategoryScreen(
    onCategorySelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var studyName by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 68.dp)
    ) {
        Text(
            text = "어떤 스터디인가요?",
            style = SpotTheme.typography.h1
        )

        Spacer(modifier = Modifier.height(20.dp))

        StudyNameTextField(
            value = studyName,
            onValueChange = { studyName = it }
        )
    }
}
