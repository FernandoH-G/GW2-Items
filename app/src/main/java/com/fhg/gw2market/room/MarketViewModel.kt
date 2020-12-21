package com.fhg.gw2market.room

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import java.util.*

// Refer to Room with a view to remove passing in an application
// in the view model.
class MarketViewModel(application: Application) :
    AndroidViewModel(application) {
    //    private val allItemsVM: LiveData<List<Item>>
    private val mRepository: ItemRepository

    init {
        val itemsDao = ItemRoomDatabase.getDatabase(application, viewModelScope)
            .itemDao()
        mRepository = ItemRepository(itemsDao)

        // Uncommenting will fix the uninitialized allItemsVM declaration.
        // allItemsVM = repository.allItemsRepo
    }

    private val _itemID: MutableLiveData<String> by lazy {
        MutableLiveData()
    }
    val itemID: LiveData<String> get() = _itemID

    private val _item: MutableLiveData<GW2Item> by lazy {
        MutableLiveData()
    }
    val item: LiveData<GW2Item> get() = _item

    private val _eMap: MutableLiveData<MutableMap<String, String>> by lazy {
        MutableLiveData()
    }
    val eMap: LiveData<MutableMap<String, String>> get() = _eMap

    fun getItem(id: String) {
        viewModelScope.launch {
            val itemResp =
                mRepository.getItem(id).data?.getItemByID ?: return@launch
            _item.value = GW2Item(
                id = itemResp.id,
                name = itemResp.name,
                imgURL = itemResp.imgURL,
                sell = itemResp.sell,
                buy = itemResp.buy,
                description = itemResp.description,
                type = itemResp.type,
                rarity = itemResp.rarity,
                level = itemResp.level
            )
        }
    }

    fun loadEquipmentMap() {
        viewModelScope.launch {
            val itemNameIds =
                mRepository.loadEquipmentMap()?.data?.getItemNames
                    ?: return@launch

            Log.d("MVM", "loading eMap")
            var id: String
            var name: String
            val tmpMap = mutableMapOf<String,String>()

            itemNameIds.forEach {
                id = it.id
                name = it.name
                tmpMap[name.toUpperCase(Locale.ROOT)] = id
            }
            _eMap.value = tmpMap
            Log.d("MVM", "Done loading eMap")
        }
        Log.d("MVM", "After launch loadEquipmentMap()")
    }

    fun isItemNameFound(itemName: String): Boolean {
        // Use map to figure out whether string id is in map.
        val itemID =
            eMap.value?.get(itemName.toUpperCase(Locale.ROOT)) ?: "null"
        Log.i("MVM", "Item Name: $itemName - Item ID: $itemID")
        if (itemID.contentEquals("null")) {
            return false
        }
        _itemID.value = itemID
        return true
    }

    // I don't think I'll be using this method unless I start saving user queries...
//    fun insertItem(item: Item) = viewModelScope.launch(Dispatchers.IO) {
//        repository.insertItem(item)
//    }
}