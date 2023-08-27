package com.android.pacificist.helloandroid.data.model

import androidx.databinding.ObservableField
import com.bumptech.glide.signature.MediaStoreSignature

data class Gallery(
    val id: Long,
    val name: String,
    val path: String,
    val mimeType: String,
    val orientation: Int,
    val modified: Long
) : IEdit {
    override val isSelected = ObservableField(false)

    fun toggleSelected() = isSelected.set(!(isSelected.get() ?: true))

    val signature = MediaStoreSignature(mimeType, modified, orientation)
}
