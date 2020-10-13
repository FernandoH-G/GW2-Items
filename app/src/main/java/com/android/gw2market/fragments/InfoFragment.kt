package com.android.gw2market.fragments

import android.graphics.Typeface
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
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iBinding.FABBackButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        val queue = Volley.newRequestQueue(requireContext())
        val itemIDArg: InfoFragmentArgs by navArgs()
        val itemID = itemIDArg.itemId
        val tpURL = "http://api.gw2tp.com/1/items?ids=${itemID}&fields=name,sell,buy,img"
        val infoURL = "https://api.guildwars2.com/v2/items?ids=${itemID}&lang=en"

        val jsonRequestTP = JsonObjectRequest(Request.Method.GET, tpURL, null,
            Response.Listener { resp ->
                val respObj = resp.getJSONArray("results")
                // Response.Listener is necessary because of return@Listener.
                if (respObj.isNull(0)) return@Listener
                // url for copper/silver/gold currency icon.
                // https://www.gw2tp.com/static/img/copper.png
                iBinding.MTXTVName.setTypeface(null,Typeface.BOLD)
                iBinding.MTXTVName.text = parseItemInfo(respObj, 1)
                val selPrice = parseItemInfo(respObj, 2)
                val buyPrice = parseItemInfo(respObj, 3)
                setGW2Currency(selPrice, buyPrice)
//            Log.i("price char count: ", parseItemInfo(respObj,2).count().toString())
                Picasso.get().load(parseItemInfo(respObj, 4))
                    .into(iBinding.IMGVIcon)
            }, { Log.i("InfoFrag", "Error in jsonRequestTP") })

        val jsonRequestInfo = JsonArrayRequest(Request.Method.GET, infoURL, null,
            { resp ->
                val respObj = resp.getJSONObject(0)
                iBinding.MTXTVType.text = parseItemInfo(respObj, "type")
                iBinding.MTXTVLevel.text = parseItemInfo(respObj, "level")
                iBinding.MTXTVDescription.text = parseItemInfo(respObj, "description")
                iBinding.MTXTVRarity.text = parseItemInfo(respObj, "rarity")
                // query vendor value for the market card!
//                Log.i("InfoFrag", resp.getJSONObject(0).get("level").toString())

            }, { Log.i("InfoFrag", "Error in new json request.") })

        queue.add(jsonRequestTP)
        queue.add(jsonRequestInfo)
    }

    override fun onDestroyView() {
        tmpBinding = null
        super.onDestroyView()
    }

    // 1: item name, 2: selling price, 3: buying price, 4: img url.
    private fun parseItemInfo(obj: JSONArray, infoIndex: Int): String {
        val errorReason = arrayOf("Item Name", "Selling Price", "IMG URL")
        return obj.getJSONArray(0)?.get(infoIndex)?.toString() ?: errorReason[infoIndex]
    }

    // Things to get from obj: description, type, level, rarity
    private fun parseItemInfo(obj: JSONObject, info: String): String {
        try {
            obj.get(info)
        } catch (e : Exception) {
            return "No $info"
        }
        return obj.get(info).toString()
    }

    // Find more elegant way to do this.
    // Add way to reuse this function for buy and sell price.
    private fun setGW2Currency(sellNum: String, buyNum: String) {
        val (sGold, sSilver, sCopper) = parseNum(sellNum)
        iBinding.MTXTVSellingPriceGold.text = sGold
        iBinding.MTXTVSellingPriceSilver.text = sSilver
        iBinding.MTXTVSellingPriceCopper.text = sCopper
        val (bGold, bSilver, bCopper) = parseNum(buyNum)
        iBinding.MTXTVBuyingPriceGold.text = bGold
        iBinding.MTXTVBuyingPriceSilver.text = bSilver
        iBinding.MTXTVBuyingPriceCopper.text = bCopper
    }

    private fun parseNum(num: String): Triple<String, String, String> {
        val numLen = num.length
        Log.i("infoFrag", "Num length: $numLen")
        var gold = "0"
        var silver = "0"
        var copper = "0"
        when {
            numLen > 4 -> {
                gold = num.subSequence(0, numLen - 4).toString()
                silver = num.subSequence(numLen - 4, numLen - 2).toString()
                copper = num.subSequence(numLen - 2, numLen).toString()
            }
            numLen > 3 -> {
                gold = "0"
                silver = num.subSequence(0, numLen - 2).toString()
                copper = num.subSequence(numLen - 2, numLen).toString()
            }
            numLen > 2 -> {
                gold = "0"
                silver = num[0].toString()
                copper = num.subSequence(numLen - 2, numLen).toString()
            }
            numLen > 1 -> {
                gold = "0"
                silver = "0"
                copper = num.subSequence(numLen - 2, numLen).toString()
            }
            numLen > 0 -> {
                gold = "0"
                silver = "0"
                copper = num[0].toString()
            }
        }
        return Triple(gold, silver, copper)
    }
}