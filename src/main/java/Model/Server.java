package Model;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private BlockingDeque<Task> tasks;
    private AtomicInteger waitingPeriod;

    public Server(BlockingDeque<Task> tasks, AtomicInteger waitingPeriod) {
        this.tasks = tasks;
        this.waitingPeriod = waitingPeriod;
    }

    public Server() {
        //initialize queue and waiting period

    }

    public void addTask(Task task) {
        //add task to queue and update waiting period
        tasks.add(task);
        waitingPeriod.addAndGet(task.getServiceTime());
    }

    public void removeTask() {
        tasks.remove();
    }

    public BlockingDeque<Task> getTasks() {
        return tasks;
    }

    public void setTasks(BlockingDeque<Task> tasks) {
        this.tasks = tasks;
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    public void setWaitingPeriod(AtomicInteger waitingPeriod) {
        this.waitingPeriod = waitingPeriod;
    }

    @Override
    public void run() {
        while(true) {
            // take next task from queue
            // wait for service time
            // update waiting period
            try {
                if (tasks.size() > 0) {
                    waitingPeriod.addAndGet(-1);
                    Thread.sleep(1000);
                    if (tasks.getFirst().getServiceTime() == 0) {
                        tasks.remove();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
