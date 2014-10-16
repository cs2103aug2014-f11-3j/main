package taskbuddy.logic;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;

public class TaskTest {
    // @formatter:off
    private static final String DELIMITER           = " | ";
    private static final String TASK_ID             = "Task ID: ";
    private static final String TITLE               = "Title: ";
    private static final String DESCRIPTION         = "Description: ";
    private static final String START               = "Start: ";
    private static final String END                 = "End: ";
    private static final String PRIORITY            = "Priority: ";
    private static final String IS_COMPLETE         = "Completed: ";
    private static final String IS_FLOATING         = "Floating task: ";
    private static final String GOOGLE_CALENDAR_ID  = "Google Calendar ID: ";
    // @formatter:on

    int year;
    int month;
    int date;
    int hour;
    int minute;
    int second;
    Calendar start;
    Calendar end;

    Task task;

    int taskId;
    String title;
    String description;
    int priority;
    boolean isComplete;
    boolean isFloating;
    String googleCalendarId;

    public void setup() {
        year = 1;
        month = 1;
        date = 1;
        hour = 1;
        minute = 1;
        second = 1;

        // Month argument is an int from 0-11, not 1-12
        start = new GregorianCalendar(year, month - 1, date, hour, minute,
                second);
        end = new GregorianCalendar(year, month - 1, date, hour, minute, second);

        taskId = 1;
        title = "Title";
        description = "Description";
        priority = 1;
        isComplete = true;
        isFloating = false;
        googleCalendarId = "11111";

        task = new Task("Title");
        task.setTaskId(taskId);
        task.setDescription(description);
        task.setStartTime(start);
        task.setEndTime(end);
        task.setPriority(priority);
        task.setCompletion(true);
        task.setFloating(isFloating);
        task.setGID(googleCalendarId);
    }

    @Test
    public void testDisplayTask() throws Exception {
        setup();
        String expected;
        String actual;

        // Test for conversion of start/end dates/times from string to Calendar
        // type.
        expected = date + "-" + month + "-" + String.format("%04d", year)
                + " at " + String.format("%02d", hour) + ":"
                + String.format("%02d", minute);
        // Test for start
        actual = task.displayDateTime(start);
        assertTrue("Calendar not converted to string properly.",
                actual.equals(expected));
        // Test for end
        actual = task.displayDateTime(end);
        assertTrue("Calendar not converted to string properly.",
                actual.equals(expected));

        // @formatter:off
        expected = TASK_ID + taskId + DELIMITER +
               TITLE + title + DELIMITER + 
               DESCRIPTION + description + DELIMITER + 
               START + task.displayDateTime(task.getStartTime()) + DELIMITER +
               END + task.displayDateTime(end) + DELIMITER +
               PRIORITY + priority + DELIMITER +
               IS_COMPLETE + Boolean.toString(isComplete) + DELIMITER + 
               IS_FLOATING + Boolean.toString(isFloating) + DELIMITER + 
               GOOGLE_CALENDAR_ID + googleCalendarId;
        // @formatter:on

        actual = task.displayTask();
        assertTrue("Task does not convert to string properly.",
                expected.equals(actual));

    }
}
