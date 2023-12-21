package dev.sub07.angine.test

import dev.sub07.angine.collection.BitSet
import dev.sub07.angine.collection.BooleanArrayBitSet
import dev.sub07.angine.collection.MutableBitSet
import dev.sub07.angine.ecs.Entity
import dev.sub07.angine.ecs.EntityFilter
import dev.sub07.angine.ecs.entity.SimpleEntity
import dev.sub07.angine.ecs.entity.SimpleEntityFilter
import org.koin.core.component.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module
import java.util.concurrent.atomic.AtomicBoolean

abstract class TestCase : KoinComponent {
    init {
        if (!koinInit.getAndSet(true)) {
            startKoin {
                printLogger(level = Level.ERROR)
                allowOverride(false)

                val m = module {
                    factory<BitSet> { BooleanArrayBitSet() }
                    factory<MutableBitSet> { BooleanArrayBitSet() }
                    factory<Entity> { SimpleEntity() }
                    factory<EntityFilter> { param -> SimpleEntityFilter(param.get()) }
                }

                modules(m)
            }
        }
    }

    companion object {
        private var koinInit = AtomicBoolean(false)
    }
}