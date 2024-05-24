package UnisexBathroomProblem;



// not more than 3 employees
// both men and women can't be there together
// starvation doesn't matter

import java.util.concurrent.Semaphore;

class UnisexBathroom{
    String inUseBy = "NONE"; // man, women, none
    int empsInBathroom = 0;
    Semaphore maxEmployees = new Semaphore(3);

    void useBathroom(String name) throws InterruptedException {
        System.out.println(name + " using bathroom. Current employees in bathroom = " + empsInBathroom);
        Thread.sleep(10000);
        System.out.println(name + " done using bathroom");
    }

    void maleUseBathroom(String name) throws InterruptedException {
        synchronized (this){
            while("WOMEN".equals(inUseBy)){
                this.wait();
            }
            maxEmployees.acquire();
            inUseBy = "MEN";
            empsInBathroom++;
        }
        useBathroom(name);
        maxEmployees.release();
        // release the bathroom
        synchronized (this){
            empsInBathroom--;
            if(empsInBathroom==0){
                inUseBy = "NONE";
            }
            this.notifyAll();
        }

    }

    void femaleUseBathroom(String name) throws InterruptedException {
        synchronized (this){
            while("MEN".equals(inUseBy)){
                this.wait();
            }
            maxEmployees.acquire();
            inUseBy = "WOMEN";
            empsInBathroom++;
        }
        useBathroom(name);
        maxEmployees.release();
        // release the bathroom
        synchronized (this){
            empsInBathroom--;
            if(empsInBathroom==0){
                inUseBy = "NONE";
            }
            this.notifyAll();
        }
    }

}

public class ImplementUnisexBathroom {
    public static void main(String[] args){

    }
}
