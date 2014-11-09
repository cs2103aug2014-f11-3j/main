package taskbuddy.logic;

import java.util.Calendar;

import taskbuddy.database.Database;

public class AddCommand {

	private static String nullValue = "padding value";

	public static AcknowledgeBundle addTask(UserInputBundle extras, Database db) {
		AcknowledgeBundle ackBundle = new AcknowledgeBundle();
		Task taskToAdd = new Task();
		String title = extras.getTitle();
		String desc = extras.getDescription();
		String endDate = extras.getEndDate();
		String endtime = extras.getEndTime();
		String startDate = extras.getStartDate();
		String startTime = extras.getStartTime();
		taskToAdd.setTitle(title);
		if (!desc.equals(nullValue)) {
			taskToAdd.setDescription(desc);
		} else {
			taskToAdd.setDescription("no description available");
		}
		addTimeToTask(taskToAdd, startTime, startDate, endtime, endDate);
		try {
			db.addTask(taskToAdd);
			ackBundle.putSuccess();
			ackBundle.putMessage("added task to database with no errors");
			ackBundle.putTask(taskToAdd);
			CommandParser cp = CommandParser.getInstance();
			cp.initRedo();
			cp.pushUndo(extras);
			cp.pushUndoTask(taskToAdd);
		} catch (Exception e) {
			e.printStackTrace();
			ackBundle.putFailure();
			ackBundle.putMessage("failed to add task to online database");
			ackBundle.putTask(taskToAdd);
		}
		return ackBundle;
	}

	private static void addTimeToTask(Task taskToMod, String timeStart,
			String dateStart, String timeEnd, String dateEnd) {

		if (dateStart.equals(nullValue) && dateEnd.equals(nullValue)) {
			if (!timeStart.equals(nullValue) && !timeEnd.equals(nullValue)) {
				Calendar calStart = Calendar.getInstance();
				Calendar calEnd = Calendar.getInstance();
				int startH = Integer.parseInt(timeStart.substring(0, 2));
				int startM = Integer.parseInt(timeStart.substring(2));
				int endH = Integer.parseInt(timeEnd.substring(0, 2));
				int endM = Integer.parseInt(timeEnd.substring(2));
				calStart.set(Calendar.HOUR_OF_DAY, startH);
				calStart.set(Calendar.MINUTE, startM);
				calEnd.set(Calendar.HOUR_OF_DAY, endH);
				calEnd.set(Calendar.MINUTE, endM);
				taskToMod.setEndTime(calEnd);
				taskToMod.setStartTime(calStart);
			} else if (!timeEnd.equals(nullValue)) {
				taskToMod.setFloating(false);
				Calendar cal = Calendar.getInstance();
				int hh = Integer.parseInt(timeEnd.substring(0, 2));
				int mm = Integer.parseInt(timeEnd.substring(2));
				cal.set(Calendar.HOUR_OF_DAY, hh);
				cal.set(Calendar.MINUTE, mm);
				taskToMod.setEndTime(cal);
				taskToMod.setStartTime(cal);
			} else if (!timeStart.equals(nullValue)) {
				taskToMod.setFloating(false);
				Calendar cal = Calendar.getInstance();
				int hh = Integer.parseInt(timeStart.substring(0, 2));
				int mm = Integer.parseInt(timeStart.substring(2));
				cal.set(Calendar.HOUR_OF_DAY, hh);
				cal.set(Calendar.MINUTE, mm);
				taskToMod.setEndTime(cal);
				taskToMod.setStartTime(cal);
			} else {
				taskToMod.setFloating(true);
				Calendar cal = Calendar.getInstance();
				taskToMod.setEndTime(cal);
				taskToMod.setStartTime(cal);
			}
		} else {
			// not floating
			taskToMod.setFloating(false);
			if (!dateStart.equals(nullValue) && !dateEnd.equals(nullValue)) {
				// task within 2 dates
				String startT = "0000";
				String endT = "2359";
				if (timeStart.equals(nullValue)) {
					taskToMod.setStartTime(dateStart, startT);
				} else {
					taskToMod.setStartTime(dateStart, timeStart);
				}
				if (timeEnd.equals(nullValue)) {
					taskToMod.setEndTime(dateEnd, endT);
				} else {
					taskToMod.setEndTime(dateEnd, timeEnd);
				}
			}
		}
	}
}
