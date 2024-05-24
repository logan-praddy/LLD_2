package LLD.PubSub.public_interface;

import LLD.PubSub.models.Message;
import lombok.NonNull;

public interface ISubscriber {
    String getId();
    void consume(Message message) throws  InterruptedException;

}
