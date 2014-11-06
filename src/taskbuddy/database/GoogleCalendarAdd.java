package taskbuddy.database;

import java.net.UnknownHostException;

import taskbuddy.logic.Task;

public class GoogleCalendarAdd extends GoogleCalendarCommand {
    // @formatter:off
    private static final String ERR_NOT_SYNCED_GOOGLE_CALENDAR = 
            "Changes made to database and task log but not Google Calendar. ";
    // @formatter:on

    public GoogleCalendarAdd(Task task) {
        super(task);
    }

    @Override
    public void execute() throws UnknownHostException {
        try {
            googleCal.add(task);
        } catch (UnknownHostException e) {
            throw new UnknownHostException(ERR_NOT_SYNCED_GOOGLE_CALENDAR
                    + e.getMessage());
            // TODO Add add command to command queue
        }

    }

}
