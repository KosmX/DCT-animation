package dev.kosmx.system_test

import kotlin.math.PI
import kotlin.math.cos




fun MDCT4(source: Matrix<Double>): Matrix<Double> {
    val result: Matrix<Double> = Matrix(source)
    val N = source.width
    val M = source.height

    for (k in 0 until N) {
        for (l in 0 until M) {
            result[k, l] = MDCT4comp(source, k, l, M, N)
        }
    }
    return result
}

fun MDCT4comp(source: Matrix<Double>, k: Int, l: Int, M: Int, N: Int) : Double {

    var sum = 0.0
    for (n in 0 until N) {
        for (m in 0 until M) {
            sum += source[n, m] * cos(((2 * m + 1) * (2 * k + n) * PI)/(4 * N)) * cos(((2 * n + 1) * (2 * l + n) * PI)/(4 * M))
        }
    }
    return sum
}