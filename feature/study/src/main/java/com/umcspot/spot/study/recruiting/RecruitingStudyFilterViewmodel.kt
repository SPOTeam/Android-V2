package com.umcspot.spot.study.recruiting

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.umcspot.spot.model.ActivityType
import com.umcspot.spot.model.FeeRange
import com.umcspot.spot.model.StudyTheme
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList 
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList 
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.Serializable
import javax.inject.Inject

@HiltViewModel
class RecruitingStudyFilterViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val KEY_ACTIVITIES = "recruit_filter_activities"
    private val KEY_FEES       = "recruit_filter_fees"
    private val KEY_THEMES     = "recruit_filter_themes"

    private val _activities = MutableStateFlow<PersistentList<ActivityType>>(
        savedStateHandle.get<List<ActivityType>>(KEY_ACTIVITIES)?.toPersistentList() ?: persistentListOf()
    )
    val activities: StateFlow<ImmutableList<ActivityType>> = _activities.asStateFlow()

    private val _fees = MutableStateFlow<PersistentList<FeeRange>>(
        savedStateHandle.get<List<FeeRange>>(KEY_FEES)?.toPersistentList() ?: persistentListOf()
    )
    val fees: StateFlow<ImmutableList<FeeRange>> = _fees.asStateFlow()

    private val _themes = MutableStateFlow<PersistentList<StudyTheme>>(
        savedStateHandle.get<List<StudyTheme>>(KEY_THEMES)?.toPersistentList() ?: persistentListOf()
    )
    val themes: StateFlow<ImmutableList<StudyTheme>> = _themes.asStateFlow()

    private fun calcNotNull(): Boolean =
        _activities.value.isNotEmpty() || _fees.value.isNotEmpty() || _themes.value.isNotEmpty()

    private val _notNull = MutableStateFlow(calcNotNull())
    val notNull: StateFlow<Boolean> = _notNull.asStateFlow()

    sealed interface Event : Serializable {
        data class Applied(val filter: RecruitingStudyFilter) : Event
    }
    private val _events = MutableSharedFlow<Event>(extraBufferCapacity = 1)
    val events: SharedFlow<Event> = _events

    data class RecruitingStudyFilter(
        val activities: List<ActivityType>,
        val fees: List<FeeRange>,
        val themes: List<StudyTheme>
    ) : Serializable {
        fun isEmpty() = activities.isEmpty() && fees.isEmpty() && themes.isEmpty()
    }

    fun toggleActivity(type: ActivityType) {
        _activities.update { current ->
            
            val newList = if (current.contains(type)) current.remove(type) else current.add(type)
            savedStateHandle[KEY_ACTIVITIES] = ArrayList(newList)
            newList
        }
        _notNull.value = calcNotNull()
    }

    fun toggleFee(fee: FeeRange) {
        _fees.update { current ->
            val newList = if (current.contains(fee)) current.remove(fee) else current.add(fee)
            savedStateHandle[KEY_FEES] = ArrayList(newList)
            newList
        }
        _notNull.value = calcNotNull()
    }

    fun toggleTheme(theme: StudyTheme) {
        _themes.update { current ->
            val newList = if (current.contains(theme)) current.remove(theme) else current.add(theme)
            savedStateHandle[KEY_THEMES] = ArrayList(newList)
            newList
        }
        _notNull.value = calcNotNull()
    }

    fun reset() {
        _activities.value = persistentListOf()
        _fees.value = persistentListOf()
        _themes.value = persistentListOf()

        savedStateHandle[KEY_ACTIVITIES] = null
        savedStateHandle[KEY_FEES] = null
        savedStateHandle[KEY_THEMES] = null

        _notNull.value = false
    }

    fun apply() {
        _events.tryEmit(
            Event.Applied(
                RecruitingStudyFilter(
                    activities = _activities.value,
                    fees = _fees.value,
                    themes = _themes.value
                )
            )
        )
    }
}