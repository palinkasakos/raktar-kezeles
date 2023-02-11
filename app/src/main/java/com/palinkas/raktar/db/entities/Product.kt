package com.palinkas.raktar.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal
import java.util.*

@Entity(
    tableName = Product.TABLE_NAME
)
class Product(
    @PrimaryKey()
    @ColumnInfo(name = "oid")
    var oid: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "product_number")
    var productNumber: String = "",
    @ColumnInfo(name = "name")
    var name: String? = null,
    @ColumnInfo(name = "unit_id")
    var unitId: String? = null,
    @ColumnInfo(name = "barcode")
    var barcode: String? = null,
    @ColumnInfo(name = "gross_weight")
    var grossWeight: BigDecimal? = null,
    @ColumnInfo(name = "product_code")
    var productCode: String? = null
) {
    companion object {
        const val TABLE_NAME = "products"
    }

    override fun equals(other: Any?): Boolean {
        return super.equals(other)
    }
}