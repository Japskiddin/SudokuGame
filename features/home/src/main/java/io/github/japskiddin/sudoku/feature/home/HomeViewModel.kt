package io.github.japskiddin.sudoku.feature.home

import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.japskiddin.sudoku.core.game.model.BoardCell
import io.github.japskiddin.sudoku.core.game.qqwing.GameDifficulty
import io.github.japskiddin.sudoku.core.game.qqwing.GameType
import io.github.japskiddin.sudoku.core.game.qqwing.QQWingController
import io.github.japskiddin.sudoku.core.game.utils.SudokuParser
import io.github.japskiddin.sudoku.data.model.Board
import io.github.japskiddin.sudoku.feature.home.usecase.CreateBoardUseCase
import io.github.japskiddin.sudoku.navigation.AppNavigator
import io.github.japskiddin.sudoku.navigation.Destination
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import javax.inject.Inject
import javax.inject.Provider

@HiltViewModel
internal class HomeViewModel @Inject constructor(
  private val appNavigator: AppNavigator,
  private val createBoardUseCase: Provider<CreateBoardUseCase>,
) : ViewModel() {
  private val _uiState = MutableStateFlow(UiState.Initial)
  val uiState: StateFlow<UiState>
    get() = _uiState.asStateFlow()

  val currentYear: String
    get() = Calendar.getInstance().get(Calendar.YEAR).toString()

  fun onStartClick() {
    generateNewBoard()
  }

  fun onSettingsClick() {
    TODO("In Development")
  }

  fun onRecordsClick() {
    TODO("In Development")
  }

  private fun generateNewBoard() {
    val selectedType = GameType.DEFAULT9X9
    val selectedDifficulty = GameDifficulty.INTERMEDIATE

    viewModelScope.launch(Dispatchers.IO) {
      _uiState.update { UiState.Loading(R.string.preparing_game_please_wait) }

      val puzzle = List(selectedType.size) { row ->
        List(selectedType.size) { col -> BoardCell(row, col, 0) }
      }
      val solvedPuzzle = List(selectedType.size) { row ->
        List(selectedType.size) { col -> BoardCell(row, col, 0) }
      }
      val qqWingController = QQWingController()
      val generatedBoard =
        qqWingController.generate(selectedType, selectedDifficulty) ?: return@launch
      val solvedBoard = qqWingController.solve(generatedBoard, selectedType)

      if (qqWingController.isImpossible || qqWingController.solutionCount != 1) {
        // TODO сообщить об ошибке
        return@launch
      }

      for (i in 0 until selectedType.size) {
        for (j in 0 until selectedType.size) {
          puzzle[i][j].value = generatedBoard[i * selectedType.size + j]
          solvedPuzzle[i][j].value = solvedBoard[i * selectedType.size + j]
        }
      }

      withContext(Dispatchers.IO) {
        val sudokuParser = SudokuParser()
        val board = Board(
          initialBoard = sudokuParser.boardToString(puzzle),
          solvedBoard = sudokuParser.boardToString(solvedPuzzle),
          difficulty = selectedDifficulty,
          type = selectedType,
        )
        val insertedBoardUid = createBoardUseCase.get().invoke(board)
        navigateToGame(insertedBoardUid)
      }

      _uiState.update { UiState.Menu }
    }
  }

  private fun navigateToGame(boardUid: Long) {
    appNavigator.tryNavigateTo(Destination.GameScreen(boardUid = boardUid))
  }
}

@Immutable
internal sealed class UiState {
  @Immutable
  class Loading(@StringRes val message: Int) : UiState()

  @Immutable
  data object Menu : UiState()

  companion object {
    val Initial: UiState = Menu
  }
}