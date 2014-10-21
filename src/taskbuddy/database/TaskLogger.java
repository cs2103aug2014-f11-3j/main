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
import java.util.ArrayList;
import java.util.Calendar;

import taskbuddy.logic.Task;

public class TaskLogger {

    private static final String ERR_CANNOT_PARSE = "Cannot parse task";
    private static final String ERR_CANNOT_OPEN_LOG = "Cannot open log file.";

    SimpleDateFormat formatter = new SimpleDateFormat(
            Task.DATABASE_DATE_TIME_FORMATTER);

    // @formatter:off
    private static final String TASK_ID             = "Task ID: ";
    private static final String TITLE               = "Title: ";
    private static final String DESCRIPTION         = "Description: ";
    private static final String START               = "Start: ";
    private static final String END                 = "End: ";
    private static final String PRIORITY            = "Priority: ";
    private static final String IS_COMPLETE         = "Completed: ";
    private static final String IS_FLOATING         = "Floating task: ";
    private static final String GOOGLE_CALENDAR_ID  = "Google Calendar ID: ";

    private static final String EMPTY_STRING        = "";
    private static final String DELIMITER_SPLIT     = "\\|";
    private static final int NUMBER_OF_FIELDS       = 9;
    
    
    private static final int POSITION_TASK_ID       = 0;
    private static final int POSITION_TITLE         = 1;
    private static final int POSITION_DESCRIPTION   = 2;
    private static final int POSITION_START         = 3;
    private static final int POSITION_END           = 4;
    private static final int POSITION_PRIORITY      = 5;
    private static final int POSITION_IS_COMPLETE   = 6;
    private static final int POSITION_IS_FLOATING   = 7;
    private static final int POSITION_GOOGLE_ID     = 8;
    // @formatter:on

    private static final String TASKS = " tasks:";

    File log;
    BufferedWriter writer;
    BufferedReader reader;

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
     * Prepares the log file to be read/written from/to. If the log file exists,
     * this method assumes that the log file came from a previous session. Thus
     * to use an existing log file, the text in the log file must be properly
     * formatted. If the log file does not exist, this method creates it and
     * logs all tasks into newly created log file.
     * 
     * @param logName
     *            name of log file
     * @return list of tasks read from existing log file, otherwise empty list
     *         of tasks for non-existing log file
     * @throws IOException
     *             when log file cannot be read properly
     * @throws ParseException
     *             when tasks cannot be parsed from log file properly
     */
    public ArrayList<Task> prepareLog(String logName) throws IOException,
            ParseException {
        ArrayList<Task> tasks = new ArrayList<Task>();
        this.log = new File(logName);

        if (this.getLog().isFile()) {
            tasks = this.readTasks();
        } else {
            try {
                log.createNewFile();
            } catch (Exception e) {
                // TODO Test this
                throw new IOException("Cannot create log file.", e);
            }
        }
        return tasks;
    }

    /**
     * Converts list of tasks to a string for writing to task log.
     * 
     * @param tasks
     *            list of tasks to be converted to string
     * @return string containing all tasks and associated information
     */
    public String tasksToString(ArrayList<Task> tasks) {
        int numberOfTasks = tasks.size();
        String result = Integer.toString(numberOfTasks) + TASKS + "\n";

        for (Task aTask : tasks) {
            result = result + aTask.displayTask() + "\n";
        }
        return result;
    }

    /**
     * Writes all existing tasks represented as a <code>String</code> into the
     * log file. The <code>prepareLog</code> method must be called first before
     * calling this.
     * 
     * @param tasks
     *            all currently existing tasks in TextBuddy's task manager
     *            represented as a <code>String</code>
     * @throws IOException
     *             if there are write problems to the log file
     */
    public void writeToLogFile(ArrayList<Task> tasks) throws IOException {
        Path logFilePath = this.getLog().toPath();
        try {
            writer = Files.newBufferedWriter(logFilePath,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.TRUNCATE_EXISTING);
            writer.write(this.tasksToString(tasks));
            writer.flush();
        } finally {
            writer.close();
        }
    }

    /**
     * Splits a task string, that is a string representing all the information
     * of a task, into a string array holding separate fields of the task.
     * 
     * @param taskString
     *            string holding all information of a task
     * @return a string array holding separate fields of the task
     */
    public String[] splitToFields(String taskString) {
        String[] splitFields = taskString.split(DELIMITER_SPLIT,
                NUMBER_OF_FIELDS);
        for (int i = 0; i < NUMBER_OF_FIELDS; i++) {
            splitFields[i] = splitFields[i].trim();
        }
        return splitFields;
    }

    /**
     * Extracts the task ID out of the task ID field split by
     * <code>splitToField</code> method.
     * 
     * @param displayTaskId
     *            task ID field split by <code>splitToField</code> method, which
     *            is also equivalent to the <code>Task</code> class'
     *            <code>displayTaskId</code> method
     * @return task ID
     */
    public int extractTaskId(String displayTaskId) {
        String taskIdString = displayTaskId.replace(TASK_ID, EMPTY_STRING);
        return Integer.parseInt(taskIdString);
    }

    /**
     * Similar to <code>extractTaskId</code> method, except that task title is
     * extracted here.
     * 
     * @param displayTitle
     *            description field split by <code>splitToField</code> method
     * @return title of this task
     */
    public String extractTitle(String displayTitle) {
        return displayTitle.replace(TITLE, EMPTY_STRING);
    }

    /**
     * Similar to <code>extractTitle</code> method, except that task description
     * is extracted here.
     * 
     * @param displayDescription
     *            description field split by <code>splitToField</code> method
     * @return description of this task
     */
    public String extractDescription(String displayDescription) {
        return displayDescription.replace(DESCRIPTION, EMPTY_STRING);
    }

    /**
     * Similar to <code>extractTitle</code> method, except that task's start
     * time is extracted here.
     * 
     * @param displayStart
     *            start time field split by <code>splitToField</code> method
     * @return start time of this task
     */
    public Calendar extractStart(String displayStart) throws ParseException {
        String startString = displayStart.replace(START, EMPTY_STRING);

        Calendar cal = Calendar.getInstance();
        cal.setTime(formatter.parse(startString));
        return cal;
    }

    /**
     * Similar to <code>extractStart</code> method, except that task's end time
     * is extracted here.
     * 
     * @param displayEnd
     *            end time field split by <code>splitToField</code> method
     * @return end time of this task
     */
    public Calendar extractEnd(String displayEnd) throws ParseException {
        String endString = displayEnd.replace(END, EMPTY_STRING);

        Calendar cal = Calendar.getInstance();
        cal.setTime(formatter.parse(endString));
        return cal;
    }

    /**
     * Similar to <code>extractTitle</code> method, except that task's priority
     * rank is extracted here
     * 
     * @param displayPriority
     *            priority field split by <code>splitToField</code> method
     * @return priority rank of this task
     */
    public int extractPriority(String displayPriority) {
        String priorityString = displayPriority.replace(PRIORITY, EMPTY_STRING);
        return Integer.parseInt(priorityString);
    }

    /**
     * Similar to <code>extractTitle</code> method, except that task completion
     * status is extracted here.
     * 
     * @param displayIsComplete
     *            isComplete field split by <code>splitToField</code> method
     * @return true if task is completed, false otherwise
     */
    public boolean extractIsComplete(String displayIsComplete) {
        String isCompleteString = displayIsComplete.replace(IS_COMPLETE,
                EMPTY_STRING);
        return Boolean.parseBoolean(isCompleteString);
    }

    /**
     * Similar to <code>extractIsComplete</code> method, except that task
     * floating status is extracted here.
     * 
     * @param displayIsFloating
     *            isFloating field split by <code>splitToField</code> method
     * @return true for a floating task, false otherwise
     */
    public boolean extractIsFloating(String displayIsFloating) {
        String isFloatingString = displayIsFloating.replace(IS_FLOATING,
                EMPTY_STRING);
        return Boolean.parseBoolean(isFloatingString);
    }

    /**
     * Similar to <code>extractTitle</code> method, except that task's Google
     * Calendar ID is extracted here.
     * 
     * @param displayGoogleId
     *            google ID field split by <code>splitToField</code> method
     * @return task's Google Calendar ID
     */
    public String extractGoogleId(String displayGoogleId) {
        return displayGoogleId.replace(GOOGLE_CALENDAR_ID, EMPTY_STRING);
    }

    /**
     * Converts a string representing all information of a task to a
     * <code>Task</code> object.
     * 
     * @param taskString
     *            string representing all information of a task
     * @return this task as a <code>Task</code> object
     * @throws ParseException
     *             when task string is not parsed properly
     */
    public Task readTask(String taskString) throws ParseException {
        Task result = new Task();
        try {
            String[] splitFields = this.splitToFields(taskString);

            result.setTaskId(this.extractTaskId(splitFields[POSITION_TASK_ID]));
            result.setTitle(this.extractTitle(splitFields[POSITION_TITLE]));
            result.setDescription(this
                    .extractDescription(splitFields[POSITION_DESCRIPTION]));
            result.setStartTime(this.extractStart(splitFields[POSITION_START]));
            result.setEndTime(this.extractEnd(splitFields[POSITION_END]));
            result.setPriority(this.extractPriority(splitFields[POSITION_PRIORITY]));
            result.setCompletion(this
                    .extractIsComplete(splitFields[POSITION_IS_COMPLETE]));
            result.setFloating(this
                    .extractIsFloating(splitFields[POSITION_IS_FLOATING]));
            result.setGID(this.extractGoogleId(splitFields[POSITION_GOOGLE_ID]));
        } catch (ParseException e) {
            // TODO Test this
            throw new ParseException(ERR_CANNOT_PARSE, e.getErrorOffset());
        }

        return result;
    }

    /**
     * Reads tasks from the log file and returns an arraylist of tasks.
     * 
     * @throws ParseException
     *             when task string read from log file is not parsed properly
     * @throws IOException
     *             when log file is not readable
     * @return arraylist of read tasks
     *
     */
    public ArrayList<Task> readTasks() throws IOException, ParseException {
        Path logPath = this.getLog().toPath();
        ArrayList<Task> result = new ArrayList<Task>();
        String aTaskString;

        try {
            reader = Files.newBufferedReader(logPath);
            // Always remove first header line, i.e. the line that says
            // "n tasks:", where n is the number of stored tasks.
            @SuppressWarnings("unused")
            String headerLineToDiscard = reader.readLine();

            while ((aTaskString = reader.readLine()) != null) {
                Task aTask = this.readTask(aTaskString);
                result.add(aTask);
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