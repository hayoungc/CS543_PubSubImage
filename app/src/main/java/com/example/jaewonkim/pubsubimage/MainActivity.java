package com.example.jaewonkim.pubsubimage;

import android.app.ActivityGroup;
import android.app.TabActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class MainActivity extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost tab = (TabHost) findViewById(android.R.id.tabhost);
        //tab.setup(this.getLocalActivityManager());
        //tab.setup();

        TabHost.TabSpec spec1 = tab.newTabSpec("Subscribe");
        spec1.setIndicator("Subscribe");
        spec1.setContent(new Intent(this, SubActivity.class));
        tab.addTab(spec1);

        TabHost.TabSpec spec2 = tab.newTabSpec("Publish");
        spec2.setIndicator("Publish");
        spec2.setContent(new Intent(this, PubActivity.class));
        tab.addTab(spec2);

    }

}
