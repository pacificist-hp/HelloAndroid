package com.android.pacificist.helloandroid.ui.state

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.pacificist.helloandroid.data.model.Gallery
import com.android.pacificist.helloandroid.data.repository.MediaRepository
import kotlinx.coroutines.launch

class GalleryViewModel : ViewModel() {

    val data = ObservableField<List<Gallery>>()

    private val mediaRepository = MediaRepository()

    init {
        load()
    }

    private fun load() {
        viewModelScope.launch {
            data.set(mediaRepository.loadMedias())
        }
    }
}