package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.List;

public class ConcreteStrategyQueue implements Strategy{
    @Override
    public void addTask(List<Server> servers, Task task) {
        Server selectedServer = null;
        int shortestQueueSize = Integer.MAX_VALUE;

        // Iterate through all servers to find the one with the shortest queue
        for (Server server : servers) {
            if (server.getTasks().size() < shortestQueueSize) {
                shortestQueueSize = server.getTasks().size();
                selectedServer = server;
            }
        }

        // If a server was found, add the task to its queue
        if (selectedServer != null) {
            selectedServer.addTask(task);
        }

        /*Server server = servers.get(0);
        for (Server s : servers) {
            if (s.getTasks().size() < server.getTasks().size()) {
                server = s;
            }
        }
        server.addTask(task);*/
    }
}
