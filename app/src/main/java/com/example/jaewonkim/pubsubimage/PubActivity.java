package com.example.jaewonkim.pubsubimage;

//import android.support.v7.app.AppCompatActivity;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

public class PubActivity extends Activity {

    ListView listView;
    ListViewAdapter adapter;
    EditText TagText;
    EditText UrlText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pub);

        adapter = new ListViewAdapter();
        listView = (ListView)findViewById(R.id.pubList);
        listView.setAdapter(adapter);

        TagText = (EditText)findViewById(R.id.TagText);
        UrlText = (EditText)findViewById(R.id.UrlText);
    }

    public void addPub(View v) {
        String tagName = TagText.getText().toString();
        String urlName = UrlText.getText().toString();
        tagName = tagName.replaceAll(" ", "");
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
