package com.android.pacificist.helloandroid.ui.fragment

import com.android.pacificist.helloandroid.R
import com.android.pacificist.helloandroid.data.model.Gallery
import com.android.pacificist.helloandroid.databinding.FragmentDetailBinding

class DetailFragment : BaseFragment<FragmentDetailBinding>() {

    override val layoutId = R.layout.fragment_detail

    override fun bindData(binding: FragmentDetailBinding) {
        binding.model = arguments?.get("gallery") as? Gallery
    }
}