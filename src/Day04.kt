fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Int {
    var sum = 0
    input.forEach { line ->
        val parts = line.split(":", " |")
        val winning = parts[1].parseInput().toSet()
        val actual = parts[2].parseInput()

        var score = 0
        actual.forEach {
            if (it in winning) {
                score = if (score == 0) 1 else score * 2
            }
        }
        sum += score
    }
    return sum
}

private fun String.parseInput(): List<Int> =
    this.chunked(3)
        .map { chunk -> chunk.filter { it.isDigit() } }
        .map { it.toInt() }

private fun part2(input: List<String>): Int {
    val pile = MutableList(input.size) { 1 }
    input.forEachIndexed { index, line ->
        val parts = line.split(":", " |")
        val winning = parts[1].parseInput().toSet()
        val actual = parts[2].parseInput()
        val winCount = actual.count { it in winning }

        repeat(pile[index]) {
            if (winCount != 0) {
                for (i in 1..winCount) {
                    pile[index + i]++
                }
            }
        }
    }
    return pile.sum()
}
