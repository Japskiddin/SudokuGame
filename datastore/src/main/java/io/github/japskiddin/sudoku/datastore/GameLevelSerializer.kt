package io.github.japskiddin.sudoku.datastore

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

internal object GameLevelSerializer : Serializer<GameLevelDSO> {
    private val TAG = GameLevelSerializer.javaClass.simpleName

    override val defaultValue: GameLevelDSO
        get() = GameLevelDSO.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): GameLevelDSO = try {
        GameLevelDSO.parseFrom(input)
    } catch (exception: InvalidProtocolBufferException) {
        Log.e(TAG, "Cannot read proto. Create default.")
        defaultValue
    }

    override suspend fun writeTo(t: GameLevelDSO, output: OutputStream) = t.writeTo(output)
}

internal val Context.gameLevelDatastore: DataStore<GameLevelDSO> by dataStore(
    fileName = "game_level.pb",
    serializer = GameLevelSerializer
)