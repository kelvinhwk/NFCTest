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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.nio.charset.Charset;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("kaikai", "onCreate");
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
    }


    /**
     * 发送mime类型的静态消息
     *
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void sendMime(View view) {

        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        NdefRecord record, record2;
        try {

            // 这里创建mime类型的记录，（第二个参数是mimeType），也可选择用NdefRecord的静态方法创建，
            // 注意mimeType一定要和接收消息Activity的intentFilter中的过滤mimeType一致
            record = new NdefRecord(
                    NdefRecord.TNF_MIME_MEDIA,
                    "application/chongfok.xyz".getBytes(Charset.forName("US-ASCII")),
                    new byte[0], "Beam me up, Android!哈哈".getBytes(Charset.forName("utf-8")));
//            record = NdefRecord.createMime("application/chongfok.xyz", "Beam me up, Android!哈哈".getBytes(Charset.forName("utf-8")));

            // 多追加一条text记录，但是接收端payload会包含languageCode
            // 此处languagecode若为空，会默认为zh('zh'会在payload中)
//            record2 = NdefRecord.createTextRecord(null, "this is my text3");
            // 第二条记录也改为mime类型，免得处理languageCode，第二条记录的mimeType则没有要求
            record2 = NdefRecord.createMime("application/xxx", "text2".getBytes(Charset.forName("utf-8")));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 用2条记录创建NdefMessage
        NdefMessage ndefMessage = new NdefMessage(record, record2);

        // 发送静态NdefMessage
        nfcAdapter.setNdefPushMessage(ndefMessage, this);

    }

    /**
     * 发送动态mime类型的消息
     *
     * @param view
     */
    public void sendMimeCallback(View view) {
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        // 此处发送动态mime类型的消息，两台机子触碰后，需要发送消息时，才回调这个方法生成NdefMessage，这样就可动态与用户界面进行交互
        // 例子是动态获取EditText组件的值再发送
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

    /**
     * 发送一个uri消息（接收端会跳转至该网址）
     *
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void sendUri(View view) {
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        NdefRecord record = NdefRecord.createUri("http://chongfok.xyz/index.html");
        NdefMessage ndefMessage = new NdefMessage(record);
        nfcAdapter.setNdefPushMessage(ndefMessage, this);
    }


    /**
     * 发送一个文本消息（接收端处理文本消息，在华为mate20会用记事本程序处理）
     *
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void sendText(View view) {
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        NdefRecord record = NdefRecord.createTextRecord("zh_cn", "你好");
        NdefMessage ndefMessage = new NdefMessage(record);
        nfcAdapter.setNdefPushMessage(ndefMessage, this);
    }


    /**
     * 发送一个externalType类型的消息
     * <p>
     * (mate20接收此种类型消息，不会自动弹出Activity，只会在消息栏显示通知，点击后才会跳到，
     * 但用三星note3接收却能自动跳转到Activity)
     *
     * @param view
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void sendExternalType(View view) {
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        byte[] payload = "external pay load".getBytes(Charset.forName("utf-8"));
        String domain = "com.example";
        String type = "externalType"; // T可能会变为小写
        NdefRecord record = NdefRecord.createExternal(domain, type, payload);
        NdefMessage message = new NdefMessage(record);
        nfcAdapter.setNdefPushMessage(message, this);
    }
}
