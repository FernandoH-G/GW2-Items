package com.fhg.gw2market.room

import android.util.Log
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import com.fhg.gw2market.GetItemByIDQuery
import com.fhg.gw2market.GetItemNamesQuery

// AKA GetItemByIDQuery.Data
data class GW2Item(
    val id: String,
    val name: String,
    val imgURL: String,
    val sell: GetItemByIDQuery.Sell,
    val buy: GetItemByIDQuery.Buy,
    val description: String,
    val type: String,
    val rarity: String,
    val level: String
)

object ApolloClientNetwork {
    // 10.0.2.2 to target computer localhost, not emulator localhost.
    private const val URL = "http://206.189.68.62:5001/query"
    val apolloClient: ApolloClient by lazy {
        ApolloClient.builder().serverUrl(URL).build()
    }
}

// Look at Dagger for dependency injections.
// The repo handles whether to fetch data from a network, cache, or the database.
class ItemRepository(
    private val itemDao: ItemDao
) {

    // GetItemNamesQuery.Data == List{id :String, name:String}
    suspend fun loadEquipmentMap(): Response<GetItemNamesQuery.Data>? {
        return try {
            val inQuery = GetItemNamesQuery()
            ApolloClientNetwork.apolloClient.query(inQuery).await()
        } catch (e: ApolloException) {
            Log.d("repo", "loadEquipmentMap() error: $e")
            null
        }
    }

    suspend fun getItem(id: String): Response<GetItemByIDQuery.Data> {
        val itemQuery = GetItemByIDQuery(id)
        return ApolloClientNetwork.apolloClient.query(itemQuery).await()
    }

//    val allItemsRepo: LiveData<List<Item>> = itemDao.getAllItems()
//    suspend fun insertItem(item: Item) {
//        itemDao.insertItem(item)
//    }
//    val Item: LiveData<Item> = itemDao.getItem( )
}