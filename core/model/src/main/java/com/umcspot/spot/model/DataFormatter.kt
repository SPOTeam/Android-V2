package com.umcspot.spot.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private val DATE_FMT_DEFAULT = DateTimeFormatter.ofPattern("yy.MM.dd", Locale.getDefault())
private val DATE_FMT_MASKED  = DateTimeFormatter.ofPattern("yy.'00'.'00'", Locale.getDefault())


fun String.formatCreatedAt(): String {
    return try {
        val inputFormatter = DateTimeFormatter.ofPattern(
            "yyyy-MM-dd'T'HH:mm:ss.SSSSSS",
            Locale.getDefault()
        )
        val outputFormatter = DateTimeFormatter.ofPattern(
            "yy.MM.dd  HH:mm",
            Locale.getDefault()
        )

        val parsed = LocalDateTime.parse(this, inputFormatter)
        parsed.format(outputFormatter)
    } catch (e: Exception) {
        this
    }
}