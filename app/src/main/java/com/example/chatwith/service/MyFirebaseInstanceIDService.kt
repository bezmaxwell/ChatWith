package com.example.chatwith.service

import android.util.JsonToken
import com.example.chatwith.util.FirestoreUtil
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import java.lang.NullPointerException

open class MyFirebaseInstanceIDService: MyFirebaseInstanceIDService()  {

        fun onTokenRefresh()  {
         val newRegistrationToken = FirebaseInstanceId.getInstance().token

        if(FirebaseAuth.getInstance().currentUser !=null)
            addTokenToFirestore(newRegistrationToken)
     }

    companion object {
        fun addTokenToFirestore (newRegistrationToken: String?) {
            if(newRegistrationToken == null)  throw NullPointerException("FCM token is null !!!")

            FirestoreUtil.getFCMRegistrationTokens { tokens ->  
                if(tokens.contains(newRegistrationToken))
                    return@getFCMRegistrationTokens

                tokens.add(newRegistrationToken)
                FirestoreUtil.setFCMRegistrationTokens(tokens)
            }
        }
    }

}