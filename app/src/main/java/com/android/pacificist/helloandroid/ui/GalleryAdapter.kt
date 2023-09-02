package com.android.pacificist.helloandroid.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.databinding.BindingAdapter
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.android.pacificist.helloandroid.R
import com.android.pacificist.helloandroid.TAG
import com.android.pacificist.helloandroid.data.model.Gallery
import com.android.pacificist.helloandroid.databinding.GridItemGalleryBinding
import com.android.pacificist.helloandroid.ui.select.DragSelectTouchListener
import com.android.pacificist.helloandroid.ui.select.DragSelectionProcessor
import com.bumptech.glide.Glide
import com.bumptech.glide.load.Key

class GalleryAdapter(private val view: RecyclerView) :
    RecyclerView.Adapter<GalleryAdapter.ViewHolder>(), DragSelectionProcessor.ISelectionHandler {

    var items: List<Gallery>? = null

    private val touchListener = DragSelectTouchListener(view, DragSelectionProcessor(this))

    init {
        view.addOnItemTouchListener(touchListener)
    }

    fun showDetail(gallery: Gallery) {
        view.findNavController()
            .navigate(R.id.action_grid_to_detail, bundleOf("gallery" to gallery))
    }

    fun startDragSelection(gallery: Gallery): Boolean {
        items?.indexOf(gallery)?.let {
            touchListener.startDragSelection(it)
            return true
        }
        return false
    }

    override fun getSelection(): Set<Int> {
        val selection = HashSet<Int>()
        items?.forEachIndexed { index, gallery ->
            if (gallery.isSelected.get() == true) {
                selection.add(index)
            }
        }
        return selection
    }

    override fun updateSelection(start: Int, end: Int, isSelected: Boolean) {
        Log.d(TAG, "updateSelection[$start, $end]->$isSelected")
        for (i in start..end) {
            items?.get(i)?.isSelected?.let {
                if (it.get() != isSelected) {
                    it.set(isSelected)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.grid_item_gallery,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.model = items?.get(position)
        holder.binding.adapter = this
    }

    override fun getItemCount(): Int = items?.size ?: 0

    class ViewHolder(val binding: GridItemGalleryBinding) : RecyclerView.ViewHolder(binding.root)
}

@BindingAdapter(
    value = ["url", "signature"],
    requireAll = true
)
fun loadUrl(view: ImageView, url: String, signature: Key) {
    Glide.with(view).load(url).signature(signature).into(view)
}

@BindingAdapter("items")
fun setItems(view: RecyclerView, items: List<Gallery>?) {
    Log.d(TAG, "setItems: size=${items?.size}, adapter=${view.adapter}")
    (view.adapter as? GalleryAdapter)?.apply {
        this.items = items
        notifyDataSetChanged()
    }
}