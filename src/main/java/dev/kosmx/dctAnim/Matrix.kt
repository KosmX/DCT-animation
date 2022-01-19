package dev.kosmx.dctAnim

class Matrix<T>(val width: Int, val height: Int) {
    private val array: MutableList<MutableList<T?>>;

    init {
        array = ArrayList(width);
        for (i in 0 until width) {
            array.add(ArrayList(height))
            for (n in 0 until height) {
                array[i].add(null)
            }
        }
    }

    constructor(matrix: Matrix<T>) : this(matrix.width, matrix.height)

    operator fun get(a: Int, b: Int): T {
        return array[a][b]!!;
    }

    operator fun get(pair: Pair<Int, Int>): T{
        return get(pair.first, pair.second)
    }

    operator fun set(a: Int, b: Int, t: T): T {
        array[a][b] = t
        return array[a][b]!!
    }

    override fun toString(): String {
        var s = ""
        for (i in array) {
            s += "$i$\n"
        }
        return s
    }

}
