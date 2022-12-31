package com.kigya.unique.data.dto.lesson

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(
    tableName = "lessons"
)
data class Lesson(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @Expose
    val id: Int? = null,

    @SerializedName("course")
    @Expose
    val course: Int,

    @SerializedName("group")
    @Expose
    val group: Int,

    @SerializedName("day")
    @Expose
    val day: String,

    @SerializedName("time")
    @Expose
    val time: String,

    @SerializedName("subgroup")
    @Expose
    val subgroup: String?,

    @SerializedName("regularity")
    @Expose
    val regularity: String?,

    @SerializedName("subject")
    @Expose
    val subject: String,

    @SerializedName("teacher")
    @Expose
    val teacher: String,

    @SerializedName("type")
    @Expose
    val type: String?,

    @SerializedName("audience")
    @Expose
    val audience: String?
) : Serializable