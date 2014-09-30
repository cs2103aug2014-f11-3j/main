package taskbuddy.database;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;

import org.junit.Test;

import taskbuddy.database.Database;
import taskbuddy.logic.Task;

public class DatabaseTest {

    Database database;
    Task task;

    public void createTask() {
        task = new Task();
    }

    public void setup() {
        database = new Database();
        createTask();
    }

    @Test
    public void testDatabase() throws Exception {
        setup();
        assertTrue("Database not constructed with arraylist of Task objects.",
                database.tasks instanceof ArrayList);
        assertTrue("Database not constructed with linkedlist of DbCommand "
                + "objects.", database.commands instanceof LinkedList);
    }

    @Test
    public void testAddTask() throws Exception {
        setup();
        database.addTask(task);
        assertTrue("Task not added properly", database.getTasks().get(0)
                .equals(task));
    }
}
