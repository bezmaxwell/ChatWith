package com.example.chatwith.entity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.chatwith.views.MainActivity
import com.example.chatwith.views.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(FirebaseAuth.getInstance().currentUser == null)
            startActivity<LoginActivity>()
            else
                startActivity<MainActivity>()
        finish()

    }
}
