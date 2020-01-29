package com.example.ratitovec

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface PohodDAO {
    @Insert
    fun insert(pohod: Pohod)

    @Insert
    fun insert(pohodi: List<Pohod>)

    @Delete
    fun delete(pohod: Pohod)

    @Query("SELECT * FROM pohodi WHERE id=:id")
    fun selectWithId(id: Int): Pohod

    @Query("SELECT * FROM pohodi ORDER BY Datum Desc")
    fun selectAll(): DataSource.Factory<Int, Pohod>

    @Query("SELECT * FROM pohodi WHERE strftime('%Y','now')==strftime('%Y', datetime(Datum/1000,'unixepoch')) ORDER BY Datum Desc")
    fun selectThisYear(): DataSource.Factory<Int, Pohod>

}