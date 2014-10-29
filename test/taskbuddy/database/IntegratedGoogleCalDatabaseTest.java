package taskbuddy.database;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;

import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import taskbuddy.database.Database;
import taskbuddy.logic.Task;

public class IntegratedGoogleCalDatabaseTest {

    Task task;
    Database database;
    private Task firstTask;
    private Task secondTask;
    private Task thirdTask;

    String expected;
    String actual;

    public void setup() throws Exception {
        database = new Database();

        firstTask = createTask("First");
        secondTask = createTask("Second");
        thirdTask = createTask("Third");

        database.addTask(firstTask);
        database.addTask(secondTask);
        database.addTask(thirdTask);
    }

    public Task createTask(String title) {
        String description = "Description";
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

    // TODO
    @Test
    public void testEdit() throws Exception {
        setup();
        assertEquals("Number of tasks is not three", 3, database.getTasks()
                .size());

        // Create new task with same task and Google ID as old task
        Task newTask = createTask("New task");
        int taskIndexToEdit = 1;
        Task oldTask = database.getTasks().get(taskIndexToEdit);
        newTask.setTaskId(oldTask.getTaskId());
        newTask.setGID(oldTask.getGID());

        // Edit second task by replacing second task with new task
        database.edit(newTask);
        assertTrue("Second task is not replaced properly",
                newTask.equals(database.getTasks().get(taskIndexToEdit)));

        // Check that other tasks are not edited
        expected = firstTask.displayTitle();
        actual = database.getTasks().get(0).displayTitle();
        assertTrue("First task got edited.", expected.equals(actual));

        expected = secondTask.displayTitle();
        actual = database.getTasks().get(2).displayTitle();
        assertTrue("Third task got edited.", expected.equals(actual));

        // Test for task edition to task log
        // Read tasks from log file first
        ArrayList<Task> readTasks = database.databaseHandler.taskLogger.readTasks();
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

    /**
     * Deletes existing log file after all tests have been run
     */
    @After
    public void deleteLog() {
        File log = new File("log");

        if (log.isFile()) {
            log.delete();
        }
    }

}
