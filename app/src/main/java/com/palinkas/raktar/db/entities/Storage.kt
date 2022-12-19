package com.palinkas.raktar.db.entities

import androidx.room.*

@Entity(
    tableName = Storage.TABLE_NAME
)
data class Storage(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    /**
     * Telephely id
     */
    @ColumnInfo(name = "company_site_id")
    val companySiteId: String,
    /**
     * Raktár neve
     */
    @ColumnInfo(name = "name")
    val name: String,
    /**
     * Sajátcég azon
     */
    @ColumnInfo(name = "own_company_id")
    val ownCompanyId: String? = null,

    @ColumnInfo(name = "default_usage")
    val defaultUsage: Boolean = false
) {

    /**
     * Spinnerben amikor ez kerül megjelenítésre
     */
    override fun toString(): String {
        if (id.isEmpty()) return "" //dummy item for chooser dialog

        return name
    }

    fun isDummyItem(): Boolean = id.isEmpty()

    companion object {
        const val TABLE_NAME = "storage"

        @JvmStatic
        fun createDummyItem(): Storage {
            return Storage("", "", "")
        }
    }
}