package com.android.pacificist.helloandroid.ui

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.android.pacificist.helloandroid.R

/**
 * Created by pacificist on 2023/7/15.
 */
class GalleryActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)
    }
}