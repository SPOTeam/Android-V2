package com.umcspot.spot.home.repositoryimpl

import com.umcspot.spot.home.model.Home
import com.umcspot.spot.home.model.HomeResult
import com.umcspot.spot.home.repository.HomeRepository
import com.umcspot.spot.study.repository.StudyRepository
import com.umcspot.spot.weather.repository.WeatherRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val studyRepository: StudyRepository
) : HomeRepository {
    override suspend fun getDummies(request: Home): Result<HomeResult> = runCatching {
        coroutineScope {
            val weatherDef = async { weatherRepository.getDummies(request.weather) }  // Result<WeatherResult> 를 반환한다고 가정
            val popularDef  = async { studyRepository.getDummies() }   // Result<StudyResult>
            val recommendedDef  = async { studyRepository.getDummies() }   // Result<StudyResult>

            HomeResult(
                weatherInfo = weatherDef.await().getOrThrow(),
                popularStudies = listOf(popularDef.await().getOrThrow()),
                recommendedStudies = listOf(recommendedDef.await().getOrThrow())
            )
        }
    }
}