package io.github.japskiddin.sudoku.core.common

public class BoardNotFoundException(message: String) : Exception(message)

public class SavedGameNotFoundException(message: String) : Exception(message)

public class SudokuNotGeneratedException(message: String = "Error with generating sudoku") : Exception(message)

public class IncorrectGameStatusException(message: String) : Exception(message)

public class IncorrectGameDifficultyException(message: String) : Exception(message)

public class IncorrectGameTypeException(message: String) : Exception(message)
