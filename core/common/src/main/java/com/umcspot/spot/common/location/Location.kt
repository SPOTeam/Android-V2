package com.umcspot.spot.common.location

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.charset.Charset

data class LocationRow(
    val code: String,
    val province: String,
    val district: String,
    val neighborhood: String,
) {
    /** 리스트/검색용 전체 이름 */
    val fullName: String
        get() = listOf(province, district, neighborhood)
            .filter { it.isNotBlank() }
            .joinToString(" ")
}

object LocationStore {
    @Volatile private var cache: List<LocationRow>? = null

    suspend fun load(context: Context): List<LocationRow> = withContext(Dispatchers.IO) {
        cache?.let {
            Log.d("LocationStore", "✅ Returning cached data: ${it.size} rows")
            return@withContext it
        }

        Log.d("LocationStore", "📂 Loading region_data.tsv from assets...")

        val lines = try {
            context.assets.open("region_data.tsv")
                .bufferedReader(Charset.forName("UTF-8")) // 파일 인코딩이 다르면 여기만 변경
                .use { it.readLines() }
        } catch (e: Exception) {
            Log.e("LocationStore", "❌ Failed to load asset: ${e.message}", e)
            emptyList()
        }

        Log.d("LocationStore", "📄 Read ${lines.size} lines")

        // 첫 줄이 헤더(code	province	district	neighborhood)라고 가정
        val parsed = lines
            .drop(1) // 헤더 제거
            .mapNotNull { line ->
                val parts = line.split('\t')
                if (parts.size < 4) return@mapNotNull null

                val code = parts[0].trim()
                val province = parts[1].trim()
                val district = parts[2].trim()
                val neighborhood = parts[3].trim()

                if (code.isBlank() || neighborhood.isBlank()) return@mapNotNull null

                LocationRow(
                    code = code,
                    province = province,
                    district = district,
                    neighborhood = neighborhood
                )
            }

        Log.d("LocationStore", "✅ Parsed ${parsed.size} rows")

        cache = parsed
        parsed
    }
}

fun searchLocations(query: String, list: List<LocationRow>, limit: Int = 20): List<LocationRow> {
    if (query.isBlank()) return emptyList()

    val normalized = query.trim().replace(" ", "").lowercase()

    return list
        .filter {
            it.fullName.replace(" ", "").lowercase().contains(normalized)
        }
        .take(limit)
}

