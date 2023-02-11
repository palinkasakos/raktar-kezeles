package com.palinkas.raktar.db.entities

import androidx.room.*
import java.util.*

@Entity(
    tableName = Storage.TABLE_NAME
)
data class Storage(
    @PrimaryKey
    @ColumnInfo(name = "oid")
    val oid: String = UUID.randomUUID().toString(),
    /**
     * Raktár neve
     */
    @ColumnInfo(name = "name")
    val name: String = "",

    @ColumnInfo(name = "default_usage")
    val defaultUsage: Boolean = false
) {

    /**
     * Spinnerben amikor ez kerül megjelenítésre
     */
    override fun toString(): String {
        if (oid.isEmpty()) return "" //dummy item for chooser dialog

        return name
    }

    fun isDummyItem(): Boolean = oid.isEmpty()

    companion object {
        const val TABLE_NAME = "storage"

        @JvmStatic
        fun createDummyItem(): Storage {
            return Storage("", "")
        }
    }
}