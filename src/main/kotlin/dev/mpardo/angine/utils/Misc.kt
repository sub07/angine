package dev.mpardo.angine.utils

fun <T> threadLocal(initialValue: T): ThreadLocal<T> = ThreadLocal.withInitial { initialValue }