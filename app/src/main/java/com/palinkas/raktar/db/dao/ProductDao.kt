package com.palinkas.raktar.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
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

    @Query("""SELECT * FROM products where id == :id""")
    abstract fun get(id: Int): LiveData<Product>

    @Query("""SELECT * FROM products""")
    abstract fun getAll(): LiveData<List<Product>>

    @Delete
    abstract fun deleteProduct(p: List<Product>)
}