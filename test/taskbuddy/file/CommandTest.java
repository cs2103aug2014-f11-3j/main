package taskbuddy.file;

import static org.junit.Assert.*;

import org.junit.Test;

public class CommandTest {
    Command command;
    CommandType commandType;
    Task task;

    public void setup() {
        commandType = CommandType.ADD;
        command = new Command(commandType);
        task = new Task("this is a task");
    }

    @Test
    public void testCommand() throws Exception {
        setup();
        assertTrue(
                "commandType is not initialised properly, i.e. commandType is not ADD",
                command.getCommandType().equals(commandType));
    }

    @Test
    public void testSetTask() throws Exception {
        setup();
        command.setTask(task);
        assertTrue("task is not set or retrieved properly", command.getTask()
                .equals(task));
    }
}
