package dev.mpardo.angine.utils

import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.TimeSource

private val start = TimeSource.Monotonic.markNow()
val now get() = start.elapsedNow()

val Duration.sec: Float get() = this.toDouble(DurationUnit.SECONDS).f
val Duration.ms: Float get() = this.toDouble(DurationUnit.MILLISECONDS).f
val Duration.ns: Long get() = inWholeNanoseconds