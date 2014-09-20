package taskbuddy.file;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ DatabaseTest.class, GoogleCalendarManagerTest.class,
        LogicTest.class, UserInterfaceTest.class })
public class AllTests {

}
