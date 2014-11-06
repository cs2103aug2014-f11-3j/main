package taskbuddy.database;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TaskLoggerTest.class, DatabaseMethodsTest.class, GoogleCalendarSyncFailTest.class,
        DatabaseObserverStubTest.class })
public class AllTests {

}