package ReadWriteLock;

// if we are already writing, reader shouldn't be in the critical section
// if we are reading or writing, don't allow a new writer

// the writer thread might starve here!! if suppose 1 reader is always present then!!
class ReadWriteLock{
    int readers = 0;
    boolean isWriteLocked= false;

    public synchronized void acquireReadLock() throws InterruptedException {
        while(isWriteLocked){
            wait();
        }
        readers++;
    }

    public synchronized void releaseReadLock() { // method is synchronized!
        readers--;
        notify(); // this is because of the read!=0 condition, else there can be starvation
    }

    public synchronized void acquireWriteLock() throws InterruptedException {
        while(isWriteLocked || readers!=0){
            wait();
        }
        isWriteLocked=true;
    }

    public synchronized void releaseWriteLock() {
        isWriteLocked=false;
        notifyAll();
    }
}

public class ImplementReadWriteLock {
    public static void main(String[] args){

    }
}
