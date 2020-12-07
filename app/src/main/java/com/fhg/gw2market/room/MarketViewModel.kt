package com.fhg.gw2market.room

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.util.*

class Price(
    var gold: String = "0",
    var silver: String = "0",
    var copper: String = "0"
)

data class TPItem(
    val id: String,
    val name: String,
    val sell: Price,
    val buy: Price,
    val imgURL: String
)

data class InfoItem(
    val description: String,
    val type: String,
    val rarity: String,
    val level: String
)

// Refer to Room with a view to remove passing in an application
// in the view model.
class MarketViewModel(application: Application) :
    AndroidViewModel(application) {
    //    private val allItemsVM: LiveData<List<Item>>
    private val mRepository: ItemRepository

    private val _itemID: MutableLiveData<Int> by lazy {
        MutableLiveData()
    }
    val itemID: LiveData<Int> get() = _itemID

    private val _tpItem: MutableLiveData<TPItem> by lazy {
        MutableLiveData()
    }
    val tpItem: LiveData<TPItem> get() = _tpItem

    private val _infoItem: MutableLiveData<InfoItem> by lazy {
        MutableLiveData()
    }
    val infoItem: LiveData<InfoItem> get() = _infoItem

    private val mEMap = mutableMapOf<String, String>()

    init {
        val itemsDao = ItemRoomDatabase.getDatabase(application, viewModelScope)
            .itemDao()
        mRepository = ItemRepository(itemsDao)
        // QUESTION: Why does calling loadEquipmentMap() from init block
        //  not prepare the eMap to be used in time?
        // eMap = repository.loadEquipmentMap()

        // Uncommenting will fix the uninitialized allItemsVM declaration.
        // allItemsVM = repository.allItemsRepo
    }

    fun getTPItem(id: Int, fields: String) {
        viewModelScope.launch {
            // item[id, name, sell, buy, img]
            val item = mRepository.getTPItem(id, fields)
            _tpItem.value = TPItem(
                item.results[0][0].toString().substringBefore('.'),
                item.results[0][1] as String,
                parseNum(item.results[0][2].toString().substringBefore('.')),
                parseNum(item.results[0][3].toString().substringBefore('.')),
                item.results[0][4] as String
            )
        }
    }

    fun getInfoItem(url: String, id: Int) {
        viewModelScope.launch {
            val item = mRepository.getInfoItem(url, id)
            _infoItem.value = InfoItem(
                item[0].description ?: "No Description",
                item[0].type,
                item[0].rarity,
                item[0].level.toString(),
            )
        }
    }

    fun loadEquipmentMap() {
        viewModelScope.launch {
            val itemNameIds = mRepository.loadEquipmentMap()
            var id: String
            var name: String
            itemNameIds.items.forEach {
                id = it[0].toString().substringBefore('.')
                name = it[1].toString()
                mEMap[name.toUpperCase(Locale.ROOT)] = id
            }
        }
    }

    fun isItemNameFound(itemName: String): Boolean {
        // Use map to figure out whether string id is in map.
        val itemID = mEMap[itemName.toUpperCase(Locale.ROOT)] ?: "null"
        Log.i("MVM", "Item Name: $itemName - Item ID: $itemID")
        if (itemID.contentEquals("null")) {
            return false
        }
        _itemID.value = itemID.toInt()
        return true
    }

    fun parseNum(num: String): Price {
        val numLen = num.length
        val price = Price()
        when {
            numLen > 4 -> {
                price.gold = num.subSequence(0, numLen - 4).toString()
                price.silver =
                    num.subSequence(numLen - 4, numLen - 2).toString()
                price.copper = num.subSequence(numLen - 2, numLen).toString()
            }
            numLen > 3 -> {
                price.silver = num.subSequence(0, numLen - 2).toString()
                price.copper = num.subSequence(numLen - 2, numLen).toString()
            }
            numLen > 2 -> {
                price.silver = num[0].toString()
                price.copper = num.subSequence(numLen - 2, numLen).toString()
            }
            numLen > 1 -> {
                price.copper = num.subSequence(numLen - 2, numLen).toString()
            }
            numLen > 0 -> {
                price.copper = num[0].toString()
            }
        }
        return price
    }

    // I don't think I'll be using this method unless I start saving user queries...
//    fun insertItem(item: Item) = viewModelScope.launch(Dispatchers.IO) {
//        repository.insertItem(item)
//    }
}