package com.example.jaewonkim.pubsubimage;

//import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

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
