package com.example.jaewonkim.pubsubimage;

//import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.*;

public class SubActivity extends Activity{

    ListView listView;
    ListViewAdapter adapter;

    EditText topicEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        adapter = new ListViewAdapter();
        listView = (ListView)findViewById(R.id.listview1);
        listView.setAdapter(adapter);

        topicEditText = (EditText)findViewById(R.id.TopicEditText);

//        adapter.addItem("Topic A");
//        adapter.addItem("Topic B");
//        adapter.addItem("Topic C");


    }

    public void addSubscTopic(View v) {
        String topicName = topicEditText.getText().toString();
        topicName = topicName.replaceAll(" ", "");

        try {
            MqttAndroidClient mqttAndroidClient = MqttClientManager.getMqttClient();
            mqttAndroidClient.subscribe(topicName, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    System.out.println("LOG: Subscribed!");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    System.out.println("LOG: Failed to subscribed!");
                }
            });
        } catch (MqttException ex){
            System.err.println("Exception whilst subscribing");
            ex.printStackTrace();
        }

        adapter.addItem(topicName);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
        topicEditText.setText("");
    }
}
