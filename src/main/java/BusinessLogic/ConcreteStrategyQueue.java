package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.List;

public class ConcreteStrategyQueue implements Strategy{
    @Override
    public void addTask(List<Server> servers, Task task) {
        Server server = servers.get(0);
        for (Server s : servers) {
            if (s.getTasks().size() < server.getTasks().size()) {
                server = s;
            }
        }
        server.addTask(task);
    }
}
