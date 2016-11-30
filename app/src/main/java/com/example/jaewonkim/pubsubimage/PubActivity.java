package com.example.jaewonkim.pubsubimage;

//import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.*;

public class PubActivity extends Activity {

    ListView listView;
    ListViewAdapter adapter;
    EditText ImgURL;
    EditText ContentText;

    private MqttAndroidClient mMqttClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub);

        Intent intent = getIntent();

        adapter = new ListViewAdapter();
        listView = (ListView)findViewById(R.id.pubList);
        listView.setAdapter(adapter);

        ImgURL = (EditText)findViewById(R.id.ImgURL);
        ContentText = (EditText)findViewById(R.id.ContentText);

        ImgURL.setText("https://upload.wikimedia.org/wikipedia/commons/thumb/2/2f/Google_2015_logo.svg/200px-Google_2015_logo.svg.png");
        ContentText.setText("#hello #world content #tag_anywhere");

    }

    public void addPub(View v) {
        Pattern tagPattern = Pattern.compile("#(\\w+)");

        String urlName = ImgURL.getText().toString();
        String content = ContentText.getText().toString();

        Matcher match = tagPattern.matcher(content);

        try {
            MqttAndroidClient mqttAndroidClient = MqttClientManager.getMqttClient();
            FeedPost fp = new FeedPost(UUID.randomUUID(), urlName, content);
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

                adapter.addItem(curTag + " : " + urlName);
            }
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
        ImgURL.setText("");
        ContentText.setText("");
    }
}
