package NonBlockingStack;


import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

class AtomicReferenceBasedStack<T>{
    private AtomicReference<StackNode3<T>> top = new AtomicReference<>();
    private AtomicInteger count = new AtomicInteger(0);

    public int size(){
        return count.get();
    }

    public void push(T item){
        StackNode3<T> oldHead;
        StackNode3<T> newHead;
        do{
            oldHead = top.get();
            newHead = new StackNode3<>(item);
            newHead.setNext(oldHead);
        }while(!top.compareAndSet(oldHead,newHead));

        count.incrementAndGet();
    }

    public T pop(){
        StackNode3<T> oldHead;
        StackNode3<T> newHead;
        do{
            oldHead = top.get();
            if(oldHead==null) return null;
            newHead = oldHead.getNext();
        }while(!top.compareAndSet(oldHead,newHead));
        count.decrementAndGet();
        return oldHead.getItem();
    }


}



class StackNode3<T>{
    private T item;
    private StackNode3<T> next;

    public StackNode3(T item) {
        this.item  = item;
    }

    public StackNode3<T> getNext(){
        return next;
    }

    public void setNext(StackNode3<T> stackNode){
        next = stackNode;
    }

    public T getItem(){
        return item;
    }
}


public class ImplementAtomicReferenceStack {
}
