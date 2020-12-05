package com.fhg.gw2market.room

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import java.util.*

// Don't reference context in view model!
// application holds the context, so it's ok!
class MarketViewModel(application: Application) :
    AndroidViewModel(application) {
    //    private val allItemsVM: LiveData<List<Item>>
    private val mRepository: ItemRepository
    private lateinit var mEMap: MutableMap<String, Int>
    val mItemID = MutableLiveData<String>()

    init {
        val itemsDao = ItemRoomDatabase.getDatabase(application, viewModelScope)
            .itemDao()
        mRepository = ItemRepository(itemsDao, application)
        // QUESTION: Why does calling loadEquipmentMap() from init block
        //  not prepare the eMap to be used in time?
        // eMap = repository.loadEquipmentMap()

        // Uncommenting will fix the uninitialized allItemsVM declaration.
        // allItemsVM = repository.allItemsRepo
    }

    fun loadEquipmentMap() {
        mEMap = mRepository.loadEquipmentMap()
    }

    fun isItemNameFound(itemName: String): Boolean {
        // Use map to figure out whether string id is in map.
        Log.d("MVM", itemName)
        val itemID = mEMap[itemName.toUpperCase(Locale.ROOT)].toString()
        Log.i("MVM", "Item ID: $itemID")
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