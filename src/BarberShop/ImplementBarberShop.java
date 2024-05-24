package BarberShop;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

class BarberShop{
    final int CHAIRS = 3;
    int waitingCustomers = 0;
    ReentrantLock lock = new ReentrantLock();
    Semaphore waitForCustomerToEnter = new Semaphore(0);
    Semaphore waitForBarberToGetReady = new Semaphore(0);
    Semaphore waitForCustomerToLeave = new Semaphore(0);
    Semaphore waitForBarberToCutHair = new Semaphore(0);
    int hairCutsGiven = 0;

    void customerWalksIn() throws InterruptedException {
        lock.lock();
        if(waitingCustomers==CHAIRS){
            lock.unlock();
            return;
        }
        waitingCustomers++;
        lock.unlock();

        waitForCustomerToEnter.release();
        waitForBarberToGetReady.acquire();

        lock.lock();
        waitingCustomers--;
        lock.unlock();

        waitForBarberToCutHair.acquire();
        waitForCustomerToLeave.release();


    }

    void barber() throws InterruptedException {

        while(true){
            waitForCustomerToEnter.acquire();
            Thread.sleep(50);
            hairCutsGiven++;
            waitForBarberToGetReady.release();
            waitForBarberToCutHair.release();
            waitForCustomerToLeave.acquire();
        }
    }
}
public class ImplementBarberShop {
    public static void main(String[] args){

    }
}
