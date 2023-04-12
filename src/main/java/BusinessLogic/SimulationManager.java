package BusinessLogic;

import GUI.SimulationFrame;
import Model.Server;
import Model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SimulationManager implements Runnable{

    //data read  from UI
    public int timeLimit = 100;
    public int maxProcessingTime = 10;
    public int minProcessingTime = 2;
    public int numberOfServers = 3;
    public int numberOfClients = 100;
    public SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_TIME;

    //entity responsible for queue management and client distribution
    private Scheduler scheduler = new Scheduler(numberOfServers, 5);

    //frame for displaying the simulation
    private SimulationFrame frame;

    //pool of tasks to be processed
    private List<Task> generatedTasks = Collections.synchronizedList(new ArrayList<>());

    public SimulationManager() {
        //initialize the scheduler
        Scheduler scheduler = new Scheduler(numberOfServers, 5);

        //  -create and start numberOfServers threads
        for (int i = 0; i < numberOfServers; i++) {
            Server server = new Server();
            Thread thread = new Thread(server);
            thread.start();
        }

        //  -initialize selection strategy => createStrategy()
        scheduler.changeStrategy(selectionPolicy);

        //initialize the frame
        frame = new SimulationFrame();
        //generate nrOfClients clients => generateNRandomTasks()
        //store the tasks in generatedTasks
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

        generatedTasks.sort((o1, o2) -> o1.getArrivalTime() - o2.getArrivalTime());
        System.out.println("Generated tasks: " + generatedTasks.size());
    }

    @Override
    public void run() {

        int currentTime = 0;
        while (currentTime < timeLimit) {
            //iterate through the list of tasks (generatedTasks)
            //pick the tasks that have the current time as arrival time
            //  -send task to queue => scheduler.dispatchTask(task)
            //  -remove task from list
            //update the UI frame => frame.update()

            Iterator<Task> iterator = generatedTasks.iterator();
            while (iterator.hasNext()) {
                Task task = iterator.next();
                if (task.getArrivalTime() == currentTime) {
                    scheduler.dispatchTask(task);
                    generatedTasks.remove(task);
                    System.out.println("Task " + task.getId() + " arrived at " + task.getArrivalTime() + " and has a service time of " + task.getServiceTime());

                }
                //iterator.remove();
                //frame.update();
            }

            currentTime++;
            System.out.println("Current time: " + currentTime);
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
    }
}
