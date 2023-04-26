package BusinessLogic;

import Model.Server;
import Model.Task;
import com.example.queuemanager.MainController;

import java.util.*;

public class SimulationManager implements Runnable {

    //data read  from UI
    public int timeLimit = 100;
    public int maxProcessingTime = 10;
    public int minProcessingTime = 2;

    public int maxArrivalTime = 10;
    public int minArrivalTime = 2;
    public int numberOfServers = 3;
    public int numberOfTasks = 100;
    public SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_QUEUE;

    //entity responsible for queue management and client distribution
    private Scheduler scheduler;

    //frame for displaying the simulation

    //pool of tasks to be processed
    private List<Task> generatedTasks = Collections.synchronizedList(new ArrayList<>());

    public SimulationManager() {
        //initialize the scheduler
        scheduler = new Scheduler(numberOfServers);

        //  -create and start numberOfServers threads
        //  -initialize selection strategy => createStrategy()
        //scheduler.changeStrategy(selectionPolicy);

        //initialize the frame
        //generate nrOfClients clients => generateNRandomTasks()
        //store the tasks in generatedTasks
        int N = (int) Math.random() * 100;
        generateNRandomTasks(N);
    }

    public SimulationManager(int N, int timeLimit, int maxProcessingTime, int minProcessingTime, int minArrivalTime, int maxArrivalTime, int numberOfServers, SelectionPolicy selectionPolicy) {
        this.timeLimit = timeLimit;
        this.maxProcessingTime = maxProcessingTime;
        this.minProcessingTime = minProcessingTime;
        this.numberOfServers = numberOfServers;
        this.selectionPolicy = selectionPolicy;
        this.maxArrivalTime = maxArrivalTime;
        this.minArrivalTime = minArrivalTime;

        scheduler = new Scheduler(numberOfServers);
        scheduler.changeStrategy(selectionPolicy);

        generateNRandomTasks(N);
    }

    public void generateNRandomTasks(int N) {
        //generate N random tasks
        //  -random processing time
        //minProcessingTime < processingTime < maxProcessingTime
        //  -random arrival time
        //sort the list by arrival time

        for (int i = 0; i < N; i++) {
            Task task = new Task();
            task.setServiceTime((int) (Math.random() * (maxProcessingTime - minProcessingTime) + minProcessingTime));
            task.setArrivalTime((int) (Math.random() * (maxArrivalTime - minArrivalTime) + minArrivalTime));
            task.setId(i);
            generatedTasks.add(task);
        }

        generatedTasks.sort(Comparator.comparingInt(Task::getArrivalTime));
        System.out.println("Generated tasks: " + generatedTasks.size());
        FileWrite.write("output.txt", "Generated tasks: " + generatedTasks.size() + "\n");
    }

    public int getAverageWaitingTime(List<Task> generatedTasks) {
        int sum = 0;
        int count = 0;
        for (Task t : generatedTasks) {
            sum += t.getServiceTime();
            count++;
        }
        if (count == 0) {
            return 0;
        } else {
            return (sum / count);
        }
    }

    public int getAverageQueueLength(List<Task> generatedTasks) {
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

        System.setOut(MainController.ps);
        System.setErr(MainController.ps);

        FileWrite.emptyFile("output.txt");
        int currentTime = 0;
        System.out.println("Average waiting time: " + getAverageWaitingTime(generatedTasks));
        FileWrite.write("output.txt", "Average waiting time: " + getAverageWaitingTime(generatedTasks) + "\n");
        System.out.println("Average service time: " + getAverageServiceTime(generatedTasks));
        FileWrite.write("output.txt", "Average service time: " + getAverageServiceTime(generatedTasks) + "\n");
        System.out.println("Peak hour: " + getPeakTime(generatedTasks));
        FileWrite.write("output.txt", "Peak hour: " + getPeakTime(generatedTasks) + "\n");
        while (currentTime < timeLimit) {
            //iterate through the list of tasks (generatedTasks)
            //pick the tasks that have the current time as arrival time
            //  -send task to queue => scheduler.dispatchTask(task)
            //  -remove task from list
            //update the UI frame => frame.update()

            synchronized (generatedTasks) {
                Iterator<Task> iterator = generatedTasks.iterator();
                System.out.println("Average queue length: " + getAverageQueueLength(generatedTasks));
                FileWrite.write("output.txt", "Average queue length: " + getAverageQueueLength(generatedTasks) + "\n");
                System.out.println("Waiting tasks: " + generatedTasks.toString());
                FileWrite.write("output.txt", "Waiting tasks: " + generatedTasks.toString() + "\n");
                while (iterator.hasNext()) {
                    Task task = iterator.next();
                    if (task.getArrivalTime() == currentTime) {
                        scheduler.dispatchTask(task);
                        iterator.remove();
                    }
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

            //wait for 1 second
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private int getPeakTime(List<Task> generatedTasks) {
        int[] peakTime = new int[timeLimit];
        for (int i = 0; i < timeLimit; i++) {
            peakTime[i] = 0;
        }

        for (Task t : generatedTasks) {
            peakTime[t.getArrivalTime()]++;
        }
        return Arrays.stream(peakTime).max().getAsInt();
    }

    private int getAverageServiceTime(List<Task> tasks) {
        int sum = 0;
        int count = 0;
        for (Task t : tasks) {
            sum += t.getServiceTime();
            count++;
        }
        if (count == 0) {
            return 0;
        } else {
            return (int) (sum / count);
        }
    }

    public static void main(String[] args) {
        SimulationManager gen = new SimulationManager();
        Thread t = new Thread(gen);
        t.start();
    }
}
