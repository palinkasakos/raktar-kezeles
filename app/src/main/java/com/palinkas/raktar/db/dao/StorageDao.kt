package com.palinkas.raktar.db.dao

import androidx.room.*
import com.palinkas.raktar.db.entities.Storage

@Dao
abstract class StorageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(items: List<Storage>)

    @Query("DELETE FROM storage")
    abstract fun deleteAll()

    @Query("""SELECT * FROM storage""")
    abstract fun getAll(): List<Storage>

    @Delete
    abstract fun deleteProduct(p: List<Storage>)
}