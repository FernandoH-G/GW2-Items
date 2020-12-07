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
import com.squareup.picasso.Picasso


class InfoFragment : Fragment() {
    private var tmpBinding: FragmentInfoBinding? = null
    private val iBinding get() = tmpBinding!!
    private val mViewModel: MarketViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel.itemID.observe(this, { itemID ->
            mViewModel.getTPItem(itemID, "name,sell,buy,img")
            mViewModel.getInfoItem(
                "https://api.guildwars2.com/v2/items",
                itemID
            )
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tmpBinding = FragmentInfoBinding.inflate(inflater, container, false)
        return iBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iBinding.FABBackButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
        mViewModel.tpItem.observe(viewLifecycleOwner, { tpItem ->
            Log.i("infofrag", tpItem.toString())
            iBinding.MTXTVName.setTypeface(null, Typeface.BOLD)
            iBinding.MTXTVName.text = tpItem.name
            Picasso.get().load(tpItem.imgURL).into(iBinding.IMGVIcon)
            // Sell Price
            iBinding.MTXTVSellingPriceGold.text = tpItem.sell.gold
            iBinding.MTXTVSellingPriceSilver.text = tpItem.sell.silver
            iBinding.MTXTVSellingPriceCopper.text = tpItem.sell.copper
            // Buy Price
            iBinding.MTXTVBuyingPriceGold.text = tpItem.buy.gold
            iBinding.MTXTVBuyingPriceSilver.text = tpItem.buy.silver
            iBinding.MTXTVBuyingPriceCopper.text = tpItem.buy.copper
        })
        mViewModel.infoItem.observe(viewLifecycleOwner, { infoItem ->
            iBinding.MTXTVType.text = infoItem.type
            iBinding.MTXTVLevel.text = infoItem.level
            iBinding.MTXTVRarity.text = infoItem.rarity
            iBinding.MTXTVDescription.text = infoItem.description
        })
    }

    override fun onDestroyView() {
        tmpBinding = null
        super.onDestroyView()
    }
}
