package com.android.pacificist.helloandroid.ui.fragment

import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import com.android.pacificist.helloandroid.R
import com.android.pacificist.helloandroid.TAG
import com.android.pacificist.helloandroid.data.model.Gallery
import com.android.pacificist.helloandroid.databinding.FragmentDetailBinding
import com.android.pacificist.helloandroid.ui.interpolator.EaseCubicInterpolator

class DetailFragment : BaseFragment<FragmentDetailBinding>() {

    override val layoutId = R.layout.fragment_detail

    override fun bindData(binding: FragmentDetailBinding) {
        binding.model = arguments?.get("gallery") as? Gallery
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        Log.d(TAG, "onCreateAnimation: transit=$transit, enter=$enter")
        return if (enter) AnimationSet(false).apply {
            addAnimation(ScaleAnimation(
                0f,
                1f,
                0f,
                1f,
                Animation.ABSOLUTE,
                arguments?.getFloat("x") ?: 0f,
                Animation.ABSOLUTE,
                arguments?.getFloat("y") ?: 0f
            ).apply {
                interpolator = EaseCubicInterpolator(0.4f, 0f, 0.2f, 1f)
                duration = 500
            })
        } else AnimationUtils.loadAnimation(context, android.R.anim.slide_out_right)
    }
}