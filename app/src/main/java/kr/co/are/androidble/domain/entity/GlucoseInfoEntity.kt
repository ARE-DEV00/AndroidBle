package kr.co.are.androidble.domain.entity

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GlucoseInfoEntity(
    val key: Long? = null,

    val type: String,
    @Json(name = "service_uuid")
    val serviceUuid: String,
    @Json(name = "glucose_level")
    val glucoseLevel: Int,
    val unit: String,
    val time: String,

    var rawData: String? = null,

    var createdTime: String? = null,
    var modifiedTime: String? = null
)