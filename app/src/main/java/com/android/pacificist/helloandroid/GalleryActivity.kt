package com.android.pacificist.helloandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.android.pacificist.helloandroid.databinding.ActivityGalleryBinding
import com.android.pacificist.helloandroid.vm.GalleryViewModel

/**
 * Created by pacificist on 2023/7/15.
 */
class GalleryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityGalleryBinding>(this, R.layout.activity_gallery)
        binding.vm = GalleryViewModel()
        binding.adapter = GalleryAdapter()
        binding.layoutManager = GridLayoutManager(this, 4)
    }
}