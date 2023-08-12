package com.android.pacificist.helloandroid.model

import androidx.databinding.ObservableField

data class Gallery(val id: Int) : IEdit {
    override val isSelected = ObservableField(false)

    override fun toString(): String {
        return "Gallery[$id]{isSelected=${isSelected.get()}"
    }
}
