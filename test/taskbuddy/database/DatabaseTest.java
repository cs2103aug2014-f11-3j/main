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
        assertEquals("Number of tasks did not increase from 0 to 1 after task "
                + "addition", 1, database.getTasks().size());
        assertTrue("Task not added properly", database.getTasks().get(0)
                .equals(task));
    }

    @Test
    public void testRead() throws Exception {
        setup();
        assertNull(database.read(title));

        database.addTask(task);
        assertTrue("Task titled 'Title' not retrieved.", database.read(title)
                .equals(task));

        // Task titled 'untitled' not added yet
        title = "Untitled";
        assertNull(database.read(title));

        task.setTitle(title);
        database.addTask(task);
        assertTrue("Task titled 'Untitled' not retrieved.", database
                .read(title).equals(task));
    }

    @Test
    public void testDelete() throws Exception {
        setup();
        assertFalse("Task is deleted even though list of tasks is empty",
                database.delete(title));

        // Number of tasks is now 1
        database.addTask(task);

        assertFalse("Delete method returns true even when there's no task "
                + "titled 'Untitled'.", database.delete("Untitled"));
        assertEquals("Number of tasks is not 1 even no task was deleted.", 1,
                database.getTasks().size());

        assertTrue("Delete method does not return true after deletion.",
                database.delete(title));
        assertTrue("List of tasks is not empty after deletion.", database
                .getTasks().isEmpty());

    }
}
