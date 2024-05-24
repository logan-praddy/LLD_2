package DiningPhilosphersProblem;

import java.util.Random;
import java.util.concurrent.Semaphore;

class DiningPhilosophers {

    // This random variable is used for test purporses only
    private static Random random = new Random(System.currentTimeMillis());
    // Five semaphore represent the five forks.
    private Semaphore[] forks = new Semaphore[5];
    private Semaphore maxDiners = new Semaphore(4);

    // Initializing the semaphores with a permit of 1
    public DiningPhilosophers() {
        forks[0] = new Semaphore(1);
        forks[1] = new Semaphore(1);
        forks[2] = new Semaphore(1);
        forks[3] = new Semaphore(1);
        forks[4] = new Semaphore(1);
    }

    void comtemplate() throws InterruptedException{
        Thread.sleep(random.nextInt(500));
    }
// two ways, no 1 -> use max4 diners at a time
    // sol 2 -> for a particular id say 3, reverse the order of picking up of fork!
    void eat(int id) throws InterruptedException{
        // required folk = fork[id] , fork[(id-1+5)%5] = fork[(id+4)%5]
        maxDiners.acquire();
        // acquire the left fork first
        forks[id].acquire();

        // acquire the right fork second
        forks[(id + 4) % 5].acquire();

        // eat to your heart's content
        System.out.println("Philosopher " + id + " is eating");

        // release forks for others to use
        forks[id].release();
        forks[(id + 4) % 5].release();
        maxDiners.release();

    }

    public void lifeCycleOfPhilospher(int id) throws InterruptedException{
        while(true){
            comtemplate();
            eat(id);
        }
    }
}
public class ImplementDiningPhilsophersProblem {
    public static void main(String[] args){

    }

}
