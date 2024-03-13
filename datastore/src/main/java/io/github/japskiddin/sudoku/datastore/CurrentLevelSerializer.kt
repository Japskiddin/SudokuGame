package io.github.japskiddin.sudoku.datastore

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream

object CurrentLevelSerializer : Serializer<CurrentLevel> {
    override val defaultValue: CurrentLevel
        get() = CurrentLevel.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): CurrentLevel = try {
        CurrentLevel.parseFrom(input)
    } catch (exception: InvalidProtocolBufferException) {
        throw CorruptionException("Cannot read proto.", exception)
    }

    override suspend fun writeTo(t: CurrentLevel, output: OutputStream) = t.writeTo(output)
}

val Context.currentLevelDatastore: DataStore<CurrentLevel> by dataStore(
    fileName = "current_level.pb",
    serializer = CurrentLevelSerializer
)