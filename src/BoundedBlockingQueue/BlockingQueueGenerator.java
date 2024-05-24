package BoundedBlockingQueue;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;


class BoundedQueue<T>{
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock writeLock = lock.writeLock();
    private Lock readLock = lock.readLock();
    private Condition empty = writeLock.newCondition();
    private Condition full = writeLock.newCondition();
    private Queue<T> queue = new LinkedList<>();
    int capacity;

    public BoundedQueue(int capacity) {
        this.capacity = capacity;
    }

    public void enqueue(T element) throws InterruptedException {
        writeLock.lock();
        try{
            while(queue.size()==capacity){
                full.await();
            }
            queue.offer(element);
            empty.signal();
        }finally{
            writeLock.unlock();
        }
    }

    public T dequeue() throws InterruptedException {
        writeLock.lock();
        try{
            while(queue.size()==0){
                empty.await();
            }
            T head = queue.poll();
            full.signal();
            return head;
        }finally {
            writeLock.unlock();
        }
    }

    public int size() {
        readLock.lock();
        try{
            return queue.size();
        }finally {
            readLock.unlock();
        }
    }
}



public class BlockingQueueGenerator {
    public static void main(String[] args) throws InterruptedException {

//        Scanner sc = new Scanner(System.in);
//        Integer tj = sc.nextInt();
//        String tjj = sc.nextLine();

        final BoundedQueue<Integer> q = new BoundedQueue<Integer>(5);

        Thread t1 = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    for (int i = 0; i < 50; i++) {
                        q.enqueue(new Integer(i));
                        System.out.println("enqueued " + i);
                    }
                } catch (InterruptedException ie) {

                }
            }
        });

        Thread t2 = new Thread(new Runnable(){
           @Override
           public void run(){
               try{
                   for (int i = 0; i < 25; i++) {
                       System.out.println("Thread 2 dequeued: " + q.dequeue());
                   }
               }catch(InterruptedException ie){

               }
           }
        });

        Thread t3 = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    for (int i = 0; i < 25; i++) {
                        System.out.println("Thread 3 dequeued: " + q.dequeue());
                    }
                } catch (InterruptedException ie) {
                }
            }
        });

        t1.start();
        Thread.sleep(4000);
        t2.start();

        t2.join();

        t3.start();
        t1.join();
        t3.join();
    }
}
