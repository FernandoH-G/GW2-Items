package com.android.gw2market.fragments

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.android.gw2market.databinding.FragmentInfoBinding
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import java.io.InputStream
import java.net.URL

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
        val queue = Volley.newRequestQueue(requireContext())
        val itemIDArg : InfoFragmentArgs by navArgs()
        val itemID = itemIDArg.itemId
        val url = "http://api.gw2tp.com/1/items?ids=${itemID}&fields=name,sell,img"

        val jsonRequest = JsonObjectRequest(Request.Method.GET,url, null,
        Response.Listener { resp ->
            val obj = resp.getJSONArray("results")
//            Log.d("item is null?: ", obj.isNull(0).toString())
            if (obj.isNull(0)) return@Listener
            // Figure out an elegant way to deal with the item not being in the DB.
            val itemName = obj.getJSONArray(0).get(1).toString()
            val itemPrice = obj.getJSONArray(0).get(2).toString()
            val itemImgUrl = obj.getJSONArray(0).get(3).toString()
            Picasso.with(requireContext()).load(itemImgUrl).into(iBinding.IMGVItemIcon)
            iBinding.TXTVItemInfo.text = itemName
            iBinding.TXTVItemPrice.text = itemPrice
        }, Response.ErrorListener { iBinding.TXTVItemInfo.text = "Something wrong with json" })

        queue.add(jsonRequest)
    }

    override fun onDestroyView() {
        tmpBinding = null
        super.onDestroyView()
    }
}