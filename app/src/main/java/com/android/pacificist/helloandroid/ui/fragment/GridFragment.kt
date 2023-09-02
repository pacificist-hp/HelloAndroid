package com.android.pacificist.helloandroid.ui.fragment

import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.android.pacificist.helloandroid.R
import com.android.pacificist.helloandroid.databinding.FragmentGridBinding
import com.android.pacificist.helloandroid.ui.GalleryAdapter
import com.android.pacificist.helloandroid.ui.state.GalleryViewModel

class GridFragment : BaseFragment<FragmentGridBinding>() {

    private val viewModel: GalleryViewModel by viewModels()

    override val layoutId = R.layout.fragment_grid

    override fun bindData(binding: FragmentGridBinding) {
        binding.apply {
            galleryListView.adapter = GalleryAdapter(galleryListView)
            galleryListView.layoutManager = GridLayoutManager(context, 3)
            state = viewModel
        }
    }
}