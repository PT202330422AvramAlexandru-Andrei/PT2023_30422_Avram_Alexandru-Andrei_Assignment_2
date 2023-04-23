package BusinessLogic;

import GUI.SimulationFrame;
import Model.Server;
import Model.Task;
import com.example.queuemanager.App;
import com.example.queuemanager.MainController;
import javafx.application.Platform;

import java.util.*;

public class SimulationManager implements Runnable {

    //data read  from UI
    public int timeLimit = 100;
    public int maxProcessingTime = 10;
    public int minProcessingTime = 2;
    public int numberOfServers = 3;
    public int numberOfTasks = 100;
    public SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_QUEUE;

    //entity responsible for queue management and client distribution
    private Scheduler scheduler;

    //frame for displaying the simulation
    private SimulationFrame frame;

    //pool of tasks to be processed
    private List<Task> generatedTasks = Collections.synchronizedList(new ArrayList<>());

    public SimulationManager() {
        //initialize the scheduler
        scheduler = new Scheduler(numberOfServers);

        //  -create and start numberOfServers threads
        //===========================================//
        /*for (int i = 0; i < numberOfServers; i++) {
            Thread thread = new Thread();
            thread.start();
        }*/
        //===========================================//

        //  -initialize selection strategy => createStrategy()
        //scheduler.changeStrategy(selectionPolicy);

        //initialize the frame
        frame = new SimulationFrame();
        //generate nrOfClients clients => generateNRandomTasks()
        //store the tasks in generatedTasks
        generateNRandomTasks();
    }

    public SimulationManager(int timeLimit, int maxProcessingTime, int minProcessingTime, int numberOfServers, SelectionPolicy selectionPolicy) {
        this.timeLimit = timeLimit;
        this.maxProcessingTime = maxProcessingTime;
        this.minProcessingTime = minProcessingTime;
        this.numberOfServers = numberOfServers;
        this.selectionPolicy = selectionPolicy;

        scheduler = new Scheduler(numberOfServers);
        scheduler.changeStrategy(selectionPolicy);

        frame = new SimulationFrame();
        generateNRandomTasks();
    }

    public void generateNRandomTasks() {
        //generate N random tasks
        //  -random processing time
        //minProcessingTime < processingTime < maxProcessingTime
        //  -random arrival time
        //sort the list by arrival time

        for (int i = 0; i < Math.random() * 100; i++) {
            Task task = new Task();
            task.setServiceTime((int) (Math.random() * (maxProcessingTime - minProcessingTime) + minProcessingTime));
            //change 10 to timeLimit after testing
            task.setArrivalTime((int) ((Math.random() * 100) % 10));
            task.setId(i);
            generatedTasks.add(task);
        }

        generatedTasks.sort(Comparator.comparingInt(Task::getArrivalTime));
        System.out.println("Generated tasks: " + generatedTasks.size());
        FileWrite.write("output.txt", "Generated tasks: " + generatedTasks.size() + "\n");
    }

    public int getAverageWaitingTime() {
        int sum = 0;
        int count = 0;
        for (Server s : scheduler.getServers()) {
            for (Task t : s.getTasks()) {
                sum += t.getServiceTime();
                count++;
            }
        }
        if (count == 0) {
            return 0;
        } else {
            return (sum / count);
        }
    }

    public int getAverageQueueLength() {
        int sum = 0;
        int count = 0;
        for (Server s : scheduler.getServers()) {
            sum += s.getTasks().size();
            count++;
        }

        if (count == 0) {
            return 0;
        } else {
            return (sum / count);
        }
    }

    public int getCurrentTime() {
        return scheduler.getCurrentTime();
    }

    @Override
    public void run() {
        FileWrite.emptyFile("output.txt");
        int currentTime = 0;
        while (currentTime < timeLimit) {
            //iterate through the list of tasks (generatedTasks)
            //pick the tasks that have the current time as arrival time
            //  -send task to queue => scheduler.dispatchTask(task)
            //  -remove task from list
            //update the UI frame => frame.update()

            synchronized (generatedTasks) {
                Iterator<Task> iterator = generatedTasks.iterator();
                System.out.println("Waiting tasks: " + generatedTasks.toString());
                FileWrite.write("output.txt", "Waiting tasks: " + generatedTasks.toString() + "\n");
                while (iterator.hasNext()) {
                    Task task = iterator.next();
                    if (task.getArrivalTime() == currentTime) {
                        scheduler.dispatchTask(task);
                        //generatedTasks.remove(task);
                        iterator.remove();
                        //System.out.println("Task " + task.getId() + " arrived at " + task.getArrivalTime() + " and has a service time of " + task.getServiceTime());
                        //System.out.println("Thread: " + Thread.currentThread().getName());
                    }
                    //iterator.remove();
                    //frame.update();
                }
                scheduler.printQueues();
            }

            currentTime++;
            scheduler.setCurrentTime(currentTime);
            System.out.println("==================");
            System.out.println("Current time: " + currentTime);
            System.out.println("==================");
            FileWrite.write("output.txt", "\n\n==================\n");
            FileWrite.write("output.txt", "Current time: " + currentTime + "\n");
            FileWrite.write("output.txt", "==================\n");

            MainController.updateLabels(currentTime);

            //wait for 1 second
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void main(String[] args) {
        SimulationManager gen = new SimulationManager();
        Thread t = new Thread(gen);
        t.start();

        /*Thread timeThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }


        });

        timeThread.start();*/
    }
}
