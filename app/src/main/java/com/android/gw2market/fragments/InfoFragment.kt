package com.android.gw2market.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.android.gw2market.R
import com.android.gw2market.databinding.FragmentInfoBinding
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

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
        val url = "http://api.gw2tp.com/1/items?ids=${itemID}&fields=name,sell"

        val stringRequest = StringRequest(Request.Method.GET, url,
            Response.Listener<String> {  resp ->
               iBinding.TXTVItemInfo.text = "$resp"
            }, Response.ErrorListener { iBinding.TXTVItemInfo.text = "Something wrong!"})
        queue.add(stringRequest)
    }

    override fun onDestroyView() {
        tmpBinding = null
        super.onDestroyView()
    }
}