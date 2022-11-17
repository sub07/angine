package dev.sub07.angine.utils

fun <T> threadLocal(initialValue: T): ThreadLocal<T> = ThreadLocal.withInitial { initialValue }