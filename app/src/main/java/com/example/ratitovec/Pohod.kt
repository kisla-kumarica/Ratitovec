package com.example.ratitovec

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.jetbrains.annotations.NotNull
import java.util.*

@Entity(tableName = "pohodi")
class Pohod(var Datum: Long) {
    @PrimaryKey(autoGenerate = true)
    @NotNull
    var id: Int = 0

    override fun equals(other: Any?): Boolean {
        return Datum == (other as Pohod).Datum
    }
}