package LLD.PubSub.models;

import LLD.PubSub.public_interface.ISubscriber;
import lombok.Getter;
import lombok.NonNull;

import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class TopicSubscriber {
    private final ISubscriber subscriber;
    private final AtomicInteger offset;

    public TopicSubscriber(@NonNull final ISubscriber subscriber){
        this.subscriber = subscriber;
        this.offset = new AtomicInteger(0);
    }
}
