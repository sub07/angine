package dev.sub07.angine.playground

import dev.sub07.angine.Angine
import dev.sub07.angine.AngineConfiguration
import org.koin.dsl.module

fun main() {
    val userModule = module {
        single { AngineConfiguration() }
    }
    Angine.launch(userModule)
}