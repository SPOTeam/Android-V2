package com.umcspot.spot.weather.dto.response

import android.annotation.SuppressLint
import com.umcspot.spot.model.WeatherType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.LocalTime

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class WeatherResponseDto(
    @SerialName("response")
    val response: WeatherResponse
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class WeatherResponse(
    @SerialName("header")
    val header: WeatherHeader,

    @SerialName("body")
    val body: WeatherBody
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class WeatherHeader(
    @SerialName("resultCode")
    val resultCode: String,

    @SerialName("resultMsg")
    val resultMsg: String
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class WeatherBody(
    @SerialName("dataType")
    val dataType: String,

    @SerialName("items")
    val items: WeatherItems,

    @SerialName("pageNo")
    val pageNo: Int,

    @SerialName("numOfRows")
    val numOfRows: Int,

    @SerialName("totalCount")
    val totalCount: Int
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class WeatherItems(
    @SerialName("item")
    val item: List<WeatherItem>
)

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class WeatherItem(
    @SerialName("baseDate")
    val baseDate: String,

    @SerialName("baseTime")
    val baseTime: String,

    @SerialName("category")
    val category: String,

    @SerialName("nx")
    val nx: Int,

    @SerialName("ny")
    val ny: Int,

    @SerialName("obsrValue")
    val obsrValue: String
)