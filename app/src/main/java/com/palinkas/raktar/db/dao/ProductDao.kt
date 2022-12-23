package com.palinkas.raktar.db.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import com.palinkas.raktar.db.entities.Product

@Dao
abstract class ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(items: List<Product>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(item: Product)

    @Query("DELETE FROM products")
    abstract fun deleteAll()

    @Transaction
    open fun deleteAndInsertAll(items: List<Product>) {
        deleteAll()
        insertAll(items)
    }

    @Query("""SELECT * FROM products""")
    abstract fun getAll(): LiveData<List<Product>>

    @Delete
    abstract fun deleteProduct(p: List<Product>)
}