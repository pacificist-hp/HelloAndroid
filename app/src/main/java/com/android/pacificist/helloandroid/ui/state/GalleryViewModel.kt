package com.android.pacificist.helloandroid.ui.state

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.RecyclerView
import com.android.pacificist.helloandroid.R
import com.android.pacificist.helloandroid.databinding.GridItemGalleryBinding
import com.android.pacificist.helloandroid.model.Gallery
import com.android.pacificist.helloandroid.ui.select.DragSelectTouchListener
import com.android.pacificist.helloandroid.ui.select.DragSelectionProcessor

class GalleryViewModel(view: RecyclerView) : DragSelectionProcessor.ISelectionHandler,
    RecyclerView.Adapter<GalleryViewModel.ViewHolder>() {

    val dragSelectTouchListener = DragSelectTouchListener(view, DragSelectionProcessor(this))

    val title = ObservableField("Gallery")
    private val items = ObservableField<List<Gallery>>()

    init {
        load()
    }

    private fun load() {
        val galleryList = ArrayList<Gallery>()
        for (i in 0..52) {
            galleryList.add(Gallery(i))
        }
        items.set(galleryList)
    }

    fun toggleSelected(gallery: Gallery) {
        val current = gallery.isSelected.get() ?: true
        gallery.isSelected.set(!current)
    }

    fun startDragSelection(gallery: Gallery): Boolean {
        items.get()?.indexOf(gallery)?.let {
            dragSelectTouchListener.startDragSelection(it)
            return true
        }
        return false
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
        holder.dataBinding.model = items.get()?.get(position)
        holder.dataBinding.vm = this
    }

    override fun getItemCount(): Int {
        return items.get()?.size ?: 0
    }

    class ViewHolder(binding: GridItemGalleryBinding) : RecyclerView.ViewHolder(binding.root) {
        val dataBinding = binding
    }

    override fun getSelection(): Set<Int> {
        val selection = HashSet<Int>()
        items.get()?.forEachIndexed { index, gallery ->
            if (gallery.isSelected.get() == true) {
                selection.add(index)
            }
        }
        return selection
    }

    override fun updateSelection(start: Int, end: Int, isSelected: Boolean) {
        Log.d("Gallery", "updateSelection[$start, $end]->$isSelected")
        for (i in start..end) {
            items.get()?.get(i)?.isSelected?.let {
                if (it.get() != isSelected) {
                    it.set(isSelected)
                }
            }
        }
    }
}
