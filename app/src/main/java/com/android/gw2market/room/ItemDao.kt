package com.android.gw2market.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ItemDao {
    @Query("select *, `rowid` from item_table where name = :iName")
    fun getItem(iName: String): LiveData<Item>

    @Query("select *, `rowid` from item_table")
    fun getAllItems(): LiveData<List<Item>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItem(item:Item)

    @Query("delete from item_table")
    suspend fun deleteAllItems()
}