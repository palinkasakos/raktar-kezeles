package com.palinkas.raktar.db.dao

import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import com.palinkas.raktar.db.entities.OwnCompany
import com.palinkas.raktar.db.entities.Storage

@Dao
abstract class OwnCompanyDao{

    @Query("DELETE FROM own_companies")
    abstract fun deleteAll()

    @Query("""SELECT * FROM own_companies""")
    abstract fun getAll(): List<OwnCompany>

    @Insert(onConflict = IGNORE)
    abstract fun insert(ownCompany: OwnCompany) : Long

    @Update
    abstract fun update(ownCompany: OwnCompany)

    open fun insertOrUpdate(item: OwnCompany){
        if (insert(item) == "-1".toLong()){
            update(item)
        }
    }

    @Delete
    abstract fun deleteOwnCompanies(p: List<OwnCompany>)

    @Query("SELECT * FROM OWN_COMPANIES limit 1")
    abstract fun getOne(): OwnCompany
}