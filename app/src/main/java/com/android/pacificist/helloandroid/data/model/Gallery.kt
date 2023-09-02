package com.android.pacificist.helloandroid.data.model

import android.os.Parcelable
import androidx.databinding.ObservableField
import com.bumptech.glide.signature.MediaStoreSignature
import kotlinx.parcelize.Parcelize

@Parcelize
data class Gallery(
    val id: Long,
    val name: String,
    val path: String,
    val mimeType: String,
    val orientation: Int,
    val modified: Long
) : IEdit, Parcelable {
    override val isSelected = ObservableField(false)

    fun toggleSelected() = isSelected.set(!(isSelected.get() ?: true))

    val signature = MediaStoreSignature(mimeType, modified, orientation)
}
