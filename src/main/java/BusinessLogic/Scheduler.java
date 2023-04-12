package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.List;

public class Scheduler {
    private List<Server> servers;
    private Integer maxNoServers;
    private Integer maxTasksPerServer;
    private Strategy strategy;

    public Scheduler(List<Server> servers, Integer maxNoServers, Integer maxTasksPerServer, Strategy strategy) {
        this.servers = servers;
        this.maxNoServers = maxNoServers;
        this.maxTasksPerServer = maxTasksPerServer;
        this.strategy = strategy;
    }

    public Scheduler(int maxNoServers, int maxTasksPerServer) {
        //for max nr of servers
        // -create a new server object
        // -create a new thread for the object

        Server server = new Server();
        Thread thread = new Thread(server);
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

    public void dispatchTask(Task task) {
        strategy.addTask(servers, task);
    }

    public List<Server> getServers() {
        return servers;
    }
}
