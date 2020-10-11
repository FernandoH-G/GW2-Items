package com.android.gw2market.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.navigation.Navigation
import com.android.gw2market.databinding.FragmentMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.util.*

data class Equipment(val name: String, val id: Int)

class MainFragment : Fragment() {
    private var tmpBinding: FragmentMainBinding? = null
    private val mBinding get() = tmpBinding!!
    private val eMap = mutableMapOf<String,Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadEquipmentMap()
        Log.i("Fernando", "onCreate: loaded Equipment Map.")
    }

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
                val msg = "Please input full item name."
                val snackbar = Snackbar.make(
                    mBinding.BTNSearch, msg,
                    Snackbar.LENGTH_SHORT
                )
                snackbar.show()
                return@setOnClickListener
            }
            val itemName = mBinding.TXTIItemId.text.toString()
            // Use map to figure out whether string id is in map.
            val itemID = eMap[itemName.toUpperCase(Locale.ROOT)].toString()
            if (itemID.contentEquals("null")) {
                val msg = "Please check for misspellings."
                val snackbar = Snackbar.make(
                    mBinding.BTNSearch,msg,
                    Snackbar.LENGTH_SHORT
                )
                snackbar.show()
                return@setOnClickListener
            } else {
                mBinding.TXTIItemId.text?.clear()
                val toInfo = MainFragmentDirections.actionMainFragmentToInfoFragment(itemID)
                Navigation.findNavController(mBinding.root).navigate(toInfo)
            }
        }
        mBinding.TXTVAboutLink.setOnClickListener {
            val toAbout = MainFragmentDirections.actionMainFragmentToAboutFragment()
            Navigation.findNavController(mBinding.root).navigate(toAbout)
        }
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

    private fun loadEquipmentMap() {
        // Save json from json file as a map.
        val filename = "gw2_equipment_uppercase.json"
        val jsonFileString: String = getJSONDataFromAsset(requireContext(),filename)
        val gson = Gson()

        val listEquipmentType = object : TypeToken<List<Equipment>>() {}.type
        val equipment: List<Equipment> = gson.fromJson(jsonFileString,listEquipmentType)
        equipment.forEach{
            eMap[it.name] = it.id
        }
    }
}