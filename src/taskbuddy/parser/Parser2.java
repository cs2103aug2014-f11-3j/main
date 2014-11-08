package taskbuddy.parser;

import taskbuddy.logic.AcknowledgeBundle;
import taskbuddy.logic.CommandParser;
import taskbuddy.logic.UserInputBundle;

//@Author: andrew
public class Parser2 {
	private final static String commandSync = "sync";
	private final static String commandDone = "done";
	private final static String commandRevert = "revert";
	private final static String commandEdit = "edit";
	private final static String commandPriority = "priority";
	private final static String nullValue = "padding value";
	private static CommandParser cp;

	public static AcknowledgeBundle parseOtherCommands(String userInputLine) {
		UserInputBundle inputs = new UserInputBundle();
		AcknowledgeBundle aBundle = new AcknowledgeBundle();
		String[] tokens = userInputLine.split(" ");
		try {
			aBundle = performCommand(tokens, inputs);
		} catch (Exception e) {
			aBundle.putFailure();
			aBundle.putMessage(e.getMessage());
			e.printStackTrace();
		}
		return aBundle;
	}

	protected static AcknowledgeBundle performCommand(String[] line, UserInputBundle bundle){
		AcknowledgeBundle acks = new AcknowledgeBundle();
		boolean containsSync = (searchTokens(commandSync, line) != -1);
		boolean containsDone = (searchTokens(commandDone, line) != -1);
		boolean containsRevert = (searchTokens(commandRevert, line) != -1);
		boolean containsPriority = (searchTokens(commandPriority, line) != -1);
		if (containsSync){
			acks = syncCmd(line, bundle);
		} else  if (containsDone){
			acks = doneCmd(line, bundle);
		} else if (containsRevert){
			acks = revertCmd(line, bundle);
		} else if (containsPriority){
			acks = priorityCmd(line, bundle);
		} else {
			acks.putFailure();
			acks.putMessage("wrong parser");
		}
		return acks;
	}
	
	protected static AcknowledgeBundle syncCmd(String[] line, UserInputBundle bundle) {
		int position = searchTokens(commandSync, line);
		AcknowledgeBundle acks = new AcknowledgeBundle();
		try {
			cp = new CommandParser();
			String sync = line[position];
			bundle.putCommand(sync);
			cp.parseUserInputs(bundle);
		} catch (Exception e){
			e.printStackTrace();
		}
		return acks;
	}

	protected static AcknowledgeBundle doneCmd(String[] line, UserInputBundle bundle) {
		AcknowledgeBundle acks = new AcknowledgeBundle();
		try {
			cp = new CommandParser();
			bundle.putCommand(commandEdit);
			if (line.length != 2){
				acks.putFailure();
				acks.putMessage("invalid number of args");
			} else {
				int position = searchTokens(commandDone, line);
				if (position == 1){
					bundle.putID(line[0]);
				} else if (position == 0){
					bundle.putID(line[1]);
				}
				bundle.putDescription(nullValue);
				bundle.putEndDate(nullValue);
				bundle.putEndTime(nullValue);
				bundle.putStartDate(nullValue);
				bundle.putStartTime(nullValue);
				bundle.putCompletion(true);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return acks;
	}

	protected static AcknowledgeBundle revertCmd(String[] line, UserInputBundle bundle) {
		AcknowledgeBundle acks = new AcknowledgeBundle();
		try {
			cp = new CommandParser();
			bundle.putCommand(commandEdit);
			if (line.length != 2){
				acks.putFailure();
				acks.putMessage("invalid number of args");
			} else {
				int position = searchTokens(commandRevert, line);
				if (position == 1){
					bundle.putID(line[0]);
				} else if (position == 0){
					bundle.putID(line[1]);
				}
				bundle.putDescription(nullValue);
				bundle.putEndDate(nullValue);
				bundle.putEndTime(nullValue);
				bundle.putStartDate(nullValue);
				bundle.putStartTime(nullValue);
				bundle.putCompletion(false);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return acks;
	}
	
	protected static AcknowledgeBundle priorityCmd(String[] line, UserInputBundle bundle){
		AcknowledgeBundle acks = new AcknowledgeBundle();
		try {
			cp = new CommandParser();
			bundle.putCommand(commandEdit);
			if (line.length != 3){
				acks.putFailure();
				acks.putMessage("invalid number of args");
			} else {
				bundle.putID(line[1]);
				bundle.putPriority(line[2]);
				bundle.putDescription(nullValue);
				bundle.putEndDate(nullValue);
				bundle.putEndTime(nullValue);
				bundle.putStartDate(nullValue);
				bundle.putStartTime(nullValue);
			}
		} catch (Exception e){
			e.printStackTrace();
		}
		return acks;
	}

	protected AcknowledgeBundle runCommands(UserInputBundle bundle) {
		AcknowledgeBundle a = new AcknowledgeBundle();
		try {
			cp = new CommandParser();
			a = cp.parseUserInputs(bundle);
		} catch (Exception e) {
			a.putMessage(e.getMessage());
			e.printStackTrace();
		}
		return a;
	}

	protected static int searchTokens(String searchItem, String[] list) {
		for (int i = 0; i < list.length; i++) {
			String token = list[i];
			if (searchItem.equals(token)) {
				return i;
			}
		}
		return -1;
	}
}
