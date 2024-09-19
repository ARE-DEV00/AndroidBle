package kr.co.are.androidble.data.local.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import kr.co.are.androidble.data.local.room.AppDataBaseKey
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset

@Entity(tableName = AppDataBaseKey.TABLE_GLUCOSE_INFO)
data class TableGlucoseInfoEntity(
    @PrimaryKey(autoGenerate = true)
    val key: Long = 0,

    val type: String,
    val serviceUuid: String,
    val glucoseLevel: Int,
    val unit: String,
    val time: String,
    val rawData: String?,

    var createdTime: LocalDateTime = LocalDateTime.now(ZoneId.from(ZoneOffset.UTC)),
    var modifiedTime: LocalDateTime = LocalDateTime.now(ZoneId.from(ZoneOffset.UTC))

)

