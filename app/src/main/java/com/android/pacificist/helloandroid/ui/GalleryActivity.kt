package com.android.pacificist.helloandroid.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.android.pacificist.helloandroid.R
import com.android.pacificist.helloandroid.databinding.ActivityGalleryBinding
import com.android.pacificist.helloandroid.ui.state.GalleryViewModel

/**
 * Created by pacificist on 2023/7/15.
 */
class GalleryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityGalleryBinding>(this, R.layout.activity_gallery)
        binding.state = GalleryViewModel()
        binding.adapter = GalleryAdapter(binding.galleryListView)
        binding.layoutManager = GridLayoutManager(this, 3)
    }
}