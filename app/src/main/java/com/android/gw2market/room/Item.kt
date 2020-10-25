package com.android.gw2market.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

@Fts4
@Entity(tableName = "item_table")
data class Item(
    @PrimaryKey @ColumnInfo(name = "rowid") val id: Int,
    val name: String,
    val imgURL: String,
    val type: String,
    val level: String,
    val description: String,
    val rarity: String
)