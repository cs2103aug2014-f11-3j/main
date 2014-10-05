package taskbuddy.logic;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;

public class TaskTest {

    int year;
    int month;
    int date;
    int hour;
    int minute;
    int second;
    Calendar cal;
    Task task;

    public void setup() {
        year = 1;
        month = 1;
        date = 1;
        hour = 1;
        minute = 1;
        second = 1;

        // Month argument is an int from 0-11, not 1-12
        cal = new GregorianCalendar(year, month - 1, date, hour, minute, second);
        task = new Task();
    }

    @Test
    public void testToString() throws Exception {
        setup();
        
        String expected = date + "-" + month + "-" + year + " at " + hour + ":"
                + minute;
        String actual = task.toString(cal);
        assertTrue("Calendar not converted to string properly.",
                actual.equals(expected));
    }
}
