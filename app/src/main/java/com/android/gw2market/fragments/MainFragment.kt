package com.android.gw2market.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.android.gw2market.R
import com.android.gw2market.databinding.FragmentMainBinding
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment() {
    private var tmpBinding: FragmentMainBinding? = null
    private val mBinding get() = tmpBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
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
    }

    override fun onDestroyView() {
        tmpBinding = null
        super.onDestroyView()
    }

    private val isInputNull: Boolean
        get() = mBinding.TXTIItemId.text.toString().isEmpty()
}