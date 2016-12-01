package com.example.jaewonkim.pubsubimage;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TabHost;
import android.widget.Toast;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.*;
import org.json.JSONObject;

import java.util.LinkedList;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {

    private static String TAG = "MainActivity";

    private static String TAB_TAG_FEED = "Feed";

    private MqttAndroidClient mMqttClient;
    private TabHost mTab;
    private LinkedList<FeedPost> mPostQueue;
    private Object lPostQueue = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTab = (TabHost) findViewById(android.R.id.tabhost);
        //tab.setup(this.getLocalActivityManager());
        //tab.setup();

        TabHost.TabSpec spec1 = mTab.newTabSpec("Subscribe");
        spec1.setIndicator(getString(R.string.tab_subscribe));
        spec1.setContent(new Intent(this, SubActivity.class));
        mTab.addTab(spec1);

        TabHost.TabSpec spec2 = mTab.newTabSpec("Publish");
        spec2.setIndicator(getString(R.string.tab_publish));
        spec2.setContent(new Intent(this, PubActivity.class));
        mTab.addTab(spec2);

        TabHost.TabSpec spec3 = mTab.newTabSpec(TAB_TAG_FEED);
        spec3.setIndicator(getString(R.string.tab_feed));
        spec3.setContent(new Intent(this, FeedActivity.class));
        mTab.addTab(spec3);

        // Add a event listener to consume the post queue when the Feed tab gets active.
        mTab.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                synchronized (lPostQueue) {
                    if (s.equals(TAB_TAG_FEED) && !mPostQueue.isEmpty()) {
                        FeedActivity feedActivity = (FeedActivity) getLocalActivityManager().getCurrentActivity();
                        feedActivity.addNewPosts(mPostQueue);
                        mPostQueue.clear();
                    }
                }
            }
        });

        // Create a queue for posts which are arrived when the Feed tab is not active.
        mPostQueue = new LinkedList<>();

        // Establish a connection to the MQTT broker and set the callback.
        MqttClientManager.setContext(getApplicationContext());
        mMqttClient = MqttClientManager.getMqttClient();
        try {
            mMqttClient.connect();
            mMqttClient.setCallback(new MainMqttCallback());
            Toast.makeText(this, R.string.toast_mqtt_success, Toast.LENGTH_SHORT).show();
        } catch (MqttException e) {
            Log.e(TAG, "failed to connect to the MQTT broker", e);
            Toast.makeText(this, R.string.toast_mqtt_fail, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Close the connection to the MQTT broker.
        if (null == mMqttClient) {
            return;
        }
        try {
            if (mMqttClient.isConnected()) {
                mMqttClient.disconnect();
            }
        } catch (MqttException e) {
            Log.e(TAG, "failed to disconnect from the MQTT broker", e);
        }
        mMqttClient.unregisterResources();
    }

    private void addToFeed(FeedPost post) {
        if (null != mTab && mTab.getCurrentTabTag().equals(TAB_TAG_FEED)) {
            // Current tab is the Feed tab. Put the new post into the child activity right away.
            FeedActivity feedActivity = (FeedActivity) getLocalActivityManager().getCurrentActivity();
            feedActivity.addNewPost(post);
        } else {
            // Current tab is not the Feed tab or tab host is not available. Put the new post into the queue.
            synchronized (lPostQueue) {
                mPostQueue.addFirst(post);
            }
        }
    }

    class MainMqttCallback implements MqttCallback {

        private static final String TAG = "MainMqttCallback";

        @Override
        public void connectionLost(Throwable cause) {
            Log.e(TAG, "connection to MQTT broker lost", cause);
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            byte[] payload = message.getPayload();
            String jsonStr = new String(payload, "UTF8");
            System.out.println("Arrived Message: " + jsonStr);
            JSONObject json = new JSONObject(jsonStr);
            FeedPost post = new FeedPost(json);
            if (!FeedPost.hasReceived(post)) {
                FeedPost.receive(post);
                addToFeed(post);
            }
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            Log.d(TAG, "completed to deliver a MQTT message (token:" + token.toString() + ")");
        }
    }

}
