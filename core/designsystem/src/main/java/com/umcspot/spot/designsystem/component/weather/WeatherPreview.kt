package com.umcspot.spot.designsystem.component.weather

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.umcspot.spot.designsystem.theme.SpotTheme
import com.umcspot.spot.model.WeatherType
import java.time.LocalTime

// ❄ 낮, Snow
@Preview(showBackground = true)
@Composable
fun WeatherCardPreview_SnowDay() {
    SpotTheme{
        WeatherCard(
            modifier = Modifier.padding(8.dp),
            temperature = -3.0, weatherType = WeatherType.SNOW, currentTime = LocalTime.of(8, 30))
    }
}

// ❄ 눈 오는 밤
@Preview(showBackground = true)
@Composable
fun WeatherCardPreview_SnowNight() {
    SpotTheme{
        WeatherCard(
            modifier = Modifier.padding(8.dp),
            temperature = -3.0, weatherType = WeatherType.SNOW, currentTime = LocalTime.of(19, 30))
    }
}

// 🌬 낮, 바람
@Preview(showBackground = true)
@Composable
fun WeatherCardPreview_WindDay() {
    SpotTheme{
        WeatherCard(
            modifier = Modifier.padding(8.dp),
            temperature = 15.0, weatherType = WeatherType.WIND, currentTime = LocalTime.of(8, 30))
    }
}

// 🌬 밤, 바람
@Preview(showBackground = true)
@Composable
fun WeatherCardPreview_WindNight() {
    SpotTheme{
        WeatherCard(
            modifier = Modifier.padding(8.dp),
            temperature = 15.0, weatherType = WeatherType.WIND, currentTime = LocalTime.of(19, 30))
    }
}

// 🥶 낮, 추움
@Preview(showBackground = true)
@Composable
fun WeatherCardPreview_ColdDay() {
    SpotTheme{
        WeatherCard(
            modifier = Modifier.padding(8.dp),
            temperature = 5.0, weatherType = WeatherType.COLD, currentTime = LocalTime.of(8, 30))
    }
}

// 🥶 밤, 추움
@Preview(showBackground = true)
@Composable
fun WeatherCardPreview_ColdNight() {
    SpotTheme{
        WeatherCard(
            modifier = Modifier.padding(8.dp),
            temperature = 5.0, weatherType = WeatherType.COLD, currentTime = LocalTime.of(19, 30))
    }
}

// 🔥 낮, 더움
@Preview(showBackground = true)
@Composable
fun WeatherCardPreview_HotDay() {
    SpotTheme{
        WeatherCard(
            modifier = Modifier.padding(8.dp),
            temperature = 35.0, weatherType = WeatherType.HOT, currentTime = LocalTime.of(8, 30))
    }
}

// 🔥 밤, 더움
@Preview(showBackground = true)
@Composable
fun WeatherCardPreview_HotNight() {
    SpotTheme{
        WeatherCard(
            modifier = Modifier.padding(8.dp),
            temperature = 35.0, weatherType = WeatherType.HOT, currentTime = LocalTime.of(19, 30))
    }
}

// 🌙 낮, rain
@Preview(showBackground = true)
@Composable
fun WeatherCardPreview_RainDay() {
    SpotTheme{
        WeatherCard(
            modifier = Modifier.padding(8.dp),
            temperature = 15.0, weatherType = WeatherType.RAIN, currentTime = LocalTime.of(8, 30))
    }
}

// 🌙 밤, rain
@Preview(showBackground = true)
@Composable
fun WeatherCardPreview_RainNight() {
    SpotTheme{
        WeatherCard(
            modifier = Modifier.padding(8.dp),
            temperature = 15.0, weatherType = WeatherType.RAIN, currentTime = LocalTime.of(19, 30))
    }
}

// ☀ 낮, 맑음
@Preview(showBackground = true)
@Composable
fun WeatherCardPreview_SunnyDay() {
    SpotTheme{
        WeatherCard(
            modifier = Modifier.padding(8.dp),
            temperature = 20.0, weatherType = WeatherType.SUNNY, currentTime = LocalTime.of(8, 30))
    }
}

// ☀ 밤, 맑음
@Preview(showBackground = true)
@Composable
fun WeatherCardPreview_SunnyNight() {
    SpotTheme{
        WeatherCard(
            modifier = Modifier.padding(8.dp),
            temperature = 20.0, weatherType = WeatherType.SUNNY, currentTime = LocalTime.of(19, 30))
    }
}