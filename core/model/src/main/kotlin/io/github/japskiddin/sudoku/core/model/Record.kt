package io.github.japskiddin.sudoku.core.model

public data class Record(
    val uid: Long = ID_NONE,
    val time: Long,
) {
    public companion object {
        public const val ID_NONE: Long = 0L
    }
}
