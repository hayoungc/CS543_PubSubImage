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

import java.util.ArrayList;

/**
 * Created by Jaewon Kim on 2016-05-18.
 */
public class ListViewAdapter  extends BaseAdapter {

    private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();
    private static final long serialVersionUID = 1L;

    @Override
    public int getCount() {
        return listViewItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position);
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
        Button TopicUnsubsc = (Button) convertView.findViewById(R.id.TopicUnsubsc);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        final ListViewItem listViewItem = listViewItemList.get(position);
        titleTextView.setSingleLine(true);

        // 아이템 내 각 위젯에 데이터 반영
        titleTextView.setText(listViewItem.getTopicName());
        if(listViewItem.isTopicActivation())
            TopicActivation.setBackgroundResource(R.drawable.subsc_state);
        else
            TopicActivation.setBackgroundResource(R.drawable.unsubsc_state);

        TopicActivation.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listViewItem.isTopicActivation()) {
                    listViewItem.setTopicActivation(false);
                    titleTextView.setPaintFlags(titleTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    titleTextView.setTextColor(Color.GRAY);
                    TopicActivation.setBackgroundResource(R.drawable.unsubsc_state);
                }
                else {
                    listViewItem.setTopicActivation(true);
                    titleTextView.setPaintFlags(titleTextView.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);
                    titleTextView.setTextColor(Color.BLACK);
                    TopicActivation.setBackgroundResource(R.drawable.subsc_state);
                }
            }
        });

        TopicUnsubsc.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                listViewItemList.remove(pos);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public void addItem(String TopicName) {
        ListViewItem item = new ListViewItem();

        item.setTopicName(TopicName);
        item.setTopicActivation(true);
        listViewItemList.add(item);
    }

    public void clear_list() {
        listViewItemList.clear();
    }
}