package NonBlockingStack;

// PROBLEM STATEMENT:


//Design a stack that doesn’t use locks or synchronized and is thread-safe.
// You may assume that you are provided with an application-level API that
// mocks the hardware instruction compare-and-swap, to atomically compare
// and swap values at a memory location.

// TODO -> to be done with synchronized, CAS, and other methods later! + Webcrawler
// + LeetCode Questions


class SynchronizedStack<T>{
    private StackNode<T> top;

    public synchronized void push(T item){
        if(top==null){
            top = new StackNode<>(item);
        }else{
            StackNode<T> oldHead = top;
            top = new StackNode<>(item);
            top.setNext(oldHead);
        }
    }

    public synchronized T pop(){
        if(top==null){
            return null;
        }
        StackNode<T> oldHead = top;
        top = top.getNext();
        return oldHead.getItem();
    }
}

class StackNode<T>{
    private T item;
    private StackNode<T> next;

    public StackNode(T item) {
        this.item  = item;
    }

    public StackNode<T> getNext(){
        return next;
    }

    public void setNext(StackNode<T> stackNode){
        next = stackNode;
    }

    public T getItem(){
        return item;
    }
}

public class ImplementNonBlockingStack {
}
