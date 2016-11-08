package com.example.jaewonkim.pubsubimage;


/**
 * Created by Jaewon Kim on 2016-05-18.
 */
public class ListViewItem{

    private String TopicName;
    private boolean TopicActivation;

    public String getTopicName() { return TopicName; }
    public boolean isTopicActivation() { return TopicActivation; }

    public void setTopicName(String topicName) { TopicName = topicName; }
    public void setTopicActivation(boolean topicActivation) { TopicActivation = topicActivation; }
}