package com.example.jaewonkim.pubsubimage;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.ArrayList;

/**
 * Created by Jaewon Kim on 2016-05-18.
 */
public class ListViewAdapter  extends BaseAdapter {

    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();
    private static final long serialVersionUID = 1L;

    private int viewType = 0;

    public static final int LISTVIEW_TYPE_SUBSCRIBE = 0;
    public static final int LISTVIEW_TYPE_PUBLISH = 1;

    public ListViewAdapter() {
        this(LISTVIEW_TYPE_SUBSCRIBE);
    }

    public ListViewAdapter(int listviewType) {
        viewType = listviewType;
    }


    @Override
    public int getCount() {
        return Math.max(listViewItemList.size(), 8);
    }

    @Override
    public Object getItem(int position) {
        if (position < listViewItemList.size())
            return listViewItemList.get(position);
        else
            return new ListViewItem();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final int pos = position;
        Context context = parent.getContext();

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_item, parent, false);
        }

        final TextView titleTextView = (TextView) convertView.findViewById(R.id.TopicName) ;
        final Button TopicActivation = (Button) convertView.findViewById(R.id.TopicActivation);
        final Button TopicUnsubsc = (Button) convertView.findViewById(R.id.TopicUnsubsc);

        if (position < listViewItemList.size()) {
            // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
            final ListViewItem listViewItem = listViewItemList.get(position);
            //titleTextView.setSingleLine(true);

            // 아이템 내 각 위젯에 데이터 반영
            titleTextView.setText(listViewItem.getTopicName());

            if (viewType == LISTVIEW_TYPE_PUBLISH) {
                TopicActivation.setVisibility(View.INVISIBLE);
                TopicUnsubsc.setVisibility(View.INVISIBLE);
            } else {
                TopicActivation.setVisibility(View.VISIBLE);
                TopicUnsubsc.setVisibility(View.VISIBLE);

                if (listViewItem.isTopicActivation())
                    TopicActivation.setBackgroundResource(R.drawable.subsc_state);
                else
                    TopicActivation.setBackgroundResource(R.drawable.unsubsc_state);

                TopicActivation.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (listViewItem.isTopicActivation()) {
                            listViewItem.setTopicActivation(false);
                            deactivateSubscription(listViewItem.getTopicName());
                            titleTextView.setPaintFlags(titleTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            titleTextView.setTextColor(Color.GRAY);
                            TopicActivation.setBackgroundResource(R.drawable.unsubsc_state);
                        } else {
                            listViewItem.setTopicActivation(true);
                            activateSubscription(listViewItem.getTopicName());
                            titleTextView.setPaintFlags(titleTextView.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
                            titleTextView.setTextColor(Color.BLACK);
                            TopicActivation.setBackgroundResource(R.drawable.subsc_state);
                        }
                    }
                });

                TopicUnsubsc.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deactivateSubscription(listViewItem.getTopicName());
                        listViewItemList.remove(pos);
                        notifyDataSetChanged();
                    }
                });
            }
        } else {
            titleTextView.setText("");
            TopicActivation.setVisibility(View.INVISIBLE);
            TopicUnsubsc.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    public void addItem(String TopicName) {
        ListViewItem item = new ListViewItem();

        item.setTopicName(TopicName);
        item.setTopicActivation(true);
        listViewItemList.add(item);
    }

    private void activateSubscription(String topicName) {
        try {
            MqttAndroidClient mqttAndroidClient = MqttClientManager.getMqttClient();
            mqttAndroidClient.subscribe(topicName, 0, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    System.out.println("LOG: Subscribed");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    System.out.println("LOG: Failed to subscribe!");
                }
            });
        } catch (MqttException ex){
            System.err.println("Exception whilst subscribing");
            ex.printStackTrace();
        }
    }

    private void deactivateSubscription(String topicName) {
        try {
            MqttAndroidClient mqttAndroidClient = MqttClientManager.getMqttClient();
            mqttAndroidClient.unsubscribe(topicName, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    System.out.println("LOG: Unubscribed");
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    System.out.println("LOG: Failed to unsubscribe!");
                }
            });
        } catch (MqttException ex){
            System.err.println("Exception while unsubscribing");
            ex.printStackTrace();
        }
    }

    public void clear_list() {
        listViewItemList.clear();
    }
}