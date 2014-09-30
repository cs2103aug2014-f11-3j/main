package taskbuddy.database;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.LinkedList;

import org.junit.Test;

import taskbuddy.database.Database;

public class DatabaseTest {

    Database database;

    public void setup() {
        database = new Database();
    }

    @Test
    public void testDatabase() throws Exception {
        setup();
        assertTrue("database not constructed with arraylist of Task objects",
                database.tasks instanceof ArrayList);
        assertTrue(
                "database not constructed with linkedlist of DbCommand objects",
                database.commands instanceof LinkedList);
    }
}
