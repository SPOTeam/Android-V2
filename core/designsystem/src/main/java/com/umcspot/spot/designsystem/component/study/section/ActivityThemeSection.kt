package com.umcspot.spot.designsystem.component.study.section

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.umcspot.spot.model.StudyTheme
import com.umcspot.spot.ui.extension.screenHeightDp
import com.umcspot.spot.ui.extension.screenWidthDp
import com.umcspot.spot.designsystem.R
import com.umcspot.spot.designsystem.component.button.MultiButton
import kotlinx.collections.immutable.ImmutableList

@Composable
fun ActivityThemeSection(
    activityTheme: StudyTheme?,
    onSelect: (StudyTheme) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(screenWidthDp(14.dp)),
            verticalArrangement = Arrangement.spacedBy(screenHeightDp(16.dp))
        ) {
            StudyTheme.entries.forEach { theme ->
                val iconRes = when (theme) {
                    StudyTheme.LANGUAGE -> painterResource(R.drawable.language)
                    StudyTheme.LICENSE -> painterResource(R.drawable.license)
                    StudyTheme.EMPLOYMENT -> painterResource(R.drawable.employment)
                    StudyTheme.DISCUSSION -> painterResource(R.drawable.discussion)
                    StudyTheme.NEWS -> painterResource(R.drawable.news)
                    StudyTheme.SELFSTUDY -> painterResource(R.drawable.self_study)
                    StudyTheme.PROJECT -> painterResource(R.drawable.project)
                    StudyTheme.CONTEST -> painterResource(R.drawable.contest)
                    StudyTheme.MAJOR -> painterResource(R.drawable.major)
                    StudyTheme.ETC -> painterResource(R.drawable.resource_else)
                }

                MultiButton(
                    text = theme.title,
                    painter = iconRes,
                    checked = activityTheme == theme,
                    onClick = { onSelect(theme) },
                )
            }
        }
    }
}

@Composable
fun ActivityThemeSection(
    selectedThemes: ImmutableList<StudyTheme>,
    onSelect: (StudyTheme) -> Unit,
    modifier: Modifier = Modifier
) {
    val themesInRows = StudyTheme.entries.chunked(2)
    val isMaxSelected = selectedThemes.size >= 3

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
                    val iconRes = getIconForTheme(theme)
                    val isChecked = selectedThemes.contains(theme)

                    MultiButton(
                        text = theme.title,
                        painter = iconRes,
                        checked = isChecked,
                        enabled = !isMaxSelected || isChecked,
                        onClick = { onSelect(theme) },
                    )
                }
            }
        }
    }
}
@Composable
private fun getIconForTheme(theme: StudyTheme) = when (theme) {
    StudyTheme.LANGUAGE -> painterResource(R.drawable.language)
    StudyTheme.LICENSE -> painterResource(R.drawable.license)
    StudyTheme.EMPLOYMENT -> painterResource(R.drawable.employment)
    StudyTheme.DISCUSSION -> painterResource(R.drawable.discussion)
    StudyTheme.NEWS -> painterResource(R.drawable.news)
    StudyTheme.SELFSTUDY -> painterResource(R.drawable.self_study)
    StudyTheme.PROJECT -> painterResource(R.drawable.project)
    StudyTheme.CONTEST -> painterResource(R.drawable.contest)
    StudyTheme.MAJOR -> painterResource(R.drawable.major)
    StudyTheme.ETC -> painterResource(R.drawable.resource_else)
}