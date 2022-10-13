package dev.mpardo.angine.test

import dev.mpardo.angine.ecs.*
import dev.mpardo.angine.structure.BitSet
import dev.mpardo.angine.structure.BooleanArrayBitSet
import dev.mpardo.angine.structure.MutableBitSet
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
                }
                
                modules(m, escModule)
            }
        }
    }
    
    companion object {
        private var koinInit = AtomicBoolean(false)
    }
}