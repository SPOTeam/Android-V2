package com.example.core.ui.component.weather

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.core.ui.R
import com.example.core.ui.shapes.SpotShapes
import com.example.core.ui.theme.SpotTypography
import java.time.LocalTime

enum class WeatherType { HEAVYRAIN, RAIN, SNOW, WIND, COLD, HOT, SUNNY }

@Composable
fun WeatherCard(
    temperature: Int? = null,
    weatherType: WeatherType? = null,
    currentTime: LocalTime? = null,
    modifier: Modifier = Modifier,
) {
    // 1) 배경 이미지 결정
    val isDay = currentTime?.hour in 6..17
    val backgroundRes = when {
        !isDay -> R.drawable.night_background
        weatherType == WeatherType.RAIN -> R.drawable.rainy_background
        weatherType == WeatherType.HEAVYRAIN -> R.drawable.rainy_background
        else -> R.drawable.default_background
    }

    // 2) 날씨별 아이콘, 그라데이션 오버레이, 메시지
    val (iconRes, message) = when (weatherType) {
        WeatherType.HEAVYRAIN   -> R.drawable.heavy_rain  to "실내에서 집중! 목표는 선명히!"
        WeatherType.RAIN        -> R.drawable.light_rain  to "우산 챙기고 오늘도 파이팅!"
        WeatherType.SNOW        -> R.drawable.heavy_snow  to "눈길 조심! 한 걸음씩 나아가요!"
        WeatherType.WIND        -> R.drawable.gale        to "바람 조심! 흔들려도 전진!"
        WeatherType.COLD        -> R.drawable.cold        to "따뜻하게! 오늘도 열정 가득!"
        WeatherType.HOT         -> R.drawable.hot         to "수분 보충! 더위도 이겨내요!"
        WeatherType.SUNNY -> R.drawable.sunny       to "좋은 날! 목표 향해 달려요!"
        null -> R.drawable.spot_logo to "날씨를 불러오는 중 입니다."
    }



    Card(
        shape = SpotShapes.Hard,                           // ✅ 프로젝트 정의 모서리
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        modifier = modifier
            .width(190.dp)
            .height(80.dp)
    ) {
        Box(Modifier.fillMaxSize()) {
            // 배경 이미지
            Image(
                painter = painterResource(backgroundRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(iconRes),
                            contentDescription = null,
                            modifier = Modifier.size(40.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "${"%.1f".format(temperature?.toFloat())} °C",
                            style = SpotTypography.header01,
                            fontSize = 25.sp,
                            color = Color.White
                        )
                    }
                    Text(
                        text = message,
                        style = SpotTypography.bodySmall500,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}
