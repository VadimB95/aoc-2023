fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288)
    check(part2(testInput) == 71503)

    val input = readInput("Day06")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Int {
    val timeChunks = input[0].split("\\s+".toRegex()).run { subList(1, size) }.map(String::toInt)
    val distanceChunks = input[1].split("\\s+".toRegex()).run { subList(1, size) }.map(String::toInt)
    val winCounts = mutableListOf<Int>()
    for (i in 0..timeChunks.lastIndex) {
        val raceTime = timeChunks[i]
        val currentRecord = distanceChunks[i]
        var winCount = 0
        for (holdTime in 0..raceTime) {
            val moveTime = raceTime - holdTime
            val distance = holdTime * moveTime
            if (distance > currentRecord) winCount++
        }
        winCounts.add(winCount)
    }
    return winCounts.reduce(Int::times)
}

private fun part2(input: List<String>): Int {
    val time = input[0].filter { it.isDigit() }.toLong()
    val record = input[1].filter { it.isDigit() }.toLong()
    var winCount = 0
    for (holdTime in 0..time) {
        val moveTime = time - holdTime
        val distance = holdTime * moveTime
        if (distance > record) winCount++
    }
    return winCount
}
