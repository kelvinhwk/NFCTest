package com.kelvin.nfctest;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcEvent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity {


    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("kaikai", "onCreate");

        textView = findViewById(R.id.helloworld);


    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("kaikai", "onNewIntent");
        setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("kaikai", "onResume");

//        Intent intent = getIntent();
//
//        Log.d("kaikai", "action:" + intent.getAction());
//
//        if (intent.getAction().equals(NfcAdapter.ACTION_NDEF_DISCOVERED)) {
//            processIntent(intent);
//        }
//        else {
//            Log.d("kaikai", "action不是NDEFDiscovered");
////            processIntent(intent);
//        }
    }

//    private void processIntent(Intent intent) {
//
//        Log.d("kaikai", "processIntent");
//
//        Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
//
//        NdefMessage message = (NdefMessage) parcelables[0];
//
//        try {
//            String text = new String(message.getRecords()[1].getPayload(), "utf-8");
//            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
//            Log.d("kaikai", text);
//
//            textView_2.setText(text);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void sendMime(View view) {

        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        NdefRecord record, record2;
        try {
//            record = NdefRecord.createUri("http://chongfok.xyz/index.html");
//            record = NdefRecord.createApplicationRecord("com.kelvin.nfctest");
//            record = NdefRecord.createTextRecord("zh_cn", "kaikai text");

//            record = new NdefRecord(
//                    NdefRecord.TNF_ABSOLUTE_URI ,
//                    "http://chongfok.xyz/index.html".getBytes(Charset.forName("US-ASCII")),
//                    new byte[0], new byte[0]);

            record = new NdefRecord(
                    NdefRecord.TNF_MIME_MEDIA,
                    "application/chongfok.xyz".getBytes(Charset.forName("US-ASCII")),
                    new byte[0], "Beam me up, Android!哈哈".getBytes(Charset.forName("utf-8")));

            // 此处languagecode若为空，会默认为zh('zh'会在payload中)
            record2 = NdefRecord.createTextRecord(null, "this is my text3");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        NdefMessage ndefMessage;
        ndefMessage = new NdefMessage(record, record2);

        nfcAdapter.setNdefPushMessage(ndefMessage, this);

    }

    public void sendMimeCallback(View view) {
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        nfcAdapter.setNdefPushMessageCallback(new CreateNdefMessageCallback() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public NdefMessage createNdefMessage(NfcEvent event) {

                EditText editText = MainActivity.this.findViewById(R.id.editText);

                String text = editText.getText().toString();

                NdefRecord record = NdefRecord.createMime("application/chongfok.xyz", text.getBytes(Charset.forName("utf-8")));

                NdefMessage message = new NdefMessage(record);

                return message;
            }
        }, this);

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void sendUri(View view) {
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        NdefRecord record = NdefRecord.createUri("http://chongfok.xyz/index.html");
        NdefMessage ndefMessage = new NdefMessage(record);
        nfcAdapter.setNdefPushMessage(ndefMessage, this);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void sendText(View view) {
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        NdefRecord record = NdefRecord.createTextRecord("zh_cn", "你好");
        NdefMessage ndefMessage = new NdefMessage(record);
        nfcAdapter.setNdefPushMessage(ndefMessage, this);
    }


}
