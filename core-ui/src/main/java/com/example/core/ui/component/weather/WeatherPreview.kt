package com.example.core.ui.component.weather

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.time.LocalTime

// 🌧️ 낮, HeavyRain
@Preview(showBackground = true)
@Composable
fun WeatherCardPreview_HeavyRainyDay() {
    WeatherCard(
        modifier = Modifier.padding(8.dp),
        temperature = 15, weatherType = WeatherType.HEAVYRAIN, currentTime = LocalTime.of(8, 30))
}

// 🌧️ 밤, HeavyRain
@Preview(showBackground = true)
@Composable
fun WeatherCardPreview_HeavyRainyNight() {
    WeatherCard(
        modifier = Modifier.padding(8.dp),
        temperature = 15, weatherType = WeatherType.HEAVYRAIN, currentTime = LocalTime.of(19, 30))
}

// ❄ 낮, Snow
@Preview(showBackground = true)
@Composable
fun WeatherCardPreview_SnowDay() {
    WeatherCard(
        modifier = Modifier.padding(8.dp),
        temperature = -3, weatherType = WeatherType.SNOW, currentTime = LocalTime.of(8, 30))
}

// ❄ 눈 오는 밤
@Preview(showBackground = true)
@Composable
fun WeatherCardPreview_SnowNight() {
    WeatherCard(
        modifier = Modifier.padding(8.dp),
        temperature = -3, weatherType = WeatherType.SNOW, currentTime = LocalTime.of(19, 30))
}

// 🌬 낮, 바람
@Preview(showBackground = true)
@Composable
fun WeatherCardPreview_WindDay() {
    WeatherCard(
        modifier = Modifier.padding(8.dp),
        temperature = 15, weatherType = WeatherType.WIND, currentTime = LocalTime.of(8, 30))
}

// 🌬 밤, 바람
@Preview(showBackground = true)
@Composable
fun WeatherCardPreview_WindNight() {
    WeatherCard(
        modifier = Modifier.padding(8.dp),
        temperature = 15, weatherType = WeatherType.WIND, currentTime = LocalTime.of(19, 30))
}

// 🥶 낮, 추움
@Preview(showBackground = true)
@Composable
fun WeatherCardPreview_ColdDay() {
    WeatherCard(
        modifier = Modifier.padding(8.dp),
        temperature = 5, weatherType = WeatherType.COLD, currentTime = LocalTime.of(8, 30))
}

// 🥶 밤, 추움
@Preview(showBackground = true)
@Composable
fun WeatherCardPreview_ColdNight() {
    WeatherCard(
        modifier = Modifier.padding(8.dp),
        temperature = 5, weatherType = WeatherType.COLD, currentTime = LocalTime.of(19, 30))
}

// 🔥 낮, 더움
@Preview(showBackground = true)
@Composable
fun WeatherCardPreview_HotDay() {
    WeatherCard(
        modifier = Modifier.padding(8.dp),
        temperature = 35, weatherType = WeatherType.HOT, currentTime = LocalTime.of(8, 30))
}

// 🔥 밤, 더움
@Preview(showBackground = true)
@Composable
fun WeatherCardPreview_HotNight() {
    WeatherCard(
        modifier = Modifier.padding(8.dp),
        temperature = 35, weatherType = WeatherType.HOT, currentTime = LocalTime.of(19, 30))
}

// 🌙 낮, rain
@Preview(showBackground = true)
@Composable
fun WeatherCardPreview_RainDay() {
    WeatherCard(
        modifier = Modifier.padding(8.dp),
        temperature = 15, weatherType = WeatherType.RAIN, currentTime = LocalTime.of(8, 30))
}

// 🌙 밤, rain
@Preview(showBackground = true)
@Composable
fun WeatherCardPreview_RainNight() {
    WeatherCard(
        modifier = Modifier.padding(8.dp),
        temperature = 15, weatherType = WeatherType.RAIN, currentTime = LocalTime.of(19, 30))
}

// ☀ 낮, 맑음
@Preview(showBackground = true)
@Composable
fun WeatherCardPreview_SunnyDay() {
    WeatherCard(
        modifier = Modifier.padding(8.dp),
        temperature = 20, weatherType = WeatherType.SUNNY, currentTime = LocalTime.of(8, 30))
}

// ☀ 밤, 맑음
@Preview(showBackground = true)
@Composable
fun WeatherCardPreview_SunnyNight() {
    WeatherCard(
        modifier = Modifier.padding(8.dp),
        temperature = 20, weatherType = WeatherType.SUNNY, currentTime = LocalTime.of(19, 30))
}