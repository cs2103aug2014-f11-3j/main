package taskbuddy.database;

import java.net.UnknownHostException;

import taskbuddy.logic.Task;

public class GoogleCalendarAdd extends GoogleCalendarCommand {
    

    public GoogleCalendarAdd(Task task) {
        super(task);
    }

    @Override
    public void execute() throws UnknownHostException {
        try {
            googleCal.add(task);
        } catch (UnknownHostException e) {
            throw new UnknownHostException(ERR_NOT_SYNCED_GOOGLE_CALENDAR);
            // TODO Then don't remove this command from command queue.
        }

    }

}
