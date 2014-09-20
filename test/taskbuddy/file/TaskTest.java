package taskbuddy.file;

import static org.junit.Assert.*;

import org.junit.Test;

public class TaskTest {

    private String testDescription;
    private Task testTask;

    public void setup() {
        testDescription = "test description";
        testTask = new Task(testDescription);
    }

    @Test
    public void testTask() {
        setup();
        assertEquals(testDescription, testTask.getDescription());
    }

    @Test
    public void testSetDescription() {
        setup();
        String anotherTestDescription = "another test description";
        testTask.setDescription(anotherTestDescription);
        assertEquals(anotherTestDescription, testTask.getDescription());
    }
}
