package Barrier;

class Barrier{
    int released = 0;
    int count = 0;
    int totalThreads = 0;
    Barrier(int totalThreads){
        this.totalThreads = totalThreads;
    }
    // why can't we just do count =0 in the if block because then others will starve, that's why we have
    // introduced relased
    // also the first while ensures that first previous threads are released and only
    // then block of execution moves forward
    public synchronized void await() throws InterruptedException{
// block any new threads from proceeding till,
           // all threads from previous barrier are released
        while(count==totalThreads) wait();

        count++;
        if(count==totalThreads){
// wake up all the threds.
            notifyAll();
            released=totalThreads;
        }else{
            // to handle spurious wakeups
            // wait till all threads reach barrier
            while(count<totalThreads){
                wait();
            }
        }
        // to check no of threads released
        released--;
        // resets the barrier
        if(released==0) {
            count = 0;
            notifyAll(); // this one if for anyone sleeping/waiting in line 17!
        }
    }
}

public class ImplementBarrier {
    public static void main(String[] args){

    }
}
