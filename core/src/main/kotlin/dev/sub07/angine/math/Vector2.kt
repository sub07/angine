package dev.sub07.angine.math

import dev.sub07.angine.memory.VecPool
import dev.sub07.angine.memory.borrow
import dev.sub07.angine.utils.f
import dev.sub07.angine.utils.i
import org.koin.core.component.KoinComponent
import org.koin.core.context.GlobalContext
import kotlin.math.cos
import kotlin.math.sin

interface Vec {
    val xc: Float
    val yc: Float

    fun norm(): Float
    fun norm2(): Float
    fun normal(): MutableVec
    fun dist(x: Number, y: Number): Float
    fun dist2(x: Number, y: Number): Float
    fun middle(x: Number, y: Number): MutableVec
    fun det(x: Number, y: Number): Float
    fun dot(x: Number, y: Number): Float
    fun angle(x: Number, y: Number): Float
    fun negated(): MutableVec
    fun copy(): MutableVec
    fun rotated(angle: Number): MutableVec
    fun normalized(): MutableVec
    fun inverted(): MutableVec
    fun added(x: Number, y: Number): MutableVec
    fun subtracted(x: Number, y: Number): MutableVec
    fun multiplied(x: Number, y: Number): MutableVec
    fun divided(x: Number, y: Number): MutableVec
    fun added(x: Number): MutableVec
    fun subtracted(x: Number): MutableVec
    fun multiplied(x: Number): MutableVec
    fun divided(x: Number): MutableVec
    infix fun eq(other: Vec): Boolean
    infix fun neq(other: Vec): Boolean
    operator fun plus(vec: Vec): MutableVec
    operator fun minus(vec: Vec): MutableVec
    operator fun times(vec: Vec): MutableVec
    operator fun div(vec: Vec): MutableVec
    operator fun rem(vec: Vec): MutableVec
    operator fun plus(value: Number): MutableVec
    operator fun minus(value: Number): MutableVec
    operator fun times(value: Number): MutableVec
    operator fun div(value: Number): MutableVec
    operator fun rem(value: Number): MutableVec
    operator fun component1(): Float
    operator fun component2(): Float
}

interface MutableVec : Vec, KoinComponent {
    var x: Float
    var y: Float
    fun set(x: Number = 0f, y: Number = x): MutableVec
    fun rotate(angle: Number): MutableVec
    fun normalize(): MutableVec
    fun negate(): MutableVec
    fun invert(): MutableVec
    fun add(x: Number, y: Number = x): MutableVec
    fun sub(x: Number, y: Number = x): MutableVec
    fun mul(x: Number, y: Number = x): MutableVec
    fun div(x: Number, y: Number = x): MutableVec
    operator fun plusAssign(vec: Vec)
    operator fun minusAssign(vec: Vec)
    operator fun timesAssign(vec: Vec)
    operator fun divAssign(vec: Vec)
    operator fun remAssign(vec: Vec)
    operator fun plusAssign(value: Number)
    operator fun minusAssign(value: Number)
    operator fun timesAssign(value: Number)
    operator fun divAssign(value: Number)
    operator fun remAssign(value: Number)
}

private class VecImpl(override var x: Float, override var y: Float) : Vec, MutableVec {
    override val xc: Float
        get() = x
    override val yc: Float
        get() = y

    override fun norm(): Float = Geometry.norm(x, y)

    override fun norm2(): Float = Geometry.norm2(x, y)

    override fun normal(): MutableVec = vec(y, -x)

    override fun dist(x: Number, y: Number): Float = Geometry.dist(this.x, this.y, x.f, y.f)

    override fun dist2(x: Number, y: Number): Float = Geometry.dist2(this.x, this.y, x.f, y.f)

    override fun middle(x: Number, y: Number): MutableVec = vec(x.f / 2f, y.f / 2f)

    override fun det(x: Number, y: Number): Float = Geometry.det(this.x, this.y, x, y)

    override fun dot(x: Number, y: Number): Float = Geometry.dot(this.x, this.y, x, y)

    override fun angle(x: Number, y: Number): Float = Geometry.angle(this.x, this.y, x, y)

    override fun negated(): MutableVec = vec(-x, -y)

    override fun copy(): MutableVec = vec(x, y)

    override fun rotated(angle: Number): MutableVec {
        val cos = cos(angle.f)
        val sin = sin(angle.f)
        return vec(x * cos - y * sin, x * sin + y * cos)
    }

    override fun normalized(): MutableVec = vec(x / norm(), y / norm())

    override fun inverted(): MutableVec = vec(1f / x, 1f / y)

    override fun added(x: Number, y: Number): MutableVec = vec(this.x + x.f, this.y + y.f)

    override fun subtracted(x: Number, y: Number): MutableVec = vec(this.x - x.f, this.y - y.f)

    override fun multiplied(x: Number, y: Number): MutableVec = vec(this.x * x.f, this.y * y.f)

    override fun divided(x: Number, y: Number): MutableVec = vec(this.x / x.f, this.y / y.f)

    override fun added(x: Number): MutableVec = vec(this.x + x.f, this.y + x.f)

    override fun subtracted(x: Number): MutableVec = vec(this.x - x.f, this.y - x.f)

    override fun multiplied(x: Number): MutableVec = vec(this.x * x.f, this.y * x.f)

    override fun divided(x: Number): MutableVec = vec(this.x / x.f, this.y / x.f)

    override fun plus(vec: Vec): MutableVec = vec(this.x + vec.xc, this.y + vec.yc)

    override fun plus(value: Number): MutableVec = vec(this.x + value.f, this.y + value.f)

    override fun minus(vec: Vec): MutableVec = vec(this.x - vec.xc, this.y - vec.yc)

    override fun minus(value: Number): MutableVec = vec(this.x - value.f, this.y - value.f)

    override fun times(vec: Vec): MutableVec = vec(this.x * vec.xc, this.y * vec.yc)

    override fun times(value: Number): MutableVec = vec(this.x * value.f, this.y * value.f)

    override fun div(vec: Vec): MutableVec = vec(this.x / vec.xc, this.y / vec.yc)

    override fun div(value: Number): MutableVec = vec(this.x / value.f, this.y / value.f)

    override fun rem(value: Number): MutableVec = vec(x % value.f, y % value.f)

    override fun rem(vec: Vec): MutableVec = vec(x % vec.xc, y % vec.yc)

    override fun set(x: Number, y: Number): MutableVec {
        this.x = x.f
        this.y = y.f
        return this
    }

    override fun rotate(angle: Number): MutableVec {
        val cos = cos(angle.f)
        val sin = sin(angle.f)
        val x = this.x * cos - this.y * sin
        val y = this.x * sin + this.y * cos
        this.x = x
        this.y = y
        return this
    }

    override fun normalize(): MutableVec {
        val norm = norm()
        x /= norm
        y /= norm
        return this
    }

    override fun negate(): MutableVec {
        x = -x
        y = -y
        return this
    }

    override fun invert(): MutableVec {
        x = 1f / x
        y = 1f / y
        return this
    }

    override fun add(x: Number, y: Number): VecImpl {
        this.x += x.f
        this.y += y.f
        return this
    }

    override fun sub(x: Number, y: Number): VecImpl {
        this.x -= x.f
        this.y -= y.f
        return this
    }

    override fun mul(x: Number, y: Number): VecImpl {
        this.x *= x.f
        this.y *= y.f
        return this
    }

    override fun div(x: Number, y: Number): VecImpl {
        this.x /= x.f
        this.y /= y.f
        return this
    }

    override fun plusAssign(vec: Vec) {
        this.x += vec.xc
        this.y += vec.yc
    }

    override fun plusAssign(value: Number) {
        this.x += value.f
        this.y += value.f
    }

    override fun minusAssign(vec: Vec) {
        this.x -= vec.xc
        this.y -= vec.yc
    }

    override fun minusAssign(value: Number) {
        this.x -= value.f
        this.y -= value.f
    }

    override fun timesAssign(vec: Vec) {
        this.x *= vec.xc
        this.y *= vec.yc
    }

    override fun timesAssign(value: Number) {
        this.x *= value.f
        this.y *= value.f
    }

    override fun divAssign(vec: Vec) {
        this.x /= vec.xc
        this.y /= vec.yc
    }

    override fun divAssign(value: Number) {
        this.x /= value.f
        this.y /= value.f
    }

    override fun remAssign(value: Number) {
        this.x %= value.f
        this.y %= value.f
    }

    override fun remAssign(vec: Vec) {
        this.x %= vec.xc
        this.y %= vec.yc
    }

    override fun component1(): Float = x

    override fun component2(): Float = y

    override infix fun eq(other: Vec): Boolean = (x - other.xc).isNearZero() && (y - other.yc).isNearZero()

    override infix fun neq(other: Vec): Boolean = !eq(other)

    override fun toString() = "($x, $y)"
}

fun vec(x: Number = 0f, y: Number = x): MutableVec = VecImpl(x.f, y.f)

val Vec.wc: Float get() = xc
val Vec.hc: Float get() = yc
val Vec.ic: Int get() = xc.i
val Vec.jc: Int get() = yc.i
val Vec.onlyX: MutableVec get() = vec(xc, 0f)
val Vec.onlyY: MutableVec get() = vec(0f, yc)
infix fun Vec.dot(vec: Vec): Float = dot(vec.xc, vec.yc)
infix fun Vec.det(vec: Vec): Float = det(vec.xc, vec.yc)
fun Vec.dist(vec: Vec): Float = dist(vec.xc, vec.yc)
fun Vec.dist2(vec: Vec): Float = dist2(vec.xc, vec.yc)

var MutableVec.w: Float
    get() = x
    set(value) {
        x = value
    }
var MutableVec.h: Float
    get() = y
    set(value) {
        y = value
    }
var MutableVec.i: Int
    get() = x.i
    set(value) {
        x = value.f
    }
var MutableVec.j: Int
    get() = y.i
    set(value) {
        y = value.f
    }
var MutableVec.xy: Float
    get() = x
    set(value) {
        x = value
        y = value
    }

fun MutableVec.add(vec: Vec): MutableVec {
    x += vec.xc
    y += vec.yc
    return this
}

fun MutableVec.sub(vec: Vec): MutableVec {
    x -= vec.xc
    y -= vec.yc
    return this
}

fun MutableVec.mul(vec: Vec): MutableVec {
    x *= vec.xc
    y *= vec.yc
    return this
}

fun MutableVec.set(vec: Vec): MutableVec {
    x = vec.xc
    y = vec.yc
    return this
}

operator fun Number.div(vec: Vec) = vec(this.f / vec.xc, this.f / vec.yc)
operator fun Number.times(vec: Vec) = vec(this.f * vec.xc, this.f * vec.yc)
operator fun Number.plus(vec: Vec) = vec(this.f + vec.xc, this.f + vec.yc)
operator fun Number.minus(vec: Vec) = vec(this.f - vec.xc, this.f - vec.yc)

fun MutableVec.reflect(normal: Vec): MutableVec = vecPool.borrow { sub(it.set(normal).mul(2f).mul(this.dot(normal))) }

val vecPool get() = GlobalContext.get().get<VecPool>()