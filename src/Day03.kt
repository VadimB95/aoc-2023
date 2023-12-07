fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day03_test")
    check(part1(testInput) == 4361)
//    check(part2(testInput) == 0)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Int {
    var sum = 0
    for (i in input.indices) {
        var isNumberInterval = false
        var isNumberIntervalIncluded = false
        var number = ""
        var rawSum = 0
        for (j in input[i].indices) {
            fun handleNumberInterval() {
                if (isNumberInterval && isNumberIntervalIncluded) {
                    rawSum += number.toInt()
                }
                isNumberInterval = false
                isNumberIntervalIncluded = false
                number = ""
            }

            if (input[i][j].isDigit()) {
                isNumberInterval = true
                number += input[i][j]

                val adjacentChars = buildList {
                    for (offsetI in -1..1) for (offsetJ in -1..1) {
                        if (offsetI == 0 && offsetJ == 0) continue
                        add(input.getOrNull(i + offsetI)?.getOrNull(j + offsetJ))
                    }
                }.filterNotNull()
                for (char in adjacentChars) {
                    if (!char.isDigit() && char != '.') {
                        isNumberIntervalIncluded = true
                        break
                    }
                }
                if (j == input[i].lastIndex) {
                    handleNumberInterval()
                }
            } else {
                handleNumberInterval()
            }
        }
        sum += rawSum
    }
    return sum
}

private fun part2(input: List<String>): Int {
    return 0
}
