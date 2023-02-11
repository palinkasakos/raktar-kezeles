package com.palinkas.raktar.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import com.palinkas.raktar.db.entities.Storage

@Dao
abstract class StorageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertAll(items: List<Storage>)

    @Query("DELETE FROM storage")
    abstract fun deleteAll()

    @Query("""SELECT * FROM storage""")
    abstract fun getAll(): LiveData<List<Storage>>

    @Insert(onConflict = IGNORE)
    abstract fun insert(storage: Storage) : Long

    @Update
    abstract fun update(storage: Storage)

    open fun insertOrUpdate(item: Storage){
        if (insert(item) == "-1".toLong()){
            update(item)
        }
    }

    @Delete
    abstract fun deleteStorages(p: List<Storage>)
}