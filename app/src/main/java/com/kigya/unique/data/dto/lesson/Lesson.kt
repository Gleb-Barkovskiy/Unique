package com.kigya.unique.data.dto.lesson

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(
    tableName = "lessons"
)
@Parcelize
data class Lesson(
    @PrimaryKey(autoGenerate = true)
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

    @Expose
    val subject: String,

    @Expose
    val teacher: String,

    @Expose
    val type: String?,

    @Expose
    val audience: String?
) : Parcelable