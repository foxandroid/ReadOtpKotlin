package com.example.readotpkotlin


import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.material.textfield.TextInputEditText
import java.util.regex.Pattern


class MainActivity : AppCompatActivity() {

    private val REQ_USER_CONSENT = 200
    var smsBroadcastReceiver : SmsBroadcastReceiver? = null
    var etOtp : TextInputEditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etOtp = findViewById(R.id.etOTP)
        startSmartUserConsent()

    }

    private fun startSmartUserConsent() {

        val client = SmsRetriever.getClient(this)
        client.startSmsUserConsent(null)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQ_USER_CONSENT){

            if(resultCode == RESULT_OK && data != null){

                val message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE)
                getOtpFromMessage(message)


            }

        }

    }

    private fun getOtpFromMessage(message: String?) {

        val otpPatter = Pattern.compile("(|^)\\d{6}")
        val matcher = otpPatter.matcher(message)
        if (matcher.find()){

            etOtp!!.setText(matcher.group(0))

        }


    }

    private fun registerBroadcastReceiver(){

        smsBroadcastReceiver = SmsBroadcastReceiver()
        smsBroadcastReceiver!!.smsBroadcasrReceiverListener = object  : SmsBroadcastReceiver.SmsBroadcasrReceiverListener{
            override fun onSuccess(intent: Intent?) {

                startActivityForResult(intent,REQ_USER_CONSENT)

            }

            override fun onFailure() {
                
            }


        }

        val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
        registerReceiver(smsBroadcastReceiver,intentFilter)

    }

    override fun onStart() {
        super.onStart()
        registerBroadcastReceiver()

    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(smsBroadcastReceiver)
    }

}