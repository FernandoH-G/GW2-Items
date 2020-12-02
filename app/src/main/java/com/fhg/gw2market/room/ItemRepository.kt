package com.fhg.gw2market.room

import android.content.Context
import androidx.lifecycle.LiveData
import com.android.volley.Request
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Is this where I make my network calls?
// Database calls?
// The repo handles whether to fetch data from a network, cache, or the database.
class ItemRepository (private val itemDao: ItemDao, private val context: Context) {

//    val allItemsRepo: LiveData<List<Item>> = itemDao.getAllItems()

//    suspend fun insertItem(item: Item) {
//        itemDao.insertItem(item)
//    }

    suspend fun fetchItems() {
        withContext(Dispatchers.IO) {
            //Fetch and save into database
            val queue = Volley.newRequestQueue(context)
        }
    }

//    val Item: LiveData<Item> = itemDao.getItem( )
}