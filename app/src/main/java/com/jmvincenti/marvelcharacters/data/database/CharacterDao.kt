package com.jmvincenti.marvelcharacters.data.database

import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.jmvincenti.marvelcharacters.data.model.Character
import io.reactivex.Single

/**
 * TODO: Add a class header comment! 😘
 */

@Dao
interface CharacterDao {

    @Query("SELECT * FROM character ORDER BY name ASC")
    fun characterByName(): DataSource.Factory<Int, Character>

    @Query("SELECT id FROM character ORDER BY name ASC")
    fun getCount(): Single<List<Character>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertOrUpdate(items: List<Character>)
}