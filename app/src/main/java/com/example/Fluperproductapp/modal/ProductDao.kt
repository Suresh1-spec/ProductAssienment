package com.example.Fluperproductapp.modal

import androidx.room.*

@Dao
interface ProductDao {

    @Query("SELECT * FROM product")
    open fun getAll(): MutableList<Product?>?

    @Insert
    fun insert(task: Product?)

    @Delete
    fun delete(task: Product?)

    @Update
    fun update(task: Product?)

}