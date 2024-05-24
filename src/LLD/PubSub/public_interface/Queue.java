package LLD.PubSub.public_interface;


import LLD.PubSub.handler.TopicHandler;
import LLD.PubSub.models.Message;
import LLD.PubSub.models.Topic;
import LLD.PubSub.models.TopicSubscriber;
import lombok.NonNull;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Queue {
    private final Map<String, TopicHandler> topicProcessors;

    public Queue(){
        this.topicProcessors = new HashMap<>();
    }

    public Topic createTopic(@NonNull final String topicName){
        Topic topic = new Topic(UUID.randomUUID().toString(), topicName);
        TopicHandler topicHandler = new TopicHandler(topic);
        this.topicProcessors.put(topic.getTopicId(),topicHandler);
        System.out.println("Created topic: " + topic.getTopicName());
        return topic;
    }

    public void subscribe(@NonNull final Topic topic, @NonNull final ISubscriber subscriber){
        topic.addSubscriber(new TopicSubscriber(subscriber));
        System.out.println(subscriber.getId() + " subscribed to topic: " + topic.getTopicName());
    }

    public void publish(@NonNull final Topic topic, @NonNull final Message message){
        topic.addMessage(message);
        System.out.println(message.getMsg() + " published to topic: " + topic.getTopicName());
        new Thread(()-> topicProcessors.get(topic.getTopicId()).publish()).start();
    }

    public void resetOffset(@NonNull final Topic topic, @NonNull final ISubscriber subscriber,@NonNull final Integer newOffset){
        for(TopicSubscriber topicSubscriber: topic.getSubscribers()){
            if(topicSubscriber.getSubscriber().equals(subscriber)){
                topicSubscriber.getOffset().set(newOffset);
                System.out.println(topicSubscriber.getSubscriber().getId() + " offset reset to: " + newOffset);
                // we need this because if the offset is done now, we just want to replay old messages again! that's why again
                // create/awake the subscriber workers
                new Thread(() -> topicProcessors.get(topic.getTopicId()).startSubsriberWorker(topicSubscriber)).start();
                break;
            }
        }
    }
}
