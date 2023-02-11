package com.palinkas.raktar.db.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import com.palinkas.raktar.db.entities.Product

@Dao
abstract class ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(items: List<Product>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertOrReplace(item: Product)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract fun insertOrIgnore(item: Product): Long

    @Update
    abstract fun update(item: Product)

    open fun insertOrUpdate(item: Product){
        if (insertOrIgnore(item) == "-1".toLong()){
            update(item)
        }
    }

    @Query("DELETE FROM products")
    abstract fun deleteAll()

    @Transaction
    open fun deleteAndInsertAll(items: List<Product>) {
        deleteAll()
        insertAll(items)
    }

    @Query("""SELECT * FROM products where oid == :oid""")
    abstract fun get(oid: String): LiveData<Product>

    @Query("""SELECT * FROM products""")
    abstract fun getAll(): LiveData<List<Product>>

    @RawQuery(observedEntities = [Product::class])
    abstract fun getAllPaged(simpleSQLiteQuery: SimpleSQLiteQuery): PagingSource<Int, Product>

    @Delete
    abstract fun deleteProduct(p: List<Product>)
}