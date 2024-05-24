package MutliThreadedTaskScheduler;

import lombok.Getter;
import lombok.Setter;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static MutliThreadedTaskScheduler.ScheduledTaskType.*;
import static MutliThreadedTaskScheduler.taskCreator.createRunnableTask;

enum ScheduledTaskType{
RUN_ONCE, RECUR, RECUR_WITH_DELAY;
}

@Getter
@Setter
class ScheduledTask{
    private Runnable task;
    private Long scheduledTime;
    private ScheduledTaskType scheduledTaskType;
    private Long delay;
    private Long replayTime;
    private TimeUnit timeUnit;

    ScheduledTask(Runnable task, Long scheduledTime, ScheduledTaskType scheduledTaskType, Long delay, Long replayTime, TimeUnit timeUnit){
        this.task=task;
        this.scheduledTime=scheduledTime;
        this.scheduledTaskType=scheduledTaskType;
        this.delay=delay;
        this.replayTime=replayTime;
        this.timeUnit=timeUnit;
    }
}

interface IschedulerService extends Runnable{
    public void scheduleTask(Runnable task, Long delay, TimeUnit timeUnit);
    public void scheduleRecurringTask(Runnable task, Long delay, Long replayTime, TimeUnit timeUnit);
    public void scheduleRecurringTaskWithWait(Runnable task, Long delay, Long replayTime, TimeUnit timeUnit);
}

class Scheduler implements IschedulerService{
    private static IschedulerService schedulerService;
    private final PriorityQueue<ScheduledTask> taskPriorityQueue;
    private final ThreadPoolExecutor taskExecutor;
    private final Lock lock;
    private final Condition newTaskScheduled;

    Scheduler(int threadSize){  // we are using cached thread pool, no need to declare the size!
        this.taskPriorityQueue=new PriorityQueue<>(Comparator.comparingLong(ScheduledTask::getScheduledTime));
        this.taskExecutor = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        this.lock = new ReentrantLock();
        this.newTaskScheduled=this.lock.newCondition();
    }

    public static synchronized Scheduler getInstance(int thread_size){
        if(schedulerService==null){
            schedulerService = new Scheduler(thread_size);
        }
        return (Scheduler) schedulerService;
    }


    @Override
    public void run(){
        Long sleepFor = Long.valueOf(0L);
        while(true){
            this.lock.lock();
            try {
                while (taskPriorityQueue.isEmpty()) {
                    newTaskScheduled.await();
                }

                while(!taskPriorityQueue.isEmpty()){
                    sleepFor = this.taskPriorityQueue.peek().getTimeUnit().toMillis(this.taskPriorityQueue.peek().getScheduledTime()) - System.currentTimeMillis();
                    if(sleepFor<=0){
                        break;
                    }
                    this.newTaskScheduled.await(sleepFor,TimeUnit.MILLISECONDS);
                }

                ScheduledTask scheduledTask = this.taskPriorityQueue.poll();
                Long newScheduledTime = Long.valueOf(0);

                switch(scheduledTask.getScheduledTaskType()){
                    case RUN_ONCE:
                        this.taskExecutor.submit(scheduledTask.getTask());
                        break;
                    case RECUR:
                        newScheduledTime = System.currentTimeMillis() + scheduledTask.getTimeUnit().toMillis(scheduledTask.getReplayTime());
                        this.taskExecutor.submit(scheduledTask.getTask());
                        scheduledTask.setScheduledTime(newScheduledTime);
                        this.taskPriorityQueue.add(scheduledTask);
                        break;
                    case RECUR_WITH_DELAY:
                        Future<?> future = this.taskExecutor.submit(scheduledTask.getTask());
                        future.get();
                        newScheduledTime = System.currentTimeMillis()+ scheduledTask.getTimeUnit().toMillis(scheduledTask.getReplayTime());
                        scheduledTask.setScheduledTime(newScheduledTime);
                        this.taskPriorityQueue.add(scheduledTask);
                        break;
                }


            }catch (RejectedExecutionException r){
                r.printStackTrace();
            }catch (InterruptedException | ExecutionException e){
                throw new RuntimeException(e);
            }finally {
                this.lock.unlock();
            }

        }
    }

    @Override
    public void scheduleTask(Runnable task, Long delay, TimeUnit timeUnit){
        this.lock.lock();
        try{
            Long scheduledTime = System.currentTimeMillis() + timeUnit.toMillis(delay);
            ScheduledTask scheduledTask = new ScheduledTask(task,scheduledTime, RUN_ONCE, delay, null, timeUnit);
            taskPriorityQueue.add(scheduledTask);
            newTaskScheduled.signalAll();
        }catch (RejectedExecutionException r){
            r.printStackTrace();
        }finally {
            this.lock.unlock();
        }
    }
    public void scheduleRecurringTask(Runnable task, Long delay, Long replayTime, TimeUnit timeUnit){
        this.lock.lock();
        try{
            Long scheduledTime = System.currentTimeMillis() + timeUnit.toMillis(delay);
            ScheduledTask scheduledTask = new ScheduledTask(task,scheduledTime, RECUR, delay, replayTime, timeUnit);
            taskPriorityQueue.add(scheduledTask);
            newTaskScheduled.signalAll();
        }catch(RejectedExecutionException r){
            r.printStackTrace();
        }finally {
            this.lock.unlock();
        }
    }
    public void scheduleRecurringTaskWithWait(Runnable task, Long delay, Long replayTime, TimeUnit timeUnit){
        this.lock.lock();
        try{
            Long scheduledTime = System.currentTimeMillis() + timeUnit.toMillis(delay);
            ScheduledTask scheduledTask = new ScheduledTask(task,scheduledTime, RECUR_WITH_DELAY, delay, replayTime, timeUnit);
            taskPriorityQueue.add(scheduledTask);
            newTaskScheduled.signalAll();
        }catch(RejectedExecutionException r){
            r.printStackTrace();
        }finally {
            this.lock.unlock();
        }
    }
}


class taskCreator{
    public static Runnable createRunnableTask(String task_name) {
        return () -> {
            System.out.printf("Starting %s at %d%n", task_name, System.currentTimeMillis());
            try {
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("Ending %s at %d%n", task_name, System.currentTimeMillis());
        };
    }
}

public class SchedulerExecutor {
    IschedulerService abc = Scheduler.getInstance(23);


//    IschedulerService abc = Scheduler.getInstance(10);
    Runnable task1 = createRunnableTask("Task1");

//    schedulerService.scheduleTask(task1, 1L, TimeUnit.MILLISECONDS);

    Runnable task2 = createRunnableTask("Task2");
//        schedulerService.scheduleTask(task2, 5L, TimeUnit.MILLISECONDS);

    Runnable task3 = createRunnableTask("Task3");
//        schedulerService.scheduleRecurringTask(task3, 5L, 5L, TimeUnit.MILLISECONDS);

    Runnable task4 = createRunnableTask("Task4");
//        schedulerService.scheduleRecurringTaskWithWait(task4, 5L, 1L, TimeUnit.MILLISECONDS);

//        new Thread(schedulerService).start();
}
