package taskbuddy.database;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ TaskLogger.class, DatabaseTest.class, DbCommandTest.class })
public class AllTests {

}
