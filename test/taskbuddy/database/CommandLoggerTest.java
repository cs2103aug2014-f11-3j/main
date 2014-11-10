//@author A0098745L

package taskbuddy.database;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Calendar;
import java.util.LinkedList;

import org.junit.Test;

import taskbuddy.database.DatabaseHandler;
import taskbuddy.logic.Task;

public class CommandLoggerTest {
    Task firstTask;
    Task secondTask;
    String logName;
    CommandLogger commandLogger;

    String expected;
    String actual;
    GoogleCalendarAdd firstAddCommand;
    GoogleCalendarDelete firstDeleteCommand;
    GoogleCalendarUpdate firstUpdateCommand;
    LinkedList<GoogleCalendarCommand> commandQueue;

    /**
     * Deletes existing log file before running tests
     */
    public void deleteLog() {
        File log = new File(DatabaseHandler.COMMAND_LOG_NAME);
        if (log.isFile()) {
            log.delete();
        }
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

        firstAddCommand = new GoogleCalendarAdd(firstTask);
        firstDeleteCommand = new GoogleCalendarDelete(firstTask);
        firstUpdateCommand = new GoogleCalendarUpdate(firstTask);

        commandQueue = new LinkedList<GoogleCalendarCommand>();
        commandLogger = new CommandLogger();
        logName = DatabaseHandler.COMMAND_LOG_NAME;
    }

    @Test
    public void testSplitCommandTask() throws Exception {
        setup();
        String[] splitCommandTask = commandLogger
                .splitCommandTask(firstAddCommand.displayCommand());
        assertEquals("Add command type not split from add command correctly.",
                "Add", splitCommandTask[0]);
        assertEquals("First task not split from add command correctly.",
                firstTask.displayTask(), splitCommandTask[1]);
        // TODO repeat for other command types
    }

    @Test
    public void testReadCommand() throws Exception {
        setup();
        // @formatter:off
        GoogleCalendarCommand readCommand = 
                commandLogger.readCommand(firstAddCommand.displayCommand());
        // @formatter:on
        assertTrue("Add command type not read properly.",
                readCommand instanceof GoogleCalendarAdd);
        assertEquals("Task not read properly from add command.", readCommand
                .getTask().getTitle(), firstTask.getTitle());
        // TODO repeat for description
    }

    @Test
    public void testPrepareCommandLog() throws Exception {
        setup();
        File commandLog = new File(logName);
        assertFalse("Command log exists when it's not supposed to.",
                commandLog.isFile());

        commandQueue.add(firstAddCommand);
        commandQueue.add(firstDeleteCommand);
        commandQueue.add(firstUpdateCommand);

        // Create dummy command log
        commandLogger.prepareCommandLog(logName);
        commandLogger.writeToLogFile(commandQueue);
        assertTrue("Command log doesn't exist when it's supposed to.",
                commandLog.isFile());

        LinkedList<GoogleCalendarCommand> readCommandQueue = commandLogger
                .prepareCommandLog(logName);
        GoogleCalendarCommand nextCommand;
        nextCommand = readCommandQueue.remove();
        assertTrue(
                "First command popped out of command queue is not add command.",
                nextCommand instanceof GoogleCalendarAdd);
        assertEquals("Task in first command not correct.",
                firstTask.getTitle(), nextCommand.getTask().getTitle());
        nextCommand = readCommandQueue.remove();
        assertTrue(
                "Second command popped out of command queue is not delete command.",
                nextCommand instanceof GoogleCalendarDelete);
        assertEquals("Task in second command not correct.",
                firstTask.getTitle(), nextCommand.getTask().getTitle());
        nextCommand = readCommandQueue.remove();
        assertTrue(
                "Third command popped out of command queue is not update command.",
                nextCommand instanceof GoogleCalendarUpdate);
        assertEquals("Task in first command not correct.",
                firstTask.getTitle(), nextCommand.getTask().getTitle());

    }
}
