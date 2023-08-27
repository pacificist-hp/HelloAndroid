package com.android.pacificist.helloandroid.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.pacificist.helloandroid.R
import com.android.pacificist.helloandroid.TAG
import com.android.pacificist.helloandroid.data.model.Gallery
import com.android.pacificist.helloandroid.databinding.GridItemGalleryBinding
import com.android.pacificist.helloandroid.ui.select.DragSelectTouchListener
import com.android.pacificist.helloandroid.ui.select.DragSelectionProcessor
import com.android.pacificist.helloandroid.ui.state.GalleryViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Key

class GalleryAdapter(view: RecyclerView, private val state: GalleryViewModel) :
    RecyclerView.Adapter<GalleryAdapter.ViewHolder>(), DragSelectionProcessor.ISelectionHandler {

    val touchListener = DragSelectTouchListener(view, DragSelectionProcessor(this))

    fun startDragSelection(gallery: Gallery): Boolean {
        state.items.get()?.indexOf(gallery)?.let {
            touchListener.startDragSelection(it)
            return true
        }
        return false
    }

    override fun getSelection(): Set<Int> {
        val selection = HashSet<Int>()
        state.items.get()?.forEachIndexed { index, gallery ->
            if (gallery.isSelected.get() == true) {
                selection.add(index)
            }
        }
        return selection
    }

    override fun updateSelection(start: Int, end: Int, isSelected: Boolean) {
        Log.d(TAG, "updateSelection[$start, $end]->$isSelected")
        for (i in start..end) {
            state.items.get()?.get(i)?.isSelected?.let {
                if (it.get() != isSelected) {
                    it.set(isSelected)
                }
            }
        }
    }

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
        holder.dataBinding.model = state.items.get()?.get(position)
        holder.dataBinding.state = state
        holder.dataBinding.adapter = this
    }

    override fun getItemCount(): Int {
        return state.items.get()?.size ?: 0
    }

    class ViewHolder(binding: GridItemGalleryBinding) : RecyclerView.ViewHolder(binding.root) {
        val dataBinding = binding
    }
}

@BindingAdapter(
    value = ["url", "signature"],
    requireAll = true
)
fun loadUrl(view: ImageView, url: String, signature: Key) {
    Log.d(TAG, "loadUrl: $url")
    Glide.with(view).load(url).signature(signature).into(view)
}