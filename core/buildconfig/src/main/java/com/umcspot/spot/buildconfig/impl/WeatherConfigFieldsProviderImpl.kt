package com.umcspot.spot.buildconfig.impl

import com.umcspot.spot.buildconfig.BuildConfig.WEATHER_BASE_URL
import com.umcspot.spot.buildconfig.BuildConfig.WEATHER_TOKEN
import com.umcspot.spot.common.WeatherConfigFieldProvider
import com.umcspot.spot.common.WeatherConfigFields
import javax.inject.Inject

class WeatherConfigFieldsProviderImpl @Inject constructor() : WeatherConfigFieldProvider {
    override fun getWeather(): WeatherConfigFields =
        WeatherConfigFields(
            weatherUrl = WEATHER_BASE_URL,
            token = WEATHER_TOKEN
        )
}