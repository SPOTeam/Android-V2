package com.umcspot.spot.model

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale


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

fun cap(n: Int): String = if (n >= 1000) "999+" else n.toString()
