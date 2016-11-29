package com.example.jaewonkim.pubsubimage;

import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by nyangkun on 13/11/2016.
 */
public class FeedPost {
    private static final String TAG = "FeedPost";

    private static final String JSON_NAME_ID = "id";
    private static final String JSON_NAME_IMAGE_URL = "image_url";
    private static final String JSON_NAME_CONTENT = "content";

    private static final HashMap<UUID, FeedPost> postMap = new HashMap<>();

    public UUID id;
    public URL imageUrl;
    public String content;

    public FeedPost(UUID id, URL imageUrl, String content) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.content = content;
    }

    public FeedPost(UUID id, String imageUrl, String content) {
        try {
            this.id = id;
            this.imageUrl = new URL(imageUrl);
            this.content = content;
        } catch (MalformedURLException e) {
            ;
        }
    }

    // Converts a JSONObject into a new feed post object.
    public FeedPost(JSONObject json) {
        try {
            id = UUID.fromString(json.getString(JSON_NAME_ID));
            imageUrl = new URL(json.getString(JSON_NAME_IMAGE_URL));
            content = json.getString(JSON_NAME_CONTENT);
        } catch (JSONException e) {
            Log.e(TAG, "failed to convert JSON string to feed post", e);
        } catch (MalformedURLException e) {
            Log.e(TAG, "encountered a malformed URL in JSON string", e);
        }
    }

    // Converts itself to JSONObject.
    public JSONObject toJson() {
        try {
            JSONObject json = new JSONObject();
            json.put(JSON_NAME_ID, id.toString());
            json.put(JSON_NAME_IMAGE_URL, imageUrl.toString());
            json.put(JSON_NAME_CONTENT, content);
            return json;
        } catch (JSONException e) {
            Log.e(TAG, "failed to convert feed post to JSON object", e);
            return null;
        }
    }

    public static boolean hasReceived(FeedPost post) {
        return postMap.containsKey(post.id);
    }

    public static void receive(FeedPost post) {
        if (null == post.id) {
            return;
        }
        postMap.put(post.id, post);
    }
}
