package com.example.hyunmo.gps_in_rsa;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView R_numberText; // 번호 입력 TextView
    private EditText R_Editnumber; // 번호 입력 EditText
    private Button R_Check;         // Check
    private Button Send;            // Send
    private EditText R_Text;        // 상태 메세지, 주소 출력
    private String R_string_number;// 번호 저장 변수

    private GpsInfo gps;             // GPS 클래스
    private RSA rsa;                 // RSA 클래스
    private String PlainText;       // 평문
    private String ResultText_C;   // 위도 복호화텍스트 저장
    private String ResultText_L;   // 경도 복호화 텍스트 저장
    private int count;              // 진행 상태 count
    private int count2;             // 진행 상태 count

    public final static String BROADCAST_FINISH = "finish";

    // SMS 받았을 때
    private final BroadcastReceiver finishReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            String number = intent.getStringExtra("receiveNumber");
            PlainText = intent.getStringExtra("receiveText");

            // 받은 문자가 KEYO (KEY ONE) 일 때
            if (PlainText.substring(0,4).compareTo("KEYO") == 0) {
                PlainText = PlainText.substring(4);
                rsa.pushPublicKey(PlainText, 1);
                count++;
                if (count == 2){
                    R_Text.setText("키를 수신했습니다.");
                    SystemClock.sleep(1000);
                    sendSMS(number, 2);
                }
            }
            // 받은 문자가 KEYT (KEY TWO) 일 때
            if (PlainText.substring(0,4).compareTo("KEYT") == 0){
                PlainText = PlainText.substring(4);
                rsa.pushPublicKey(PlainText, 2);
                count++;
                if (count == 2){
                    R_Text.setText("키를 수신했습니다.");
                    SystemClock.sleep(1000);
                    sendSMS(number, 2);
                }
            }
            // 받은 문자가 위도 Cipher 일 때
            if (PlainText.substring(0,1).compareTo("C") == 0){
                PlainText = PlainText.substring(1);
                rsa.decrypt(PlainText);
                ResultText_C = rsa.getStrResult();
                count2++;
                if (count2 == 2){
                    R_Text.setText("GPS 암호문을 수신했습니다.");
                    SystemClock.sleep(1000);
                    PlainText = ResultText_C + "L" + ResultText_L;
                    pushGPSinfo(PlainText);
                }
            }
            // 받은 문자가 경도 Cipher 일 때
            if (PlainText.substring(0,1).compareTo("L") == 0){
                PlainText = PlainText.substring(1);
                rsa.decrypt(PlainText);
                ResultText_L = rsa.getStrResult();
                count2++;
                if (count2 == 2){
                    R_Text.setText("GPS 암호문을 수신했습니다.");
                    SystemClock.sleep(1000);
                    PlainText = ResultText_C + "L" + ResultText_L;
                    pushGPSinfo(PlainText);
                }
            }
        }
    };  // finishReceiver

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        count = 0;
        count2 = 0;
        rsa = new RSA();
        gps = new GpsInfo(MainActivity.this);
        ResultText_C = null;
        ResultText_L = null;
        // UI
        R_numberText = (TextView) findViewById(R.id.numberText);
        R_Editnumber = (EditText) findViewById(R.id.Editnumber);
        R_Check = (Button) findViewById(R.id.R_check);
        Send = (Button) findViewById(R.id.B_check);
        R_Text = (EditText) findViewById(R.id.ReceiveText);

        SharedPreferences pref1 = getSharedPreferences("R_NUMBER", MODE_PRIVATE);   // 번호 입력 토큰
        R_string_number = pref1.getString("R_Phone_Number", "");                    // 번호 저장
        // EditText 지정
        R_Editnumber.setText(R_string_number);

        if(R_string_number.equals("")) {
            R_string_number = null;
        }

        // R_check 버튼 클릭 리스너
        R_Check.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if (R_Editnumber.getText().toString().length() == 0) {   // 입력 값이 없을 때
                    R_numberText.setText("Input device number");
                }
                else{                  // 입력 값이 존재할 때
                    R_string_number = R_Editnumber.getText().toString();
                    SharedPreferences pref1 = getSharedPreferences("R_NUMBER", MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = pref1.edit();
                    editor1.putString("R_Phone_Number", R_string_number);
                    editor1.commit();
                    R_numberText.setText("번호가 저장되었습니다");
                }
            }
        }); //OnClickListener

        // Send 버튼 클릭 리스너
        Send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMS(R_string_number, 1);
            }
        });   //OnClickListener

    }   //OnCreatere

    // SMS 전송 (보낼 번호, 진행상태)
    private void sendSMS(String sendNumber, int numbering)
    {
        if (numbering == 1){    // numbering == 1, 키값 전송
            rsa.GenKey();

            String PulicKey1;
            String PulicKey2;
            String a1, a2;

            a1 = rsa.getPublicKeyModule();
            a2 = rsa.getPublicKeyExponent();

            PulicKey1 = "KEYO" + a1;
            PulicKey2 = "KEYT" + a2;
            SmsManager sms = SmsManager.getDefault();

            sms.sendTextMessage(sendNumber, null, PulicKey1, null, null);
            sms.sendTextMessage(sendNumber, null,  PulicKey2, null, null);
            R_Text.setText("키를 송신했습니다.");
            SystemClock.sleep(1000);
        }
        else if (numbering == 2){   // numbering == 2, GPS(Cipher)값 전송
            double a = gps.GetLatitude();
            double b = gps.GetLongitude();

            int x = (int)(a * 10000000);
            int y = (int)(b * 10000000);

            String str_x = Integer.toString(x);
            String str_y = Integer.toString(y);

            rsa.encrypt(str_x);
            String encyp_x = rsa.getStrCipher();

            rsa.encrypt(str_y);
            String encyp_y = rsa.getStrCipher();

            String sendText1 = "C" + encyp_x;
            String sendText2 = "L" + encyp_y;

            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(sendNumber, null, sendText1, null, null);
            sms.sendTextMessage(sendNumber, null, sendText2, null, null);
            R_Text.setText("GPS 암호문을 송신했습니다.");
            SystemClock.sleep(1000);
        }
    }

    // 위도 경도 값을 잘라 주소를 출력
    public void pushGPSinfo(String s){
        gps.changeLocation(s);
        String tmp = gps.GetAddress();
        String out = "복호화 완료...";
        R_Text.setText(out);
        SystemClock.sleep(2000);
        R_Text.setText(tmp);
}

    @Override
    protected void onResume()
    {
        super.onResume();
        registerReceiver(finishReceiver, new IntentFilter(BROADCAST_FINISH));
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        unregisterReceiver(finishReceiver);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }
}