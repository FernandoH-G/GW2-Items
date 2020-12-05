package com.fhg.gw2market.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.fhg.gw2market.databinding.FragmentInfoBinding
import com.fhg.gw2market.room.MarketViewModel
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.textview.MaterialTextView
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject

class Price(
    var gold: String = "0",
    var silver: String = "0",
    var copper: String = "0"
)

class InfoFragment : Fragment() {
    private var tmpBinding: FragmentInfoBinding? = null
    private val iBinding get() = tmpBinding!!
    private val mViewModel: MarketViewModel by activityViewModels()

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
        // QUESTION: Should mItemID be observed instead of .value?
        val tpURL =
            "http://api.gw2tp.com/1/items?ids=${mViewModel.mItemID.value}&fields=name,sell,buy,img"
        val infoURL =
            "https://api.guildwars2.com/v2/items?ids=${mViewModel.mItemID.value}&lang=en"
        Log.d("InfoFrag", tpURL)


        val jsonRequestTP = JsonObjectRequest(Request.Method.GET, tpURL, null,
            Response.Listener { resp ->
                val respObj = resp.getJSONArray("results")
                // Response.Listener is necessary because of return@Listener.
                if (respObj.isNull(0)) return@Listener
                // url for copper/silver/gold currency icon.
                // https://www.gw2tp.com/static/img/copper.png
                iBinding.MTXTVName.setTypeface(null, Typeface.BOLD)
                iBinding.MTXTVName.text = parseItemInfo(respObj, 1)
                val selPrice = parseItemInfo(respObj, 2)
                val buyPrice = parseItemInfo(respObj, 3)
                val sViews = arrayOf(
                    iBinding.MTXTVSellingPriceGold,
                    iBinding.MTXTVSellingPriceSilver,
                    iBinding.MTXTVSellingPriceCopper
                )
                val bViews = arrayOf(
                    iBinding.MTXTVBuyingPriceGold,
                    iBinding.MTXTVBuyingPriceSilver,
                    iBinding.MTXTVBuyingPriceCopper
                )
                setGW2Price(selPrice, sViews)
                setGW2Price(buyPrice, bViews)
                Picasso.get().load(parseItemInfo(respObj, 4))
                    .into(iBinding.IMGVIcon)
            }, { Log.i("InfoFrag", "Error in jsonRequestTP") })

        val jsonRequestInfo =
            JsonArrayRequest(Request.Method.GET, infoURL, null,
                { resp ->
                    val respObj = resp.getJSONObject(0)
                    iBinding.MTXTVType.text = parseItemInfo(respObj, "type")
                    iBinding.MTXTVLevel.text = parseItemInfo(respObj, "level")
                    iBinding.MTXTVDescription.text =
                        parseItemInfo(respObj, "description")
                    iBinding.MTXTVRarity.text = parseItemInfo(respObj, "rarity")
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
        return obj.getJSONArray(0)?.get(infoIndex)?.toString()
            ?: errorReason[infoIndex]
    }

    // Things to get from obj: description, type, level, rarity
    private fun parseItemInfo(obj: JSONObject, info: String): String {
        try {
            obj.get(info)
        } catch (e: Exception) {
            return "No $info"
        }
        return obj.get(info).toString()
    }

    private fun setGW2Price(num: String, views: Array<MaterialTextView>) {
        val price: Price = parseNum(num)
        views[0].text = price.gold
        views[1].text = price.silver
        views[2].text = price.copper
    }

    private fun parseNum(num: String): Price {
        val numLen = num.length
        Log.i("infoFrag", "Num length: $numLen")
        val price = Price()
        when {
            numLen > 4 -> {
                price.gold = num.subSequence(0, numLen - 4).toString()
                price.silver =
                    num.subSequence(numLen - 4, numLen - 2).toString()
                price.copper = num.subSequence(numLen - 2, numLen).toString()
            }
            numLen > 3 -> {
                price.silver = num.subSequence(0, numLen - 2).toString()
                price.copper = num.subSequence(numLen - 2, numLen).toString()
            }
            numLen > 2 -> {
                price.silver = num[0].toString()
                price.copper = num.subSequence(numLen - 2, numLen).toString()
            }
            numLen > 1 -> {
                price.copper = num.subSequence(numLen - 2, numLen).toString()
            }
            numLen > 0 -> {
                price.copper = num[0].toString()
            }
        }
        return price
    }
}
