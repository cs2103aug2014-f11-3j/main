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
		} catch (Exception e){
			ackBundle.putFailure();
			ackBundle.putMessage("failed to add task to database");
			ackBundle.putTask(taskToAdd);
		}
		return ackBundle;
	}

	private static void addTimeToTask(Task taskToMod, String timeStart,
			String dateStart, String timeEnd, String dateEnd) {
		if (timeStart.equals(timeEnd) && dateStart.equals(dateEnd)) {
			taskToMod.setFloating(false);
			//deadline task
			if (!timeStart.equals(nullValue)) {
				taskToMod.setStartTime(dateStart, timeStart);
				taskToMod.setEndTime(dateEnd, timeEnd);
			} else {
				String time1 = "0000";
				String time2 = "2359";
				taskToMod.setStartTime(dateStart, time1);
				taskToMod.setEndTime(dateEnd, time2);
			}
		} else if (dateStart.equals(nullValue) && dateEnd.equals(nullValue)){
			//floating task
			taskToMod.setFloating(true);
			Calendar cal = Calendar.getInstance();
			taskToMod.setEndTime(cal);
			taskToMod.setStartTime(cal);
		} else if (!dateStart.equals(nullValue) && !dateEnd.equals(nullValue)){
			taskToMod.setFloating(false);
			if (timeStart.equals(nullValue)){
				String time1 = "0000";
				taskToMod.setStartTime(dateStart, time1);
			} else {
				taskToMod.setStartTime(dateStart, timeStart);
			}
			
			if (timeEnd.equals(nullValue)){
				String time2 = "2359";
				taskToMod.setEndTime(dateEnd, time2);
			} else {
				taskToMod.setEndTime(dateEnd, timeEnd);
			}
		}
	}
}
