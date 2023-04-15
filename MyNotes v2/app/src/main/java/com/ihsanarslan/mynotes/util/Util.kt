package com.ihsanarslan.mynotes.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

val auth= Firebase.auth
val db = Firebase.firestore
val notesCollectionRef=db.collection("Notes")


fun barcolor(barrenk: Int,activity: Activity,context: Context) {
    val window: Window = activity.window
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = ContextCompat.getColor(context, barrenk)
}

fun setImageViewSize(width: Int, height: Int,colorview:Int,view: View) {
    val imageView = view.findViewById<ImageView>(colorview)
    val layoutParams = imageView?.layoutParams
    layoutParams?.width = width
    layoutParams?.height = height
    imageView?.layoutParams = layoutParams
}

fun resetImageViewSize(colorview:Int,view: View) {
    val imageView = view.findViewById<ImageView>(colorview)
    val layoutParams = imageView?.layoutParams
    layoutParams?.width = ViewGroup.LayoutParams.WRAP_CONTENT
    layoutParams?.height = ViewGroup.LayoutParams.WRAP_CONTENT
    imageView?.layoutParams = layoutParams
}

