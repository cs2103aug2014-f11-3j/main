package taskbuddy.database;

import static org.junit.Assert.*;

import org.junit.Test;

import taskbuddy.database.DbCommand;
import taskbuddy.logic.Task;

public class DbCommandTest {
    DbCommand dbCommand;
    Task task;

    public void setup() {
        task = new Task();
        dbCommand = new DbCommand(task);
    }

    @Test
    public void testDbCommand() throws Exception {
        setup();
        assertTrue("dbCommand not initialised properly", dbCommand.getTask()
                .equals(task));
    }

}
