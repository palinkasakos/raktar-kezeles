package com.palinkas.raktar.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id
import java.util.UUID

@Entity(tableName = OwnCompany.TABLE_NAME)
class OwnCompany (
    @PrimaryKey
    @ColumnInfo(name = "oid")
    var oid: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "name")
    var name: String = ""
){
    companion object {
        const val TABLE_NAME = "own_companies"
    }
}