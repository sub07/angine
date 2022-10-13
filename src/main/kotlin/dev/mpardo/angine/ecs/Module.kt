package dev.mpardo.angine.ecs

import dev.mpardo.angine.ecs.entity.simpleEntityModule
import org.koin.dsl.module

val escModule = module {
    includes(simpleEntityModule)
}