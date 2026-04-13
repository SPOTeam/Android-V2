package com.umcspot.spot.study.detail.mapper

import com.umcspot.spot.model.StudyTheme
import kotlinx.collections.immutable.ImmutableList
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

fun LocalDateTime.toUiTime(): String {
    val formatter = DateTimeFormatter.ofPattern("hh:mma", Locale.ENGLISH)
    return this.format(formatter).lowercase()
}

fun ImmutableList<StudyTheme>.toCategoryString(): String {
    return this.joinToString(" / ") { it.title }
}

val Int.formatCount: String
    get() = if (this >= 1000) "999+" else this.toString()