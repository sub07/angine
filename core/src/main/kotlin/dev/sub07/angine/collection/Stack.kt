package dev.sub07.angine.collection

typealias MutableStack<T> = MutableList<T>

fun <T> mutableStackOf(vararg e: T): MutableStack<T> = mutableListOf(*e)

fun <T> MutableStack<T>.push(item: T) = add(item)
fun <T> MutableStack<T>.pop(): T = removeAt(size - 1)
fun <T> MutableStack<T>.peek(): T = this[size - 1]