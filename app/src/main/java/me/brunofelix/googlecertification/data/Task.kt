package me.brunofelix.googlecertification.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class Task(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "state")
    val state: String = TaskStatusEnum.TODO.name,

    @ColumnInfo(name = "date")
    val date: Long,

    @ColumnInfo(name = "duration")
    val duration: Long = 0

) : Parcelable
