//@author A0098745L

package taskbuddy.database;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import taskbuddy.database.Database;
import taskbuddy.logic.Task;

public class IntegratedGoogleCalDatabaseTest {

    Database database;
    Task firstTask;
    Task secondTask;
    Task thirdTask;

    String expected;
    String actual;

    /**
     * Deletes existing log file before running tests
     */
    public void deleteLog() {
        File log = new File(DatabaseHandler.TASK_LOG_NAME);
        if (log.isFile()) {
            log.delete();
        }
    }

    public void addTasks() throws IOException, UnknownHostException {
        database.addTask(firstTask);
        database.addTask(secondTask);
        database.addTask(thirdTask);
    }

    public Task createTask(String title, String description) {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        int priority = 1;
        boolean isComplete = true;
        boolean isFloating = false;

        Task task = new Task(title);
        task.setDescription(description);
        task.setStartTime(start);
        task.setEndTime(end);
        task.setPriority(priority);
        task.setCompletion(isComplete);
        task.setFloating(isFloating);

        return task;
    }

    @Before
    public void setup() throws Exception {
        deleteLog();

        database = new Database();
        firstTask = createTask("First", "First description.");
        secondTask = createTask("Second", "Second description.");
        thirdTask = createTask("Third", "Third description.");
    }

    /**
     * Have to manually check if tasks are added to Google Calendar
     */
    @Test
    public void testEdit() throws Exception {
        addTasks();
        assertEquals("Number of tasks is not three", 3, database.getTasks()
                .size());

        // Create new task with same task and Google ID as old task
        Task newTask = createTask("New task", "New description.");
        int taskIDToEdit = 2;
        Task oldTask = database.read(taskIDToEdit);
        newTask.setTaskId(taskIDToEdit);
        newTask.setGID(oldTask.getGID());

        // Edit second task by replacing second task with new task
        database.edit(newTask);
        int indexOfEditedTask = taskIDToEdit - 1;
        assertTrue("Second task is not replaced properly",
                newTask.equals(database.getTasks().get(indexOfEditedTask)));

        // Check that other tasks are not edited
        expected = firstTask.displayTitle();
        actual = database.getTasks().get(0).displayTitle();
        assertTrue("First task got edited.", expected.equals(actual));

        expected = thirdTask.displayTitle();
        actual = database.getTasks().get(2).displayTitle();
        assertTrue("Third task got edited.", expected.equals(actual));

        // Test for task edition to task log
        // Read tasks from log file first
        ArrayList<Task> readTasks = database.databaseHandler.taskLogger
                .readTasks();
        // Then compare read tasks from log file with that of arraylist in
        // database
        for (int i = 0; i < 3; i++) {
            actual = readTasks.get(i).displayTask();
            expected = database.getTasks().get(i).displayTask();
            assertTrue("Task " + i + " not logged correctly in log file.",
                    actual.equals(expected));
        }
    }

    // @Test
    // public void testAddDelete() throws Exception {
    // setup();
    // database.delete(0);
    // }

    @Ignore
    @Test
    public void testSearchEdit() throws Exception {
        setup();

        String searchString = "DESCRIP";
        ArrayList<Task> searchResults = database.search(searchString);

        for (Task aTask : searchResults) {
            System.out.println(aTask.getTitle());
        }
        searchResults.get(1).setTitle("change title");
        for (Task aTask : searchResults) {
            System.out.println(aTask.getTaskId() + ": " + aTask.getTitle());
        }
    }

}
