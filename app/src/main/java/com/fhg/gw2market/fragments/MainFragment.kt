package com.fhg.gw2market.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.fhg.gw2market.databinding.FragmentMainBinding
import com.fhg.gw2market.room.MarketViewModel
import com.google.android.material.snackbar.Snackbar

class MainFragment : Fragment() {
    private var tmpBinding: FragmentMainBinding? = null
    private val mBinding get() = tmpBinding!!
    private val mViewModel: MarketViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initiates viewModel as soon as application starts!
        mViewModel.eMap.observe(this,{})
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        tmpBinding = FragmentMainBinding.inflate(inflater, container, false)
        return mBinding.root
    }

    override fun onResume() {
        super.onResume()
        mBinding.BTNSearch.setOnClickListener {
            if (isInputNull) {
                // TODO: Hide the virtual keyboard so user sees message.
                val msg = "Please input full item name."
                val snackbar = Snackbar.make(
                    mBinding.BTNSearch, msg,
                    Snackbar.LENGTH_SHORT
                )
                snackbar.show()
                return@setOnClickListener
            }
            val itemName = mBinding.TXTIItemId.text.toString()
            if (!mViewModel.isItemNameFound(itemName)) {
                val msg = "Please check for misspellings."
                val snackbar = Snackbar.make(
                    mBinding.BTNSearch, msg,
                    Snackbar.LENGTH_SHORT
                )
                snackbar.show()
                return@setOnClickListener
            } else {
                mBinding.TXTIItemId.text?.clear()
                navigateToInfo()
            }
        }
        mBinding.TXTVAboutLink.setOnClickListener {
            navigateToAbout()
        }
    }

    override fun onDestroyView() {
        tmpBinding = null
        super.onDestroyView()
    }

    private val isInputNull: Boolean
        get() = mBinding.TXTIItemId.text.toString().isEmpty()

    private fun navigateToInfo() {
        val toInfo = MainFragmentDirections
            .actionMainFragmentToInfoFragment()
        Navigation.findNavController(mBinding.root).navigate(toInfo)
    }

    private fun navigateToAbout() {
        val toAbout =
            MainFragmentDirections.actionMainFragmentToAboutFragment()
        Navigation.findNavController(mBinding.root).navigate(toAbout)
    }
}

