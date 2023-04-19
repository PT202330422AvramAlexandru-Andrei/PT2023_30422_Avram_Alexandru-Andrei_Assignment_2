package Model;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {
    private BlockingDeque<Task> tasks;
    private AtomicInteger waitingPeriod;

    /*public Server(BlockingDeque<Task> tasks, AtomicInteger waitingPeriod) {
        this.tasks = tasks;
        this.waitingPeriod = waitingPeriod;
    }*/

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

    /*public void removeTask() {
        tasks.remove();
    }*/

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
        while (!Thread.currentThread().isInterrupted()) {
            try {
                if (tasks.isEmpty()) {
                    Thread.sleep(1000);
                    continue;
                }

                Task task = tasks.peek();
                Thread.sleep(task.getServiceTime() * 1000);
                tasks.take();
                waitingPeriod.addAndGet(-task.getServiceTime());

                for (Task t : tasks) {
                    waitingPeriod.addAndGet(-t.getServiceTime());
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
