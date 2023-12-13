import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInput("Day05")
    println(part1(input))
    println(part2Parallel(input))
}

private fun part1(input: List<String>): Long {
    val seeds = input[0].split(": ")[1].split(" ").map(String::toLong)
    val chunks = parseInput(input)
    val chunkRanges = chunks.map { chunk ->
        chunk.map { line -> Pair(line[0]..<(line[0] + line[2]), line[1]..<(line[1] + line[2])) }
    }
    return seeds.map { seed -> getSeedLocation(seed, chunkRanges) }.min()
}

private fun part2(input: List<String>): Long {
    val seedIterables = input[0].split(": ")[1].split(" ").map(String::toLong)
        .chunked(2)
        .map { it[0]..<(it[0] + it[1]) }
        .map(LongRange::asIterable)
    val chunks = parseInput(input)
    val chunkRanges = chunks.map { chunk ->
        chunk.map { line -> Pair(line[0]..<(line[0] + line[2]), line[1]..<(line[1] + line[2])) }
    }
    var minLocation = Long.MAX_VALUE
    for (seedIterable in seedIterables) {
        for (seed in seedIterable) {
            val location = getSeedLocation(seed, chunkRanges)
            if (location < minLocation) minLocation = location
        }
    }
    return minLocation
}

private fun part2Parallel(input: List<String>): Long {
    val seedIterables = input[0].split(": ")[1].split(" ").map(String::toLong)
        .chunked(2)
        .map { it[0]..<(it[0] + it[1]) }
        .map(LongRange::asIterable)
    val chunks = parseInput(input)
    val chunkRanges = chunks.map { chunk ->
        chunk.map { line -> Pair(line[0]..<(line[0] + line[2]), line[1]..<(line[1] + line[2])) }
    }
    return runBlocking {
        var minLocation = Long.MAX_VALUE
        val jobs = mutableListOf<Job>()
        val mutex = Mutex()

        for (seedIterable in seedIterables) {
            jobs.add(
                launch(Dispatchers.Default) {
                    var rangeMinLocation = Long.MAX_VALUE
                    for (seed in seedIterable) {
                        val location = getSeedLocation(seed, chunkRanges)
                        if (location < rangeMinLocation) rangeMinLocation = location
                    }
                    mutex.withLock {
                        if (rangeMinLocation < minLocation) minLocation = rangeMinLocation
                    }
                }
            )
        }
        jobs.joinAll()
        minLocation
    }
}


private fun parseInput(input: List<String>) = buildList {
    var chunkStartIndex = 0
    for (i in 2..input.lastIndex) {
        val line = input[i]
        when {
            line.isEmpty() -> add(input.subList(chunkStartIndex, i))
            line[0].isLetter() -> chunkStartIndex = i + 1
            i == input.lastIndex -> add(input.subList(chunkStartIndex, i + 1))
            else -> continue
        }
    }
}.map { chunk -> chunk.map { line -> line.split(" ").map(String::toLong) } }

private fun getSeedLocation(initialSeed: Long, chunkRanges: List<List<Pair<LongRange, LongRange>>>): Long {
    var seed = initialSeed
    for (chunk in chunkRanges) {
        for (range in chunk) {
            if (seed in range.second) {
                seed = range.first.first + (seed - range.second.first)
                break
            }
        }
    }
    return seed
}

