package kr.co.are.androidble.data.local.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kr.co.are.androidble.data.local.room.entity.TableGlucoseInfoEntity

@Dao
interface GlucoseInfoDao {

    //전체 혈당정보 조회
    @Query("SELECT * FROM table_glucose_info ORDER BY createdTime DESC")
    fun selectAllGlucoseInfo(): List<TableGlucoseInfoEntity>

    //혈당정보 추가
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGlucoseInfoInfo(entity: TableGlucoseInfoEntity)

    //전체 삭제
    @Query("DELETE FROM table_glucose_info")
    fun deleteAllGlucoseInfoInfo()


}
