package com.android.gw2market.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.android.gw2market.R
import com.android.gw2market.databinding.FragmentMainBinding

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
            val toInfo = MainFragmentDirections.actionMainFragmentToInfoFragment()
            Navigation.findNavController(mBinding.root).navigate(toInfo)
        }
    }

    override fun onDestroyView() {
        tmpBinding = null
        super.onDestroyView()
    }
}