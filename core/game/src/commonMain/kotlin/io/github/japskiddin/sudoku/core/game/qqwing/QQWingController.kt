package io.github.japskiddin.sudoku.core.game.qqwing

import io.github.japskiddin.sudoku.core.model.GameDifficulty
import io.github.japskiddin.sudoku.core.model.GameType

public expect class QQWingController() {
    public val options: QQWingOptions
    public var isImpossible: Boolean
        private set
    public var solutionCount: Int
    public fun generate(type: GameType, difficulty: GameDifficulty): IntArray?
    public fun solve(gameBoard: IntArray?, type: GameType): IntArray

    @Suppress("EmptyDefaultConstructor")
    public class QQWingOptions() {
        public var needNow: Boolean
        public var printSolution: Boolean
        public var printHistory: Boolean
        public var printInstructions: Boolean
        public var action: Action
        public var logHistory: Boolean
        public var printStyle: PrintStyle
        public var numberToGenerate: Int
        public var printStats: Boolean
        public var gameDifficulty: GameDifficulty
        public var gameType: GameType
        public var symmetry: Symmetry
        public var threads: Int
    }
}
