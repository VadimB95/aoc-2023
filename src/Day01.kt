fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)
    check(part2(readInput("Day01_part2_test")) == 281)

    val input = readInput("Day01")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Int {
    return input.sumOf { line ->
        buildString {
            append(line.first { it.isDigit() })
            append(line.last { it.isDigit() })
        }.toInt()
    }
}

private fun part2(input: List<String>): Int {
    return input.sumOf { line ->
        val filteredDigits = buildString {
            for (i in line.indices) {
                if (line[i].isDigit()) {
                    append(line[i])
                } else {
                    val substring = line.substring(i)
                    for (digit in Digits.entries) {
                        if (substring.startsWith(digit.name, true)) {
                            append(digit.stringDigit)
                            break
                        }
                    }
                }
            }
        }
        "${filteredDigits.first()}${filteredDigits.last()}".toInt()
    }
}

private enum class Digits(val stringDigit: String) {
    ONE("1"),
    TWO("2"),
    THREE("3"),
    FOUR("4"),
    FIVE("5"),
    SIX("6"),
    SEVEN("7"),
    EIGHT("8"),
    NINE("9")
}