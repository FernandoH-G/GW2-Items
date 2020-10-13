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
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.util.*

class MainFragment : Fragment() {
    private var tmpBinding: FragmentMainBinding? = null
    private val mBinding get() = tmpBinding!!
    private val eMap = mutableMapOf<String,Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadEquipmentMap()
        Log.i("mainFrag", "onCreate: loaded Equipment Map.")
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
            Log.i("mainFrag","Item ID: $itemID")
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


    private fun loadEquipmentMap() {
        val queue = Volley.newRequestQueue(requireContext())
        val tpURL= "http://api.gw2tp.com/1/bulk/items-names.json"

        val jsonRequestTP = JsonObjectRequest(Request.Method.GET, tpURL, null,
            { resp ->
                var name : String
                var id : Int
                val respObjArr = resp.getJSONArray("items")
                for (i in 0 until respObjArr.length()) {
                    id = respObjArr.getJSONArray(i).get(0) as Int
                    name = respObjArr.getJSONArray(i).get(1).toString()
                    eMap[name.toUpperCase(Locale.ROOT)] = id
                }

            }, {Log.d("mainFrag","Error in gw2json request.")})
        queue.add(jsonRequestTP)
    }
}