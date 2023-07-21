package com.android.pacificist.helloandroid.vm

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import com.android.pacificist.helloandroid.model.Gallery
import kotlin.collections.ArrayList

class GalleryViewModel {
    val data = ObservableField<List<Gallery>>()

    init {
        load()
    }

    fun load() {
        val galleryList = ArrayList<Gallery>()
        for (i in 0..52) {
            galleryList.add(Gallery(i))
        }
        data.set(galleryList)
    }
}