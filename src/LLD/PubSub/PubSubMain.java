package LLD.PubSub;

import LLD.PubSub.models.Message;
import LLD.PubSub.models.Topic;
import LLD.PubSub.public_interface.Queue;


// Complete code - > https://github.com/anomaly2104/low-level-design-messaging-queue-pub-sub
// TODO how to manage TTL for each message ??
// maybe create a daemon thread which keeps on checking for this and
// remove them from the message list altogether for each topic ??

public class PubSubMain {
    public static void main(String[] args) throws InterruptedException {
        final Queue queue = new Queue();
        final Topic topic1 = queue.createTopic("t1");
        final Topic topic2 = queue.createTopic("t2");
        final SleepingSubscriber sub1 = new SleepingSubscriber("sub1", 10000);
        final SleepingSubscriber sub2 = new SleepingSubscriber("sub2", 10000);
        queue.subscribe(topic1, sub1);
        queue.subscribe(topic1, sub2);

        final SleepingSubscriber sub3 = new SleepingSubscriber("sub3", 5000);
        queue.subscribe(topic2, sub3);

        queue.publish(topic1, new Message("m1"));
        queue.publish(topic1, new Message("m2"));

        queue.publish(topic2, new Message("m3"));

        Thread.sleep(15000);
        queue.publish(topic2, new Message("m4"));
        queue.publish(topic1, new Message("m5"));

        queue.resetOffset(topic1, sub1, 0);
    }
}
