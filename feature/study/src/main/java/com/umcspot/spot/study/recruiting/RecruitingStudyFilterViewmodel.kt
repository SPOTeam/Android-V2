package com.umcspot.spot.study.recruiting

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.Serializable
import com.umcspot.spot.model.ActivityType
import com.umcspot.spot.model.FeeRange
import com.umcspot.spot.model.StudyTheme

@HiltViewModel
class RecruitingStudyFilterViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    // ── 저장 키
    private val KEY_ACTIVITY = "recruit_filter_activity"
    private val KEY_FEE      = "recruit_filter_fee"
    private val KEY_THEME    = "recruit_filter_theme"

    // ── 단일 값 StateFlow
    private val _activity = MutableStateFlow(savedStateHandle.get<ActivityType?>(KEY_ACTIVITY))
    val activity: StateFlow<ActivityType?> = _activity.asStateFlow()

    private val _fee = MutableStateFlow(savedStateHandle.get<FeeRange?>(KEY_FEE))
    val fee: StateFlow<FeeRange?> = _fee.asStateFlow()

    private val _theme = MutableStateFlow(savedStateHandle.get<StudyTheme?>(KEY_THEME))
    val theme: StateFlow<StudyTheme?> = _theme.asStateFlow()

    // ✅ 하나라도 선택되었는지 여부
    private fun calcNotNull(): Boolean =
        (_activity.value != null) || (_fee.value != null) || (_theme.value != null)

    private val _notNull = MutableStateFlow(calcNotNull())
    val notNull: StateFlow<Boolean> = _notNull.asStateFlow()

    // ── 1회성 이벤트
    sealed interface Event : Serializable {
        data class Applied(val filter: RecruitingStudyFilter) : Event
    }
    private val _events = MutableSharedFlow<Event>(extraBufferCapacity = 1)
    val events: SharedFlow<Event> = _events

    // ── 합쳐서 쓰는 DTO (단일값 버전)
    data class RecruitingStudyFilter(
        val activity: ActivityType?,
        val fee: FeeRange?,
        val theme: StudyTheme?
    ) : Serializable {
        fun isEmpty() = activity == null && fee == null && theme == null
    }

    fun setActivity(type: ActivityType) {
        _activity.value = _activity.value?.let { cur -> if (cur == type) null else type } ?: type
        savedStateHandle[KEY_ACTIVITY] = _activity.value
        _notNull.value = calcNotNull()           // ✅ 갱신
    }

    fun setFee(fee: FeeRange?) {
        _fee.value = fee
        savedStateHandle[KEY_FEE] = fee
        _notNull.value = calcNotNull()           // ✅ 갱신
    }

    fun setTheme(theme: StudyTheme) {
        _theme.value = _theme.value?.let { cur -> if (cur == theme) null else theme } ?: theme
        savedStateHandle[KEY_THEME] = _theme.value
        _notNull.value = calcNotNull()           // ✅ 갱신
    }


    fun reset() {
        _activity.value = null
        _fee.value = null
        _theme.value = null
        savedStateHandle[KEY_ACTIVITY] = null
        savedStateHandle[KEY_FEE] = null
        savedStateHandle[KEY_THEME] = null
        _notNull.value = calcNotNull()           // ✅ 갱신
    }

    fun apply() {
        _events.tryEmit(
            Event.Applied(
                RecruitingStudyFilter(
                    activity = _activity.value,
                    fee = _fee.value,
                    theme = _theme.value
                )
            )
        )
    }

    fun getCurrentFilter(): RecruitingStudyFilter =
        RecruitingStudyFilter(
            activity = _activity.value,
            fee = _fee.value,
            theme = _theme.value
        )
}
