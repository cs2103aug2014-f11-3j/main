package taskbuddy.logic;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;

public class TaskTest {
    private static final String DELIMITER = " | ";
    private static final String TITLE = "Title: ";
    private static final String DESCRIPTION = "Description: ";
    private static final String START = "Start: ";
    private static final String END = "End: ";
    private static final String PRIORITY = "Priority: ";
    private static final String IS_COMPLETE = "Completed: ";
    private static final String IS_FLOATING = "Floating task: ";
    private static final String GOOGLE_CALENDAR_ID = "Google Calendar ID: ";

    int year;
    int month;
    int date;
    int hour;
    int minute;
    int second;
    Calendar cal;

    Task task;

    String title;
    String description;
    String start;
    String endDate;
    String endTime;
    int priority;
    boolean isComplete;
    boolean isFloating;
    String googleCalendarId = "11111";

    public void createTask() {

    }

    public void setup() {
        year = 1;
        month = 1;
        date = 1;
        hour = 1;
        minute = 1;
        second = 1;

        // Month argument is an int from 0-11, not 1-12
        cal = new GregorianCalendar(year, month - 1, date, hour, minute, second);

        title = "Title";
        description = "Description";
        start = "";
        endDate = "01000001";
        endTime = "0101";
        priority = 1;
        isComplete = true;
        isFloating = false;

        task = new Task("Title");
        task.setDescription(description);
        task.setStartTime(start);
        task.setEndTime(endDate, endTime);
        task.setPriority(priority);
        task.setCompletion(true);
        task.setFloating(isFloating);
        task.setGID(googleCalendarId);
    }

    @Test
    public void testToString() throws Exception {
        setup();
        String expected;
        String actual;

        expected = date + "-" + month + "-" + year + " at " + hour + ":"
                + minute;
        actual = task.displayDateTime(cal);
        assertTrue("Calendar not converted to string properly.",
                actual.equals(expected));

        // @formatter:off
        expected = TITLE + title + DELIMITER + 
                   DESCRIPTION + description + DELIMITER + 
                   START + task.displayDateTime(cal) + DELIMITER +
                   END + task.displayDateTime(cal) + DELIMITER +
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
