package com.android.pacificist.helloandroid.data.repository

import android.database.Cursor
import android.provider.MediaStore
import android.util.Log
import com.android.pacificist.helloandroid.TAG
import com.android.pacificist.helloandroid.application
import com.android.pacificist.helloandroid.data.model.Gallery
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MediaRepository(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {
    private val uri = MediaStore.Files.getContentUri("external")

    private val projection = arrayOf(
        MediaStore.MediaColumns._ID,
        MediaStore.MediaColumns.DISPLAY_NAME,
        MediaStore.MediaColumns.DATA,
        MediaStore.MediaColumns.SIZE,
        MediaStore.MediaColumns.WIDTH,
        MediaStore.MediaColumns.HEIGHT,
        MediaStore.Files.FileColumns.MEDIA_TYPE,
        MediaStore.Images.ImageColumns.MIME_TYPE,
        MediaStore.Images.ImageColumns.ORIENTATION,
        MediaStore.MediaColumns.DATE_ADDED,
        MediaStore.Images.ImageColumns.DATE_TAKEN,
        MediaStore.MediaColumns.DATE_MODIFIED,
    )

    private val selection =
        """(${MediaStore.Files.FileColumns.MEDIA_TYPE} = ${MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO} 
        OR ${MediaStore.Files.FileColumns.MEDIA_TYPE} = ${MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE})""".trimMargin()

    private val sort = "${MediaStore.MediaColumns.DATE_MODIFIED} DESC"

    suspend fun loadMedias(): List<Gallery> = withContext(dispatcher) {
        val galleryList = mutableListOf<Gallery>()
        application?.contentResolver?.apply {
            var cursor: Cursor? = null
            try {
                cursor = query(uri, projection, selection, null, sort)?.apply {
                    if (count > 0) {
                        while (moveToNext()) {
                            parseMedia(this)?.let { galleryList.add(it) }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    cursor?.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        galleryList
    }

    private fun parseMedia(cursor: Cursor): Gallery? {
        var gallery: Gallery? = null
        try {
            gallery = Gallery(
                id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID)),
                name = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME)),
                path = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA)),
                size = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.SIZE)),
                width = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.WIDTH)),
                height = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.HEIGHT)),
                mediaType = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MEDIA_TYPE)),
                mimeType = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.MIME_TYPE)),
                orientation = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.ORIENTATION)),
                addTime = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_ADDED)),
                takeTime = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATE_TAKEN)),
                modified = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED))
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

        Log.d(TAG, "parseMedia: $gallery")
        return gallery
    }
}
