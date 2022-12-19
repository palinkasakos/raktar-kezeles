package com.palinkas.raktar.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id

@Entity(tableName = OwnCompany.TABLE_NAME)
class OwnCompany (
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String = "",
    @ColumnInfo(name = "name")
    var name: String = ""
){
    companion object {
        const val TABLE_NAME = "own_companies"
    }
}