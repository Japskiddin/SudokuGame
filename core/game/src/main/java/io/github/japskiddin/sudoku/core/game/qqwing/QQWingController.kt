package io.github.japskiddin.sudoku.core.game.qqwing

import java.util.LinkedList
import java.util.Random
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger

// @formatter:off
/*
 * qqwing - Sudoku solver and generator
 * Copyright (C) 2014 Stephen Ostermiller
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */
// @formatter:on
class QQWingController {
    val options = QQWingOptions()
    private var level: IntArray? = null
    private var solution: IntArray = IntArray(81)
    private val generated = LinkedList<IntArray>()
    var isImpossible = false
        private set
    var solutionCount = 0

    fun generate(type: GameType, difficulty: GameDifficulty): IntArray? {
        generated.clear()
        options.gameDifficulty = difficulty
        options.action = Action.GENERATE
        options.needNow = true
        options.printSolution = false
        options.threads = Runtime.getRuntime().availableProcessors()
        options.gameType = type
        doAction()
        return generated.poll()
    }

    fun generateMultiple(
        type: GameType,
        difficulty: GameDifficulty,
        amount: Int
    ): LinkedList<IntArray> {
        generated.clear()
        options.numberToGenerate = amount
        options.gameDifficulty = difficulty
        options.needNow = true
        options.action = Action.GENERATE
        options.printSolution = false
        options.threads = Runtime.getRuntime().availableProcessors()
        options.gameType = type
        doAction()
        return generated
    }

    /**
     * Generate a new sudoku based on a given seed, but only accept challenge sudokus with a certain probability
     * @param seed the seed based on which the sudoku should be calculated
     * @param challengePermission the probability with which a challenge sudoku is accepted upon calculation
     * @param challengeIterations the amount of times a challenge sudoku can be rejected in a row before being
     * accepted with a probability of 100%
     * @return the generated sudoku
     */
    @JvmOverloads
    fun generateFromSeed(
        seed: Int,
        challengePermission: Double = 1.0,
        challengeIterations: Int = 1
    ): IntArray? {
        @Suppress("NAME_SHADOWING")
        var seed = seed

        @Suppress("NAME_SHADOWING")
        var challengeIterations = challengeIterations
        generated.clear()
        val generator = QQWing(GameType.DEFAULT9X9, GameDifficulty.UNSPECIFIED)
        var continueSearch = true
        val random = Random(seed.toLong())
        val seedFactor = 2
        while (continueSearch && challengeIterations > 0) {
            seed *= seedFactor
            generator.setRandom(seed)
            generator.setRecordHistory(true)
            generator.generatePuzzle()
            if (generator.difficulty !== GameDifficulty.EXPERT || random.nextDouble() < challengePermission) {
                continueSearch = false
            } else {
                challengeIterations--
            }
        }
        generated.add(generator.puzzle)
        options.gameType = GameType.DEFAULT9X9
        options.gameDifficulty = generator.difficulty
        return generated.poll()
    }

    fun solve(gameBoard: IntArray?, gameType: GameType): IntArray {
        isImpossible = false
        level = gameBoard
        options.needNow = true
        options.action = Action.SOLVE
        options.printSolution = true
        options.threads = 1
        options.gameType = gameType
        doAction()
        return solution
    }

    private fun doAction() {
        // The number of puzzles solved or generated.
        val puzzleCount = AtomicInteger(0)
        val done = AtomicBoolean(false)
        val threads = arrayOfNulls<Thread>(options.threads)
        for (threadCount in threads.indices) {
            threads[threadCount] = Thread(object : Runnable {
                // Create a new puzzle board and set the options
                private val qqWing = createQQWing()
                private fun createQQWing(): QQWing {
                    val ss = QQWing(options.gameType, options.gameDifficulty)
                    ss.setRecordHistory(
                        options.printHistory ||
                            options.printInstructions ||
                            options.printStats ||
                            options.gameDifficulty !== GameDifficulty.UNSPECIFIED
                    )
                    ss.setLogHistory(options.logHistory)
                    ss.setPrintStyle(options.printStyle)
                    return ss
                }

                @Suppress("LongMethod")
                override fun run() {
                    try {
                        // Solve puzzle or generate puzzles
                        // until end of input for solving, or
                        // until we have generated the specified number.
                        while (!done.get()) {
                            // Record whether the puzzle was possible or not,
                            // so that we don't try to solve impossible givens.
                            var havePuzzle: Boolean
                            if (options.action == Action.GENERATE) {
                                // Generate a puzzle
                                havePuzzle = qqWing.generatePuzzleSymmetry(options.symmetry)
                            } else {
                                // Read the next puzzle on STDIN
                                val puzzle = IntArray(QQWing.BOARD_SIZE)
                                if (getPuzzleToSolve(puzzle)) {
                                    havePuzzle = qqWing.setPuzzle(puzzle)
                                    if (havePuzzle) {
                                        puzzleCount.getAndDecrement()
                                    } else {
                                        // Puzzle to solve is impossible.
                                        isImpossible = true
                                    }
                                } else {
                                    // Set loop to terminate when nothing is
                                    // left on STDIN
                                    havePuzzle = false
                                    done.set(true)
                                }
                            }

                            if (havePuzzle) {
                                solutionCount = qqWing.countSolutionsLimited()

                                // Solve the puzzle
                                @Suppress("ComplexCondition")
                                if (options.printSolution ||
                                    options.printHistory ||
                                    options.printStats ||
                                    options.printInstructions ||
                                    options.gameDifficulty !== GameDifficulty.UNSPECIFIED
                                ) {
                                    qqWing.solve()
                                    solution = qqWing.solution
                                }

                                // Bail out if it didn't meet the difficulty
                                // standards for generation
                                if (options.action == Action.GENERATE) {
                                    if (options.gameDifficulty != GameDifficulty.UNSPECIFIED &&
                                        options.gameDifficulty != qqWing.calculateDifficulty()
                                    ) {
                                        havePuzzle = false
                                        // check if other threads have
                                        // finished the job
                                        if (puzzleCount.get() >= options.numberToGenerate) {
                                            done.set(true)
                                        }
                                    } else {
                                        val numDone = puzzleCount.incrementAndGet()
                                        if (numDone >= options.numberToGenerate) {
                                            done.set(true)
                                        }
                                        if (numDone > options.numberToGenerate) {
                                            havePuzzle = false
                                        }
                                    }
                                }
                                if (havePuzzle) {
                                    generated.add(qqWing.puzzle)
                                }
                            }
                        }
                    } catch (e: Exception) {
                        return
                    }
                }
            })
            threads[threadCount]?.start()
        }
        if (options.needNow) {
            for (i in threads.indices) {
                try {
                    threads[i]!!.join()
                } catch (_: InterruptedException) {
                }
            }
        }
    }

    class QQWingOptions {
        // defaults for options
        var needNow = false

        @Suppress("unused")
        var printPuzzle = false
        var printSolution = false
        var printHistory = false
        var printInstructions = false

        @Suppress("unused")
        var timer = false

        @Suppress("unused")
        var countSolutions = false
        var action = Action.NONE
        var logHistory = false
        var printStyle = PrintStyle.READABLE
        var numberToGenerate = 1
        var printStats = false
        var gameDifficulty = GameDifficulty.UNSPECIFIED
        var gameType = GameType.UNSPECIFIED
        var symmetry = Symmetry.NONE
        var threads = Runtime.getRuntime().availableProcessors()
    }

    private fun getPuzzleToSolve(puzzle: IntArray?): Boolean {
        if (level != null) {
            if (puzzle!!.size == level!!.size) {
                for (i in level!!.indices) {
                    puzzle[i] = level!![i]
                }
            }
            level = null
            return true
        }
        return false
    }
}
