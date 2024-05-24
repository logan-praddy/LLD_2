package Semaphore;

class CountingSemaphore{
    int usedPermits;
    int maxCount;

    CountingSemaphore(int usedPermits, int maxCount){
        this.usedPermits = usedPermits;
        this.maxCount = maxCount;
    }

    public synchronized void acquire() throws InterruptedException {
        while(usedPermits==maxCount){
            wait();
        }
        usedPermits++;   // order of line 16, 17 won't matter because of synchronized keyword!
        notify();
    }

    public synchronized void release() throws InterruptedException {
        while(usedPermits==0)
            wait();

        usedPermits--;
        notify();
    }
}

public class ImplementSemaphore {
    public static void main(String[] args){

    }
}
