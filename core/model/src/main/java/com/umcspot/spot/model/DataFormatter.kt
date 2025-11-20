package com.umcspot.spot.model

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private val DATE_FMT_DEFAULT = DateTimeFormatter.ofPattern("yy.MM.dd", Locale.getDefault())
private val DATE_FMT_MASKED  = DateTimeFormatter.ofPattern("yy.'00'.'00'", Locale.getDefault())

/**
 * yyyy-MM-dd → "yy.MM.dd"
 * maskZeros=true → "yy.00.00" (월/일을 00으로 고정)
 */
fun LocalDate.toSpotForm(maskZeros: Boolean = false): String {
    return if (maskZeros) this.format(DATE_FMT_MASKED) else this.format(DATE_FMT_DEFAULT)
}

/**
 * HH:mm (초는 옵션)
 * maskZeros=true → "00:00"으로 고정
 */
fun LocalTime.toSpotForm(maskZeros: Boolean = false, showSeconds: Boolean = false): String {
    if (maskZeros) return if (showSeconds) "00:00:00" else "00:00"
    val pattern = if (showSeconds) "HH:mm:ss" else "HH:mm"
    return this.format(DateTimeFormatter.ofPattern(pattern, Locale.getDefault()))
}