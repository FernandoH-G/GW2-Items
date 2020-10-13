package com.android.gw2market.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.gw2market.databinding.FragmentAboutBinding


class AboutFragment : Fragment() {
    private var tmpBinding: FragmentAboutBinding? = null
    private val aBinding get() = tmpBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        tmpBinding = FragmentAboutBinding.inflate(inflater,container,false)
        return aBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        aBinding.FABBackButton.setOnClickListener {
            requireActivity().onBackPressed()
        }
    }

    override fun onDestroyView() {
        tmpBinding = null
        super.onDestroyView()
    }
}