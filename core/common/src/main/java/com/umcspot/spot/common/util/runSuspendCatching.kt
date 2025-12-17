package com.umcspot.spot.common.util

import java.util.concurrent.CancellationException

inline fun <T> runSuspendCatching(block: () -> T): Result<T> {
    return runCatching {
        block()
    }.onFailure { e ->
        if (e is CancellationException) {
            throw e
        }
    }
}