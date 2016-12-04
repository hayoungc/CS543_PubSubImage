package com.example.jaewonkim.pubsubimage;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import android.content.Context;

/**
 * Created by mgjin on 2016-11-29.
 */

public class MqttClientManager {
    // MQTT broker running on Sanggyu's computer in his dorm.
    private static String MQTT_SERVER_URI = "tcp://110.76.84.177:1883";
    private static MqttAndroidClient mMqttClient = null;

    public static void setContext(Context c) {
        mMqttClient = new MqttAndroidClient(c, MQTT_SERVER_URI, MqttClient.generateClientId());
    }

    public static MqttAndroidClient getMqttClient() {
        return mMqttClient;
    }
}
