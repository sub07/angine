package dev.sub07.angine.ecs

import dev.sub07.angine.ecs.entity.simpleEntityModule
import org.koin.dsl.module

val escModule = module {
    includes(simpleEntityModule)
}