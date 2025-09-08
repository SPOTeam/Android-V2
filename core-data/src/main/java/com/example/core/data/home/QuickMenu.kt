package com.example.core.data.home

import androidx.annotation.DrawableRes
import com.example.core.data.global.QuickMenuType

data class QuickMenuItem(
    @DrawableRes val iconRes: Int,
    val label: String,
    val type: QuickMenuType
)

