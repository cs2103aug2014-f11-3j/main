package taskbuddy.database;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import taskbuddy.logic.TaskTest;

@RunWith(Suite.class)
@SuiteClasses({ TaskLoggerTest.class, DatabaseMethodsTest.class,
        DatabaseInitialisationTest.class, DatabaseObserverStubTest.class,
        GoogleCalendarCommandsTest.class, TaskTest.class })
public class AllTests {

}