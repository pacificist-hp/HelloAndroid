package com.android.pacificist.helloandroid.model

import androidx.databinding.ObservableField

data class Gallery(val id: Int) : IEdit {
    override var isSelected = ObservableField(false)

    fun setSelected(isSelected: Boolean) {
        this.isSelected.set(isSelected)
    }

    override fun toString(): String {
        return "Gallery[$id]{isSelected=${isSelected.get()}"
    }
}
