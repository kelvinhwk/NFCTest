# NFCTest
一个测试NFC点对点功能的应用 

## IntentFilter
### mime类型的IntentFilter
```
<intent-filter>
   <action android:name="android.nfc.action.NDEF_DISCOVERED" />
   <category android:name="android.intent.category.DEFAULT" />
   <data android:mimeType="application/chongfok.xyz" />
</intent-filter>
```

### ExternalType类型的IntentFilter
```
<intent-filter>
    <action android:name="android.nfc.action.NDEF_DISCOVERED" />
    <category android:name="android.intent.category.DEFAULT" />
    <!-- android:pathPrefix="/com.example:externaltype" 用小写，官网例子用externalType，过滤不了  -->
    <data android:scheme="vnd.android.nfc"
       android:host="ext"
       android:pathPrefix="/com.example:externaltype"/>
</intent-filter>
```

## 发送NdefMessage消息
### 发送静态消息
```
nfcAdapter.setNdefPushMessage(ndefMessage, this);
```
### 发送动态回调消息
```
 nfcAdapter.setNdefPushMessageCallback(new CreateNdefMessageCallback() {
            @Override
            public NdefMessage createNdefMessage(NfcEvent event) {
                // 根据业务动态创建消息
                ...
                return message;
            }
        }, this);
```

## 各类型的NdefRecord
### Mime类型
```
// 这里创建mime类型的记录，（第二个参数是mimeType），也可选择用NdefRecord的静态方法创建，
// 注意mimeType一定要和接收消息Activity的intentFilter中的过滤mimeType一致
record = new NdefRecord(
   NdefRecord.TNF_MIME_MEDIA,
   "application/chongfok.xyz".getBytes(Charset.forName("US-ASCII")),
   new byte[0], "Beam me up, Android!哈哈".getBytes(Charset.forName("utf-8")));
// record = NdefRecord.createMime("application/chongfok.xyz", "Beam me up, Android!哈哈".getBytes(Charset.forName("utf-8")));
```

### URI类型
```
NdefRecord record = NdefRecord.createUri("http://chongfok.xyz/index.html");
```

### 文本类型
```
NdefRecord record = NdefRecord.createTextRecord("zh_cn", "你好");
```

### ExternalType类型
```
NdefRecord record = NdefRecord.createExternal(domain, type, payload);
```

## 接收并解释NdefMessage
```
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
```

# 要注意的内容
1. uri类型的消息用intent filter尝试拦截，但拦截不到，系统直接用浏览器跳去该uri
2. external类型的，拦截器intent filter中的pathprefix可能要小写（官网例子中externalType的T是大写）
3. text类型的记录，payload会包含languagecode，所以还是考虑后续的记录用mime类型的记录。
4. 接收消息的Activity最好设置launchMode为singleTop，以便重新触碰后出发onNewIntent，在onNewIntent中setIntent，在onResume方法中解释intent中的NdefMessage消息





