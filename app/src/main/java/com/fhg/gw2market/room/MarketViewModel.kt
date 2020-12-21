package com.fhg.gw2market.room

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
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

    val eMap = liveData {
        val nameIDList =
            mRepository.loadEquipmentMap()?.data?.getItemNames
                ?: return@liveData
        var id: String
        var name: String
        val eMap = mutableMapOf<String, String>()

        nameIDList.forEach {
            id = it.id
            name = it.name
            eMap[name.toUpperCase(Locale.ROOT)] = id
        }
        emit(eMap)
    }

    private val mItemID: MutableLiveData<String> by lazy {
        MutableLiveData()
    }
    val item = mItemID.switchMap { itemID ->
        liveData {
            val item =
                mRepository.getItem(itemID).data?.getItemByID ?: return@liveData
            emit(item)
        }
    }

    fun isItemNameFound(itemName: String): Boolean {
        // Use map to figure out whether string id is in map.
        val itemID =
            eMap.value?.get(itemName.toUpperCase(Locale.ROOT))
                ?: return false
        Log.i("MVM", "Item Name: $itemName - Item ID: $itemID")
        if (itemID.contentEquals("null")) {
            return false
        }
        mItemID.value = itemID
        return true
    }

    // I don't think I'll be using this method unless I start saving user queries...
//    fun insertItem(item: Item) = viewModelScope.launch(Dispatchers.IO) {
//        repository.insertItem(item)
//    }
}