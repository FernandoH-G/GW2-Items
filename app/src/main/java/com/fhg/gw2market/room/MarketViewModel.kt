package com.fhg.gw2market.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Don't reference context in view model!
// application holds the context, so it's ok!
class MarketViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ItemRepository
//    private val allItemsVM: LiveData<List<Item>>

    init {
        val itemsDao = ItemRoomDatabase.getDatabase(application, viewModelScope)
            .itemDao()
        repository = ItemRepository(itemsDao, application)
        //
        // Uncommenting will fix the uninitialized allItemsVM declaration.
//        allItemsVM = repository.allItemsRepo
    }

    // I don't think I'll be using this method unless I start saving user queries...
    fun insertItem(item: Item) = viewModelScope.launch(Dispatchers.IO) {
//        repository.insertItem(item)
    }
}