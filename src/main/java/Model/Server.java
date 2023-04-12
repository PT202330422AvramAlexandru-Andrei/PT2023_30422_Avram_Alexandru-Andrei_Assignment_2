package Model;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private BlockingDeque<Task> tasks = new LinkedBlockingDeque<>();
    private AtomicInteger waitingPeriod;

    public Server(BlockingDeque<Task> tasks, AtomicInteger waitingPeriod) {
        this.tasks = tasks;
        this.waitingPeriod = waitingPeriod;
    }

    public Server() {
        //initialize queue and waiting period
        this.tasks = new LinkedBlockingDeque<>();
        this.waitingPeriod = new AtomicInteger(0);
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
            Task task = tasks.peek();

            if (task == null) {
                continue;
            }
            // wait for service time
            try {
                Thread.sleep(task.getServiceTime() * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // update waiting period
            waitingPeriod.addAndGet(-task.getServiceTime());
        }
    }
}
