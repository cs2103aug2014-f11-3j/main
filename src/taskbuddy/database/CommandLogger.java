//@author A0098745L

package taskbuddy.database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;

import taskbuddy.logic.Task;

/**
 * This class logs the commands for syncing task manipulations to Google
 * Calendar when the user goes online again. When the user goes offline, task
 * manipulations in database are not executed in Google Calendar. These task
 * manipulations are logged so that the user can sync them to Google Calendar
 * should he exit TaskBuddy.
 *
 */
public class CommandLogger {

    private static final String ERROR_NO_SUCH_COMMAND = "No such command.";
    private static final String ERROR_CANNOT_CREATE_COMMAND_LOG = "Cannot create command log file.";
    private static final String ERR_CANNOT_OPEN_LOG = "Cannot open log file.";

    private static final String DELIMITER_SPLIT = "\\|";
    private static final int NUMBER_OF_SPLIT_ELEMENTS = 2;
    private static final int POSITION_COMMAND = 0;
    private static final int POSITION_TASK = 1;
    private static final String COMMANDS = " commands:";

    SimpleDateFormat formatter = new SimpleDateFormat(
            Task.DATABASE_DATE_TIME_FORMATTER);

    File log;
    BufferedWriter writer = null;
    BufferedReader reader = null;

    /**
     * Returns the <code>File</code> object representing the log file. Used
     * primarily for <code>File</code> and <code>Path</code> manipulation.
     * 
     * @return the <code>File</code> object representing the log file
     */
    public File getLog() {
        return log;
    }

    /**
     * Prepares the command log to be read/written from/to. If the command log
     * exists, this method assumes that it came from a previous session. If the
     * command log does not exist, this method creates it and logs all commands
     * into newly created command log.
     * 
     * @param logName
     *            name of command log file
     * @return queue of commands read from existing log file, otherwise empty
     *         command queue for non-existing command log
     * @throws IOException
     *             when log file cannot be read properly
     * @throws ParseException
     *             when commands cannot be parsed from log file properly
     */
    public LinkedList<GoogleCalendarCommand> prepareCommandLog(String logName)
            throws IOException, ParseException {
        LinkedList<GoogleCalendarCommand> commandQueue = new LinkedList<GoogleCalendarCommand>();
        this.log = new File(logName);

        if (this.getLog().isFile()) {
            commandQueue = this.readCommands();
        } else {
            try {
                log.createNewFile();
            } catch (IOException e) {
                // TODO Test this
                throw new IOException(ERROR_CANNOT_CREATE_COMMAND_LOG, e);
            }
        }
        return commandQueue;
    }

    /**
     * Converts all commands in the command queue to a string for writing to
     * command log.
     * 
     * @param commandQueue
     *            commands to be written to command log
     * @return string containing all commands and details about their associated
     *         tasks
     */
    public String commandsToString(
            LinkedList<GoogleCalendarCommand> commandQueue) {
        int numberOfCommands = commandQueue.size();
        String result = Integer.toString(numberOfCommands) + COMMANDS + "\n";

        for (GoogleCalendarCommand aCommand : commandQueue) {
            result = result + aCommand.displayCommand() + "\n";
        }
        return result;
    }

    /**
     * Writes all commands in the command queue as a string into the command
     * log. The <code>prepareCommandLog</code> method must be called first
     * before calling this.
     * 
     * @param commandQueue
     *            the commands in the command queue to be written to command log
     * @throws IOException
     *             if there are write problems to the log file
     */
    public void writeToLogFile(LinkedList<GoogleCalendarCommand> commandQueue)
            throws IOException {
        Path logFilePath = this.getLog().toPath();
        try {
            writer = Files.newBufferedWriter(logFilePath,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.TRUNCATE_EXISTING);
            writer.write(this.commandsToString(commandQueue));
            writer.flush();
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
    }

    /**
     * Splits a command string into its command type and the task to be
     * manipulated.
     * 
     * @param commandString
     *            command string read in from command log, which is also the
     *            result of the <code>displayCommand</code> method
     * @return a string array holding the command type and task
     */
    public String[] splitCommandTask(String commandString) {
        String[] result = commandString.split(DELIMITER_SPLIT,
                NUMBER_OF_SPLIT_ELEMENTS);
        for (int i = 0; i < NUMBER_OF_SPLIT_ELEMENTS; i++) {
            result[i] = result[i].trim();
        }
        return result;
    }

    /**
     * Reads in a <code>GoogleCalendarCommand</code> object from a given command
     * string.
     * 
     * @param commandString
     *            command string read in from command log, which is also the
     *            result of the <code>displayCommand</code> method
     * @return the read in <code>GoogleCalendarCommand</code> object
     * @throws ParseException
     *             when command string is not parsed properly
     */
    public GoogleCalendarCommand readCommand(String commandString)
            throws ParseException {
        String[] splitCommandTask = this.splitCommandTask(commandString);
        String commandType = splitCommandTask[POSITION_COMMAND];
        String taskString = splitCommandTask[POSITION_TASK];
        Task task = (new TaskLogger()).readTask(taskString);

        // @formatter:off
        switch (commandType) {
            case GoogleCalendarAdd.COMMAND_TYPE :
                return new GoogleCalendarAdd(task);
            case GoogleCalendarDelete.COMMAND_TYPE :
                return new GoogleCalendarDelete(task);
            case GoogleCalendarUpdate.COMMAND_TYPE :
                return new GoogleCalendarUpdate(task);
        default :
            throw new IllegalArgumentException(ERROR_NO_SUCH_COMMAND);
        }
        // @formatter:on
    }

    /**
     * Retrieves queue of commands from command log. This method can only be
     * executed after the <code>prepareCommandLog</code> method of this class.
     * 
     * @return queue of commands from command log
     * @throws ParseException
     *             when command string is not parsed properly
     * @throws IOException
     *             when command log cannot be read properly
     */
    public LinkedList<GoogleCalendarCommand> readCommands()
            throws ParseException, IOException {
        Path logPath = this.getLog().toPath();
        // @formatter:off
        LinkedList<GoogleCalendarCommand> result = 
                new LinkedList<GoogleCalendarCommand>();
        // @formatter:on
        String aCommandString;

        try {
            reader = Files.newBufferedReader(logPath);
            reader.readLine();

            while ((aCommandString = reader.readLine()) != null) {
                GoogleCalendarCommand aCommand = this
                        .readCommand(aCommandString);
                result.add(aCommand);
            }
        } catch (IOException e) {
            throw new IOException(ERR_CANNOT_OPEN_LOG, e);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return result;
    }

}