package com.example.jaewonkim.pubsubimage;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;

import java.util.LinkedList;
import java.util.List;

public class FeedActivity extends AppCompatActivity {

    private LinkedList<FeedPost> mPosts;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    /**
     * Adds a new post in the feed at the first place. Available for the
     * activity which includes this fragment.
     *
     * @param post A post object to add.
     */
    public void addNewPost(FeedPost post) {
        mPosts.addFirst(post);
        mAdapter.notifyItemInserted(0);
        /*
         * TODO: Decode bitmap from an InputStream created by url, asynchronously.
         * After decoding, put the result into ImageView. This should be done when
         * the card view is placed on the screen.
         */
    }

    /**
     * Adds a sequence of new posts in the feed at the first place. Available
     * for the activity which includes this fragment and has buffered new posts.
     *
     * @param posts A sequence of post objects to add. Latest post object first.
     */
    public void addNewPosts(List<FeedPost> posts) {
        mPosts.addAll(0, posts);
        mAdapter.notifyItemRangeInserted(0, posts.size());
        /*
         * TODO: Decode bitmap from an InputStream created by url, asynchronously.
         * After decoding, put the result into ImageView. This should be done when
         * the card view is placed on the screen.
         */
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        // Hide the action bar.
        ActionBar ab = getSupportActionBar();
        if (null != ab) {
            ab.hide();
        }

        // Set the sampling width for the feed images.
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        BitmapWorkerTask.setReqWidth(metrics.widthPixels);

        mPosts = new LinkedList<>();

        mRecyclerView = (RecyclerView) findViewById(R.id.feed_recycler_view);

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new FeedAdapter(mPosts);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // TODO: Save FeedPost objects in mPosts into the saved bundle. This is required for saving the state of this activity.
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // TODO: Fill FeedPost objects to mPosts from the saved bundle. This is required for restoring the state of this activity.
    }
}
