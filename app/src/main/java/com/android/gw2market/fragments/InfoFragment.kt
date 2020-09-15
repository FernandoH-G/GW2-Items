package com.android.gw2market.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.android.gw2market.databinding.FragmentInfoBinding
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONArray

class InfoFragment : Fragment() {
    private var tmpBinding: FragmentInfoBinding? = null
    private val iBinding get() = tmpBinding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        tmpBinding = FragmentInfoBinding.inflate(inflater, container, false)
        return iBinding.root
}

    override fun onResume() {
        super.onResume()

        iBinding.BTNNewSearch.setOnClickListener {
            // More elegant than navigating because this does not add to the stack.
            requireActivity().onBackPressed()
        }
        val queue = Volley.newRequestQueue(requireContext())
        val itemIDArg : InfoFragmentArgs by navArgs()
        val itemID = itemIDArg.itemId
//        Log.i("Fernando", itemID)
        val url = "http://api.gw2tp.com/1/items?ids=${itemID}&fields=name,sell,img"

        val jsonRequest = JsonObjectRequest(Request.Method.GET,url, null,
        Response.Listener { resp ->
            val respObj = resp.getJSONArray("results")
            if (respObj.isNull(0)) return@Listener
            // Figure out an elegant way to deal with the item not being in the DB.
            // url for copper/silver/gold currency icon.
            // https://www.gw2tp.com/static/img/copper.png
            iBinding.TXTVItemInfo.text = parseItemInfo(respObj,1)
            val selPrice = parseItemInfo(respObj,2)
            setGW2Currency("10002309")
            iBinding.TXTVItemPrice.text = ""
//            Log.d("price char count: ", parseItemInfo(respObj,2).count().toString())
            Picasso.with(requireContext()).load(parseItemInfo(respObj,3)).into(iBinding.IMGVItemIcon)
        }, Response.ErrorListener { iBinding.TXTVItemInfo.text = "Something wrong with json" })

        queue.add(jsonRequest)
    }

    override fun onDestroyView() {
        tmpBinding = null
        super.onDestroyView()
    }

    // 1: item name, 2: selling price, 3: img url.
    private fun parseItemInfo(obj : JSONArray, infoIndex : Int) : String {
        return obj.getJSONArray(0).get(infoIndex).toString()
    }

    private fun setGW2Currency(num: String) {
        val numLen = num.length
        if (numLen > 4) {
            val gold = num.subSequence(0, numLen-4)
            val silver = num.subSequence(numLen-4,numLen-2)
            val copper = num.subSequence(numLen-2,numLen)
            Log.i("Fernando","$gold $silver $copper")
        } else if (numLen > 3){

        }
    }
}