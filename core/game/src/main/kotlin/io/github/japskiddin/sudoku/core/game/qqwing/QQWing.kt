package io.github.japskiddin.sudoku.core.game.qqwing

import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.core.model.GameType
import java.util.*
import kotlin.math.abs

// @formatter:off
/*
 * qqwing - Sudoku solver and generator
 * Copyright (C) 2006-2014 Stephen Ostermiller http://ostermiller.org/
 * Copyright (C) 2007 Jacques Bensimon (jacques@ipm.com)
 * Copyright (C) 2007 Joel Yarde (joel.yarde - gmail.com)
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

/**
 * The board containing all the memory structures and methods for solving or
 * generating sudoku puzzles.
 */
@Suppress("LargeClass", "TooManyFunctions")
public class QQWing(
    type: GameType,
    difficulty: GameDifficulty
) {
    /**
     * A list of moves used to solve the puzzle. This list contains all moves,
     * even on solve branches that did not lead to a solution.
     */
    private val solveHistory = ArrayList<LogItem?>()

    /**
     * A list of moves used to solve the puzzle. This list contains only the
     * moves needed to solve the puzzle, but doesn't contain information about
     * bad guesses.
     */
    private val solveInstructions = ArrayList<LogItem?>()

    /**
     * The last round of solving
     */
    private var lastSolveRound = 0

    /**
     * The 81 integers that make up a sudoku puzzle. Givens are 1-9, unknowns
     * are 0. Once initialized, this puzzle remains as is. The answer is worked
     * out in "solution".
     */
    public var puzzle: IntArray = IntArray(BOARD_SIZE)

    /**
     * The 81 integers that make up a sudoku puzzle. The solution is built here,
     * after completion all will be 1-9.
     */
    public var solution: IntArray = IntArray(BOARD_SIZE)

    /**
     * Recursion depth at which each of the numbers in the solution were placed.
     * Useful for backing out solve branches that don't lead to a solution.
     */
    private var solutionRound = IntArray(BOARD_SIZE)

    /**
     * The 729 integers that make up a the possible values for a Sudoku puzzle.
     * (9 possibilities for each of 81 squares). If possibilities is zero,
     * then the possibility could still be filled in according to the Sudoku
     * rules. When a possibility is eliminated, possibilities is assigned the
     * round (recursion level) at which it was determined that it could not be a
     * possibility.
     */
    private var possibilities = IntArray(POSSIBILITY_SIZE)

    /**
     * An array the size of the board (81) containing each of the numbers 0-n
     * exactly once. This array may be shuffled so that operations that need to
     * look at each cell can do so in a random order.
     */
    private var randomBoardArray = fillIncrementing(IntArray(BOARD_SIZE))

    /**
     * An array with one element for each position (9), in some random order to
     * be used when trying each position in turn during guesses.
     */
    private var randomPossibilityArray = fillIncrementing(IntArray(ROW_COL_SEC_SIZE))

    /**
     * Whether or not to record history
     */
    private var recordHistory = true

    /**
     * Whether or not to print history as it happens
     */
    private var logHistory = false

    /**
     * The style with which to print puzzles and solutions
     */
    private var printStyle = PrintStyle.READABLE
    private var gameType = GameType.UNSPECIFIED
    public var difficulty: GameDifficulty = GameDifficulty.UNSPECIFIED

    /**
     * Create a new Sudoku board
     */
    init {
        gameType = type
        this.difficulty = difficulty
        GRID_SIZE_ROW = type.sectionHeight // 3    // 2
        GRID_SIZE_COL = type.sectionWidth // 3    // 3
        ROW_COL_SEC_SIZE = GRID_SIZE_ROW * GRID_SIZE_COL //  3*3 = 9     // 6
        SEC_GROUP_SIZE = ROW_COL_SEC_SIZE * GRID_SIZE_ROW // 9 * 3 = 27 ? // 12
        BOARD_SIZE = ROW_COL_SEC_SIZE * ROW_COL_SEC_SIZE // 9 * 9 = 81   // 36
        POSSIBILITY_SIZE = BOARD_SIZE * ROW_COL_SEC_SIZE // 81 * 9
        puzzle = IntArray(BOARD_SIZE)
        solution = IntArray(BOARD_SIZE)
        solutionRound = IntArray(BOARD_SIZE)
        possibilities = IntArray(POSSIBILITY_SIZE)
        randomBoardArray = fillIncrementing(IntArray(BOARD_SIZE))
        randomPossibilityArray = fillIncrementing(IntArray(ROW_COL_SEC_SIZE))
    }

    /**
     * Set the board to the given puzzle. The given puzzle must be an array of
     * 81 integers.
     */
    public fun setPuzzle(initPuzzle: IntArray?): Boolean {
        for (i in 0 until BOARD_SIZE) {
            puzzle[i] = initPuzzle?.get(i) ?: 0
        }
        return reset()
    }

    public fun setRandom(seed: Int) {
        random = Random(seed.toLong())
    }

    /**
     * Reset the board to its initial state with only the givens. This method
     * clears any solution, resets statistics, and clears any history messages.
     */
    private fun reset(): Boolean {
        solution.fill(0)
        solutionRound.fill(0)
        possibilities.fill(0)
        solveHistory.clear()
        solveInstructions.clear()
        val round = 1
        for (position in 0 until BOARD_SIZE) {
            if (puzzle[position] > 0) {
                val valIndex = puzzle[position] - 1
                val valPos = getPossibilityIndex(valIndex, position)
                val value = puzzle[position]
                if (possibilities[valPos] != 0) return false
                mark(position, round, value)
                if (logHistory || recordHistory) {
                    addHistoryItem(
                        LogItem(
                            round,
                            LogType.GIVEN,
                            value,
                            position
                        )
                    )
                }
            }
        }
        return true
    }

    /**
     * Get the gameDifficulty rating.
     */
    @Suppress("CyclomaticComplexMethod", "ReturnCount", "MagicNumber")
    public fun calculateDifficulty(): GameDifficulty {
        if (getGuessCount() > 0) return GameDifficulty.EXPERT
        if (getBoxLineReductionCount() > 0) return GameDifficulty.HARD
        if (getPointingPairTripleCount() > 0) return GameDifficulty.HARD
        if (getHiddenPairCount() > 0) return GameDifficulty.INTERMEDIATE
        if (getNakedPairCount() > 0) return GameDifficulty.INTERMEDIATE
        when (gameType) {
            GameType.DEFAULT6X6 -> if (getHiddenSingleCount() > 0) return GameDifficulty.INTERMEDIATE
            GameType.DEFAULT9X9 -> if (getHiddenSingleCount() > 10) return GameDifficulty.INTERMEDIATE
            GameType.DEFAULT12X12 -> if (getHiddenSingleCount() > 20) return GameDifficulty.INTERMEDIATE
            else -> if (getHiddenSingleCount() > 10) return GameDifficulty.INTERMEDIATE
        }
        when (gameType) {
            GameType.DEFAULT6X6 -> if (getSingleCount() > 10) return GameDifficulty.EASY
            GameType.DEFAULT9X9 -> if (getSingleCount() > 35) return GameDifficulty.EASY
            GameType.DEFAULT12X12 -> if (getSingleCount() > 50) return GameDifficulty.EASY
            else -> if (getSingleCount() > 20) return GameDifficulty.EASY
        }
        return GameDifficulty.UNSPECIFIED
    }

    /**
     * Get the gameDifficulty rating.
     */
    public fun getDifficultyName(): String = calculateDifficulty().name

    /**
     * Get the number of cells for which the solution was determined because
     * there was only one possible value for that cell.
     */
    public fun getSingleCount(): Int = getLogCount(solveInstructions, LogType.SINGLE)

    /**
     * Get the number of cells for which the solution was determined because
     * that cell had the only possibility for some value in the row, column, or
     * section.
     */
    public fun getHiddenSingleCount(): Int = getLogCount(solveInstructions, LogType.HIDDEN_SINGLE_ROW) +
        getLogCount(
            solveInstructions,
            LogType.HIDDEN_SINGLE_COLUMN
        ) +
        getLogCount(
            solveInstructions,
            LogType.HIDDEN_SINGLE_SECTION
        )

    /**
     * Get the number of naked pair reductions that were performed in solving
     * this puzzle.
     */
    public fun getNakedPairCount(): Int = getLogCount(solveInstructions, LogType.NAKED_PAIR_ROW) +
        getLogCount(
            solveInstructions,
            LogType.NAKED_PAIR_COLUMN
        ) +
        getLogCount(
            solveInstructions,
            LogType.NAKED_PAIR_SECTION
        )

    /**
     * Get the number of hidden pair reductions that were performed in solving
     * this puzzle.
     */
    public fun getHiddenPairCount(): Int = getLogCount(solveInstructions, LogType.HIDDEN_PAIR_ROW) +
        getLogCount(
            solveInstructions,
            LogType.HIDDEN_PAIR_COLUMN
        ) +
        getLogCount(
            solveInstructions,
            LogType.HIDDEN_PAIR_SECTION
        )

    /**
     * Get the number of pointing pair/triple reductions that were performed in
     * solving this puzzle.
     */
    public fun getPointingPairTripleCount(): Int = getLogCount(solveInstructions, LogType.POINTING_PAIR_TRIPLE_ROW) +
        getLogCount(
            solveInstructions,
            LogType.POINTING_PAIR_TRIPLE_COLUMN
        )

    /**
     * Get the number of box/line reductions that were performed in solving this
     * puzzle.
     */
    public fun getBoxLineReductionCount(): Int = getLogCount(solveInstructions, LogType.ROW_BOX) +
        getLogCount(
            solveInstructions,
            LogType.COLUMN_BOX
        )

    /**
     * Get the number lucky guesses in solving this puzzle.
     */
    public fun getGuessCount(): Int = getLogCount(solveInstructions, LogType.GUESS)

    /**
     * Get the number of backtracks (unlucky guesses) required when solving this
     * puzzle.
     */
    public fun getBacktrackCount(): Int = getLogCount(solveHistory, LogType.ROLLBACK)

    private fun shuffleRandomArrays() {
        shuffleArray(randomBoardArray, BOARD_SIZE)
        shuffleArray(randomPossibilityArray, ROW_COL_SEC_SIZE)
    }

    private fun clearPuzzle() {
        // Clear any existing puzzle
        for (i in 0 until BOARD_SIZE) {
            puzzle[i] = 0
        }
        reset()
    }

    public fun generatePuzzle(): Boolean = generatePuzzleSymmetry(Symmetry.NONE)

    @Suppress("CyclomaticComplexMethod", "LongMethod", "NestedBlockDepth")
    public fun generatePuzzleSymmetry(symmetry: Symmetry): Boolean {
        @Suppress("NAME_SHADOWING")
        var symmetry = symmetry
        if (symmetry == Symmetry.RANDOM) symmetry = randomSymmetry

        // Don't record history while generating.
        val recHistory = recordHistory
        setRecordHistory(false)
        val lHistory = logHistory
        setLogHistory(false)
        clearPuzzle()

        // Start by getting the randomness in order so that
        // each puzzle will be different from the last.
        shuffleRandomArrays()

        // Now solve the puzzle the whole way. The solve
        // uses random algorithms, so we should have a
        // really randomly totally filled sudoku
        // Even when starting from an empty grid
        solve()
        if (symmetry == Symmetry.NONE) {
            // Rollback any square for which it is obvious that
            // the square doesn't contribute to a unique solution
            // (ie, squares that were filled by logic rather
            // than by guess)
            rollbackNonGuesses()
        }

        // Record all marked squares as the puzzle so
        // that we can call countSolutions without losing it.
        for (i in 0 until BOARD_SIZE) {
            puzzle[i] = solution[i]
        }

        // Randomize everything so that we test squares
        // in a different order than they were added.
        shuffleRandomArrays()

        // Remove one value at a time and see if
        // the puzzle still has only one solution.
        // If it does, leave it out the point because
        // it is not needed.
        for (i in 0 until BOARD_SIZE) {
            // check all the positions, but in shuffled order
            val position = randomBoardArray[i]
            if (puzzle[position] > 0) {
                var positionsym1 = -1
                var positionsym2 = -1
                var positionsym3 = -1
                when (symmetry) {
                    Symmetry.ROTATE90 -> {
                        positionsym2 = rowColumnToCell(
                            ROW_COL_SEC_SIZE - 1 - cellToColumn(position),
                            cellToRow(position)
                        )
                        positionsym3 = rowColumnToCell(
                            cellToColumn(position),
                            ROW_COL_SEC_SIZE - 1 - cellToRow(position)
                        )
                        positionsym1 = rowColumnToCell(
                            ROW_COL_SEC_SIZE - 1 - cellToRow(position),
                            ROW_COL_SEC_SIZE - 1 - cellToColumn(position)
                        )
                    }

                    Symmetry.ROTATE180 -> positionsym1 = rowColumnToCell(
                        ROW_COL_SEC_SIZE - 1 - cellToRow(position),
                        ROW_COL_SEC_SIZE - 1 - cellToColumn(position)
                    )

                    Symmetry.MIRROR -> positionsym1 = rowColumnToCell(
                        cellToRow(position),
                        ROW_COL_SEC_SIZE - 1 - cellToColumn(position)
                    )

                    Symmetry.FLIP -> positionsym1 = rowColumnToCell(
                        ROW_COL_SEC_SIZE - 1 - cellToRow(position),
                        cellToColumn(position)
                    )

                    else -> {}
                }
                // try backing out the value and
                // counting solutions to the puzzle
                val savedValue = puzzle[position]
                puzzle[position] = 0
                var savedSym1 = 0
                if (positionsym1 >= 0) {
                    savedSym1 = puzzle[positionsym1]
                    puzzle[positionsym1] = 0
                }
                var savedSym2 = 0
                if (positionsym2 >= 0) {
                    savedSym2 = puzzle[positionsym2]
                    puzzle[positionsym2] = 0
                }
                var savedSym3 = 0
                if (positionsym3 >= 0) {
                    savedSym3 = puzzle[positionsym3]
                    puzzle[positionsym3] = 0
                }
                reset()
                if (countSolutions(2, true) > 1) {
                    // Put it back in, it is needed
                    puzzle[position] = savedValue
                    if (positionsym1 >= 0 && savedSym1 != 0) puzzle[positionsym1] = savedSym1
                    if (positionsym2 >= 0 && savedSym2 != 0) puzzle[positionsym2] = savedSym2
                    if (positionsym3 >= 0 && savedSym3 != 0) puzzle[positionsym3] = savedSym3
                }
            }
        }

        // Clear all solution info, leaving just the puzzle.
        reset()

        // Restore recording history.
        setRecordHistory(recHistory)
        setLogHistory(lHistory)
        return true
    }

    private fun rollbackNonGuesses() {
        var i = 2
        while (i <= lastSolveRound) {
            rollbackRound(i)
            i += 2
        }
    }

    public fun setPrintStyle(ps: PrintStyle) {
        printStyle = ps
    }

    public fun setRecordHistory(recHistory: Boolean) {
        recordHistory = recHistory
    }

    public fun setLogHistory(logHist: Boolean) {
        logHistory = logHist
    }

    private fun addHistoryItem(l: LogItem) {
        if (logHistory) {
            l.print()
            println()
        }
        if (recordHistory) {
            solveHistory.add(l)
            solveInstructions.add(l)
        }
    }

    private fun historyToString(v: ArrayList<LogItem?>): String {
        val sb = StringBuilder()
        if (!recordHistory) {
            sb.append("History was not recorded.").append(NEW_LINE)
            if (printStyle == PrintStyle.CSV) {
                sb.append(" -- ").append(NEW_LINE)
            } else {
                sb.append(NEW_LINE)
            }
        }
        for (i in v.indices) {
            sb.append((i + 1).toString() + ". ").append(NEW_LINE)
            v[i]!!.print()
            if (printStyle == PrintStyle.CSV) {
                sb.append(" -- ").append(NEW_LINE)
            } else {
                sb.append(NEW_LINE)
            }
        }
        if (printStyle == PrintStyle.CSV) {
            sb.append(",").append(NEW_LINE)
        } else {
            sb.append(NEW_LINE)
        }
        return sb.toString()
    }

    public fun getSolveInstructionsString(): String = if (isSolved()) {
        historyToString(solveInstructions)
    } else {
        "No solve instructions - Puzzle is not possible to solve."
    }

    public fun solve(): Boolean {
        reset()
        shuffleRandomArrays()
        return solve(2)
    }

    @Suppress("ReturnCount")
    private fun solve(round: Int): Boolean {
        lastSolveRound = round
        while (singleSolveMove(round)) {
            if (isSolved()) return true
            if (isImpossible()) return false
        }
        val nextGuessRound = round + 1
        val nextRound = round + 2
        var guessNumber = 0
        while (guess(nextGuessRound, guessNumber)) {
            if (isImpossible() || !solve(nextRound)) {
                rollbackRound(nextRound)
                rollbackRound(nextGuessRound)
            } else {
                return true
            }
            guessNumber++
        }
        return false
    }

    /**
     * Count the number of solutions to the puzzle
     * but return two any time there are two or
     * more solutions.  This method will run much
     * falter than countSolutions() when there
     * are many possible solutions and can be used
     * when you are interested in knowing if the
     * puzzle has zero, one, or multiple solutions.
     */
    public fun countSolutionsLimited(): Int = countSolutions()

    private fun countSolutions(limitToTwo: Boolean = true): Int {
        // Don't record history while generating.
        val recHistory = recordHistory
        setRecordHistory(false)
        val lHistory = logHistory
        setLogHistory(false)
        reset()
        val solutionCount = countSolutions(2, limitToTwo)

        // Restore recording history.
        setRecordHistory(recHistory)
        setLogHistory(lHistory)
        return solutionCount
    }

    @Suppress("ReturnCount")
    private fun countSolutions(
        round: Int,
        limitToTwo: Boolean
    ): Int {
        while (singleSolveMove(round)) {
            if (isSolved()) {
                rollbackRound(round)
                return 1
            }
            if (isImpossible()) {
                rollbackRound(round)
                return 0
            }
        }
        var solutions = 0
        val nextRound = round + 1
        var guessNumber = 0
        while (guess(nextRound, guessNumber)) {
            solutions += countSolutions(nextRound, limitToTwo)
            if (limitToTwo && solutions >= 2) {
                rollbackRound(round)
                return solutions
            }
            guessNumber++
        }
        rollbackRound(round)
        return solutions
    }

    private fun rollbackRound(round: Int) {
        if (logHistory || recordHistory) addHistoryItem(LogItem(round, LogType.ROLLBACK))
        for (i in 0 until BOARD_SIZE) {
            if (solutionRound[i] == round) {
                solutionRound[i] = 0
                solution[i] = 0
            }
        }
        for (i in 0 until POSSIBILITY_SIZE) {
            if (possibilities[i] == round) {
                possibilities[i] = 0
            }
        }
        while (solveInstructions.size > 0 && solveInstructions[solveInstructions.size - 1]!!.round == round) {
            val i = solveInstructions.size - 1
            solveInstructions.removeAt(i)
        }
    }

    public fun isSolved(): Boolean {
        for (i in 0 until BOARD_SIZE) {
            if (solution[i] == 0) {
                return false
            }
        }
        return true
    }

    @Suppress("NestedBlockDepth")
    private fun isImpossible(): Boolean {
        for (position in 0 until BOARD_SIZE) {
            if (solution[position] == 0) {
                var count = 0
                for (valIndex in 0 until ROW_COL_SEC_SIZE) {
                    val valPos = getPossibilityIndex(valIndex, position)
                    if (possibilities[valPos] == 0) count++
                }
                if (count == 0) {
                    return true
                }
            }
        }
        return false
    }

    @Suppress("NestedBlockDepth")
    private fun findPositionWithFewestPossibilities(): Int {
        var minPossibilities = ROW_COL_SEC_SIZE + 1
        var bestPosition = 0
        for (i in 0 until BOARD_SIZE) {
            val position = randomBoardArray[i]
            if (solution[position] == 0) {
                var count = 0
                for (valIndex in 0 until ROW_COL_SEC_SIZE) {
                    val valPos = getPossibilityIndex(valIndex, position)
                    if (possibilities[valPos] == 0) count++
                }
                if (count < minPossibilities) {
                    minPossibilities = count
                    bestPosition = position
                }
            }
        }
        return bestPosition
    }

    @Suppress("NestedBlockDepth")
    private fun guess(
        round: Int,
        guessNumber: Int
    ): Boolean {
        var localGuessCount = 0
        val position = findPositionWithFewestPossibilities()
        for (i in 0 until ROW_COL_SEC_SIZE) {
            val valIndex = randomPossibilityArray[i]
            val valPos = getPossibilityIndex(valIndex, position)
            if (possibilities[valPos] == 0) {
                if (localGuessCount == guessNumber) {
                    val value = valIndex + 1
                    if (logHistory || recordHistory) {
                        addHistoryItem(
                            LogItem(
                                round,
                                LogType.GUESS,
                                value,
                                position
                            )
                        )
                    }
                    mark(position, round, value)
                    return true
                }
                localGuessCount++
            }
        }
        return false
    }

    @Suppress("ReturnCount")
    private fun singleSolveMove(round: Int): Boolean {
        if (onlyPossibilityForCell(round)) return true
        if (onlyValueInSection(round)) return true
        if (onlyValueInRow(round)) return true
        if (onlyValueInColumn(round)) return true
        if (handleNakedPairs(round)) return true
        if (pointingRowReduction(round)) return true
        if (pointingColumnReduction(round)) return true
        if (rowBoxReduction(round)) return true
        if (colBoxReduction(round)) return true
        if (hiddenPairInRow(round)) return true
        return if (hiddenPairInColumn(round)) true else hiddenPairInSection(round)
    }

    @Suppress("CyclomaticComplexMethod", "NestedBlockDepth")
    private fun colBoxReduction(round: Int): Boolean {
        for (valIndex in 0 until ROW_COL_SEC_SIZE) {
            for (col in 0 until ROW_COL_SEC_SIZE) {
                val colStart = columnToFirstCell(col)
                var inOneBox = true
                var colBox = -1
                // this part is checked!
                for (i in 0 until GRID_SIZE_COL) {
                    for (j in 0 until GRID_SIZE_ROW) {
                        val row = i * GRID_SIZE_ROW + j
                        val position = rowColumnToCell(row, col)
                        val valPos = getPossibilityIndex(valIndex, position)
                        if (possibilities[valPos] == 0) {
                            if (colBox == -1 || colBox == i) {
                                colBox = i
                            } else {
                                inOneBox = false
                            }
                        }
                    }
                }
                if (inOneBox && colBox != -1) {
                    var doneSomething = false
                    val row = GRID_SIZE_ROW * colBox
                    val secStart = cellToSectionStartCell(rowColumnToCell(row, col))
                    val secStartRow = cellToRow(secStart)
                    val secStartCol = cellToColumn(secStart)
                    for (i in 0 until GRID_SIZE_COL) {
                        for (j in 0 until GRID_SIZE_ROW) {
                            val row2 = secStartRow + j
                            val col2 = secStartCol + i
                            val position = rowColumnToCell(row2, col2)
                            val valPos = getPossibilityIndex(valIndex, position)
                            if (col != col2 && possibilities[valPos] == 0) {
                                possibilities[valPos] = round
                                doneSomething = true
                            }
                        }
                    }
                    if (doneSomething) {
                        if (logHistory || recordHistory) {
                            addHistoryItem(
                                LogItem(
                                    round,
                                    LogType.COLUMN_BOX,
                                    valIndex + 1,
                                    colStart
                                )
                            )
                        }
                        return true
                    }
                }
            }
        }
        return false
    }

    @Suppress("CyclomaticComplexMethod", "NestedBlockDepth")
    private fun rowBoxReduction(round: Int): Boolean {
        for (valIndex in 0 until ROW_COL_SEC_SIZE) {
            for (row in 0 until ROW_COL_SEC_SIZE) {
                val rowStart = rowToFirstCell(row)
                var inOneBox = true
                var rowBox = -1
                for (i in 0 until GRID_SIZE_ROW) {
                    for (j in 0 until GRID_SIZE_COL) {
                        val column = i * GRID_SIZE_COL + j
                        val position = rowColumnToCell(row, column)
                        val valPos = getPossibilityIndex(valIndex, position)
                        if (possibilities[valPos] == 0) {
                            if (rowBox == -1 || rowBox == i) {
                                rowBox = i
                            } else {
                                inOneBox = false
                            }
                        }
                    }
                }
                if (inOneBox && rowBox != -1) {
                    var doneSomething = false
                    val column = GRID_SIZE_COL * rowBox
                    val secStart = cellToSectionStartCell(rowColumnToCell(row, column))
                    val secStartRow = cellToRow(secStart)
                    val secStartCol = cellToColumn(secStart)
                    for (i in 0 until GRID_SIZE_ROW) {
                        for (j in 0 until GRID_SIZE_COL) {
                            val row2 = secStartRow + i
                            val col2 = secStartCol + j
                            val position = rowColumnToCell(row2, col2)
                            val valPos = getPossibilityIndex(valIndex, position)
                            if (row != row2 && possibilities[valPos] == 0) {
                                possibilities[valPos] = round
                                doneSomething = true
                            }
                        }
                    }
                    if (doneSomething) {
                        if (logHistory || recordHistory) {
                            addHistoryItem(
                                LogItem(
                                    round,
                                    LogType.ROW_BOX,
                                    valIndex + 1,
                                    rowStart
                                )
                            )
                        }
                        return true
                    }
                }
            }
        }
        return false
    }

    @Suppress("CyclomaticComplexMethod", "NestedBlockDepth")
    private fun pointingRowReduction(round: Int): Boolean {
        for (valIndex in 0 until ROW_COL_SEC_SIZE) {
            for (section in 0 until ROW_COL_SEC_SIZE) {
                val secStart = sectionToFirstCell(section)
                var inOneRow = true
                var boxRow = -1
                for (j in 0 until GRID_SIZE_ROW) {
                    for (i in 0 until GRID_SIZE_COL) {
                        val secVal = secStart + i + ROW_COL_SEC_SIZE * j
                        val valPos = getPossibilityIndex(valIndex, secVal)
                        if (possibilities[valPos] == 0) {
                            if (boxRow == -1 || boxRow == j) {
                                boxRow = j
                            } else {
                                inOneRow = false
                            }
                        }
                    }
                }
                if (inOneRow && boxRow != -1) {
                    var doneSomething = false
                    val row = cellToRow(secStart) + boxRow
                    val rowStart = rowToFirstCell(row)
                    for (i in 0 until ROW_COL_SEC_SIZE) {
                        val position = rowStart + i
                        val section2 = cellToSection(position)
                        val valPos = getPossibilityIndex(valIndex, position)
                        if (section != section2 && possibilities[valPos] == 0) {
                            possibilities[valPos] = round
                            doneSomething = true
                        }
                    }
                    if (doneSomething) {
                        if (logHistory || recordHistory) {
                            addHistoryItem(
                                LogItem(
                                    round,
                                    LogType.POINTING_PAIR_TRIPLE_ROW,
                                    valIndex + 1,
                                    rowStart
                                )
                            )
                        }
                        return true
                    }
                }
            }
        }
        return false
    }

    @Suppress("CyclomaticComplexMethod", "NestedBlockDepth")
    private fun pointingColumnReduction(round: Int): Boolean {
        for (valIndex in 0 until ROW_COL_SEC_SIZE) {
            for (section in 0 until ROW_COL_SEC_SIZE) {
                val secStart = sectionToFirstCell(section)
                var inOneCol = true
                var boxCol = -1
                for (i in 0 until GRID_SIZE_COL) {
                    for (j in 0 until GRID_SIZE_ROW) {
                        val secVal = secStart + i + ROW_COL_SEC_SIZE * j
                        val valPos = getPossibilityIndex(valIndex, secVal)
                        if (possibilities[valPos] == 0) {
                            if (boxCol == -1 || boxCol == i) {
                                boxCol = i
                            } else {
                                inOneCol = false
                            }
                        }
                    }
                }
                if (inOneCol && boxCol != -1) {
                    var doneSomething = false
                    val col = cellToColumn(secStart) + boxCol
                    val colStart = columnToFirstCell(col)
                    for (i in 0 until ROW_COL_SEC_SIZE) {
                        val position = colStart + ROW_COL_SEC_SIZE * i
                        val section2 = cellToSection(position)
                        val valPos = getPossibilityIndex(valIndex, position)
                        if (section != section2 && possibilities[valPos] == 0) {
                            possibilities[valPos] = round
                            doneSomething = true
                        }
                    }
                    if (doneSomething) {
                        if (logHistory || recordHistory) {
                            addHistoryItem(
                                LogItem(
                                    round,
                                    LogType.POINTING_PAIR_TRIPLE_COLUMN,
                                    valIndex + 1,
                                    colStart
                                )
                            )
                        }
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun countPossibilities(position: Int): Int {
        var count = 0
        for (valIndex in 0 until ROW_COL_SEC_SIZE) {
            val valPos = getPossibilityIndex(valIndex, position)
            if (possibilities[valPos] == 0) count++
        }
        return count
    }

    private fun arePossibilitiesSame(
        position1: Int,
        position2: Int
    ): Boolean {
        for (valIndex in 0 until ROW_COL_SEC_SIZE) {
            val valPos1 = getPossibilityIndex(valIndex, position1)
            val valPos2 = getPossibilityIndex(valIndex, position2)
            @Suppress("ComplexCondition")
            if ((possibilities[valPos1] == 0 || possibilities[valPos2] == 0) &&
                (possibilities[valPos1] != 0 || possibilities[valPos2] != 0)
            ) {
                return false
            }
        }
        return true
    }

    private fun removePossibilitiesInOneFromTwo(
        position1: Int,
        position2: Int,
        round: Int
    ): Boolean {
        var doneSomething = false
        for (valIndex in 0 until ROW_COL_SEC_SIZE) {
            val valPos1 = getPossibilityIndex(valIndex, position1)
            val valPos2 = getPossibilityIndex(valIndex, position2)
            if (possibilities[valPos1] == 0 && possibilities[valPos2] == 0) {
                possibilities[valPos2] = round
                doneSomething = true
            }
        }
        return doneSomething
    }

    @Suppress("CyclomaticComplexMethod", "NestedBlockDepth", "LongMethod")
    private fun hiddenPairInColumn(round: Int): Boolean {
        for (column in 0 until ROW_COL_SEC_SIZE) {
            for (valIndex in 0 until ROW_COL_SEC_SIZE) {
                var r1 = -1
                var r2 = -1
                var valCount = 0
                for (row in 0 until ROW_COL_SEC_SIZE) {
                    val position = rowColumnToCell(row, column)
                    val valPos = getPossibilityIndex(valIndex, position)
                    if (possibilities[valPos] == 0) {
                        if (r1 == -1 || r1 == row) {
                            r1 = row
                        } else if (r2 == -1 || r2 == row) {
                            r2 = row
                        }
                        valCount++
                    }
                }
                if (valCount == 2) {
                    for (valIndex2 in valIndex + 1 until ROW_COL_SEC_SIZE) {
                        var r3 = -1
                        var r4 = -1
                        var valCount2 = 0
                        for (row in 0 until ROW_COL_SEC_SIZE) {
                            val position = rowColumnToCell(row, column)
                            val valPos = getPossibilityIndex(valIndex2, position)
                            if (possibilities[valPos] == 0) {
                                if (r3 == -1 || r3 == row) {
                                    r3 = row
                                } else if (r4 == -1 || r4 == row) {
                                    r4 = row
                                }
                                valCount2++
                            }
                        }
                        if (valCount2 == 2 && r1 == r3 && r2 == r4) {
                            var doneSomething = false
                            for (valIndex3 in 0 until ROW_COL_SEC_SIZE) {
                                if (valIndex3 != valIndex && valIndex3 != valIndex2) {
                                    val position1 = rowColumnToCell(r1, column)
                                    val position2 = rowColumnToCell(r2, column)
                                    val valPos1 = getPossibilityIndex(valIndex3, position1)
                                    val valPos2 = getPossibilityIndex(valIndex3, position2)
                                    if (possibilities[valPos1] == 0) {
                                        possibilities[valPos1] = round
                                        doneSomething = true
                                    }
                                    if (possibilities[valPos2] == 0) {
                                        possibilities[valPos2] = round
                                        doneSomething = true
                                    }
                                }
                            }
                            if (doneSomething) {
                                if (logHistory || recordHistory) {
                                    addHistoryItem(
                                        LogItem(
                                            round,
                                            LogType.HIDDEN_PAIR_COLUMN,
                                            valIndex + 1,
                                            rowColumnToCell(r1, column)
                                        )
                                    )
                                }
                                return true
                            }
                        }
                    }
                }
            }
        }
        return false
    }

    @Suppress("CyclomaticComplexMethod", "NestedBlockDepth", "LongMethod")
    private fun hiddenPairInSection(round: Int): Boolean {
        for (section in 0 until ROW_COL_SEC_SIZE) {
            for (valIndex in 0 until ROW_COL_SEC_SIZE) {
                var si1 = -1
                var si2 = -1
                var valCount = 0
                for (secInd in 0 until ROW_COL_SEC_SIZE) {
                    val position = sectionToCell(section, secInd)
                    val valPos = getPossibilityIndex(valIndex, position)
                    if (possibilities[valPos] == 0) {
                        if (si1 == -1 || si1 == secInd) {
                            si1 = secInd
                        } else if (si2 == -1 || si2 == secInd) {
                            si2 = secInd
                        }
                        valCount++
                    }
                }
                if (valCount == 2) {
                    for (valIndex2 in valIndex + 1 until ROW_COL_SEC_SIZE) {
                        var si3 = -1
                        var si4 = -1
                        var valCount2 = 0
                        for (secInd in 0 until ROW_COL_SEC_SIZE) {
                            val position = sectionToCell(section, secInd)
                            val valPos = getPossibilityIndex(valIndex2, position)
                            if (possibilities[valPos] == 0) {
                                if (si3 == -1 || si3 == secInd) {
                                    si3 = secInd
                                } else if (si4 == -1 || si4 == secInd) {
                                    si4 = secInd
                                }
                                valCount2++
                            }
                        }
                        if (valCount2 == 2 && si1 == si3 && si2 == si4) {
                            var doneSomething = false
                            for (valIndex3 in 0 until ROW_COL_SEC_SIZE) {
                                if (valIndex3 != valIndex && valIndex3 != valIndex2) {
                                    val position1 = sectionToCell(section, si1)
                                    val position2 = sectionToCell(section, si2)
                                    val valPos1 = getPossibilityIndex(valIndex3, position1)
                                    val valPos2 = getPossibilityIndex(valIndex3, position2)
                                    if (possibilities[valPos1] == 0) {
                                        possibilities[valPos1] = round
                                        doneSomething = true
                                    }
                                    if (possibilities[valPos2] == 0) {
                                        possibilities[valPos2] = round
                                        doneSomething = true
                                    }
                                }
                            }
                            if (doneSomething) {
                                if (logHistory || recordHistory) {
                                    addHistoryItem(
                                        LogItem(
                                            round,
                                            LogType.HIDDEN_PAIR_SECTION,
                                            valIndex + 1,
                                            sectionToCell(section, si1)
                                        )
                                    )
                                }
                                return true
                            }
                        }
                    }
                }
            }
        }
        return false
    }

    @Suppress("CyclomaticComplexMethod", "NestedBlockDepth", "LongMethod")
    private fun hiddenPairInRow(round: Int): Boolean {
        for (row in 0 until ROW_COL_SEC_SIZE) {
            for (valIndex in 0 until ROW_COL_SEC_SIZE) {
                var c1 = -1
                var c2 = -1
                var valCount = 0
                for (column in 0 until ROW_COL_SEC_SIZE) {
                    val position = rowColumnToCell(row, column)
                    val valPos = getPossibilityIndex(valIndex, position)
                    if (possibilities[valPos] == 0) {
                        if (c1 == -1 || c1 == column) {
                            c1 = column
                        } else if (c2 == -1 || c2 == column) {
                            c2 = column
                        }
                        valCount++
                    }
                }
                if (valCount == 2) {
                    for (valIndex2 in valIndex + 1 until ROW_COL_SEC_SIZE) {
                        var c3 = -1
                        var c4 = -1
                        var valCount2 = 0
                        for (column in 0 until ROW_COL_SEC_SIZE) {
                            val position = rowColumnToCell(row, column)
                            val valPos = getPossibilityIndex(valIndex2, position)
                            if (possibilities[valPos] == 0) {
                                if (c3 == -1 || c3 == column) {
                                    c3 = column
                                } else if (c4 == -1 || c4 == column) {
                                    c4 = column
                                }
                                valCount2++
                            }
                        }
                        if (valCount2 == 2 && c1 == c3 && c2 == c4) {
                            var doneSomething = false
                            for (valIndex3 in 0 until ROW_COL_SEC_SIZE) {
                                if (valIndex3 != valIndex && valIndex3 != valIndex2) {
                                    val position1 = rowColumnToCell(row, c1)
                                    val position2 = rowColumnToCell(row, c2)
                                    val valPos1 = getPossibilityIndex(valIndex3, position1)
                                    val valPos2 = getPossibilityIndex(valIndex3, position2)
                                    if (possibilities[valPos1] == 0) {
                                        possibilities[valPos1] = round
                                        doneSomething = true
                                    }
                                    if (possibilities[valPos2] == 0) {
                                        possibilities[valPos2] = round
                                        doneSomething = true
                                    }
                                }
                            }
                            if (doneSomething) {
                                if (logHistory || recordHistory) {
                                    addHistoryItem(
                                        LogItem(
                                            round,
                                            LogType.HIDDEN_PAIR_ROW,
                                            valIndex + 1,
                                            rowColumnToCell(row, c1)
                                        )
                                    )
                                }
                                return true
                            }
                        }
                    }
                }
            }
        }
        return false
    }

    @Suppress("CyclomaticComplexMethod", "NestedBlockDepth", "LongMethod", "ReturnCount")
    private fun handleNakedPairs(round: Int): Boolean {
        for (position in 0 until BOARD_SIZE) {
            val possibilities = countPossibilities(position)
            if (possibilities == 2) {
                val row = cellToRow(position)
                val column = cellToColumn(position)
                val section = cellToSectionStartCell(position)
                for (position2 in position until BOARD_SIZE) {
                    if (position != position2) {
                        val possibilities2 = countPossibilities(position2)
                        if (possibilities2 == 2 && arePossibilitiesSame(position, position2)) {
                            if (row == cellToRow(position2)) {
                                var doneSomething = false
                                for (column2 in 0 until ROW_COL_SEC_SIZE) {
                                    val position3 = rowColumnToCell(row, column2)
                                    if (position3 != position &&
                                        position3 != position2 &&
                                        removePossibilitiesInOneFromTwo(
                                            position,
                                            position3,
                                            round
                                        )
                                    ) {
                                        doneSomething = true
                                    }
                                }
                                if (doneSomething) {
                                    if (logHistory || recordHistory) {
                                        addHistoryItem(
                                            LogItem(
                                                round,
                                                LogType.NAKED_PAIR_ROW,
                                                0,
                                                position
                                            )
                                        )
                                    }
                                    return true
                                }
                            }
                            if (column == cellToColumn(position2)) {
                                var doneSomething = false
                                for (row2 in 0 until ROW_COL_SEC_SIZE) {
                                    val position3 = rowColumnToCell(row2, column)
                                    if (position3 != position &&
                                        position3 != position2 &&
                                        removePossibilitiesInOneFromTwo(
                                            position,
                                            position3,
                                            round
                                        )
                                    ) {
                                        doneSomething = true
                                    }
                                }
                                if (doneSomething) {
                                    if (logHistory || recordHistory) {
                                        addHistoryItem(
                                            LogItem(
                                                round,
                                                LogType.NAKED_PAIR_COLUMN,
                                                0,
                                                position
                                            )
                                        )
                                    }
                                    return true
                                }
                            }
                            if (section == cellToSectionStartCell(position2)) {
                                var doneSomething = false
                                val secStart = cellToSectionStartCell(position)
                                for (i in 0 until GRID_SIZE_COL) {
                                    for (j in 0 until GRID_SIZE_ROW) {
                                        val position3 = secStart + i + ROW_COL_SEC_SIZE * j
                                        if (position3 != position &&
                                            position3 != position2 &&
                                            removePossibilitiesInOneFromTwo(
                                                position,
                                                position3,
                                                round
                                            )
                                        ) {
                                            doneSomething = true
                                        }
                                    }
                                }
                                if (doneSomething) {
                                    if (logHistory || recordHistory) {
                                        addHistoryItem(
                                            LogItem(
                                                round,
                                                LogType.NAKED_PAIR_SECTION,
                                                0,
                                                position
                                            )
                                        )
                                    }
                                    return true
                                }
                            }
                        }
                    }
                }
            }
        }
        return false
    }

    /**
     * Mark exactly one cell which is the only possible value for some row, if
     * such a cell exists. This method will look in a row for a possibility that
     * is only listed for one cell. This type of cell is often called a
     * "hidden single"
     * CHECKED!
     */
    @Suppress("NestedBlockDepth")
    private fun onlyValueInRow(round: Int): Boolean {
        for (row in 0 until ROW_COL_SEC_SIZE) {
            for (valIndex in 0 until ROW_COL_SEC_SIZE) {
                var count = 0
                var lastPosition = 0
                for (col in 0 until ROW_COL_SEC_SIZE) {
                    val position = row * ROW_COL_SEC_SIZE + col
                    val valPos = getPossibilityIndex(valIndex, position)
                    if (possibilities[valPos] == 0) {
                        count++
                        lastPosition = position
                    }
                }
                if (count == 1) {
                    val value = valIndex + 1
                    if (logHistory || recordHistory) {
                        addHistoryItem(
                            LogItem(
                                round,
                                LogType.HIDDEN_SINGLE_ROW,
                                value,
                                lastPosition
                            )
                        )
                    }
                    mark(lastPosition, round, value)
                    return true
                }
            }
        }
        return false
    }

    /**
     * Mark exactly one cell which is the only possible value for some column,
     * if such a cell exists. This method will look in a column for a
     * possibility that is only listed for one cell. This type of cell is often
     * called a "hidden single"
     * CHECKED!
     */
    @Suppress("NestedBlockDepth")
    private fun onlyValueInColumn(round: Int): Boolean {
        for (col in 0 until ROW_COL_SEC_SIZE) {
            for (valIndex in 0 until ROW_COL_SEC_SIZE) {
                var count = 0
                var lastPosition = 0
                for (row in 0 until ROW_COL_SEC_SIZE) {
                    val position = rowColumnToCell(row, col)
                    val valPos = getPossibilityIndex(valIndex, position)
                    if (possibilities[valPos] == 0) {
                        count++
                        lastPosition = position
                    }
                }
                if (count == 1) {
                    val value = valIndex + 1
                    if (logHistory || recordHistory) {
                        addHistoryItem(
                            LogItem(
                                round,
                                LogType.HIDDEN_SINGLE_COLUMN,
                                value,
                                lastPosition
                            )
                        )
                    }
                    mark(lastPosition, round, value)
                    return true
                }
            }
        }
        return false
    }

    /**
     * Mark exactly one cell which is the only possible value for some section,
     * if such a cell exists. This method will look in a section for a
     * possibility that is only listed for one cell. This type of cell is often
     * called a "hidden single"
     * Checked!
     */
    @Suppress("NestedBlockDepth")
    private fun onlyValueInSection(round: Int): Boolean {
        for (sec in 0 until ROW_COL_SEC_SIZE) {
            val secPos = sectionToFirstCell(sec)
            for (valIndex in 0 until ROW_COL_SEC_SIZE) {
                var count = 0
                var lastPosition = 0
                for (i in 0 until GRID_SIZE_COL) {
                    for (j in 0 until GRID_SIZE_ROW) {
                        val position = secPos + i + ROW_COL_SEC_SIZE * j
                        val valPos = getPossibilityIndex(valIndex, position)
                        if (possibilities[valPos] == 0) {
                            count++
                            lastPosition = position
                        }
                    }
                }
                if (count == 1) {
                    val value = valIndex + 1
                    if (logHistory || recordHistory) {
                        addHistoryItem(
                            LogItem(
                                round,
                                LogType.HIDDEN_SINGLE_SECTION,
                                value,
                                lastPosition
                            )
                        )
                    }
                    mark(lastPosition, round, value)
                    return true
                }
            }
        }
        return false
    }

    /**
     * Mark exactly one cell that has a single possibility, if such a cell
     * exists. This method will look for a cell that has only one possibility.
     * This type of cell is often called a "single"
     * Checked!
     */
    @Suppress("NestedBlockDepth")
    private fun onlyPossibilityForCell(round: Int): Boolean {
        for (position in 0 until BOARD_SIZE) {
            if (solution[position] == 0) {
                var count = 0
                var lastValue = 0
                for (valIndex in 0 until ROW_COL_SEC_SIZE) {
                    val valPos = getPossibilityIndex(valIndex, position)
                    if (possibilities[valPos] == 0) {
                        count++
                        lastValue = valIndex + 1
                    }
                }
                if (count == 1) {
                    mark(position, round, lastValue)
                    if (logHistory || recordHistory) {
                        addHistoryItem(
                            LogItem(
                                round,
                                LogType.SINGLE,
                                lastValue,
                                position
                            )
                        )
                    }
                    return true
                }
            }
        }
        return false
    }

    /**
     * Mark the given value at the given position. Go through the row, column,
     * and section for the position and remove the value from the possibilities.
     *
     * @param position Position into the board (0-80)
     * @param round    Round to mark for rollback purposes
     * @param value    The value to go in the square at the given position
     * Checked!
     */
    private fun mark(
        position: Int,
        round: Int,
        value: Int
    ) {
        require(solution[position] == 0) { "Marking position that already has been marked." }
        require(solutionRound[position] == 0) { "Marking position that was marked another round." }
        var valIndex = value - 1
        solution[position] = value
        val possInd = getPossibilityIndex(valIndex, position)
        require(possibilities[possInd] == 0) { "Marking impossible position." }

        // Take this value out of the possibilities for everything in the row
        solutionRound[position] = round
        val rowStart = cellToRow(position) * ROW_COL_SEC_SIZE
        for (col in 0 until ROW_COL_SEC_SIZE) {
            val rowVal = rowStart + col
            val valPos = getPossibilityIndex(valIndex, rowVal)
            if (possibilities[valPos] == 0) {
                possibilities[valPos] = round
            }
        }

        // Take this value out of the possibilities for everything in the column
        val colStart = cellToColumn(position)
        for (i in 0 until ROW_COL_SEC_SIZE) {
            val colVal = colStart + ROW_COL_SEC_SIZE * i
            val valPos = getPossibilityIndex(valIndex, colVal)
            if (possibilities[valPos] == 0) {
                possibilities[valPos] = round
            }
        }

        // Take this value out of the possibilities for everything in section
        val secStart = cellToSectionStartCell(position)
        for (i in 0 until GRID_SIZE_COL) {
            for (j in 0 until GRID_SIZE_ROW) {
                val secVal = secStart + i + ROW_COL_SEC_SIZE * j
                val valPos = getPossibilityIndex(valIndex, secVal)
                if (possibilities[valPos] == 0) {
                    possibilities[valPos] = round
                }
            }
        }

        // This position itself is determined, it should have possibilities.
        valIndex = 0
        while (valIndex < ROW_COL_SEC_SIZE) {
            val valPos = getPossibilityIndex(valIndex, position)
            if (possibilities[valPos] == 0) {
                possibilities[valPos] = round
            }
            valIndex++
        }
    }

    /**
     * Given a vector of LogItems, determine how many log items in the vector
     * are of the specified type.
     */
    private fun getLogCount(
        v: ArrayList<LogItem?>,
        type: LogType
    ): Int {
        var count = 0
        for (i in v.indices) {
            if (v[i]!!.type == type) count++
        }
        return count
    }

    public companion object {
        private val NEW_LINE = System.getProperties().getProperty("line.separator")

        public var GRID_SIZE_ROW: Int = 3
        public var GRID_SIZE_COL: Int = 3
        public var ROW_COL_SEC_SIZE: Int = GRID_SIZE_ROW * GRID_SIZE_COL
        public var SEC_GROUP_SIZE: Int = ROW_COL_SEC_SIZE * GRID_SIZE_ROW
        public var BOARD_SIZE: Int = ROW_COL_SEC_SIZE * ROW_COL_SEC_SIZE
        public var POSSIBILITY_SIZE: Int = BOARD_SIZE * ROW_COL_SEC_SIZE
        private var random = Random()

        private fun fillIncrementing(arr: IntArray): IntArray {
            for (i in arr.indices) {
                arr[i] = i
            }
            return arr
        }

        /**
         * Shuffle the values in an array of integers.
         */
        private fun shuffleArray(
            array: IntArray,
            size: Int
        ) {
            for (i in 0 until size) {
                val tailSize = size - i
                val randTailPos = abs(random.nextInt()) % tailSize + i
                val temp = array[i]
                array[i] = array[randTailPos]
                array[randTailPos] = temp
            }
        }

        // not the first and last value which are NONE and RANDOM
        private val randomSymmetry: Symmetry
            get() {
                val values = Symmetry.entries.toTypedArray()
                // not the first and last value which are NONE and RANDOM
                return values[abs(random.nextInt()) % (values.size - 1) + 1]
            }

        /**
         * Given the index of a cell (0-80) calculate the column (0-8) in which that
         * cell resides.
         * Checked!
         */
        public fun cellToColumn(cell: Int): Int = cell % ROW_COL_SEC_SIZE

        /**
         * Given the index of a cell (0-80) calculate the row (0-8) in which it
         * resides.
         * Checked!
         */
        public fun cellToRow(cell: Int): Int = cell / ROW_COL_SEC_SIZE

        /**
         * Given the index of a cell (0-80) calculate the section (0-8) in which it
         * resides.
         * Checked!
         */
        public fun cellToSection(cell: Int): Int =
            cell / SEC_GROUP_SIZE * GRID_SIZE_ROW + cellToColumn(cell) / GRID_SIZE_COL

        /**
         * Given the index of a cell (0-80) calculate the cell (0-80) that is the
         * upper left start cell of that section.
         * Checked!
         */
        public fun cellToSectionStartCell(cell: Int): Int =
            cell / SEC_GROUP_SIZE * SEC_GROUP_SIZE + cellToColumn(cell) / GRID_SIZE_COL * GRID_SIZE_COL

        /**
         * Given a row (0-8) calculate the first cell (0-80) of that row.
         * Checked!
         */
        public fun rowToFirstCell(row: Int): Int = ROW_COL_SEC_SIZE * row

        /**
         * Given a column (0-8) calculate the first cell (0-80) of that column.
         * Checked!
         */
        public fun columnToFirstCell(column: Int): Int = column

        /**
         * Given a section (0-8) calculate the first cell (0-80) of that section.
         * Checked!
         */
        public fun sectionToFirstCell(section: Int): Int =
            section % GRID_SIZE_ROW * GRID_SIZE_COL + section / GRID_SIZE_ROW * SEC_GROUP_SIZE

        /**
         * Given a value for a cell (0-8) and a cell number (0-80) calculate the
         * offset into the possibility array (0-728).
         * Checked!
         */
        public fun getPossibilityIndex(
            valueIndex: Int,
            cell: Int
        ): Int = valueIndex + ROW_COL_SEC_SIZE * cell

        /**
         * Given a row (0-8) and a column (0-8) calculate the cell (0-80).
         * Checked!
         */
        public fun rowColumnToCell(
            row: Int,
            column: Int
        ): Int = row * ROW_COL_SEC_SIZE + column

        /**
         * Given a section (0-8) and an offset into that section (0-8) calculate the
         * cell (0-80)
         * Checked!
         */
        public fun sectionToCell(
            section: Int,
            offset: Int
        ): Int = sectionToFirstCell(section) + offset / GRID_SIZE_COL * ROW_COL_SEC_SIZE + offset % GRID_SIZE_COL
    }
}
