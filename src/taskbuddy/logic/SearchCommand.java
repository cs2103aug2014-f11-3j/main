package taskbuddy.logic;
import java.util.ArrayList;

import taskbuddy.database.Database;

public class SearchCommand {
	
	private static String nullValue = "padding value";
	
	static public AcknowledgeBundle searchForTasks(UserInputBundle extras, Database db){
		AcknowledgeBundle ack = new AcknowledgeBundle();
		ArrayList<Task> listTask = new ArrayList<Task>();
		ArrayList<String> displayToUser = new ArrayList<String>();
		String title = extras.getTitle();
		String desc = extras.getDescription();
		
		try {
			if (!title.equals(nullValue)){
				listTask = db.search(title);
				displayToUser = concatTaskList(listTask);
				ack.putSuccess();
				ack.putMessage("Search success");
				ack.putList(displayToUser);
			} else if (!title.equals(nullValue)){
				listTask = db.search(desc);
				displayToUser = concatTaskList(listTask);
				ack.putSuccess();
				ack.putMessage("Search success");
				ack.putList(displayToUser);
			} else {
				ack.putFailure();
				ack.putMessage("No search term");
			}
		} catch (Exception e){
			ack.putFailure();
			ack.putMessage("Read error, read from null");
		}
		return ack;
	}
	
	static ArrayList<String> concatTaskList(ArrayList<Task> listT) {
		ArrayList<String> toReturn = new ArrayList<String>();
		int listLength = listT.size();
		if (listLength == 0) {
			toReturn.add("No results found");
		} else {
			for (int i = 0; i < listLength; i++) {
				Task current = listT.get(i);
				String title = current.getTitle();
				int ID = current.getTaskId();
				String toAdd = "Task " + title + " with ID: " + ID;
				toReturn.add(toAdd);
			}
		}
		return toReturn;
	}
}
