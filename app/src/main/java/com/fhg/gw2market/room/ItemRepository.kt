package com.fhg.gw2market.room

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import java.util.*

// The repo handles whether to fetch data from a network, cache, or the database.
class ItemRepository(
    private val itemDao: ItemDao,
    private val context: Context
) {
    fun loadEquipmentMap(): MutableMap<String, Int> {
        // withContext was not needed.
        val eMap = mutableMapOf<String, Int>()
        val queue = Volley.newRequestQueue(context)
        val tpURL = "http://api.gw2tp.com/1/bulk/items-names.json"

        val jsonRequestTP =
            JsonObjectRequest(Request.Method.GET, tpURL, null,
                { resp ->
                    var name: String
                    var id: Int
                    val respObjArr = resp.getJSONArray("items")
                    for (i in 0 until respObjArr.length()) {
                        id = respObjArr.getJSONArray(i).get(0) as Int
                        name = respObjArr.getJSONArray(i).get(1).toString()
                        eMap[name.toUpperCase(Locale.ROOT)] = id
                    }
                }, { Log.d("itemRepo", "Error in gw2json request.") })
        queue.add(jsonRequestTP)
        return eMap
    }

//    val allItemsRepo: LiveData<List<Item>> = itemDao.getAllItems()
//    suspend fun insertItem(item: Item) {
//        itemDao.insertItem(item)
//    }
//    val Item: LiveData<Item> = itemDao.getItem( )
}