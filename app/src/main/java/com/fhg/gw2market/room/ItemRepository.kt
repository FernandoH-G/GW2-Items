package com.fhg.gw2market.room

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

data class GW2TPItemNames(
    val items: List<List<Any>>
)

data class GW2TPItem(
    val results: List<List<Any>>
)

data class GW2InfoItem(
    val description: String?,
    val type: String,
    val rarity: String,
    val level: Int
)

interface GW2TPItemNameService {
    @GET("/1/bulk/items-names.json")
    suspend fun getTPItemNames(
    ): GW2TPItemNames
}

interface GW2TPService {
    @GET("/1/items")
    suspend fun getTPItem(
        @Query("ids") id: Int,
        @Query("fields") fields: String
    ): GW2TPItem
}

interface GW2InfoService {
    @GET
    suspend fun getInfoItem(
        @Url url: String,
        @Query("ids") id: Int
    ): List<GW2InfoItem>
}

object RetrofitInstance {
    const val BASE_URL = "http://api.gw2tp.com/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val tpAPI: GW2TPService by lazy {
        retrofit.create(GW2TPService::class.java)
    }

    val itemNameAPI: GW2TPItemNameService by lazy {
        retrofit.create(GW2TPItemNameService::class.java)
    }
    val infoAPI: GW2InfoService by lazy {
        retrofit.create(GW2InfoService::class.java)
    }
}

// The repo handles whether to fetch data from a network, cache, or the database.
class ItemRepository(
    private val itemDao: ItemDao
) {

    suspend fun getTPItem(id: Int, fields: String): GW2TPItem {
        return RetrofitInstance.tpAPI.getTPItem(id, fields)
    }

    suspend fun getInfoItem(url: String, id: Int): List<GW2InfoItem> {
        return RetrofitInstance.infoAPI.getInfoItem(url, id)
    }

    suspend fun loadEquipmentMap(): GW2TPItemNames {
        return RetrofitInstance.itemNameAPI.getTPItemNames()
    }

//    val allItemsRepo: LiveData<List<Item>> = itemDao.getAllItems()
//    suspend fun insertItem(item: Item) {
//        itemDao.insertItem(item)
//    }
//    val Item: LiveData<Item> = itemDao.getItem( )
}