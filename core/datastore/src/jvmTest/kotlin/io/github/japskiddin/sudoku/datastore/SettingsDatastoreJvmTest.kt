package io.github.japskiddin.sudoku.datastore

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import io.github.japskiddin.sudoku.datastore.model.GameModeDSO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import java.nio.file.Files
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

class SettingsDatastoreJvmTest {
    @Test
    fun preferencesSurviveReopeningAndRespectSaveGameMode() = runTest {
        val directory = Files.createTempDirectory("sudoku-settings-test").toFile()
        val file = directory.resolve("settings.preferences_pb")
        val mode = GameModeDSO(difficulty = 3, type = 2)
        val firstJob = SupervisorJob()
        val secondJob = SupervisorJob()
        fun create(job: kotlinx.coroutines.Job): SettingsDatastore = SettingsDatastore(
            PreferenceDataStoreFactory.create(
                scope = CoroutineScope(Dispatchers.IO + job),
                produceFile = { file },
            )
        )
        try {
            val settings = create(firstJob)
            settings.setShowTimer(false)
            settings.setGameMode(mode)
            firstJob.cancelAndJoin()

            val reopened = create(secondJob)
            assertFalse(reopened.isShowTimer.first())
            assertEquals(mode, reopened.gameMode.first())
            reopened.setSaveGameMode(false)
            assertNull(reopened.gameMode.first())
        } finally {
            firstJob.cancelAndJoin()
            secondJob.cancelAndJoin()
            directory.deleteRecursively()
        }
    }
}
