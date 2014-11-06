package taskbuddy.database;

import static org.junit.Assert.*;

import java.net.UnknownHostException;
import java.util.Calendar;

import org.junit.Before;
import org.junit.Test;

import taskbuddy.logic.Task;

public class GoogleCalendarCommandsTest {
    Task firstTask;
    GoogleCalendarManagerStub googleCalendarManagerStub;
    OfflineGoogleCalendarManagerStub offlineGoogleCalendarManagerStub;

    public Task createTask(String title, String description) {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        int priority = 1;
        boolean isComplete = true;
        boolean isFloating = false;

        Task task = new Task(title);
        task.setDescription(description);
        task.setStartTime(start);
        task.setEndTime(end);
        task.setPriority(priority);
        task.setCompletion(isComplete);
        task.setFloating(isFloating);

        return task;
    }

    @Before
    public void setup() {
        firstTask = createTask("First", "First description.");

        googleCalendarManagerStub = new GoogleCalendarManagerStub();
        offlineGoogleCalendarManagerStub = new OfflineGoogleCalendarManagerStub();
    }

    @Test
    public void testAddCommand() throws Exception {
        GoogleCalendarAdd addCommand = new GoogleCalendarAdd(firstTask);

        GoogleCalendarCommand.googleCal = googleCalendarManagerStub;
        try {
            addCommand.execute();
        } catch (Exception e) {
            fail("Add command was not executed normally.");
        }
        assertEquals("Google Calendar ID not added to task.",
                googleCalendarManagerStub.googleCalendarId, firstTask.getGID());

        GoogleCalendarCommand.googleCal = offlineGoogleCalendarManagerStub;
        try {
            addCommand.execute();
            fail("No exception thrown when sync to Google Calendar "
                    + "fails due to user being offline.");
        } catch (UnknownHostException e) {
            assertEquals(
                    "Wrong error message when unable to execute add command.",
                    GoogleCalendarCommand.ERR_NOT_SYNCED_GOOGLE_CALENDAR,
                    e.getMessage());
        }
    }
    
    @Test
    public void testDeleteCommand() throws Exception {
        GoogleCalendarDelete deleteCommand = new GoogleCalendarDelete(firstTask);
        
        GoogleCalendarCommand.googleCal = googleCalendarManagerStub;
        try {
            deleteCommand.execute();
        } catch (Exception e) {
            fail("Delete command was not executed normally.");
        }
        
        GoogleCalendarCommand.googleCal = offlineGoogleCalendarManagerStub;
        try {
            deleteCommand.execute();
            fail("No exception thrown when sync to Google Calendar "
                    + "fails due to user being offline.");
        } catch (UnknownHostException e) {
            assertEquals(
                    "Wrong error message when unable to execute add command.",
                    GoogleCalendarAdd.ERR_NOT_SYNCED_GOOGLE_CALENDAR,
                    e.getMessage());
        }
    }
    
    @Test
    public void testUpdateCommand() throws Exception {
        GoogleCalendarUpdate updateCommand = new GoogleCalendarUpdate(firstTask);
        
        GoogleCalendarCommand.googleCal = googleCalendarManagerStub;
        try {
            updateCommand.execute();
        } catch (Exception e) {
            fail("Update command was not executed normally.");
        }
        
        GoogleCalendarCommand.googleCal = offlineGoogleCalendarManagerStub;
        try {
            updateCommand.execute();
            fail("No exception thrown when sync to Google Calendar "
                    + "fails due to user being offline.");
        } catch (UnknownHostException e) {
            assertEquals(
                    "Wrong error message when unable to execute add command.",
                    GoogleCalendarAdd.ERR_NOT_SYNCED_GOOGLE_CALENDAR,
                    e.getMessage());
        }
    }

}
