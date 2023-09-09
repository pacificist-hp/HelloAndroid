package com.android.pacificist.helloandroid.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.android.pacificist.helloandroid.TAG

abstract class BaseFragment<T : ViewDataBinding> : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView: $this")
        return DataBindingUtil.inflate<T>(
            inflater,
            layoutId,
            container,
            false
        ).apply {
            bindData(this)
        }.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView: $this")
    }

    protected abstract val layoutId: Int
    protected abstract fun bindData(binding: T)
}