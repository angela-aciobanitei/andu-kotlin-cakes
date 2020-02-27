package com.ang.acb.baking.util

import com.ang.acb.baking.utils.AppExecutors
import java.util.concurrent.Executor

/**
 * Helper class for using [AppExecutors] when testing.
 */
class InstantAppExecutors : AppExecutors(instant, instant, instant) {
    companion object {
        private val instant = Executor { it.run() }
    }
}