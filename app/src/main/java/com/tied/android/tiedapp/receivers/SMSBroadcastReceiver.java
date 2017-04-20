package com.tied.android.tiedapp.receivers;

/**
 * Created by Emmanuel on 6/16/2016.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import com.tied.android.tiedapp.util.MyUtils;

import java.util.Objects;


public class SMSBroadcastReceiver extends BroadcastReceiver {

    public static final String TAG = SMSBroadcastReceiver.class
            .getSimpleName();

    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "Intent recieved: " + intent.getAction());

        if (Objects.equals(intent.getAction(), SMS_RECEIVED)) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[])bundle.get("pdus");
                final SmsMessage[] messages = new SmsMessage[pdus.length];
                for (int i = 0; i < pdus.length; i++) {
                    messages[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                }
                if (messages.length > -1) {
                    //check if message contains the worked tied
                    String msg =messages[0].getMessageBody();
                    if(msg.contains("code")) {
                        String[] prs=msg.trim().split(" ");
                        String code=prs[prs.length-1];
                        try{
                            Integer.parseInt(code);
                        }catch (Exception e) {
                            return;
                        }
                       SharedPreferences.Editor editor = MyUtils.getSharedPreferences().edit();
                        editor.putString("VERIFICATION_CODE", code);
                        editor.apply();
                    }
                   // Log.i(TAG, "Message recieved: " + messages[0].getMessageBody());
                }
            }
        }
    }
}
