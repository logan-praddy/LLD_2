package LLD.PubSub.models;

import lombok.Getter;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public class Topic {
    private final String topicId;
    private final String topicName;
    private final List<Message> messages;
    private final List<TopicSubscriber> subscribers;

    public Topic(@NonNull final String topicId, @NonNull final String topicName){
        this.topicId = topicId;
        this.topicName = topicName;
        this.messages = new ArrayList<>();
        this.subscribers = new ArrayList<>();
    }

    public synchronized void addMessage(@NonNull final Message message){
        this.messages.add(message);
    }

    public void addSubscriber(@NonNull TopicSubscriber topicSubscriber){
        this.subscribers.add(topicSubscriber);
    }


}
