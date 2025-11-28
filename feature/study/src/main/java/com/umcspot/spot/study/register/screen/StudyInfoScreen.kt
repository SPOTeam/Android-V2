package com.umcspot.spot.study.register.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.study.register.component.BinaryChoiceRow
import com.umcspot.spot.study.register.component.FeeInputSection
import com.umcspot.spot.study.register.component.MemberCountSelector
import com.umcspot.spot.ui.extension.screenHeightDp
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@Composable
fun StudyInfoScreen(
    memberCount: Int,
    onMemberCountChange: (Int) -> Unit,
    hasFee: Boolean?,
    feeAmount: String,
    onFeeInfoChange: (Boolean?, String) -> Unit,
    preferences: ImmutableList<Int?>,
    onPersonalityChange: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = screenHeightDp(65.dp))
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "모집 정보를 작성해주세요.",
            style = SpotTheme.typography.h3
        )

        Spacer(modifier = Modifier.height(screenHeightDp(20.dp)))

        MemberCountSelector(
            memberCount = memberCount,
            onMemberCountChange = onMemberCountChange
        )

        Spacer(modifier = Modifier.height(screenHeightDp(40.dp)))

        Text(
            text = "활동비",
            style = SpotTheme.typography.h5
        )

        Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

        FeeInputSection(
            hasFee = hasFee,
            feeAmount = feeAmount,
            onFeeTypeChange = { type ->
                onFeeInfoChange(type, if (type == false) "" else feeAmount)
            },
            onFeeAmountChange = { amount ->
                onFeeInfoChange(hasFee, amount)
            }
        )

        Spacer(modifier = Modifier.height(screenHeightDp(40.dp)))

        Text(
            text = "스터디의 성격을 표현하는 단어를 모두 선택해봐요.",
            style = SpotTheme.typography.h5
        )

        Spacer(modifier = Modifier.height(screenHeightDp(16.dp)))

        val choiceLabels = persistentListOf(
            "네트워킹 중시" to "목표/규율 중시",
            "단기 목표" to "장기 목표",
            "개인 학습 + 함께 토론형" to "공동 학습 + 동시 진행형",
            "학습형" to "토론형",
            "가볍게 + 유연하게" to "규칙적인 + 계획적인"
        )

        choiceLabels.forEachIndexed { index, (left, right) ->
            BinaryChoiceRow(
                leftText = left,
                rightText = right,
                selectedIndex = preferences[index],
                onSelect = { value -> onPersonalityChange(index, value) }
            )
            if (index < choiceLabels.lastIndex) {
                Spacer(modifier = Modifier.height(screenHeightDp(12.dp)))
            }
        }

        Spacer(modifier = Modifier.height(screenHeightDp(40.dp)))
    }
}