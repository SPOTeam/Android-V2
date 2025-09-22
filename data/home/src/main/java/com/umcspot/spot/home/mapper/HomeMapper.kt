package com.umcspot.spot.home.mapper


import com.umcspot.spot.home.dto.request.HomeRequestDto
import com.umcspot.spot.home.dto.response.HomeResponseDto
import com.umcspot.spot.home.model.Dummy
import com.umcspot.spot.home.model.DummyResult

fun Dummy.toData(): HomeRequestDto =
    HomeRequestDto(
        id = this.id,
        email = this.email
    )

fun HomeResponseDto.toDomain(): DummyResult =
    DummyResult(
        info = info
    )