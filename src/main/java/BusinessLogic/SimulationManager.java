package BusinessLogic;

import Model.Server;
import Model.Task;
import graphicaluserinterface.MainController;

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


    int[] peakTime = new int[timeLimit];
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
        peakTime = new int[timeLimit];
        Arrays.fill(peakTime, 0);

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

    public int getAverageWaitingTime(List<Server> servers) {
        int sum = 0;
        int count = 0;
        for (Server s : servers) {
            sum += s.getAverageServerWaitingTime();
            count++;
        }
        if (count == 0) {
            return 0;
        } else {
            return (sum / count);
        }
    }

    public int getAverageQueueLength(List<Server> servers) {
        int sum = 0;
        int count = 0;
        for (Server s : servers) {
            sum += s.getTasks().size();
            count++;
        }
        if (count == 0) {
            return 0;
        } else {
            return (sum / count);
        }
    }

    @Override
    public void run() {

        //System.setOut(MainController.ps);
        //System.setErr(MainController.ps);

        FileWrite.emptyFile("output.txt");
        int currentTime = 0;
        System.out.println("Average service time: " + getAverageServiceTime(generatedTasks));
        FileWrite.write("output.txt", "Average service time: " + getAverageServiceTime(generatedTasks) + "\n");
        System.out.println("Peak hour: " + getPeakTime());
        FileWrite.write("output.txt", "Peak hour: " + getPeakTime() + "\n");
        while (currentTime < timeLimit) {

            System.out.println("==================");
            System.out.println("Current time: " + currentTime);
            System.out.println("==================");
            FileWrite.write("output.txt", "\n\n==================\n");
            FileWrite.write("output.txt", "Current time: " + currentTime + "\n");
            FileWrite.write("output.txt", "==================\n");
            //iterate through the list of tasks (generatedTasks)
            //pick the tasks that have the current time as arrival time
            //  -send task to queue => scheduler.dispatchTask(task)
            //  -remove task from list
            //update the UI frame => frame.update()

            synchronized (generatedTasks) {
                peakTime[currentTime] = getInQueueTasksNr();
                Iterator<Task> iterator = generatedTasks.iterator();
                System.out.println("Average queue length: " + getAverageQueueLength(scheduler.getServers()));
                FileWrite.write("output.txt", "Average queue length: " + getAverageQueueLength(scheduler.getServers()) + "\n");
                System.out.println("Average waiting time: " + getAverageWaitingTime(scheduler.getServers()));
                FileWrite.write("output.txt", "Average waiting time: " + getAverageWaitingTime(scheduler.getServers()) + "\n");
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

            //wait for 1 second
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("Peak hour: " + getPeakTime());
    }

    private int getInQueueTasksNr() {
        int sum = 0;
        for (Server s : scheduler.getServers()) {
            sum += s.getTasks().size();
        }
        return sum;
    }

    private int getPeakTime() {
        for (int i = 0; i < timeLimit; i++) {
            if (peakTime[i] == Arrays.stream(peakTime).max().getAsInt()) {
                return i;
            }
        }
        return 0;
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
            return (sum / count);
        }
    }

    public static void main(String[] args) {
        SimulationManager gen = new SimulationManager();
        Thread t = new Thread(gen);
        t.start();
    }
}
