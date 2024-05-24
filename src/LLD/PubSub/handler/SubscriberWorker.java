package LLD.PubSub.handler;

import LLD.PubSub.models.Message;
import LLD.PubSub.models.Topic;
import LLD.PubSub.models.TopicSubscriber;
import lombok.NonNull;

public class SubscriberWorker implements Runnable{
    private final TopicSubscriber topicSubscriber;
    private final Topic topic;

    public SubscriberWorker(@NonNull final TopicSubscriber topicSubscriber, @NonNull final Topic topic){
        this.topicSubscriber = topicSubscriber;
        this.topic = topic;
    }

    @Override
    public void run(){
        synchronized (topicSubscriber){
            try {
                do {
                    int curOffset = topicSubscriber.getOffset().get();
                    while (curOffset >= topic.getMessages().size()) {
                        topicSubscriber.wait();
                    }
                    Message message = topic.getMessages().get(curOffset);
                    topicSubscriber.getSubscriber().consume(message);

                    // We cannot just increment here since subscriber offset can be reset while it is consuming. So, after
                    // consuming we need to increase only if it was previous one.
                    topicSubscriber.getOffset().compareAndSet(curOffset, curOffset+1);
                } while (true);
            }catch (InterruptedException e){
            }
        }
    }

    public void wakeUpIfNeeded(){
        synchronized (topicSubscriber){
            topicSubscriber.notify();
        }
    }

}
