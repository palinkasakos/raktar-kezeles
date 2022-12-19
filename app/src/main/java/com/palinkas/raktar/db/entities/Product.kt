package com.palinkas.raktar.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.math.BigDecimal

@Entity(
    tableName = Product.TABLE_NAME
)
class Product(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: String = "",
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
}