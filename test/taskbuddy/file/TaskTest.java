package taskbuddy.file;

import static org.junit.Assert.*;

import org.junit.Test;

public class TaskTest {

    private String description;
    private Task testTask;

    public void setup() {
        description = "a description";
        testTask = new Task(description);
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
}
