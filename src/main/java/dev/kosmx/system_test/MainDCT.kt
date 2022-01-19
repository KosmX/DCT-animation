package dev.kosmx.system_test

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.io.path.Path
import kotlin.io.path.createDirectory
import kotlin.math.*

const val quantize = true
const val divider = 16

fun main() {
    val file: File

    val image = ImageIO.read(File("file.png"))

    val matrix = Matrix<Double>(image.width, image.height)
    for (x in 0 until image.width) {
        for (y in 0 until image.height) {
            matrix[x, y] = image.getRGB(x, y).let {
                val b = it and 0xff
                val g = (it shr 8) and 0xff
                val r = (it shr 16) and 0xff

                0.299 * r + 0.587 * g + 0.114 * b
            }
        }
    }



    val res = MDCT2(matrix)

    if (quantize) {
        for (x in 0 until matrix.width) {
            for (y in 0 until matrix.height) {
                val i = (res[x, y] / divider).roundToInt()
                res[x, y] = i.toDouble() * divider
            }
        }
    }


    println(res)

    val workDir = Path("work")
    if (!workDir.toFile().isDirectory) {
        workDir.createDirectory()
    }




    val source = Matrix(res)

    for (x in 0 until source.width) {
        for (y in 0 until source.height) {
            source[x, y] = 0.0
        }
    }

    var counter = 0
    for (i in zigzag(res.width, res.height)) {
        if (quantize && res[i] == 0.0) continue
        val tmp = Matrix(res)
        val tmp2 = Matrix(res)
        for (x in 0 until res.width) {
            for (y in 0 until res.height) {
                var sum = 0.0
                val n1 = i.first
                val n2 = i.second
                sum += res[n1, n2] *
                        (if (n2 == 0) 0.5 else cos(PI / res.height * (y + 0.5) * n2)) *
                        (if (n1 == 0) 0.5 else cos(PI / res.width * (x + .5) * n1))
                sum *= 4.0 /(res.width * res.height)
                source[x, y] = source[x, y] + sum
                tmp[x, y] = sum + 127
            }
        }

        for (x in 0 until res.width) {
            for (y in 0 until res.height) {
                var sum = 0.0
                val n1 = i.first
                val n2 = i.second
                sum += 128 *
                        (if (n2 == 0) 0.5 else cos(PI / res.height * (y + 0.5) * n2)) *
                        (if (n1 == 0) 0.5 else cos(PI / res.width * (x + .5) * n1))
                tmp2[x, y] = sum + 127
            }
        }

        val image = BufferedImage(res.width * 3, res.height, BufferedImage.TYPE_3BYTE_BGR)
        for (x in 0 until source.width) {
            for (y in 0 until source.height) {
                val b: Int = source[x, y].toInt().toByte().toInt()
                image.setRGB(x, y, b or (b shl 8) or (b shl 16))
            }
        }

        for (x in 0 until source.width) {
            for (y in 0 until source.height) {
                val b: Int = tmp[x, y].toInt().toByte().toInt()
                image.setRGB(x+ source.width, y, b or (b shl 8) or (b shl 16))
            }
        }

        for (x in 0 until source.width) {
            for (y in 0 until source.height) {
                val b: Int = tmp2[x, y].toInt().toByte().toInt()
                image.setRGB(x + source.width * 2, y, b or (b shl 8) or (b shl 16))
            }
        }
        val path = workDir.resolve("${counter++}+${i.first}_${i.second}.png").toFile()
        ImageIO.write(image, "png", path)
        //println(source)
    }

}

fun zigzag(w: Int, h: Int) = sequence {
    var coord = Pair(0, 0)
    while (coord.first != w - 1 || coord.second != h - 1){
        while (true) {
            yield(coord)
            coord = Pair(coord.first + 1, coord.second -1)
            if (coord.second < 0 || coord.first >= w) {
                coord = Pair(min(coord.first, w - 1), if (coord.first < w) max(coord.second, 0) else coord.second + 2)
                break
            }
        }
        while (true) {
            yield(coord)
            coord = Pair(coord.first - 1, coord.second + 1)
            if (coord.first < 0 || coord.second >= w) {
                coord = Pair(if (coord.second < h) max(0, coord.first) else coord.first + 2, min(coord.second, h - 1))
                break
            }
        }
    }
    yield(coord)
}

fun MDCT2(source: Matrix<Double>): Matrix<Double> {
    val result: Matrix<Double> = Matrix(source)
    val width = source.width
    val height = source.height

    for (k1 in 0 until width) {
        for (k2 in 0 until height) {
            var sum = 0.0
            for (n1 in 0 until width) {
                for (n2 in 0 until height) {
                    sum += source[n1, n2] * cos(PI / height * (n2 + 0.5) * k2) * cos(PI / width * (n1 + .5) * k1)
                }
            }
            result[k1, k2] = sum
        }
    }
    return result

}
