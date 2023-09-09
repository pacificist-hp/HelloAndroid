package com.android.pacificist.helloandroid.data.model

import android.os.Parcelable
import androidx.databinding.ObservableField
import com.bumptech.glide.signature.MediaStoreSignature
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class Gallery(
    val id: Long,
    val name: String,
    val path: String,
    val size: Long,
    val width: Int,
    val height: Int,
    val mediaType: Int,
    val mimeType: String,
    val orientation: Int,
    val addTime: Long,
    val takeTime: Long,
    val modified: Long
) : IEdit, Parcelable {
    @IgnoredOnParcel
    override val isSelected = ObservableField(false)

    fun toggleSelected() = isSelected.set(!(isSelected.get() ?: true))

    @IgnoredOnParcel
    val signature = MediaStoreSignature(mimeType, modified, orientation)
}