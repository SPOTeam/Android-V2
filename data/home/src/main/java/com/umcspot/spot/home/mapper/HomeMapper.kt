package com.umcspot.spot.home.mapper

import com.umcspot.spot.home.dto.request.HomeRequestDto
import com.umcspot.spot.home.model.Home

// Domain -> DTO
fun Home.toData(): HomeRequestDto =
    HomeRequestDto(
        id = this.id,
        email = this.email
    )

//// DTO -> Domain
//fun HomeResponseDto.toDomain(
//    weather : WeatherResponseDto,
//    popularStudy : StudyResponseDto,
//    recommendedStudy : StudyResponseDto
//): HomeResult =
//    HomeResult(
//        info = this.info,
//        weatherInfo = weather.toDomain(),
//        popularStudies = popularStudy.toList(),
//        recommendedStudies = recommendedStudy.toList()
//    )