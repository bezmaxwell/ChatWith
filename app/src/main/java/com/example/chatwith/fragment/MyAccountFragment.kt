package com.example.chatwith.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.*
import com.bumptech.glide.annotation.GlideModule


import com.example.chatwith.R
import com.example.chatwith.util.FirestoreUtil
import com.example.chatwith.util.StorageUtil
import com.example.chatwith.views.LoginActivity
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.auth.api.Auth
import kotlinx.android.synthetic.main.fragment_my_account.*
import kotlinx.android.synthetic.main.fragment_my_account.view.*
import org.jetbrains.anko.clearTask
import org.jetbrains.anko.newTask
import org.jetbrains.anko.support.v4.act
import org.jetbrains.anko.support.v4.intentFor
import java.io.ByteArrayOutputStream
import com.bumptech.glide.module.AppGlideModule as AppGlideModule1

/**
 * A simple [Fragment] subclass.
 */
class MyAccountFragment : Fragment() {

    private val RC_SELECT_IMAGE = 2
    private lateinit var selectedImageBytes: ByteArray
    private var pictureJustChanged = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_account, container, false)

        val apply = view.apply {
            imageView_profile_picture.setOnClickListener {
                val intent = Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_GET_CONTENT
                    putExtra(
                        Intent.EXTRA_MIME_TYPES,
                        arrayOf("image/jpeg", "image/png", "image/jpg")
                    )
                }

                startActivityForResult(
                    Intent.createChooser(intent, "Select Image"),
                    RC_SELECT_IMAGE
                )
                btn_save.setOnClickListener {
                    if (::selectedImageBytes.isInitialized)
                        StorageUtil.uploadProfilePhoto(selectedImageBytes) { imagePath ->
                            FirestoreUtil.updateCurrentUser(
                                editText_name.text.toString(),
                                editText_bio.text.toString(), imagePath
                            )
                        }
                    else

                        StorageUtil.uploadProfilePhoto(selectedImageBytes) { imagePath ->
                            FirestoreUtil.updateCurrentUser(
                                editText_name.text.toString(),
                                editText_bio.text.toString(), null
                            )

                        }
                }
                btn_sign_out.setOnClickListener {
                    AuthUI.getInstance()
                        .signOut(this@MyAccountFragment.context!!)
                        .addOnCompleteListener {
                            startActivity(intentFor<LoginActivity>().newTask().clearTask())
                        }

                }
            }
            return view
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK &&
            data != null && data.data != null
        ) {
            val selectedImagePath = data.data
            val selectImageBmp = MediaStore.Images.Media
                .getBitmap(activity?.contentResolver, selectedImagePath)

            val outputStream = ByteArrayOutputStream()
            selectImageBmp.compress(
                Bitmap.CompressFormat.JPEG, 90,
                outputStream
            )
            selectedImageBytes = outputStream.toByteArray()

            Glide.with(this).load(selectedImageBytes)
                .into(imageView_profile_picture)

            pictureJustChanged = true
        }
    }


    override fun onStart() {
        super.onStart()
        FirestoreUtil.getCurrentUser { user ->
            if (this@MyAccountFragment.isVisible) {
                editText_name.setText(user.name)
                editText_bio.setText(user.bio)
                if (!pictureJustChanged && user.profilePicturePath != null)

                // Possivel error
                    Glide.with(this)
                                .load(user.profilePicturePath)
                        .into(imageView_profile_picture)

            }
        }
    }
}