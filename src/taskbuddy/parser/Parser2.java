package taskbuddy.parser;

import java.io.IOException;
import java.text.ParseException;

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
	private final static String commandUndo = "undo";
	private final static String commandRedo = "redo";
	private final static String nullValue = "padding value";
	private static CommandParser cp;

	public static AcknowledgeBundle parseOtherCommands(String userInputLine)
			throws ParseException, IOException {
		UserInputBundle inputs = new UserInputBundle();
		AcknowledgeBundle aBundle = new AcknowledgeBundle();
		cp = CommandParser.getInstance();
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

	protected static AcknowledgeBundle performCommand(String[] line,
			UserInputBundle bundle) {
		AcknowledgeBundle acks = new AcknowledgeBundle();
		boolean containsSync = (searchTokens(commandSync, line) != -1);
		boolean containsDone = (searchTokens(commandDone, line) != -1);
		boolean containsRevert = (searchTokens(commandRevert, line) != -1);
		boolean containsPriority = (searchTokens(commandPriority, line) != -1);
		boolean containsUndo = (searchTokens(commandUndo, line) != -1);
		if (containsSync) {
			acks = syncCmd(line, bundle);
		} else if (containsDone) {
			acks = doneCmd(line, bundle);
		} else if (containsRevert) {
			acks = revertCmd(line, bundle);
		} else if (containsPriority) {
			acks = priorityCmd(line, bundle);
		} else if (containsUndo){
			System.err.println("err1");
			acks = undoCmd(line, bundle);
		} else {
			acks.putFailure();
			acks.putMessage("wrong parser");
		}
		return acks;
	}

	protected static AcknowledgeBundle syncCmd(String[] line,
			UserInputBundle bundle) {
		AcknowledgeBundle acks = new AcknowledgeBundle();
		try {
			String sync = line[0];
			bundle.putCommand(sync);
			acks = cp.parseUserInputs(bundle);
		} catch (Exception e) {
			acks.putFailure();
			acks.putMessage(e.getMessage());
		}
		return acks;
	}

	protected static AcknowledgeBundle doneCmd(String[] line,
			UserInputBundle bundle) {
		AcknowledgeBundle acks = new AcknowledgeBundle();
		try {
			bundle.putCommand(commandEdit);
			if (line.length != 2) {
				acks.putFailure();
				acks.putMessage("invalid number of args");
			} else {
				bundle.putID(line[1]);
				bundle.putDescription(nullValue);
				bundle.putEndDate(nullValue);
				bundle.putEndTime(nullValue);
				bundle.putStartDate(nullValue);
				bundle.putStartTime(nullValue);
				bundle.putCompletion(true);
				acks = cp.parseUserInputs(bundle);
			}
		} catch (Exception e) {
			acks.putFailure();
			acks.putMessage(e.getMessage());
		}
		return acks;
	}

	protected static AcknowledgeBundle revertCmd(String[] line,
			UserInputBundle bundle) {
		AcknowledgeBundle acks = new AcknowledgeBundle();
		try {
			bundle.putCommand(commandEdit);
			if (line.length != 2) {
				acks.putFailure();
				acks.putMessage("invalid number of args");
			} else {
				bundle.putID(line[1]);
				bundle.putDescription(nullValue);
				bundle.putEndDate(nullValue);
				bundle.putEndTime(nullValue);
				bundle.putStartDate(nullValue);
				bundle.putStartTime(nullValue);
				bundle.putCompletion(false);
				acks = cp.parseUserInputs(bundle);
			}
		} catch (Exception e) {
			acks.putFailure();
			acks.putMessage(e.getMessage());
		}
		return acks;
	}

	protected static AcknowledgeBundle priorityCmd(String[] line,
			UserInputBundle bundle) {
		AcknowledgeBundle acks = new AcknowledgeBundle();
		try {
			bundle.putCommand(commandEdit);
			if (line.length != 3) {
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
				acks = cp.parseUserInputs(bundle);
			}
		} catch (Exception e) {
			acks.putFailure();
			acks.putMessage(e.getMessage());
		}
		return acks;
	}

	protected static AcknowledgeBundle undoCmd(String[] line,
			UserInputBundle bundle) {
		AcknowledgeBundle acks = new AcknowledgeBundle();
		try {
			bundle.putCommand(commandUndo);
			bundle.putID(nullValue);
			bundle.putPriority(nullValue);
			bundle.putDescription(nullValue);
			bundle.putEndDate(nullValue);
			bundle.putEndTime(nullValue);
			bundle.putStartDate(nullValue);
			bundle.putStartTime(nullValue);
			acks = cp.parseUserInputs(bundle);
		} catch (Exception e) {
			acks.putFailure();
			acks.putMessage(e.getMessage());
		}
		return acks;
	}

	protected static AcknowledgeBundle redoCmd(String[] line, UserInputBundle bundle){
		AcknowledgeBundle acks = new AcknowledgeBundle();
		try {
			bundle.putCommand(commandRedo);
			bundle.putID(nullValue);
			bundle.putPriority(nullValue);
			bundle.putDescription(nullValue);
			bundle.putEndDate(nullValue);
			bundle.putEndTime(nullValue);
			bundle.putStartDate(nullValue);
			bundle.putStartTime(nullValue);
			acks = cp.parseUserInputs(bundle);
		} catch (Exception e) {
			acks.putFailure();
			acks.putMessage(e.getMessage());
		}
		return acks;
	}
	
	protected static int searchTokens(String searchItem, String[] list) {
		String token = list[0];
		if (searchItem.equals(token)) {
			return 0;
		}
		return -1;
	}
}
