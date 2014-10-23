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
		System.out.println(startDate);
		System.out.println(taskToAdd.getStartTime().toString());
		System.out.println(taskToAdd.displayStart());
		System.out.println("before try");
		try {
			System.out.println("before add to db");
			db.addTask(taskToAdd);
			System.out.println("after add to db");
			ackBundle.putSuccess();
			ackBundle.putMessage("added task to database with no errors");
			ackBundle.putTask(taskToAdd);
		} catch (Exception e) {
			System.out.println("catch");
			e.printStackTrace();
			ackBundle.putFailure();
			ackBundle.putMessage("failed to add task to database");
			ackBundle.putTask(taskToAdd);
		}
		return ackBundle;
	}

	private static void addTimeToTask(Task taskToMod, String timeStart,
			String dateStart, String timeEnd, String dateEnd) {

		if (dateStart.equals(nullValue) && dateEnd.equals(nullValue)) {
			if (timeStart.equals(nullValue) && timeEnd.equals(nullValue)) {
				// floating task
				taskToMod.setFloating(true);
				Calendar cal = Calendar.getInstance();
				System.out.println(cal.toString());
				taskToMod.setEndTime(cal);
				taskToMod.setStartTime(cal);
			} else {
				//deadline task
				taskToMod.setFloating(false);
				Calendar cal = Calendar.getInstance();
				String year = String.valueOf(cal.YEAR);
				String month = String.valueOf(cal.MONTH);
				String date = String.valueOf(cal.DATE);
				String ddmmyy = date + "/" + month + "/" + year;
				taskToMod.setEndTime(ddmmyy, timeEnd);
				taskToMod.setStartTime(ddmmyy, timeEnd);
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
