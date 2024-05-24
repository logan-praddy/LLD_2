package ThreadSafeDefferedCallback;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class DefferedCallback{
    PriorityQueue<Callback> priorityQueue = new PriorityQueue<Callback>(new Comparator<Callback>() {
        @Override
        public int compare(Callback o1, Callback o2) {
            return (int)( o1.executeAt - o2.executeAt);
        }
    });
    ReentrantLock lock = new ReentrantLock();
    Condition newCallbackArrived = lock.newCondition();

    private long findSleepDuration(){
        return priorityQueue.peek().executeAt - System.currentTimeMillis();
    }
    public void start() throws InterruptedException{
        while(true){
            lock.lock();
            long sleepFor =0;
            while(priorityQueue.size()==0){
                newCallbackArrived.await();
            }
            while(priorityQueue.size()!=0){
                sleepFor = findSleepDuration();
                if(sleepFor<=0){
                    break;
                }
                newCallbackArrived.await(sleepFor, TimeUnit.MILLISECONDS);
            }
            Callback cb = priorityQueue.poll();
            System.out.println("Executed at " + System.currentTimeMillis()/1000 + " required at " + cb.executeAt/1000
                    + ": message:" + cb.message);
            lock.unlock();
        }
    }

    public void registerCallback(Callback callback){
        lock.lock();
        priorityQueue.add(callback);
        newCallbackArrived.signal();
        lock.unlock();
    }

}
class Callback{
     String message;
     long executeAt;

    Callback(String message, long executeAt){
        this.message = message;
        this.executeAt = executeAt;
    }
}
public class DefferedCallbackExecutor {
    public static void main(String[] args){

    }
}
