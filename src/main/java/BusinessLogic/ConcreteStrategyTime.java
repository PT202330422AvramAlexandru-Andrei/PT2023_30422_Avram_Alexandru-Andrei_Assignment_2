package BusinessLogic;

import Model.Server;
import Model.Task;

import java.util.List;

public class ConcreteStrategyTime implements Strategy{

    @Override
    public void addTask(List<Server> servers, Task task) {
        Server server = selectServer(servers);
        server.addTask(task);
    }

    @Override
    public Server selectServer(List<Server> servers) {
        Server server = servers.get(0);
        for(Server s : servers) {
            if(s.getWaitingPeriod().get() < server.getWaitingPeriod().get()) {
                server = s;
            }
        }
        return server;
    }
}
