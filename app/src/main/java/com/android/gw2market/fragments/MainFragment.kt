package com.android.gw2market.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.android.gw2market.databinding.FragmentMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

data class Equipment(val name: String, val id: Int)

class MainFragment : Fragment() {
    private var tmpBinding: FragmentMainBinding? = null
    private val mBinding get() = tmpBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tmpBinding = FragmentMainBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onResume() {
        super.onResume()
        mBinding.BTNSearch.setOnClickListener {
            if (isInputNull) {
                val msg = "Please input item ID # or full item name."
                val snackbar = Snackbar.make(
                    mBinding.BTNSearch, msg,
                    Snackbar.LENGTH_SHORT
                )
                snackbar.show()
                return@setOnClickListener
            }
            val itemID = mBinding.TXTIItemId.text.toString()
            val toInfo = MainFragmentDirections.actionMainFragmentToInfoFragment(itemID)
            Navigation.findNavController(mBinding.root).navigate(toInfo)
        }

        // Save json from json file as a map.
        val jsonFileString: String = getJSONDataFromAsset(requireContext(), "gw2_equipment.json")
        val gson = Gson()
//        val mapType= object : TypeToken<Map<String,Any>>() {}.type
//        var equipment: Map<String,Any> = gson.fromJson(jsonFileString, mapType)
//        equipment.forEachIndexed { idx, equip->  Log.i("data",">Item $idx:\n$equip")}
//        equipment.forEach{Log.d("Hello", it.toString())}
//        Log.i("Main", equipment.toString())
//        equipment.forEach{
//                Log.i("Fernando",it.value.toString())
//        }

        // Need type for gson.fromJson()
        val listEquipmentType = object : TypeToken<List<Equipment>>() {}.type
        val equipment: List<Equipment> = gson.fromJson(jsonFileString,listEquipmentType)
        val eMap = mutableMapOf<String,Int>()
        equipment.forEach{
            eMap[it.name] = it.id
        }
        Log.i("Fernando", eMap["Sealed Package of Snowballs"].toString())

    }

    override fun onDestroyView() {
        tmpBinding = null
        super.onDestroyView()
    }

    private val isInputNull: Boolean
        get() = mBinding.TXTIItemId.text.toString().isEmpty()

    private fun getJSONDataFromAsset(context: Context, filename: String): String {
        val jsonString: String
        try {
            jsonString = context.assets.open(filename).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return "Error in getJSONDataFromAsset.\n"
        }
        return jsonString
    }
}