package processor

import java.util.*

data class Matrix(val row: Int, val column: Int, val init: (Int, Int) -> Double) {
    private val matrix = List(row) { i -> List(column) { j -> init(i, j) } }

    operator fun plus(other: Matrix) = Matrix(row, column) { i, j -> matrix[i][j] + other.matrix[i][j] }

    operator fun times(num: Double) = Matrix(row, column) { i, j -> matrix[i][j] * num }

    operator fun times(other: Matrix): Matrix {
        fun calcElement(i: Int, j: Int): Double {
            var sum = 0.0
            for (k in 0 until column)
                sum += matrix[i][k] * other.matrix[k][j]
            return sum
        }
        return Matrix(row, other.column) { i, j -> calcElement(i, j) }
    }

    fun transpose(type: Int) = when (type) {
        1 -> Matrix(column, row) { i, j -> matrix[j][i] }
        2 -> Matrix(column, row) { i, j -> matrix[column - 1 - j][row - 1 - i] }
        3 -> Matrix(column, row) { i, j -> matrix[i][column - 1 - j] }
        else -> Matrix(column, row) { i, j -> matrix[row - 1 - i][j] }
    }

    fun minor(k: Int, l: Int) =
        Matrix(row - 1, column - 1) { i, j -> matrix[if (i < k) i else i + 1][if (j < l) j else j + 1] }

    fun cofactor(k: Int, l: Int) = minor(k, l).det() * if ((k + l) % 2 == 0) 1 else -1

    fun det(): Double =
        if (row == 2) matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0]
        else {
            var sum = 0.0
            repeat(column) { sum += matrix[0][it] * cofactor(0, it) }
            sum
        }

    fun inverse() =
        Matrix(row, column) { i, j -> minor(i, j).det() * if ((i + j) % 2 == 0) 1 else -1 }.transpose(1) * (1.0 / det())

    override fun toString() = matrix.joinToString("\n") { row -> row.joinToString(" ") }
}

fun main() {
    val scanner = Scanner(System.`in`)
    while (true) when (scanner.nextInt()) {
        1 -> {
            val m1 = Matrix(scanner.nextInt(), scanner.nextInt()) { _, _ -> scanner.nextDouble() }
            val m2 = Matrix(scanner.nextInt(), scanner.nextInt()) { _, _ -> scanner.nextDouble() }
            println(m1 + m2)
        }
        2 -> {
            val m = Matrix(scanner.nextInt(), scanner.nextInt()) { _, _ -> scanner.nextDouble() }
            val num = scanner.nextDouble()
            println(m * num)
        }
        3 -> {
            val m1 = Matrix(scanner.nextInt(), scanner.nextInt()) { _, _ -> scanner.nextDouble() }
            val m2 = Matrix(scanner.nextInt(), scanner.nextInt()) { _, _ -> scanner.nextDouble() }
            println(m1 * m2)
        }
        4 -> {
            val type = scanner.nextInt()
            val m = Matrix(scanner.nextInt(), scanner.nextInt()) { _, _ -> scanner.nextDouble() }
            println(m.transpose(type))
        }
        5 -> {
            val m = Matrix(scanner.nextInt(), scanner.nextInt()) { _, _ -> scanner.nextDouble() }
            println(m.det())
        }
        6 -> {
            val m = Matrix(scanner.nextInt(), scanner.nextInt()) { _, _ -> scanner.nextDouble() }
            println(m.inverse())
        }
        0 -> return
    }
}