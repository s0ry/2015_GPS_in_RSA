
package com.example.hyunmo.gps_in_rsa;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

// SMS 수신 시, 전화 번호와 메세지 저장
public class ReceiveSMS extends BroadcastReceiver{

    String receiveNumber = "";
    String receiveText = "";

    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;

        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                receiveNumber = msgs[i].getOriginatingAddress();
                receiveText = msgs[i].getMessageBody().toString();
            }
            // skipping PDU header, keeping only message body
            Intent intent2 = new Intent(MainActivity.BROADCAST_FINISH);
            intent2.putExtra("receiveNumber", receiveNumber);
            intent2.putExtra("receiveText", receiveText);
            context.sendBroadcast(intent2);
        }
    }
}
