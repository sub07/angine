package dev.mpardo.angine.graphics

import dev.mpardo.angine.utils.b
import dev.mpardo.angine.utils.i
import kotlin.random.Random

@Suppress("unused")
class Color(r: Float = 0f, g: Float = 0f, b: Float = 0f, a: Float = 1f) {
    val r: Float = r.coerceIn(0f, 1f)
    val g: Float = g.coerceIn(0f, 1f)
    val b: Float = b.coerceIn(0f, 1f)
    val a: Float = a.coerceIn(0f, 1f)
    
    val rgba: Int = (a * 255).i shl 24 or (r * 255).i shl 16 or (g * 255).i shl 8 or (b * 255).i
    
    val rByte: Byte = (r * 255).b
    val gByte: Byte = (g * 255).b
    val bByte: Byte = (b * 255).b
    val aByte: Byte = (a * 255).b
    
    operator fun get(index: Int) = when (index) {
        0 -> r
        1 -> g
        2 -> b
        3 -> a
        else -> throw IndexOutOfBoundsException()
    }
    
    fun getByte(index: Int) = when (index) {
        0 -> rByte
        1 -> gByte
        2 -> bByte
        3 -> aByte
        else -> throw IndexOutOfBoundsException()
    }
    
    companion object {
        val Transparent = from(0, 0, 0, 0)
        val DarkGrey = from(50, 50, 50, 255)
        val Aliceblue = from(240, 248, 255, 255)
        val Antiquewhite = from(250, 235, 215, 255)
        val Aqua = from(0, 255, 255, 255)
        val Aquamarine = from(127, 255, 212, 255)
        val Azure = from(240, 255, 255, 255)
        val Beige = from(245, 245, 220, 255)
        val Bisque = from(255, 228, 196, 255)
        val Black = from(0, 0, 0, 255)
        val Blanchedalmond = from(255, 235, 205, 255)
        val Blue = from(0, 0, 255, 255)
        val Blueviolet = from(138, 43, 226, 255)
        val Brown = from(165, 42, 42, 255)
        val Burlywood = from(222, 184, 135, 255)
        val Cadetblue = from(95, 158, 160, 255)
        val Chartreuse = from(127, 255, 0, 255)
        val Chocolate = from(210, 105, 30, 255)
        val Coral = from(255, 127, 80, 255)
        val Cornflowerblue = from(100, 149, 237, 255)
        val Cornsilk = from(255, 248, 220, 255)
        val Crimson = from(220, 20, 60, 255)
        val Cyan = from(0, 255, 255, 255)
        val Darkblue = from(0, 0, 139, 255)
        val Darkcyan = from(0, 139, 139, 255)
        val Darkgoldenrod = from(184, 134, 11, 255)
        val Darkgray = from(169, 169, 169, 255)
        val Darkgreen = from(0, 100, 0, 255)
        val Darkgrey = from(169, 169, 169, 255)
        val Darkkhaki = from(189, 183, 107, 255)
        val Darkmagenta = from(139, 0, 139, 255)
        val Darkolivegreen = from(85, 107, 47, 255)
        val Darkorange = from(255, 140, 0, 255)
        val Darkorchid = from(153, 50, 204, 255)
        val Darkred = from(139, 0, 0, 255)
        val Darksalmon = from(233, 150, 122, 255)
        val Darkseagreen = from(143, 188, 143, 255)
        val Darkslateblue = from(72, 61, 139, 255)
        val Darkslategray = from(47, 79, 79, 255)
        val Darkslategrey = from(47, 79, 79, 255)
        val Darkturquoise = from(0, 206, 209, 255)
        val Darkviolet = from(148, 0, 211, 255)
        val Deeppink = from(255, 20, 147, 255)
        val Deepskyblue = from(0, 191, 255, 255)
        val Dimgray = from(105, 105, 105, 255)
        val Dimgrey = from(105, 105, 105, 255)
        val Dodgerblue = from(30, 144, 255, 255)
        val Firebrick = from(178, 34, 34, 255)
        val Floralwhite = from(255, 250, 240, 255)
        val Forestgreen = from(34, 139, 34, 255)
        val Fuchsia = from(255, 0, 255, 255)
        val Gainsboro = from(220, 220, 220, 255)
        val Ghostwhite = from(248, 248, 255, 255)
        val Gold = from(255, 215, 0, 255)
        val Goldenrod = from(218, 165, 32, 255)
        val Gray = from(128, 128, 128, 255)
        val Green = from(0, 128, 0, 255)
        val Greenyellow = from(173, 255, 47, 255)
        val Grey = from(128, 128, 128, 255)
        val Honeydew = from(240, 255, 240, 255)
        val Hotpink = from(255, 105, 180, 255)
        val Indianred = from(205, 92, 92, 255)
        val Indigo = from(75, 0, 130, 255)
        val Ivory = from(255, 255, 240, 255)
        val Khaki = from(240, 230, 140, 255)
        val Lavender = from(230, 230, 250, 255)
        val Lavenderblush = from(255, 240, 245, 255)
        val Lawngreen = from(124, 252, 0, 255)
        val Lemonchiffon = from(255, 250, 205, 255)
        val Lightblue = from(173, 216, 230, 255)
        val Lightcoral = from(240, 128, 128, 255)
        val Lightcyan = from(224, 255, 255, 255)
        val Lightgoldenrodyellow = from(250, 250, 210, 255)
        val Lightgray = from(211, 211, 211, 255)
        val Lightgreen = from(144, 238, 144, 255)
        val Lightgrey = from(211, 211, 211, 255)
        val Lightpink = from(255, 182, 193, 255)
        val Lightsalmon = from(255, 160, 122, 255)
        val Lightseagreen = from(32, 178, 170, 255)
        val Lightskyblue = from(135, 206, 250, 255)
        val Lightslategray = from(119, 136, 153, 255)
        val Lightslategrey = from(119, 136, 153, 255)
        val Lightsteelblue = from(176, 196, 222, 255)
        val Lightyellow = from(255, 255, 224, 255)
        val Lime = from(0, 255, 0, 255)
        val Limegreen = from(50, 205, 50, 255)
        val Linen = from(250, 240, 230, 255)
        val Magenta = from(255, 0, 255, 255)
        val Maroon = from(128, 0, 0, 255)
        val Mediumaquamarine = from(102, 205, 170, 255)
        val Mediumblue = from(0, 0, 205, 255)
        val Mediumorchid = from(186, 85, 211, 255)
        val Mediumpurple = from(147, 112, 219, 255)
        val Mediumseagreen = from(60, 179, 113, 255)
        val Mediumslateblue = from(123, 104, 238, 255)
        val Mediumspringgreen = from(0, 250, 154, 255)
        val Mediumturquoise = from(72, 209, 204, 255)
        val Mediumvioletred = from(199, 21, 133, 255)
        val Midnightblue = from(25, 25, 112, 255)
        val Mintcream = from(245, 255, 250, 255)
        val Mistyrose = from(255, 228, 225, 255)
        val Moccasin = from(255, 228, 181, 255)
        val Navajowhite = from(255, 222, 173, 255)
        val Navy = from(0, 0, 128, 255)
        val Oldlace = from(253, 245, 230, 255)
        val Olive = from(128, 128, 0, 255)
        val Olivedrab = from(107, 142, 35, 255)
        val Orange = from(255, 165, 0, 255)
        val Orangered = from(255, 69, 0, 255)
        val Orchid = from(218, 112, 214, 255)
        val Palegoldenrod = from(238, 232, 170, 255)
        val Palegreen = from(152, 251, 152, 255)
        val Paleturquoise = from(175, 238, 238, 255)
        val Palevioletred = from(219, 112, 147, 255)
        val Papayawhip = from(255, 239, 213, 255)
        val Peachpuff = from(255, 218, 185, 255)
        val Peru = from(205, 133, 63, 255)
        val Pink = from(255, 192, 203, 255)
        val Plum = from(221, 160, 221, 255)
        val Powderblue = from(176, 224, 230, 255)
        val Purple = from(128, 0, 128, 255)
        val Red = from(255, 0, 0, 255)
        val Rosybrown = from(188, 143, 143, 255)
        val Royalblue = from(65, 105, 225, 255)
        val Saddlebrown = from(139, 69, 19, 255)
        val Salmon = from(250, 128, 114, 255)
        val Sandybrown = from(244, 164, 96, 255)
        val Seagreen = from(46, 139, 87, 255)
        val Seashell = from(255, 245, 238, 255)
        val Sienna = from(160, 82, 45, 255)
        val Silver = from(192, 192, 192, 255)
        val Skyblue = from(135, 206, 235, 255)
        val Slateblue = from(106, 90, 205, 255)
        val Slategray = from(112, 128, 144, 255)
        val Slategrey = from(112, 128, 144, 255)
        val Snow = from(255, 250, 250, 255)
        val Springgreen = from(0, 255, 127, 255)
        val Steelblue = from(70, 130, 180, 255)
        val Tan = from(210, 180, 140, 255)
        val Teal = from(0, 128, 128, 255)
        val Thistle = from(216, 191, 216, 255)
        val Tomato = from(255, 99, 71, 255)
        val Turquoise = from(64, 224, 208, 255)
        val Violet = from(238, 130, 238, 255)
        val Wheat = from(245, 222, 179, 255)
        val White = from(255, 255, 255, 255)
        val Whitesmoke = from(245, 245, 245, 255)
        val Yellow = from(255, 255, 0, 255)
        val Yellowgreen = from(154, 205, 50, 255)
        
        fun from(hex: String): Color {
            @Suppress("NAME_SHADOWING") val hex = hex.trim().replace("#", "")
            val r = Integer.parseInt(hex.substring(0, 2), 16)
            val g = Integer.parseInt(hex.substring(2, 4), 16)
            val b = Integer.parseInt(hex.substring(4, 6), 16)
            return if (hex.length == 8) {
                val a = Integer.parseInt(hex.substring(6, 8), 16)
                from(r, g, b, a)
            } else {
                from(r, g, b, 255)
            }
        }
        
        fun from(r: Int, g: Int = 0, b: Int = 0, a: Int = 255) = Color(r / 255f, g / 255f, b / 255f, a / 255f)
        
        fun random(randomAlpha: Boolean = false, random: Random = Random) = Color(
            random.nextFloat(),
            random.nextFloat(),
            random.nextFloat(),
            if (randomAlpha) Random.nextFloat() else 1f
        )
    }
}