package com.example.musiclly

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.musiclly.model.UserModel

object AndroidUtil {


    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    fun passUserModelAsIntent(intent: Intent, model: UserModel) {
        intent.putExtra("username", model.username)
        intent.putExtra("phone", model.phone)
        intent.putExtra("userId", model.userId)
        intent.putExtra("fcmToken", model.fcmToken)
    }

    fun getUserModelFromIntent(intent: Intent): UserModel {
        return UserModel().apply {
            username = intent.getStringExtra("username") ?: ""
            phone = intent.getStringExtra("phone") ?: ""
            userId = intent.getStringExtra("userId") ?: ""
            fcmToken = intent.getStringExtra("fcmToken") ?: ""
        }
    }

    fun setProfilePic(context: Context, imageUri: Uri, imageView: ImageView) {
        Glide.with(context)
            .load(imageUri)
            .apply(RequestOptions.circleCropTransform())
            .into(imageView)
    }
}