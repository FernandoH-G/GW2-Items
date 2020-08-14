package com.android.gw2market.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.gw2market.R
import com.android.gw2market.databinding.FragmentInfoBinding

class InfoFragment : Fragment() {
    private var tmpBinding: FragmentInfoBinding? = null
    private val iBinding get() = tmpBinding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        tmpBinding = FragmentInfoBinding.inflate(inflater,container,false)
        return iBinding.root
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onDestroyView() {
        tmpBinding = null
        super.onDestroyView()
    }
}