package UberRiderProblem;


import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

// either the car has all democrats or republicans or 2-2 of each
// why do we need barrier one may ask, reason, drive or say door shut can happen only
// when all the occupants have been seated and are in the car just before driving

// semaphore is beign used to keep that paticular thread in a wait state till the other parties arrive and a suitable
// combination is created by the arrival of other threads!
class UberDriver{
    int republicans=0;
    int democrats=0;
    Semaphore demsWaiting = new Semaphore(0);
    Semaphore repubsWaiting = new Semaphore(0);
    ReentrantLock lock = new ReentrantLock();
    CyclicBarrier barrier = new CyclicBarrier(4);
    void seated(){
    // print x is seated
    }
    void seatDemocrats() throws InterruptedException, BrokenBarrierException {
        boolean isRideLeader = false;
        lock.lock();
        democrats++;
        if(democrats==4){
            isRideLeader=true;
            democrats-=4;
            demsWaiting.release(3);
        }else if(democrats==2 && republicans>=2){
            isRideLeader=true;
            demsWaiting.release(1);
            repubsWaiting.release(2);
            democrats -= 2;
            republicans -= 2;
        }else{
            lock.unlock();
            demsWaiting.acquire();
        }
        seated();
        barrier.await();

        if (isRideLeader) {
            drive();
            lock.unlock();
        }

    }
    void seatRepublicans()throws InterruptedException, BrokenBarrierException{
//        same as above
    }

    void drive(){
    // print drive
    }
}
public class ImplementUberDriverProblem {
    public static void main(String[] args){

    }
}
