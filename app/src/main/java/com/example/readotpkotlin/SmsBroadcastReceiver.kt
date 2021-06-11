package com.example.readotpkotlin

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.AudioRecord.SUCCESS
import android.provider.Telephony
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import javax.net.ssl.SSLEngineResult

class SmsBroadcastReceiver : BroadcastReceiver() {

    var smsBroadcasrReceiverListener : SmsBroadcasrReceiverListener ?= null

    override fun onReceive(context: Context?, intent: Intent?) {

        if  (SmsRetriever.SMS_RETRIEVED_ACTION == intent?.action){

            val extras = intent.extras
            val smsRetrieverStatus = extras?.get(SmsRetriever.EXTRA_STATUS) as Status

            when(smsRetrieverStatus.statusCode){

                CommonStatusCodes.SUCCESS ->{

                    val messageIntent = extras.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT)
                    smsBroadcasrReceiverListener?.onSuccess(messageIntent)


                }
                CommonStatusCodes.TIMEOUT -> {


                    smsBroadcasrReceiverListener?.onFailure()

                }
            }

        }

    }

    interface SmsBroadcasrReceiverListener{

        fun onSuccess(intent: Intent?)
        fun onFailure()

    }


}