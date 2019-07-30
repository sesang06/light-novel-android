package com.sesang06.lightnovellist.service

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.Handler
import android.view.PixelCopy
import android.view.View
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

final class CaptureManager(activity: Activity) {


    private val activity = activity


    fun getScreenShot(view: View, completion: () -> Unit) {

        try {
            val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
            if (Build.VERSION.SDK_INT >= 26) {
                val locationOfViewInWindow = IntArray(2)
                view.getLocationInWindow(locationOfViewInWindow)
                PixelCopy.request(
                    activity.window,
                    Rect(
                        locationOfViewInWindow[0],
                        locationOfViewInWindow[1],
                        locationOfViewInWindow[0] + view.width,
                        locationOfViewInWindow[1] + view.height
                    ),
                    bitmap, { copyResult ->
                        if (copyResult == PixelCopy.SUCCESS) {
                            this.saveBitMap(bitmap, completion)
                        }
                    }, Handler()
                )
            } else {
                val canvas = Canvas(bitmap)
                val bgDrawable = view.background
                if (bgDrawable != null)
                    bgDrawable.draw(canvas)
                else
                    canvas.drawColor(Color.WHITE)
                view.draw(canvas)
                this.saveBitMap(bitmap, completion)
            }

        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }

    }


    private fun saveBitMap(bitmap: Bitmap, completion: () -> Unit) {
        val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
        val format = SimpleDateFormat(
            "yyyyMMddHHmmss",
            Locale.getDefault()
        ).format(Date())

        val file = File(path, "screenshot-$format.png")

        try {
            val os = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
            os.flush()
            os.close()
            activity.sendBroadcast(Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)))
            completion()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

}