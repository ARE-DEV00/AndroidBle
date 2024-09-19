package kr.co.are.androidble.data.local.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kr.co.are.androidble.data.local.room.converter.DateTypeConverter
import kr.co.are.androidble.data.local.room.dao.GlucoseInfoDao
import kr.co.are.androidble.data.local.room.entity.TableGlucoseInfoEntity

@Database(
    entities = [TableGlucoseInfoEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DateTypeConverter::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun glucoseInfoDao(): GlucoseInfoDao
}