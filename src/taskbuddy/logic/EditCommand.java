package taskbuddy.logic;

import taskbuddy.database.Database;

public class EditCommand {
	
	private static String nullValue = "padding value";
	
	public static AcknowledgeBundle editTask(UserInputBundle extras, Database db){
		AcknowledgeBundle ack = new AcknowledgeBundle();
		Task toEdit = new Task();
		int taskID =  Integer.parseInt(extras.getTaskID());
		try{
			toEdit = db.findMatchingTask(taskID);
			
			String title = extras.getTitle();
			if (!title.equals(nullValue)){
				toEdit.setTitle(title);
			}
			
			String desc = extras.getDescription();
			if (!desc.equals(nullValue)){
				toEdit.setDescription(desc);
			}
			
			String startDate = extras.getStartDate();
			if (!startDate.equals(nullValue)){
				String startTime = extras.getStartTime();
				if (!startTime.equals(nullValue)){
					toEdit.setStartTime(startDate, startTime);
				} else {
					startTime = "0000";
					toEdit.setStartTime(startDate, startTime);
				}
			}
			
			String endDate = extras.getEndDate();
			if (!endDate.equals(nullValue)){
				String endTime = extras.getEndTime();
				if (!endTime.equals(nullValue)){
					toEdit.setStartTime(endDate, endTime);
				} else {
					endTime = "2359";
					toEdit.setStartTime(endDate, endTime);
				}
			}
			//TODO STUB : edit method in db
			ack.putSuccess();
			ack.putMessage("task edited");
		} catch (Exception e){
			ack.putFailure();
			ack.putMessage("Unable to find task in DB");
		}
		return ack;
	}
}
