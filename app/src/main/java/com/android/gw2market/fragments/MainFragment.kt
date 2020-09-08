package com.android.gw2market.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.android.gw2market.R
import com.android.gw2market.databinding.FragmentMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject
import java.io.IOException

data class Equipment(val name: String)

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
                val snackbar = Snackbar.make(mBinding.BTNSearch, msg,
                    Snackbar.LENGTH_SHORT)
                snackbar.show()
                return@setOnClickListener
            }
            val itemID = mBinding.TXTIItemId.text.toString()
            val toInfo = MainFragmentDirections.actionMainFragmentToInfoFragment(itemID)
            Navigation.findNavController(mBinding.root).navigate(toInfo)
        }
        // TESTING
        val jsonFileString: String =  getJSONDataFromAsset(requireContext(),"new_json.json")
//        val gson = Gson()
//        val listEquipmentType = object : TypeToken<List<Equipment>>() {}.type
//        var equipment: List<Equipment> = gson.fromJson(jsonFileString, listEquipmentType)
//        equipment.forEachIndexed { idx, equip->  Log.i("data",">Item $idx:\n$equip")}


        // TESTING
    }

    override fun onDestroyView() {
        tmpBinding = null
        super.onDestroyView()
    }

    private val isInputNull: Boolean
        get() = mBinding.TXTIItemId.text.toString().isEmpty()

    private fun getJSONDataFromAsset(context: Context, filename: String) : String {
        val jsonString : String
        try {
            jsonString = context.assets.open(filename).bufferedReader().use{it.readText()}
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return "Error in getJSONDataFromAsset.\n"
        }
        return jsonString
    }
}