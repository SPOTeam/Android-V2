package com.umcspot.spot.common.location

import android.content.Context
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.charset.Charset

data class LocationRow(
    val code: String,
    val name: String,
)

object LocationStore {
    @Volatile private var cache: List<LocationRow>? = null

    suspend fun load(context: Context): List<LocationRow> = withContext(Dispatchers.IO) {
        cache?.let {
            Log.d("LocationStore", "✅ Returning cached data: ${it.size} rows")
            return@withContext it
        }

        Log.d("LocationStore", "📂 Loading Location_info.txt from assets...")

        val lines = try {
            context.assets.open("Location_info.txt")
                .bufferedReader(Charset.forName("EUC-KR"))
                .use {
                    it.readLines()
                }
        } catch (e: Exception) {
            Log.e("LocationStore", "❌ Failed to load asset: ${e.message}", e)
            emptyList()
        }

        Log.d("LocationStore", "📄 Read ${lines.size} lines")

        val parsed = lines.mapNotNull { line ->
            val parts = line.split('\t')
            if (parts.size < 3) return@mapNotNull null
            val code = parts[0].trim()
            val name = parts[1].trim()
            val status = parts[2].trim()
            if (status != "존재") null else LocationRow(code, name)
        }

        Log.d("LocationStore", "✅ Parsed ${parsed.size} valid rows")

        cache = parsed
        parsed
    }
}

fun searchLocations(query: String, list: List<LocationRow>, limit: Int = 20): List<LocationRow> {
    if (query.isBlank()) return emptyList()

    val normalized = query.trim().replace(" ", "").lowercase()
    val results = list.filter {
        it.name.replace(" ", "").lowercase().contains(normalized)
    }.take(limit)

    return results
}
