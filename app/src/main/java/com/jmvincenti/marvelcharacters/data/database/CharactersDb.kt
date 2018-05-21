package com.jmvincenti.marvelcharacters.data.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.jmvincenti.marvelcharacters.data.model.Character

/**
 * TODO: Add a class header comment! 😘
 */

@Database(entities = [Character::class], version = 1)
@TypeConverters(Converters::class)
abstract class CharactersDb : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
}