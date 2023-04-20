package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Scheduler {
    private List<Server> servers = Collections.synchronizedList(new ArrayList<>());
    private Integer maxNoServers;
    private Integer maxTasksPerServer;
    private Strategy strategy = new ConcreteStrategyQueue();

    private int currentTime = 0;

    public Scheduler(List<Server> servers, Integer maxNoServers, Integer maxTasksPerServer, Strategy strategy) {
        this.servers = servers;
        this.maxNoServers = maxNoServers;
        this.maxTasksPerServer = maxTasksPerServer;
        this.strategy = strategy;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    public Scheduler(int maxNoServers) {
        //for max nr of servers
        // -create a new server object
        // -create a new thread for the object

        for (int i = 0; i < maxNoServers; i++) {
            Server server = new Server();
            servers.add(server);
            Thread thread = new Thread(server);
            thread.start();
        }
    }

    public void changeStrategy(SelectionPolicy policy) {
        //apply strategy pattern to instantiate the strategy with the concrete strategy corresponding to the policy
        switch (policy) {
            case SHORTEST_QUEUE:
                strategy = new ConcreteStrategyQueue();
                break;
            case SHORTEST_TIME:
                strategy = new ConcreteStrategyTime();
                break;
        }
    }

    public synchronized void dispatchTask(Task task) {
        Server selectedServer = strategy.selectServer(servers);
        selectedServer.addTask(task);

        //System.out.println("Thread: " + Thread.currentThread().getName());

    }

    public List<Server> getServers() {
        return servers;
    }

    public void printQueues() {
        for (Server s : servers) {
            if (s.getTasks().size() > 0) {
                System.out.println("Queue for server " + servers.indexOf(s) + ": ");
                FileWrite.write("output.txt", "\nQueue for server " + servers.indexOf(s) + ": ");
                for (Task t : s.getTasks()) {
                    System.out.println(t.getId());
                    FileWrite.write("output.txt", t.getId() + "  ");
                }
                System.out.println();
            }else {
                System.out.println("Queue for server " + servers.indexOf(s) + ": empty");
                FileWrite.write("output.txt", "\nQueue for server " + servers.indexOf(s) + ": empty");
            }
        }
    }
}
