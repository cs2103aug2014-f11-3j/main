package taskbuddy.logic;

import static org.junit.Assert.*;

import org.junit.Test;

public class BundleTest {

    Bundle bundle;
    String status;
    String message;

    // Constructs bundle
    public void setup() {
        bundle = new Bundle();
    }

    @Test
    public void testSuccessful() throws Exception {
        setup();

        // Say we have added a task successfully and want to report this
        // success message using bundle
        status = "Successful";
        message = "Task added successfully.";

        bundle.putString(status, message);
        assertTrue("Status key not extracted properly from bundle",
                bundle.bundle.containsKey(status));
    }

    @Test
    public void testFailure() throws Exception {
        setup();

        // Say we have added a task unsuccessfully and want to report this
        // failure message using bundle
        status = "Fail";
        message = "Task not added due to invalid task title";

        bundle.putString(status, message);
        assertFalse(bundle.bundle.containsKey("Successful"));
        assertTrue("Fail status key not extracted from bundle",
                bundle.bundle.containsKey(status));
        assertTrue("Fail status message not extracted from bundle.", bundle
                .getItem(status).equals(message));
    }
}
