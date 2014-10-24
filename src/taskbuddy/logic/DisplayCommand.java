package taskbuddy.logic;

import java.util.ArrayList;
import taskbuddy.database.Database;

public class DisplayCommand {

	private static String floating = "Floating task: ";
	private static String normal = "Task: ";
	private static String due = ", due by ";
	private static String started = ", started on ";

	public static AcknowledgeBundle displayAllTasks(Database db) {
		AcknowledgeBundle ack = new AcknowledgeBundle();
		try {
			ArrayList<Task> tasksInDB = db.getTasks();
			ArrayList<String> toDisplay = new ArrayList<String>();
			int length = tasksInDB.size();
			for (int i = 0; i < length; i++) {
				String taskInfo = "";
				Task current = tasksInDB.get(i);
				boolean isCompleted = current.getCompletionStatus();
				if (!isCompleted) {
					String title = current.getTitle();
					String ID = String.valueOf(current.getTaskId());
					boolean isFloating = current.isFloatingTask();
					if (isFloating) {
						taskInfo = "ID:" + ID + " " + floating + title;
						toDisplay.add(taskInfo);
					} else {
						String startTime = current.getStartTime().getTime().toString();
						String endTime = current.getEndTime().getTime().toString();
						taskInfo = "ID:" + ID + " " + normal + title + started + startTime + due + endTime;
						toDisplay.add(taskInfo);
					}
				} 
			}
			ack.putSuccess();
			ack.putMessage("retrieval successful");
			ack.putList(toDisplay);
		} catch (Exception e) {
			ack.putFailure();
			ack.putMessage("failed to generate tasks in DB");
		}
		return ack;
	}
}
