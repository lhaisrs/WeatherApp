package com.example.weatherapp.notifications

import android.util.Log
import com.example.weatherapp.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.inlocomedia.android.engagement.InLocoEngagement
import com.inlocomedia.android.engagement.PushMessage
import com.inlocomedia.android.engagement.request.FirebasePushProvider

class WeatherAppFirebaseMessagingService : FirebaseMessagingService() {

    //Debug TAG
    private val DEBUG_RECEIVE = "Firebase Received"
    private val DEBUG_TOKEN ="Token"

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        super.onMessageReceived(remoteMessage)

        val data = remoteMessage?.data
        if(data != null) {
            val pushContent = InLocoEngagement.decodeReceivedMessage(this, data)

            if(pushContent != null){
                InLocoEngagement.presentNotification(
                    this,
                    pushContent,
                    R.drawable.ic_notification,
                    1111111
                )
            } else {
                Log.d(DEBUG_RECEIVE, "Regular Message")
            }
        }
    }

    override fun onNewToken(token: String?) {
        super.onNewToken(token)
        if(token != null && !token.isEmpty()) {
            val pushProvider = FirebasePushProvider.Builder()
                .setFirebaseToken(token)
                .build()

            InLocoEngagement.setPushProvider(this, pushProvider)
        }
    }
}
