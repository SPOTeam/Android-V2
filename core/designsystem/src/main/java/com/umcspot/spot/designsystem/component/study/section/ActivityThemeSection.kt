package com.umcspot.spot.designsystem.component.study.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.button.MultiButton
import com.umcspot.spot.designsystem.shapes.SpotShapes
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp

@Composable
fun ActivityThemeSection(
    selectedThemes: List<StudyTheme>,
    onSelect: (StudyTheme) -> Unit,
    modifier: Modifier = Modifier,
    maxSelection: Int = 3,
    enabled: Boolean = true
) {
    val isMaxSelected = selectedThemes.size >= maxSelection

    BaseActivityThemeSection(
        modifier = modifier,
        isThemeSelected = { selectedThemes.contains(it) },
        isThemeEnabled = { theme -> enabled && (!isMaxSelected || selectedThemes.contains(theme)) },
        onSelect = { if (enabled) onSelect(it) }
    )
}

@Composable
private fun BaseActivityThemeSection(
    modifier: Modifier,
    isThemeSelected: (StudyTheme) -> Boolean,
    isThemeEnabled: (StudyTheme) -> Boolean,
    onSelect: (StudyTheme) -> Unit
) {
    val themesInRows = StudyTheme.entries.chunked(2)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(screenHeightDp(16.dp))
    ) {
        themesInRows.forEach { themesInRow ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(screenWidthDp(14.dp))
            ) {
                themesInRow.forEach { theme ->
                    MultiButton(
                        modifier = Modifier.weight(1f),
                        text = theme.title,
                        shape = SpotShapes.Soft,
                        painter = getIconForTheme(theme),
                        checked = isThemeSelected(theme),
                        enabled = isThemeEnabled(theme),
                        onClick = { onSelect(theme) },
                    )
                }

                if (themesInRow.size < 2) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun getIconForTheme(theme: StudyTheme) = when (theme) {
    StudyTheme.LANGUAGE -> painterResource(R.drawable.language)
    StudyTheme.CERTIFICATION -> painterResource(R.drawable.license)
    StudyTheme.CAREER -> painterResource(R.drawable.employment)
    StudyTheme.DEBATE -> painterResource(R.drawable.discussion)
    StudyTheme.CURRENT_AFFAIRS -> painterResource(R.drawable.news)
    StudyTheme.SELF_STUDY -> painterResource(R.drawable.self_study)
    StudyTheme.PROJECT -> painterResource(R.drawable.project)
    StudyTheme.COMPETITION -> painterResource(R.drawable.contest)
    StudyTheme.MAJOR_CAREER -> painterResource(R.drawable.major)
    StudyTheme.OTHER -> painterResource(R.drawable.resource_else)
}