fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)

    val input = readInput("Day07")
    println(part1(input))
    println(part2(input))
}

private fun part1(input: List<String>): Int {
    val sortedBids = input.map { line ->
        val lineChunks = line.split(" ")
        val cards = lineChunks[0].map { Card.valueOf(it.toString()) }
        Hand(cards) to lineChunks[1].toInt()
    }.sortedBy { it.first }.map { it.second }
    val result = sortedBids.withIndex().sumOf { (i, b) -> b * (i + 1) }
    return result
}

private fun part2(input: List<String>): Int {
    val sortedBids = input.map { line ->
        val lineChunks = line.split(" ")
        val cards = lineChunks[0].map { Card2.valueOf(it.toString()) }
        Hand2(cards) to lineChunks[1].toInt()
    }.sortedBy { it.first }.map { it.second }
    val result = sortedBids.withIndex().sumOf { (i, b) -> b * (i + 1) }
    return result
}

private enum class Card {
    `2`, `3`, `4`, `5`, `6`, `7`, `8`, `9`, T, J, Q, K, A
}

private enum class Card2 {
    J, `2`, `3`, `4`, `5`, `6`, `7`, `8`, `9`, T, Q, K, A
}

private enum class HandType {
    HIGH_CARD,
    ONE_PAIR,
    TWO_PAIR,
    THREE_OF_A_KIND,
    FULL_HOUSE,
    FOUR_OF_A_KIND,
    FIVE_OF_A_KIND,
}

private data class Hand(
    val cards: List<Card>
) : Comparable<Hand> {

    fun getHandType(): HandType {
        val cardSet = cards.toSet()
        return when (cardSet.size) {
            1 -> HandType.FIVE_OF_A_KIND
            2 -> {
                val isFourOfAKind = cards.groupBy { it }.values.any { it.size == 4 }
                if (isFourOfAKind) {
                    HandType.FOUR_OF_A_KIND
                } else {
                    HandType.FULL_HOUSE
                }
            }

            3 -> {
                val isThreeOfAKind = cards.groupBy { it }.values.any { it.size == 3 }
                if (isThreeOfAKind) {
                    HandType.THREE_OF_A_KIND
                } else {
                    HandType.TWO_PAIR
                }
            }

            4 -> HandType.ONE_PAIR
            5 -> HandType.HIGH_CARD
            else -> throw IllegalArgumentException()
        }
    }

    override fun compareTo(other: Hand): Int {
        if (cards == other.cards) return 0
        val (type, otherType) = getHandType() to other.getHandType()
        return when {
            type > otherType -> 1
            type < otherType -> -1
            else -> {
                for (i in cards.indices) {
                    return when {
                        cards[i] > other.cards[i] -> 1
                        cards[i] < other.cards[i] -> -1
                        else -> continue
                    }
                }
                0
            }
        }
    }
}

private data class Hand2(
    val cards: List<Card2>
) : Comparable<Hand2> {

    fun getHandType(): HandType {
        return if (cards.contains(Card2.J)) {
            val jIndexes = buildList {
                for (card in cards.withIndex()) {
                    if (card.value == Card2.J) add(card.index)
                }
            }
            val cardsMutable = cards.toMutableList()
            for (jIndex in jIndexes) {
                var strongestType = HandType.HIGH_CARD
                var bestWildcard = Card2.J
                for (j in Card2.entries) {
                    cardsMutable[jIndex] = j
                    val type = getHandType(cardsMutable)
                    if (type > strongestType) {
                        strongestType = type
                        bestWildcard = j
                    }
                }
                cardsMutable[jIndex] = bestWildcard
            }
            getHandType(cardsMutable)
        } else {
            getHandType(cards)
        }
    }

    private fun getHandType(cardz: List<Card2>): HandType {
        val cardSet = cardz.toSet()
        return when (cardSet.size) {
            1 -> HandType.FIVE_OF_A_KIND
            2 -> {
                val isFourOfAKind = cardz.groupBy { it }.values.any { it.size == 4 }
                if (isFourOfAKind) {
                    HandType.FOUR_OF_A_KIND
                } else {
                    HandType.FULL_HOUSE
                }
            }

            3 -> {
                val isThreeOfAKind = cardz.groupBy { it }.values.any { it.size == 3 }
                if (isThreeOfAKind) {
                    HandType.THREE_OF_A_KIND
                } else {
                    HandType.TWO_PAIR
                }
            }

            4 -> HandType.ONE_PAIR
            5 -> HandType.HIGH_CARD
            else -> throw IllegalArgumentException()
        }
    }

    override fun compareTo(other: Hand2): Int {
        if (cards == other.cards) return 0
        val (type, otherType) = getHandType() to other.getHandType()
        return when {
            type > otherType -> 1
            type < otherType -> -1
            else -> {
                for (i in cards.indices) {
                    return when {
                        cards[i] > other.cards[i] -> 1
                        cards[i] < other.cards[i] -> -1
                        else -> continue
                    }
                }
                0
            }
        }
    }
}
