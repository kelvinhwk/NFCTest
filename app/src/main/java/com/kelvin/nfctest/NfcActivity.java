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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        this.setIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = getIntent();

        Log.d("kaikai", "action:" + intent.getAction());

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            processIntent(intent);
        }

    }

    private void processIntent(Intent intent) {
        Log.d("kaikai", "processIntent");

        Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

        NdefMessage message = (NdefMessage) parcelables[0];

        try {
            String text = new String(message.getRecords()[0].getPayload(), "utf-8");
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            Log.d("kaikai", "text:" + text);

            textView_2.setText(text);

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
