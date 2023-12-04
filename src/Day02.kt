fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Int {
    return parseGames(input).filter { game ->
        game.info.all { it.red <= 12 && it.green <= 13 && it.blue <= 14 }
    }.sumOf { game -> game.id }
}

private fun part2(input: List<String>): Int {
    return parseGames(input).sumOf { game ->
        val maxRed = game.info.maxOf { it.red }
        val maxGreen = game.info.maxOf { it.green }
        val maxBlue = game.info.maxOf { it.blue }
        maxRed * maxGreen * maxBlue
    }
}

private fun parseGames(input: List<String>) = input.map { line ->
    val chunks = line.split(": ", "; ")
    val id = chunks[0].filter { it.isDigit() }.toInt()
    val info = chunks.subList(1, chunks.size).map { gameInfo ->
        var red = 0
        var green = 0
        var blue = 0
        gameInfo.split(", ").forEach { cubeInfo ->
            val countToColor = cubeInfo.split(" ")
            val count = countToColor[0].toInt()
            when (countToColor[1]) {
                "red" -> red = count
                "green" -> green = count
                "blue" -> blue = count
            }
        }
        GameInfo(red, green, blue)
    }
    Game(id, info)
}

private data class Game(
    val id: Int,
    val info: List<GameInfo>
)

private data class GameInfo(
    val red: Int = 0,
    val green: Int = 0,
    val blue: Int = 0,
)