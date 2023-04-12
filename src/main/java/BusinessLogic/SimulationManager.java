package BusinessLogic;

import GUI.SimulationFrame;
import Model.Task;

import java.util.List;

public class SimulationManager implements Runnable{

    //data read  from UI
    public int timeLimit = 100;
    public int maxProcessingTime;
    public int minProcessingTime;
    public int numberOfServers;
    public int numberOfClients;
    public SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_TIME;

    //entity responsible for queue management and client distribution
    private Scheduler scheduler;

    //frame for displaying the simulation
    private SimulationFrame frame;

    //pool of tasks to be processed
    private List<Task> tasks;

    public SimulationManager() {
        //initialize the scheduler
        //  -create and start numberOfSerbers threads
        //  -initialize selection strategy => createStrategy()
        //initialize the frame
        //generate nrOfClients clients => generateNRandomTasks()
        //store the tasks in generatedTasks
    }

    public void generateNRandomTasks() {
        //generate N random tasks
        //  -random processing time
        //minProcessingTime < processingTime < maxProcessingTime
        //  -random arrival time
        //sort the list by arrival time
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
            currentTime++;
            //wait for 1 second
        }
    }

    public static void main(String[] args) {
        SimulationManager gen = new SimulationManager();
        Thread t = new Thread(gen);
        t.start();
    }
}
