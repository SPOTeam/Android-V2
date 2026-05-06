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

fun String.toFormattedMemoirDate(): String {
    return try {
        val dt = this.split("T")
        val date = dt[0].replace("-", ".").substring(2)
        val time = dt[1].substring(0, 5)
        "$date $time"
    } catch (e: Exception) {
        this
    }
}

val Int.formatCount: String
    get() = if (this >= 1000) "999+" else this.toString()

