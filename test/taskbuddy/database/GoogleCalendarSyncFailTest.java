//@author A0098745L

package taskbuddy.database;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

import org.junit.Test;

import taskbuddy.database.GoogleCalendarCommand;
import taskbuddy.logic.Task;

public class GoogleCalendarSyncFailTest {
    // @formatter:off
    private static final String ERR_NOT_SYNCED_GOOGLE_CALENDAR = 
            "Changes made to database and task log but not Google Calendar. ";
    // @formatter:on

    Task firstTask;
    Task secondTask;
    Database database;
    DatabaseHandler myDatabaseHandler;
    OfflineGoogleCalendarManagerStub offlineGoogleCalendarManagerStub;

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

    public void setup() throws Exception {
        deleteLog();

        firstTask = createTask("First", "First description.");
        secondTask = createTask("Second", "Second description.");

        database = new Database();
        myDatabaseHandler = database.databaseHandler;

        // @formatter:off
        offlineGoogleCalendarManagerStub 
            = new OfflineGoogleCalendarManagerStub();
        // @formatter:on
        myDatabaseHandler.googleCal = offlineGoogleCalendarManagerStub;
        GoogleCalendarCommand.googleCal = myDatabaseHandler.googleCal;
    }

    public void checkCommandQueueCorrectness(
            LinkedList<GoogleCalendarCommand> googleCalendarCommands)
            throws ParseException, IOException {
        LinkedList<GoogleCalendarCommand> readCommands = myDatabaseHandler.commandLogger
                .readCommands();
        assertEquals("Number of commands read from command log is not the "
                + "same as that in command queue.",
                googleCalendarCommands.size(), readCommands.size());
        for (int i = 0; i < readCommands.size(); i++) {
            assertEquals("Title of command " + i + " in command log is"
                    + " different from that in command queue.",
                    googleCalendarCommands.get(i).getTask().getTitle(),
                    readCommands.get(i).getTask().getTitle());
        }
    }

    @Test
    public void testAdd() throws Exception {
        setup();
        LinkedList<GoogleCalendarCommand> googleCalendarCommands = myDatabaseHandler
                .getCommandQueue();
        ArrayList<Task> tasks = database.getTasks();

        try {
            database.addTask(firstTask);
            fail("No exception thrown when adding task "
                    + "to Google Calendar when user is offline.");
        } catch (UnknownHostException e) {
            String errorDatabase = ERR_NOT_SYNCED_GOOGLE_CALENDAR;
            // @formatter:on
            String errorGoogleCalendarManager = OfflineGoogleCalendarManagerStub.ERROR_USER_OFFLINE;
            // @formatter:off
            assertEquals("Wrong exception message when syncing to "
                    + "Google Calendar when user is offline.", errorDatabase
                    + errorGoogleCalendarManager, e.getMessage());

            assertEquals("Number of commands in command queue is not one.", 1,
                    googleCalendarCommands.size());
            assertEquals("Number of tasks in database is not one.", 1,
                    tasks.size());            
            checkCommandQueueCorrectness(googleCalendarCommands);
        }

        try {
            secondTask.setTaskId(firstTask.getTaskId());
            database.edit(secondTask);
            fail("No exception thrown when editing task to "
                    + "Google Calendar when user is offline.");
        } catch (UnknownHostException e) {
            assertEquals("Number of commands in command queue is not two.", 2,
                    googleCalendarCommands.size());
            assertEquals("Number of tasks in database is not one.", 1,
                    tasks.size());
            assertEquals("Task title not edited properly in database.",
                    secondTask.getTitle(), tasks.get(0).getTitle());
            assertEquals("Task description not edited properly in database.",
                    secondTask.getDescription(), tasks.get(0).getDescription());
            checkCommandQueueCorrectness(googleCalendarCommands);
        }

        try {
            database.delete(1);
            fail("No exception thrown when deleting task from "
                    + "Google Calendar when user is offline.");
        } catch (UnknownHostException e) {
            assertEquals("Number of commands in command queue is not three.",
                    3, googleCalendarCommands.size());
            assertEquals("Number of tasks in database is not zero.", 0,
                    tasks.size());
            checkCommandQueueCorrectness(googleCalendarCommands);
        }

        assertTrue("First command to be executed is not the add command.",
                googleCalendarCommands.peek() instanceof GoogleCalendarAdd);
        assertTrue(
                "Last command to be executed is not the delete command.",
                googleCalendarCommands.peekLast() instanceof 
                    GoogleCalendarDelete);

        try {
            myDatabaseHandler.forwardSync();
            fail("No exception thrown when trying "
                    + "to sync when user is offline.");
        } catch (UnknownHostException e) {
            assertEquals("Number of commands in command queue is not three.",
                    3, googleCalendarCommands.size());
            checkCommandQueueCorrectness(googleCalendarCommands);
        }

        // @formatter:off
        GoogleCalendarManagerStub googleCalendarManagerStub 
            = new GoogleCalendarManagerStub();
        // @formatter:on
        myDatabaseHandler.googleCal = googleCalendarManagerStub;
        GoogleCalendarCommand.googleCal = googleCalendarManagerStub;
        try {
            myDatabaseHandler.forwardSync();
            assertEquals("Number of commands in command queue is not zero.", 0,
                    googleCalendarCommands.size());
        } catch (UnknownHostException e) {
            fail("Exception thrown even though sync is supposed to be "
                    + "successful when user is online.");
            checkCommandQueueCorrectness(googleCalendarCommands);
        }
    }

    @Test
    public void testGoogleIdLog() throws Exception {
        setup();
        try {
            database.addTask(firstTask);
        } catch (Exception e) {
            assertNull(firstTask.getGID());
            Database newDatabase = new Database();
            assertNotEquals("Google Calendar ID of read task from task log is "
                    + "the same as that of firstTask, which is null.",
                    firstTask.getGID(), newDatabase.getTasks().get(0).getGID());
            assertEquals("Google Calendar ID of read task from "
                    + "task log is not 'null'.", "null", newDatabase.getTasks()
                    .get(0).getGID());
        }
    }
}