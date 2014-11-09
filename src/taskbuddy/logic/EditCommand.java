package taskbuddy.logic;

import java.net.UnknownHostException;
import java.util.Calendar;

import taskbuddy.database.Database;

public class EditCommand {

	private static String nullValue = "padding value";

	public static AcknowledgeBundle editTask(UserInputBundle extras, Database db) {
		AcknowledgeBundle ack = new AcknowledgeBundle();
		Task toEdit = new Task();
		int taskID = Integer.parseInt(extras.getTaskID());
		try {
			toEdit = db.read(taskID);
			CommandParser cp = CommandParser.getInstance();
			Task dup = duplicateTask(toEdit);
			cp.pushUndoTask(dup);
			System.err.println(dup.getTitle() + "++ pushing");
			String title = extras.getTitle();
			if (!title.equals(nullValue)) {
				toEdit.setTitle(title);
			}

			String desc = extras.getDescription();
			if (!desc.equals(nullValue)) {
				toEdit.setDescription(desc);
			}

			String startDate = extras.getStartDate();
			String startTime = extras.getStartTime();
			if (!startDate.equals(nullValue) && !startTime.equals(nullValue)) {
				toEdit.setStartTime(startDate, startTime);
			} else if (!startDate.equals(nullValue)) {
				Calendar cal = Calendar.getInstance();
				String[] ddmmyyyy = startDate.split("/");
				int d = Integer.parseInt(ddmmyyyy[0]);
				int m = Integer.parseInt(ddmmyyyy[1]);
				int y = Integer.parseInt(ddmmyyyy[2]);
				cal.set(y, m, d);
				toEdit.setStartTime(cal);
			} else if (!startTime.equals(nullValue)) {
				Calendar cal = Calendar.getInstance();
				int h = Integer.parseInt(startTime.substring(0, 2));
				int m = Integer.parseInt(startTime.substring(2));
				cal.set(Calendar.HOUR_OF_DAY, h);
				cal.set(Calendar.MINUTE, m);
				toEdit.setStartTime(cal);
			}

			String endDate = extras.getEndDate();
			String endTime = extras.getEndTime();
			if (!endDate.equals(nullValue) && !endTime.equals(nullValue)) {
				toEdit.setEndTime(endDate, endTime);
			} else if (!endDate.equals(nullValue)) {
				Calendar cal = Calendar.getInstance();
				String[] ddmmyyyy = endDate.split("/");
				int d = Integer.parseInt(ddmmyyyy[0]);
				int m = Integer.parseInt(ddmmyyyy[1]);
				int y = Integer.parseInt(ddmmyyyy[2]);
				cal.set(y, m, d);
				toEdit.setEndTime(cal);
			} else if (!endTime.equals(nullValue)) {
				Calendar cal = Calendar.getInstance();
				int h = Integer.parseInt(endTime.substring(0, 2));
				int m = Integer.parseInt(endTime.substring(2));
				cal.set(Calendar.HOUR_OF_DAY, h);
				cal.set(Calendar.MINUTE, m);
				toEdit.setEndTime(cal);
			}
			try {
				Boolean completion = extras.getCompletionStatus();
				toEdit.setCompletion(completion);
			} catch (Exception e){
				;
			}
			try {
				Integer priority = Integer.parseInt(extras.getPriority());
				toEdit.setPriority(priority);
			} catch (Exception e){
				;
			}
			try {
				db.edit(toEdit);
				ack.putSuccess();
				ack.putMessage("task edited");
				ack.putTask(toEdit);
				cp.pushUndo(extras);
				cp.pushUndoTask(toEdit);
			} catch (UnknownHostException e){
				ack.putSuccess();
				ack.putMessage("task edited locally");
				ack.putTask(toEdit);
				cp.pushUndo(extras);
				cp.pushUndoTask(toEdit);
			}
		} catch (Exception e) {
			ack.putFailure();
			ack.putMessage("Unable to find task in DB");
		}
		return ack;
	}
	
	protected static Task duplicateTask(Task toDup){
		Task t = new Task();
		t.setTaskId(toDup.getTaskId());
		t.setCompletion(toDup.getCompletionStatus());
		t.setDescription(toDup.getDescription());
		t.setEndTime(toDup.getEndTime());
		t.setGID(toDup.getGID());
		t.setTitle(toDup.getTitle());
		t.setStartTime(toDup.getStartTime());
		t.setPriority(toDup.getPriority());
		return t;
	}
}
