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
        mViewModel.itemID.observe(viewLifecycleOwner, { itemID ->
            mViewModel.getItem(itemID)
        })
        mViewModel.item.observe(viewLifecycleOwner, { item ->
            Log.i("infofrag", item.toString())
            iBinding.MTXTVName.setTypeface(null, Typeface.BOLD)
            iBinding.MTXTVName.text = item.name
            Picasso.get().load(item.imgURL).into(iBinding.IMGVIcon)
            // Sell Price
            iBinding.MTXTVSellingPriceGold.text = item.sell.gold
            iBinding.MTXTVSellingPriceSilver.text = item.sell.silver
            iBinding.MTXTVSellingPriceCopper.text = item.sell.copper
            // Buy Price
            iBinding.MTXTVBuyingPriceGold.text = item.buy.gold
            iBinding.MTXTVBuyingPriceSilver.text = item.buy.silver
            iBinding.MTXTVBuyingPriceCopper.text = item.buy.copper
            iBinding.MTXTVType.text = item.type
            iBinding.MTXTVLevel.text = item.level
            iBinding.MTXTVRarity.text = item.rarity
            iBinding.MTXTVDescription.text = item.description
        })
    }

    override fun onDestroyView() {
        tmpBinding = null
        super.onDestroyView()
    }
}
