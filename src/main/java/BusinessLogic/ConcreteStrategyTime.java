package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.List;

public class ConcreteStrategyTime implements Strategy{
    @Override
    public void addTask(List<Server> servers, Task task) {
        for (Server server : servers) {
            if (server.getTasks().size() < 5) {
                server.addTask(task);
                break;
            }
        }
    }
}
