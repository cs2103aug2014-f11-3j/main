package taskbuddy.database;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import taskbuddy.logic.Task;

public class Database {
    ArrayList<Task> tasks;
    LinkedList<DbCommand> commands;
    
    public Database() {
        tasks = new ArrayList<Task>();
        commands = new LinkedList<DbCommand>();
    }
    
    

}
