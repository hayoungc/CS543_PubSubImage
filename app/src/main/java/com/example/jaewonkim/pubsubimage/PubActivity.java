package com.example.jaewonkim.pubsubimage;

//import android.support.v7.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.UUID;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PubActivity extends Activity {

    ListView listView;
    ListViewAdapter adapter;
    EditText ImgURL;
    EditText ContentText;

    final String defaultURL = "https://s16.postimg.org/3z1pfmskl/Common_dog_behaviors_explained.jpg"; // https://s17.postimg.org/8rk66o6q7/scroll0015.jpg";
    final String defaultContent = "This is my cute baby! #dog #puppy";

    private MqttAndroidClient mMqttClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub);

        Intent intent = getIntent();

        adapter = new ListViewAdapter(ListViewAdapter.LISTVIEW_TYPE_PUBLISH);
        listView = (ListView)findViewById(R.id.pubList);
        listView.setAdapter(adapter);

        ImgURL = (EditText)findViewById(R.id.ImgURL);
        ContentText = (EditText)findViewById(R.id.ContentText);

        ImgURL.setText(defaultURL);
        ContentText.setText(defaultContent);

    }

    public void addPub(View v) {
        Pattern tagPattern = Pattern.compile("#(\\w+)");

        String urlName = ImgURL.getText().toString();
        String content = ContentText.getText().toString();

        Matcher match = tagPattern.matcher(content);

        try {
            MqttAndroidClient mqttAndroidClient = MqttClientManager.getMqttClient();
            FeedPost fp = new FeedPost(UUID.randomUUID(), getCurrentTimeStamp(), urlName, content);
            MqttMessage message = new MqttMessage();
            message.setPayload(fp.toJson().toString().getBytes());

            while (match.find()) {
                String curTag = match.group().substring(1);
                mqttAndroidClient.publish(curTag, message);
                System.out.println("LOG: Message Published on tag: " + curTag);
                if (!mqttAndroidClient.isConnected()) {
                    System.out.println("LOG: Publish failed because connection is lost");
                    break;
                }
            }

            adapter.addItem(content);
        } catch (MqttException e) {
            System.err.println("Error Publishing: " + e.getMessage());
            e.printStackTrace();
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
        ImgURL.setText(defaultURL);
        ContentText.setText(defaultContent);
    }

    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }
}
