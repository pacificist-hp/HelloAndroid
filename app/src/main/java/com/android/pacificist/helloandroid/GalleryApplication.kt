package com.android.pacificist.helloandroid

import android.app.Application

const val TAG = "Gallery"

var application: Application? = null

class GalleryApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        application = this
    }
}