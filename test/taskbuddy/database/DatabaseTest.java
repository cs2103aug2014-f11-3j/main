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
    String title;

    public void createTask() {
        title = "Title";
        task = new Task("Title");
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
        assertTrue(database.addTask(task));
        assertTrue("Task not added properly", database.getTasks().get(0)
                .equals(task));
    }

    @Test
    public void testRead() throws Exception {
        setup();
        assertNull(database.read(title));

        database.addTask(task);
        assertTrue("Task titled 'task' not retrieved.", database.read(title)
                .equals(task));

        // Task titled 'untitled' not added yet
        title = "Untitled";
        assertNull(database.read(title));

        task.setTitle(title);
        database.addTask(task);
        assertTrue("Task titled 'untitled' not retrieved.",
                database.read(title).equals(task));

    }
}
