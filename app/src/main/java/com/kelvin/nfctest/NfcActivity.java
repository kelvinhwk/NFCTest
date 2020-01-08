package com.kelvin.nfctest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;

public class NfcActivity extends AppCompatActivity {

    TextView textView_2;
    TextView textView_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfc);

        textView_2 = findViewById(R.id.textView2);
        textView_3 = findViewById(R.id.textView3);
    }

    /**
     * launchMode=singleTop,nfc重新触碰后会触发onNewIntent,onNewIntent调用完后会调用onResume
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("kaikai", "onNewIntent -- NfcActivity");
        this.setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("kaikai", "onResume -- NfcActivity");

        Intent intent = getIntent();

        Log.d("kaikai", "action:" + intent.getAction());

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            processIntent(intent);
        }

    }

    /**
     * 处理Ndef消息
     *
     * @param intent
     */
    private void processIntent(Intent intent) {
        Log.d("kaikai", "processIntent");

        // 从intent中获取Parcelable[]
        Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

        // 强制转换为NdefMessage
        NdefMessage message = (NdefMessage) parcelables[0];

        try {
            // 获取message中各记录的内容
            String text2 = new String(message.getRecords()[0].getPayload(), "utf-8");
            Toast.makeText(this, text2, Toast.LENGTH_SHORT).show();
            Log.d("kaikai", "text2:" + text2);
            textView_2.setText(text2);

            // 若有更多记录，则继续处理...
            if (message.getRecords().length > 1) {
                String text3 = new String(message.getRecords()[1].getPayload(), "utf-8");
                Log.d("kaikai", "text3:" + text3);
                textView_3.setText(text3);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
