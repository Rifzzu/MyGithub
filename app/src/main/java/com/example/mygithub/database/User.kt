package com.example.mygithub.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @field:ColumnInfo(name = "id")
    @field:PrimaryKey
    var id: Int,

    @ColumnInfo
    var login: String?,

    @ColumnInfo(name = "imageUrl")
    var imageUrl: String?,
)