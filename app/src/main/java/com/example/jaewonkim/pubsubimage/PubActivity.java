package com.example.jaewonkim.pubsubimage;

//import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import java.util.UUID;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.*;

public class PubActivity extends Activity {

    ListView listView;
    ListViewAdapter adapter;
    EditText TagText;
    EditText ContentText;
    EditText UrlText;

    private MqttAndroidClient mMqttClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub);

        Intent intent = getIntent();

        adapter = new ListViewAdapter();
        listView = (ListView)findViewById(R.id.pubList);
        listView.setAdapter(adapter);

        TagText = (EditText)findViewById(R.id.TagText);
        ContentText = (EditText)findViewById(R.id.ContentText);
        UrlText = (EditText)findViewById(R.id.UrlText);

        ContentText.setText("hello content");
        UrlText.setText("https://upload.wikimedia.org/wikipedia/commons/thumb/2/2f/Google_2015_logo.svg/200px-Google_2015_logo.svg.png");

    }

    public void addPub(View v) {
        String tagName = TagText.getText().toString();
        String content = ContentText.getText().toString();
        String urlName = UrlText.getText().toString();
        tagName = tagName.replaceAll(" ", "");

        try {
            MqttAndroidClient mqttAndroidClient = MqttClientManager.getMqttClient();
            FeedPost fp = new FeedPost(UUID.randomUUID(), urlName, content);
            MqttMessage message = new MqttMessage();
            message.setPayload(fp.toJson().toString().getBytes());
            mqttAndroidClient.publish(tagName, message);
            System.out.println("LOG: " + "Message Published");
            if(!mqttAndroidClient.isConnected()){
                System.out.println("LOG: Publish failed because connection is lost");
            }
        } catch (MqttException e) {
            System.err.println("Error Publishing: " + e.getMessage());
            e.printStackTrace();
        }

        adapter.addItem(tagName+" : "+urlName);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
        TagText.setText("");
        UrlText.setText("");
    }
}
