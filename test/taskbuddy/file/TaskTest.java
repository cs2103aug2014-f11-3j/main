package taskbuddy.file;

import static org.junit.Assert.*;

import org.junit.Test;

public class TaskTest {

    Task testTask;

    String description;
    Date date;
    Time time;

    public void setup() {
        description = "a description";
        testTask = new Task(description);
        date = new Date(31, 12, 2014);
        time = new Time(23, 59);
    }

    @Test
    public void testTask() {
        setup();
        assertEquals(description, testTask.getDescription());
    }

    @Test
    public void testSetDescription() {
        setup();
        String description = "another description";
        testTask.setDescription(description);
        assertEquals(description, testTask.getDescription());
    }

    @Test
    public void testSetDateAndTime() throws Exception {
        setup();
        
        testTask.setDate(date);
        assertTrue("date field of task is not set properly", testTask.getDate()
                .equals(date));
        
        testTask.setTime(time);
        assertTrue("time field of task is not set properly", testTask.getTime()
                .equals(time));
    }
}
