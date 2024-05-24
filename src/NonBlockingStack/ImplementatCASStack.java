package NonBlockingStack;

class SimulatedCompareAndSwap<T>{
    private T value;

    public SimulatedCompareAndSwap(T initValue){
        value = initValue;
    }

    synchronized T getValue(){
        return value;
    }

    synchronized T compareAndSwap(T expectedValue, T newValue){
        if(value==null){
            if(expectedValue==null){
                value = newValue;
            }
            return null;
        }

        if(value.equals(expectedValue)){
            value=newValue;
        }
        return value;
    }

    synchronized boolean compareAndSet(T expectedValue, T newValue){
        T returnValue = compareAndSwap(expectedValue, newValue);
        if(returnValue==null && newValue==null) return true;
        if(returnValue==null && newValue!=null) return false;
        else {
            return newValue.equals(returnValue);
        }
    }

}

class StackNode2<T>{
    private T item;
    private StackNode2<T> next;

    public StackNode2(T item) {
        this.item  = item;
    }

    public StackNode2<T> getNext(){
        return next;
    }

    public void setNext(StackNode2<T> stackNode){
        next = stackNode;
    }

    public T getItem(){
        return item;
    }
}


class CASBasedSStack<T>{

    private SimulatedCompareAndSwap<StackNode2<T>> simulatedCAS;

    public CASBasedSStack(){
        simulatedCAS = new SimulatedCompareAndSwap<>(null);
    }


    public void push(T item){
        StackNode2<T> oldHead;
        StackNode2<T> newHead;
        do{
            oldHead = simulatedCAS.getValue();
            newHead = new StackNode2<>(item);
            newHead.setNext(oldHead);
        }while(!simulatedCAS.compareAndSet(oldHead, newHead));
    }

    public T pop(){
        StackNode2<T> oldHead;
        StackNode2<T> newHead;
        do{
            oldHead = simulatedCAS.getValue();
            if(oldHead==null) return null;
            newHead = oldHead.getNext();
        }while(!simulatedCAS.compareAndSet(oldHead,newHead));
        return oldHead.getItem();
    }

}



