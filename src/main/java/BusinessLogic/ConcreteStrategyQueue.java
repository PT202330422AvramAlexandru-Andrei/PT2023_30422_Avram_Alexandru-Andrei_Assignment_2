package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.Comparator;
import java.util.List;

public class ConcreteStrategyQueue implements Strategy{
    @Override
    public void addTask(List<Server> servers, Task task) {
        Server server = selectServer(servers);
        server.addTask(task);
        //System.out.println("Thread: " + Thread.currentThread().getName());
    }

    @Override
    public Server selectServer(List<Server> servers) {

        /*Server selected = null;
        int min = Integer.MAX_VALUE;
        for (Server s : servers) {
            if (s.getTasks().size() < min) {
                min = s.getTasks().size();
                selected = s;
            }
        }*/

        Server selected = servers.stream()
                .min(Comparator.comparingInt(server -> server.getTasks().size()))
                .orElse(null);

        return selected;
    }
}
