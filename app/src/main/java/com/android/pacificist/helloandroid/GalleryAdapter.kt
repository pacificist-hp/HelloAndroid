package com.android.pacificist.helloandroid

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.pacificist.helloandroid.databinding.GridItemGalleryBinding
import com.android.pacificist.helloandroid.model.Gallery

class GalleryAdapter : RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {

    var items: List<Gallery> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: GridItemGalleryBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.grid_item_gallery,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dataBinding.model = items[position]
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ViewHolder(binding: GridItemGalleryBinding) : RecyclerView.ViewHolder(binding.root) {
        val dataBinding = binding
    }
}

@BindingAdapter("galleryData")
fun setGalleryData(recyclerView: RecyclerView, galleryData: List<Gallery>) {
    (recyclerView.adapter as? GalleryAdapter)?.items = galleryData
}
