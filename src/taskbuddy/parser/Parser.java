package taskbuddy.parser;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import taskbuddy.logic.AcknowledgeBundle;
import taskbuddy.logic.CommandParser;
import taskbuddy.logic.UserInputBundle;

/*
 *  User Manual:
 *  1.Command type: add/edit/display/delete/undo/redo
 *   	
 *  2.Time must be this format: 9am, 9:30am, 10pm, 11:15pm
 *    User can enter "today", "tomorrow", "this Saturday", "next Wednesday"
 * 	
 *  3.Date must be this format 19/10/2014 
 *  
 */

/*
 * Add command: (use task index/task id to identify?)
 * 
 * add <title> at <starttime/endtime>      #<description>
 * add <title> from <starttime> to <endtime>
 * add <title> at <starttime/endtime> on <startdate/enddate>
 * add <title> from <starttime> to <endtime> on <startdate/enddate>
 * add <title> on <startdate/enddate>
 * add <title> from <startdate> to <enddate>
 * add <title> from <startdate><starttime> to <enddate><endtime>
 * add <title> 
 * add <title> today/tomorrow/this Saturday/next Wednesday
 * 
 * FORMAT FOR TIME: 8am, 0830am
 * 
 * for deadline task:
 * add <title> by <starttime/endtime>
 * add <title> by <startdate/enddate> 
 * add <title> by <startdate/enddate> <starttime/endtime>
 * 
 * 
 * add <title> at <time> <every Tuesday> 
 * floating task: no start time, no end time
 */

public class Parser {

	private static UserInputBundle userInputs = new UserInputBundle();
	// private static Scanner scanner = new Scanner(System.in);
	private static final String NULL_VALUE = "padding value";
	private static String userCommand = "";

	// public static void main(String[] args) throws ParseException, IOException
	// {

	public static AcknowledgeBundle userInput(String command)
			throws ParseException, IOException {
		CommandParser commandParser = CommandParser.getInstance();
		// boolean continueLoop = true;
		// while (continueLoop) {
		System.out.print("command:");
		// String userCommand = scanner.nextLine();
		userCommand = command;

		String commandType = getFirstWord(userCommand);

		//DEPRECATED, USER PARSER2
		if (isUndoType(commandType)) {
			undoDataPadding(userInputs, commandType);
			AcknowledgeBundle a = commandParser.parseUserInputs(userInputs);
			return a;

		} else if (isRedoType(commandType)) {
			redoDataPadding(userInputs, commandType);
			AcknowledgeBundle a = commandParser.parseUserInputs(userInputs);
			return a;
			
		} else if (isAddType(commandType)) {
			addDataPadding(userInputs, commandType, userCommand);
			AcknowledgeBundle a = commandParser.parseUserInputs(userInputs);
			return a;

		} else if (isDeleteType(commandType)) {
			deleteDataPadding(userInputs, commandType, userCommand);
			AcknowledgeBundle a = commandParser.parseUserInputs(userInputs);
			return a;

		} else if (isDisplayType(commandType)) {
			displayDataPadding(userInputs, commandType);
			AcknowledgeBundle a = commandParser.parseUserInputs(userInputs);
			return a;

		} else if (isEditType(commandType)) {
			editDataPadding(userInputs, commandType, userCommand);
			AcknowledgeBundle a = commandParser.parseUserInputs(userInputs);
			return a;

		} else if (isSearchType(commandType)) {
			searchDatePadding(userInputs, commandType, userCommand);
			AcknowledgeBundle a = commandParser.parseUserInputs(userInputs);
			return a;
		} else {
			AcknowledgeBundle a = new AcknowledgeBundle();
			a.putFailure();
			a.putMessage("parse error, invalid command");
			return a;
		}
	}

	public static UserInputBundle getUserInput() {
		return userInputs;
	}

	private static String getFirstWord(String userCommand) {
		String commandTypeString = userCommand.trim().split("\\s+")[0];
		return commandTypeString;
	}

	private static String removeFirstWord(String userCommand) {
		String[] splitCommand = userCommand.trim().split("\\s+");
		ArrayList<String> splitCommandCopy = new ArrayList<String>();
		for (int i = 0; i < splitCommand.length; i++) {
			splitCommandCopy.add(splitCommand[i]);
		}
		splitCommandCopy.remove(0);
		String result = "";
		for (int j = 0; j < splitCommandCopy.size(); j++) {
			result += splitCommandCopy.get(j);
			result += " ";
		}
		result = result.trim();
		return result;
	}


	private static boolean isUndoType(String commandType) {
		if (commandType.equalsIgnoreCase("undo")) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isRedoType(String commandType) {
		if (commandType.equalsIgnoreCase("redo")) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isAddType(String commandType) {
		if (commandType.equalsIgnoreCase("add")) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isDeleteType(String commandType) {
		if (commandType.equalsIgnoreCase("delete")) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isDisplayType(String commandType) {
		if (commandType.equalsIgnoreCase("display")) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isEditType(String commandType) {
		if (commandType.equalsIgnoreCase("edit")) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isSearchType(String commandType) {
		if (commandType.equalsIgnoreCase("search")) {
			return true;
		} else {
			return false;
		}
	}

	private static void undoDataPadding(UserInputBundle b, String commandType) {
		b.putCommand(commandType);
		b.putTitle(NULL_VALUE);
		b.putDescription(NULL_VALUE);
		b.putStartTime(NULL_VALUE);
		b.putEndTime(NULL_VALUE);
		b.putStartDate(NULL_VALUE);
		b.putEndDate(NULL_VALUE);
	}

	private static void redoDataPadding(UserInputBundle b, String commandType) {
		b.putCommand(commandType);
		b.putTitle(NULL_VALUE);
		b.putDescription(NULL_VALUE);
		b.putStartTime(NULL_VALUE);
		b.putEndTime(NULL_VALUE);
		b.putStartDate(NULL_VALUE);
		b.putEndDate(NULL_VALUE);
	}

	private static void addDataPadding(UserInputBundle b, String commandType,
			String userCommand) {
		String contentToAdd = removeFirstWord(userCommand);

		if(hasTitleSpecifier(contentToAdd)){
			
			String titleWithSpecifier = findTitleWithSpecifier(contentToAdd);
			contentToAdd = removeTitleWithSpecifier(contentToAdd);
			String startDateWithSpecifier = "";
			String endDateWithSpecifier = "";
			String startTimeWithSpecifier = "";
			String endTimeWithSpecifier = "";
			String descriptionWithSpecifier = "";
			
			if(hasStartDateSpecifier(contentToAdd)){
				startDateWithSpecifier = findStartDateWithSpecifier(contentToAdd);
				contentToAdd = removeStartDate(contentToAdd);		
			}else{
				startDateWithSpecifier = NULL_VALUE;
			}
			
			if(hasEndDateSpecifier(contentToAdd)){
				endDateWithSpecifier = findEndDateWithSpecifier(contentToAdd);
				contentToAdd = removeEndDate(contentToAdd);		
			}else{
				endDateWithSpecifier = NULL_VALUE;
			}
			
			if(hasStartTimeSpecifier(contentToAdd)){
				startTimeWithSpecifier = findStartTimeWithSpecifier(contentToAdd);
				contentToAdd = removeStartTime(contentToAdd);
				startTimeWithSpecifier = convertTimeToFourDigit(startTimeWithSpecifier);
			}else{
				startTimeWithSpecifier = NULL_VALUE;
			}
			
			if(hasEndTimeSpecifier(contentToAdd)){
				endTimeWithSpecifier = findEndTimeWithSpecifier(contentToAdd);
				contentToAdd = removeEndTime(contentToAdd);
				endTimeWithSpecifier = convertTimeToFourDigit(endTimeWithSpecifier);
			}else{
				endTimeWithSpecifier = NULL_VALUE;
			}
			
			if(contentToAdd.isEmpty() == false){
				descriptionWithSpecifier = contentToAdd.trim();
			}else{
				descriptionWithSpecifier = NULL_VALUE;
			}
			
			startDateWithSpecifier = convertTodayAndTomorrow(startDateWithSpecifier);
			endDateWithSpecifier = convertTodayAndTomorrow(endDateWithSpecifier);
			
			b.putCommand(commandType);
			b.putTitle(titleWithSpecifier);
			b.putDescription(descriptionWithSpecifier);
			b.putStartTime(startTimeWithSpecifier);
			b.putEndTime(endTimeWithSpecifier);
			b.putStartDate(startDateWithSpecifier);
			b.putEndDate(endDateWithSpecifier);
			
		}else{
			
			String description = findDescription(contentToAdd);
			if (description.isEmpty()) {
				description = NULL_VALUE;
			}
			if (description.equals(NULL_VALUE) == false) {
				contentToAdd = removeDescription(contentToAdd);
			}
			// System.out.println("Description is "+description);
			// System.out.println("After remove description: "+contentToAdd);

			String title = findTitle(contentToAdd);
			if (title.isEmpty()) {
				title = NULL_VALUE;
			}
			// System.out.println("Title is "+title);

			if (title.equals(NULL_VALUE) == false) {
				contentToAdd = removeTitle(contentToAdd, title);
			}
			// System.out.println("new content after remove title: "+contentToAdd);

			String startTime = findStartTime(contentToAdd);
			if (startTime.isEmpty()) {
				startTime = NULL_VALUE;
			}
			if (startTime.equals(NULL_VALUE) == false) {
				startTime = convertTimeToFourDigit(startTime);
			}
			// System.out.println("Start time is "+startTime);

			String endTime = findEndTime(contentToAdd);
			if (endTime.isEmpty()) {
				endTime = NULL_VALUE;
			}
			if (endTime.equals(NULL_VALUE) == false) {
				endTime = convertTimeToFourDigit(endTime);
			}
			// System.out.println("End time is "+endTime);

			if (startTime.equals(NULL_VALUE) == false
					|| endTime.equals(NULL_VALUE) == false) {
				contentToAdd = removeStartAndEndTime(startTime, endTime,
						contentToAdd);
			}
			// System.out.println("new content after remove start and end time: "+contentToAdd);

			String startDate = findStartDate(contentToAdd);
			if (startDate.isEmpty()) {
				startDate = NULL_VALUE;
			}
			// System.out.println("start date is "+startDate);

			String endDate = findEndDate(contentToAdd);
			if (endDate.isEmpty()) {
				endDate = NULL_VALUE;
			}
			// System.out.println("end date is "+endDate);

			
			startDate = convertTodayAndTomorrow(startDate);
			endDate = convertTodayAndTomorrow(endDate);
			
			b.putCommand(commandType);
			b.putTitle(title);
			b.putDescription(description);
			b.putStartTime(startTime);
			b.putEndTime(endTime);
			b.putStartDate(startDate);
			b.putEndDate(endDate);
		}
		
	}

	private static void deleteDataPadding(UserInputBundle b,
			String commandType, String userCommand) {
		String contentToDelete = removeFirstWord(userCommand);
		if (contentToDelete.isEmpty() == false) {
			b.putCommand(commandType);
			b.putDescription(NULL_VALUE);
			b.putStartTime(NULL_VALUE);
			b.putEndTime(NULL_VALUE);
			b.putStartDate(NULL_VALUE);
			b.putEndDate(NULL_VALUE);
			b.putTitle(NULL_VALUE);
			b.putID(contentToDelete);
		} 

	}

	private static void displayDataPadding(UserInputBundle b, String commandType) {

		b.putCommand(commandType);
		b.putTitle(NULL_VALUE);
		b.putDescription(NULL_VALUE);
		b.putStartTime(NULL_VALUE);
		b.putEndTime(NULL_VALUE);
		b.putStartDate(NULL_VALUE);
		b.putEndDate(NULL_VALUE);
	}

	private static void editDataPadding(UserInputBundle b, String commandType,
			String userCommand) {
		String contentToEdit = removeFirstWord(userCommand);
		String ID = getFirstWord(contentToEdit);
		if (isNumericType(ID) == true) {
			// System.out.println("ID is "+ID);
			contentToEdit = removeFirstWord(contentToEdit);

			String description = findDescription(contentToEdit);
			if (description.isEmpty()) {
				description = NULL_VALUE;
			}
			// System.out.println("Description is "+description);

			if (description.equals(NULL_VALUE) == false) {
				contentToEdit = removeDescription(contentToEdit);
			}

			String title = findTitle(contentToEdit);
			if (title.isEmpty()) {
				title = NULL_VALUE;
			}
			// System.out.println("Title is "+title);

			if (title.equals(NULL_VALUE) == false) {
				contentToEdit = removeTitle(contentToEdit, title);
			}

			String startTime = findStartTime(contentToEdit);
			if (startTime.isEmpty()) {
				startTime = NULL_VALUE;
			}
			if (startTime.equals(NULL_VALUE) == false) {
				startTime = convertTimeToFourDigit(startTime);
			}
			// System.out.println("Start time is "+startTime);

			String endTime = findEndTime(contentToEdit);
			if (endTime.isEmpty()) {
				endTime = NULL_VALUE;
			}
			if (endTime.equals(NULL_VALUE) == false) {
				endTime = convertTimeToFourDigit(endTime);
			}
			// System.out.println("End time is "+endTime);

			if (startTime.equals(NULL_VALUE) == false
					|| endTime.equals(NULL_VALUE) == false) {
				contentToEdit = removeStartAndEndTime(startTime, endTime,
						contentToEdit);
			}

			String startDate = findStartDate(contentToEdit);
			if (startDate.isEmpty()) {
				startDate = NULL_VALUE;
			}
			// System.out.println("start date is "+startDate);

			String endDate = findEndDate(contentToEdit);
			if (endDate.isEmpty()) {
				endDate = NULL_VALUE;
			}
			// System.out.println("end date is "+endDate);
			
			startDate = convertTodayAndTomorrow(startDate);
			endDate = convertTodayAndTomorrow(endDate);

			b.putCommand(commandType);
			b.putID(ID);
			b.putTitle(title);
			b.putDescription(description);
			b.putStartTime(startTime);
			b.putEndTime(endTime);
			b.putStartDate(startDate);
			b.putEndDate(endDate);
		} else {
			throw new NullPointerException("Please enter task ID");
		}

	}

	private static boolean isNumericType(String ID) {
		try {
			Integer.parseInt(ID);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	private static void searchDatePadding(UserInputBundle b,
			String commandType, String userCommand) {
		String contentToSearch = removeFirstWord(userCommand);
		if (contentToSearch.isEmpty()) {
			throw new NullPointerException("Please enter search keyword");
		} else {
			b.putCommand(commandType);
			b.putDescription(contentToSearch);
			b.putTitle(contentToSearch);
			b.putStartTime(NULL_VALUE);
			b.putEndTime(NULL_VALUE);
			b.putStartDate(NULL_VALUE);
			b.putEndDate(NULL_VALUE);

			// System.out.println("Title is "+b.getTitle());
			// System.out.println("Description is "+b.getDescription());
		}

	}

	private static String findDescription(String content) {
		String[] contentSplit = content.trim().split("\\s+");
		String description = "";
		ArrayList<String> contentSplitCopy = new ArrayList<String>();
		for (int i = 0; i < contentSplit.length; i++) {
			contentSplitCopy.add(contentSplit[i]);
		}

		boolean hasDescription = false;
		int j;
		for (j = 0; j < contentSplitCopy.size(); j++) {
			if (contentSplitCopy.get(j).contains("#")) {
				hasDescription = true;
				break;
			}
		}

		if (hasDescription) {
			for (int k = j; k < contentSplitCopy.size(); k++) {
				description += contentSplitCopy.get(k);
				description += " ";
			}
		}
		description = description.trim();
		description = description.replace("#", "");

		return description;
	}

	private static String removeDescription(String content) {
		String[] contentSplit = content.trim().split("\\s+");
		ArrayList<String> contentSplitCopy = new ArrayList<String>();
		for (int i = 0; i < contentSplit.length; i++) {
			contentSplitCopy.add(contentSplit[i]);
		}

		int j;
		for (j = 0; j < contentSplitCopy.size(); j++) {
			if (contentSplitCopy.get(j).contains("#")) {
				break;
			}
		}

		while (j < contentSplitCopy.size()) {
			contentSplitCopy.remove(j);
		}

		String contentToAdd = "";
		for (int k = 0; k < contentSplitCopy.size(); k++) {
			contentToAdd += contentSplitCopy.get(k);
			contentToAdd += " ";
		}
		contentToAdd = contentToAdd.trim();
		return contentToAdd;
	}

	private static String findTitle(String content) {
		String[] contentSplit = content.trim().split("\\s+");
		String title = "";
		ArrayList<String> contentSplitCopy = new ArrayList<String>();
		for (int i = 0; i < contentSplit.length; i++) {
			contentSplitCopy.add(contentSplit[i]);
		}

		// remove from this Fri 3pm to next Mon 10am
		// remove from next Mon 4pm to Wed 11am
		// remove from this Fri 4pm to 6pm
		boolean modified = false;
		int j = 0;
		for (; j < contentSplitCopy.size(); j++) {
			if (isFromKeyword(contentSplitCopy.get(j))) {
				if (j + 3 < contentSplitCopy.size()) {
					if (isThisKeyword(contentSplitCopy.get(j + 1))
							|| isNextKeyword(contentSplitCopy.get(j + 1))) {
						if (isWeekDayKeyword(contentSplitCopy.get(j + 2))) {
							if (isTimeString(contentSplitCopy.get(j + 3))) {
								contentSplitCopy.remove(j);
								contentSplitCopy.remove(j);
								contentSplitCopy.remove(j);
								contentSplitCopy.remove(j);
								modified = true;
								j--;
							}
						}
					}
				}
				if (modified == true) {
					if (j + 4 < contentSplitCopy.size()) {
						if (isToKeyword(contentSplitCopy.get(j + 1))) {
							if (isThisKeyword(contentSplitCopy.get(j + 2))
									|| isNextKeyword(contentSplitCopy
											.get(j + 2))) {
								if (isWeekDayKeyword(contentSplitCopy
										.get(j + 3))) {
									if (isTimeString(contentSplitCopy
											.get(j + 4))) {
										contentSplitCopy.remove(j + 1);
										contentSplitCopy.remove(j + 1);
										contentSplitCopy.remove(j + 1);
										contentSplitCopy.remove(j + 1);
									}
								}
							}
						}
					}
					if (j + 3 < contentSplitCopy.size()) {
						if (isToKeyword(contentSplitCopy.get(j + 1))) {
							if (isWeekDayKeyword(contentSplitCopy.get(j + 2))) {
								if (isTimeString(contentSplitCopy.get(j + 3))) {
									contentSplitCopy.remove(j + 1);
									contentSplitCopy.remove(j + 1);
									contentSplitCopy.remove(j + 1);
								}
							}
						}
					}
					if (j + 2 < contentSplitCopy.size()) {
						if (isToKeyword(contentSplitCopy.get(j + 1))) {
							if (isTimeString(contentSplitCopy.get(j + 2))) {
								contentSplitCopy.remove(j + 1);
								contentSplitCopy.remove(j + 1);
							}
						}
					}
				}
			}

		}

		// remove from 3pm this Fri to 10am next Mon
		// remove from 2pm on Mon to 10am on Wed
		modified = false;
		j = 0;
		for (; j < contentSplitCopy.size(); j++) {
			if (isFromKeyword(contentSplitCopy.get(j))) {
				if (j + 3 < contentSplitCopy.size()) {
					if (isTimeString(contentSplitCopy.get(j + 1))) {
						if (isThisKeyword(contentSplitCopy.get(j + 2))
								|| isNextKeyword(contentSplitCopy.get(j + 2))
								|| isOnKeyword(contentSplitCopy.get(j + 2))) {
							if (isWeekDayKeyword(contentSplitCopy.get(j + 3))) {
								contentSplitCopy.remove(j);
								contentSplitCopy.remove(j);
								contentSplitCopy.remove(j);
								contentSplitCopy.remove(j);
								modified = true;
								j--;
							}
						}
					}
				}
				if (modified == true) {
					if (j + 4 < contentSplitCopy.size()) {
						if (isToKeyword(contentSplitCopy.get(j + 1))) {
							if (isTimeString(contentSplitCopy.get(j + 2))) {
								if (isThisKeyword(contentSplitCopy.get(j + 3))
										|| isNextKeyword(contentSplitCopy
												.get(j + 3))
										|| isOnKeyword(contentSplitCopy
												.get(j + 3))) {
									if (isWeekDayKeyword(contentSplitCopy
											.get(j + 4))) {
										contentSplitCopy.remove(j + 1);
										contentSplitCopy.remove(j + 1);
										contentSplitCopy.remove(j + 1);
										contentSplitCopy.remove(j + 1);
									}
								}
							}
						}
					}
				}
			}
		}

		// remove from 9pm today to 10am tomorrow
		// remove from 9pm 12/12/2014 to 10am 13/12/2014
		// remove from 9am mon to 10pm sat
		modified = false;
		j = 0;
		for (; j < contentSplitCopy.size(); j++) {
			if (isFromKeyword(contentSplitCopy.get(j))) {
				if (j + 2 < contentSplitCopy.size()) {
					if (isTimeString(contentSplitCopy.get(j + 1))
							&& (isSpecificDateString(contentSplitCopy
									.get(j + 2))
									|| isBlurredDateString(contentSplitCopy
											.get(j + 2)) || isWeekDayKeyword(contentSplitCopy
										.get(j + 2)))) {
						contentSplitCopy.remove(j);
						contentSplitCopy.remove(j);
						contentSplitCopy.remove(j);
						modified = true;
						j--;
					}
				}
				if (modified == true) {
					if (j + 3 < contentSplitCopy.size()) {
						if (isToKeyword(contentSplitCopy.get(j + 1))) {
							if (isTimeString(contentSplitCopy.get(j + 2))) {
								if (isSpecificDateString(contentSplitCopy
										.get(j + 3))
										|| isBlurredDateString(contentSplitCopy
												.get(j + 3))
										|| isWeekDayKeyword(contentSplitCopy
												.get(j + 3))) {
									contentSplitCopy.remove(j + 1);
									contentSplitCopy.remove(j + 1);
									contentSplitCopy.remove(j + 1);
								}
							}
						}
					}
				}
			}
		}

		// remove from this Sat to next Mon
		// remove from this Fri to Sat
		modified = false;
		j = 0;
		for (; j < contentSplitCopy.size(); j++) {
			if (isFromKeyword(contentSplitCopy.get(j))) {
				if (j + 2 < contentSplitCopy.size()) {
					if (isThisKeyword(contentSplitCopy.get(j + 1))
							|| isNextKeyword(contentSplitCopy.get(j + 1))) {
						if (isWeekDayKeyword(contentSplitCopy.get(j + 2))) {
							contentSplitCopy.remove(j);
							contentSplitCopy.remove(j);
							contentSplitCopy.remove(j);
							modified = true;
							j--;
						}
					}
				}
				if (modified == true) {
					if (j + 3 < contentSplitCopy.size()) {
						if (isToKeyword(contentSplitCopy.get(j + 1))) {
							if (isThisKeyword(contentSplitCopy.get(j + 2))
									|| isNextKeyword(contentSplitCopy
											.get(j + 2))) {
								if (isWeekDayKeyword(contentSplitCopy
										.get(j + 3))) {
									contentSplitCopy.remove(j + 1);
									contentSplitCopy.remove(j + 1);
									contentSplitCopy.remove(j + 1);
								}
							}
						}
					}
					if (j + 2 < contentSplitCopy.size()) {
						if (isToKeyword(contentSplitCopy.get(j + 1))) {
							if (isWeekDayKeyword(contentSplitCopy.get(j + 2))) {
								contentSplitCopy.remove(j + 1);
								contentSplitCopy.remove(j + 1);
							}
						}
					}
				}
			}

		}

		// remove from today 3pm to tomorrow 9am
		// remove from 23/10/2014 2pm to 25/10/2014 5pm
		// remove from tomorrow 1pm to 10pm
		// remove from Sat 3pm to Sun 9am
		// remove from Sat 5pm to 8pm
		modified = false;
		j = 0;
		for (; j < contentSplitCopy.size(); j++) {
			if (isFromKeyword(contentSplitCopy.get(j))) {
				if (j + 2 < contentSplitCopy.size()) {
					if ((isSpecificDateString(contentSplitCopy.get(j + 1))
							|| isBlurredDateString(contentSplitCopy.get(j + 1)) || isWeekDayKeyword(contentSplitCopy
								.get(j + 1)))
							&& isTimeString(contentSplitCopy.get(j + 2))) {
						contentSplitCopy.remove(j);
						contentSplitCopy.remove(j);
						contentSplitCopy.remove(j);
						modified = true;
						j--;
					}
				}
				if (modified == true) {
					if (j + 3 < contentSplitCopy.size()) {
						if (isToKeyword(contentSplitCopy.get(j + 1))) {
							if ((isSpecificDateString(contentSplitCopy
									.get(j + 2))
									|| isBlurredDateString(contentSplitCopy
											.get(j + 2)) || isWeekDayKeyword(contentSplitCopy
										.get(j + 2)))
									&& isTimeString(contentSplitCopy.get(j + 3))) {
								contentSplitCopy.remove(j + 1);
								contentSplitCopy.remove(j + 1);
								contentSplitCopy.remove(j + 1);
							}
						}
					}
					if (j + 2 < contentSplitCopy.size()) {
						if (isToKeyword(contentSplitCopy.get(j + 1))) {
							if (isTimeString(contentSplitCopy.get(j + 2))) {
								contentSplitCopy.remove(j + 1);
								contentSplitCopy.remove(j + 1);
							}
						}
					}
				}

			}
		}

		// remove from 23/10/2014 to 25/10/2014
		// remove from today to tomorrow
		// remove from today to 25/10/2014
		// remove from Fri to Sat
		modified = false;
		j = 0;
		for (; j < contentSplitCopy.size(); j++) {
			if (isFromKeyword(contentSplitCopy.get(j))) {
				if (j + 1 < contentSplitCopy.size()) {
					if (isSpecificDateString(contentSplitCopy.get(j + 1))
							|| isBlurredDateString(contentSplitCopy.get(j + 1))
							|| isWeekDayKeyword(contentSplitCopy.get(j + 1))) {
						contentSplitCopy.remove(j);
						contentSplitCopy.remove(j);
						modified = true;
						j--;
					}
				}
				if (modified == true) {
					if (j + 2 < contentSplitCopy.size()) {
						if (isToKeyword(contentSplitCopy.get(j + 1))) {
							if (isSpecificDateString(contentSplitCopy
									.get(j + 2))
									|| isBlurredDateString(contentSplitCopy
											.get(j + 2))
									|| isWeekDayKeyword(contentSplitCopy
											.get(j + 2))) {
								contentSplitCopy.remove(j + 1);
								contentSplitCopy.remove(j + 1);
							}
						}
					}
				}
			}
		}

		// remove by next Mon 2pm
		// remove by this Thur
		j = 0;
		for (; j < contentSplitCopy.size(); j++) {
			if (isDeadlineKeyword(contentSplitCopy.get(j))) {
				if (j + 3 < contentSplitCopy.size()) {
					if (isThisKeyword(contentSplitCopy.get(j + 1))
							|| isNextKeyword(contentSplitCopy.get(j + 1))) {
						if (isWeekDayKeyword(contentSplitCopy.get(j + 2))) {
							if (isTimeString(contentSplitCopy.get(j + 3))) {
								contentSplitCopy.remove(j);
								contentSplitCopy.remove(j);
								contentSplitCopy.remove(j);
								contentSplitCopy.remove(j);
							}
						}
					}
				}
				if (j + 2 < contentSplitCopy.size()) {
					if (isThisKeyword(contentSplitCopy.get(j + 1))
							|| isNextKeyword(contentSplitCopy.get(j + 1))) {
						if (isWeekDayKeyword(contentSplitCopy.get(j + 2))) {
							contentSplitCopy.remove(j);
							contentSplitCopy.remove(j);
							contentSplitCopy.remove(j);
						}
					}
				}
			}
		}

		// remove by tomorrow 2pm
		// remove by 25/10/2014 2pm
		// remove by Fri 5pm
		j = 0;
		for (; j < contentSplitCopy.size(); j++) {
			if (isDeadlineKeyword(contentSplitCopy.get(j))) {
				if (j + 2 < contentSplitCopy.size()) {
					if (isSpecificDateString(contentSplitCopy.get(j + 1))
							|| isBlurredDateString(contentSplitCopy.get(j + 1))
							|| isWeekDayKeyword(contentSplitCopy.get(j + 1))) {
						if (isTimeString(contentSplitCopy.get(j + 2))) {
							contentSplitCopy.remove(j);
							contentSplitCopy.remove(j);
							contentSplitCopy.remove(j);
						}
					}
				}

			}
		}

		// remove by tomorrow
		// remove by 26/10/2014
		// remove by 12pm
		// remove by Fri
		j = 0;
		for (; j < contentSplitCopy.size(); j++) {
			if (isDeadlineKeyword(contentSplitCopy.get(j))) {
				if (j + 1 < contentSplitCopy.size()) {
					if (isSpecificDateString(contentSplitCopy.get(j + 1))
							|| isBlurredDateString(contentSplitCopy.get(j + 1))
							|| isWeekDayKeyword(contentSplitCopy.get(j + 1))
							|| isTimeString(contentSplitCopy.get(j + 1))) {
						contentSplitCopy.remove(j);
						contentSplitCopy.remove(j);
					}
				}
			}
		}

		// remove at 2pm
		j = 0;
		for (; j < contentSplitCopy.size(); j++) {
			if (isAtKeyword(contentSplitCopy.get(j))) {
				if (j + 1 < contentSplitCopy.size()) {
					if (isTimeString(contentSplitCopy.get(j + 1))) {
						contentSplitCopy.remove(j);
						contentSplitCopy.remove(j);
					}
				}
			}
		}

		// remove from 2pm to 3pm
		modified = false;
		j = 0;
		for (; j < contentSplitCopy.size(); j++) {
			if (isFromKeyword(contentSplitCopy.get(j))) {
				if (j + 1 < contentSplitCopy.size()) {
					if (isTimeString(contentSplitCopy.get(j + 1))) {
						contentSplitCopy.remove(j);
						contentSplitCopy.remove(j);
						modified = true;
						j--;
					}
				}
				if (modified == true) {
					if (j + 2 < contentSplitCopy.size()) {
						if (isToKeyword(contentSplitCopy.get(j + 1))) {
							if (isTimeString(contentSplitCopy.get(j + 2))) {
								contentSplitCopy.remove(j + 1);
								contentSplitCopy.remove(j + 1);
							}
						}
					}
				}
			}
		}

		// remove on 11/12/2014
		j = 0;
		for (; j < contentSplitCopy.size(); j++) {
			if (isOnKeyword(contentSplitCopy.get(j))) {
				if (j + 1 < contentSplitCopy.size()) {
					if (isSpecificDateString(contentSplitCopy.get(j + 1))) {
						contentSplitCopy.remove(j);
						contentSplitCopy.remove(j);
					}
				}
			}
		}

		// remove today/tomorrow/this Sat/next Sat/on Sat
		j = 0;
		for (; j < contentSplitCopy.size(); j++) {
			if (isTodayKeyword(contentSplitCopy.get(j))) {
				contentSplitCopy.remove(j);
			} else if (isTomorrowKeyword(contentSplitCopy.get(j))) {
				contentSplitCopy.remove(j);
			} else if (isThisKeyword(contentSplitCopy.get(j))
					|| isNextKeyword(contentSplitCopy.get(j))) {
				if (j + 1 < contentSplitCopy.size()) {
					if (isWeekDayKeyword(contentSplitCopy.get(j + 1))) {
						contentSplitCopy.remove(j);
						contentSplitCopy.remove(j);
					}
				}
			} else if (isOnKeyword(contentSplitCopy.get(j))) {
				if (j + 1 < contentSplitCopy.size()) {
					if (isWeekDayKeyword(contentSplitCopy.get(j + 1))) {
						contentSplitCopy.remove(j);
						contentSplitCopy.remove(j);
					}
				}
			}
		}

		// If the user do not use preposition

		// eg. having dinner 8pm, remove 8pm
		// eg. meet friends Thur, remove Thur
		// eg. having dinner 10/22/2014, remove 10/22/2014
		j = 0;
		for (; j < contentSplitCopy.size(); j++) {
			if (isTimeString(contentSplitCopy.get(j))) {
				contentSplitCopy.remove(j);
			}
		}

		j = 0;
		for (; j < contentSplitCopy.size(); j++) {
			if (isWeekDayKeyword(contentSplitCopy.get(j))) {
				contentSplitCopy.remove(j);
			}
		}

		j = 0;
		for (; j < contentSplitCopy.size(); j++) {
			if (isSpecificDateString(contentSplitCopy.get(j))) {
				contentSplitCopy.remove(j);
			}
		}

		for (int k = 0; k < contentSplitCopy.size(); k++) {
			title += contentSplitCopy.get(k);
			title += " ";
		}

		title = title.trim();
		return title;

	}

	private static String removeTitle(String content, String title) {
		String[] contentSplit = content.trim().split("\\s+");
		ArrayList<String> contentSplitCopy = new ArrayList<String>();
		for (int i = 0; i < contentSplit.length; i++) {
			contentSplitCopy.add(contentSplit[i]);
		}

		String[] titleSplit = title.trim().split("\\s+");
		ArrayList<String> titleSplitCopy = new ArrayList<String>();
		for (int i = 0; i < titleSplit.length; i++) {
			titleSplitCopy.add(titleSplit[i]);
		}

		for (int j = 0; j < titleSplitCopy.size(); j++) {
			contentSplitCopy.remove(titleSplitCopy.get(j));
		}

		String newContent = "";
		for (int k = 0; k < contentSplitCopy.size(); k++) {
			newContent += contentSplitCopy.get(k);
			newContent += " ";
		}

		newContent = newContent.trim();
		return newContent;

	}

	private static String findStartTime(String content) {
		String startTime = "";

		String[] contentSplit = content.trim().split("\\s+");
		ArrayList<String> contentSplitCopy = new ArrayList<String>();
		for (int i = 0; i < contentSplit.length; i++) {
			contentSplitCopy.add(contentSplit[i]);
		}

		boolean hasPreposition = false;
		int j = 0;
		for (; j < contentSplitCopy.size(); j++) {
			if (isAtKeyword(contentSplitCopy.get(j))) {
				if (j + 1 < contentSplitCopy.size()) {
					// eg. at 2pm
					if (isTimeString(contentSplitCopy.get(j + 1))) {
						startTime = contentSplitCopy.get(j + 1);
						hasPreposition = true;
					}
				}
			} else if (isFromKeyword(contentSplitCopy.get(j))) {
				if (j + 1 < contentSplitCopy.size()) {
					// eg. from 9am to 4pm
					// eg. from 9am today to 4pm tomorrow
					// eg. from 3pm this Fri to 10am next Mon
					if (isTimeString(contentSplitCopy.get(j + 1))) {
						startTime = contentSplitCopy.get(j + 1);
						hasPreposition = true;

						// eg. from 23/10/2014 2pm to 25/10/2014 5pm
						// eg. from today 3pm to tomorrow 9am
						// eg. from Sat 3pm to Sun 9am
						// eg. from Sat 3pm to 9pm
					} else if (isSpecificDateString(contentSplitCopy.get(j + 1))
							|| isBlurredDateString(contentSplitCopy.get(j + 1))
							|| isWeekDayKeyword(contentSplitCopy.get(j + 1))) {
						if (j + 2 < contentSplitCopy.size()) {
							if (isTimeString(contentSplitCopy.get(j + 2))) {
								startTime = contentSplitCopy.get(j + 2);
								hasPreposition = true;
							}
						}

						// eg. from this Fri 3pm to next Mon 10am
					} else if (isThisKeyword(contentSplitCopy.get(j + 1))
							|| isNextKeyword(contentSplitCopy.get(j + 1))) {
						if (j + 3 < contentSplitCopy.size()) {
							if (isWeekDayKeyword(contentSplitCopy.get(j + 2))
									&& isTimeString(contentSplitCopy.get(j + 3))) {
								startTime = contentSplitCopy.get(j + 3);
								hasPreposition = true;
							}
						}
					}
				}
			} else if (isDeadlineKeyword(contentSplitCopy.get(j))) {
				if (j + 1 < contentSplitCopy.size()) {
					// eg. by 10pm
					if (isTimeString(contentSplitCopy.get(j + 1))) {
						startTime = contentSplitCopy.get(j + 1);
						hasPreposition = true;
						// eg. by tomorrow 2pm
						// eg. by 25/10/2014 2pm
						// eg. by Fri 5pm
					} else if (isSpecificDateString(contentSplitCopy.get(j + 1))
							|| isBlurredDateString(contentSplitCopy.get(j + 1))
							|| isWeekDayKeyword(contentSplitCopy.get(j + 1))) {
						if (j + 2 < contentSplitCopy.size()) {
							if (isTimeString(contentSplitCopy.get(j + 2))) {
								startTime = contentSplitCopy.get(j + 2);
								hasPreposition = true;
							}
						}
						// eg. by next Mon 2pm
					} else if (isThisKeyword(contentSplitCopy.get(j + 1))
							|| isNextKeyword(contentSplitCopy.get(j + 1))) {
						if (j + 3 < contentSplitCopy.size()) {
							if (isWeekDayKeyword(contentSplitCopy.get(j + 2))
									&& isTimeString(contentSplitCopy.get(j + 3))) {
								startTime = contentSplitCopy.get(j + 3);
								hasPreposition = true;
							}
						}
					}
				}
			}
		}

		j = 0;
		if (hasPreposition == false) {
			for (; j < contentSplitCopy.size(); j++) {
				if (isTimeString(contentSplitCopy.get(j))) {
					startTime = contentSplitCopy.get(j);
				}
			}
		}

		return startTime;
	}

	private static String findEndTime(String content) {

		String endTime = "";

		String[] contentSplit = content.trim().split("\\s+");
		ArrayList<String> contentSplitCopy = new ArrayList<String>();
		for (int i = 0; i < contentSplit.length; i++) {
			contentSplitCopy.add(contentSplit[i]);
		}

		boolean hasPreposition = false;
		int j = 0;
		for (; j < contentSplitCopy.size(); j++) {
			if (isAtKeyword(contentSplitCopy.get(j))) {
				if (j + 1 < contentSplitCopy.size()) {
					// eg. at 2pm
					if (isTimeString(contentSplitCopy.get(j + 1))) {
						endTime = contentSplitCopy.get(j + 1);
						hasPreposition = true;
					}
				}
			} else if (isFromKeyword(contentSplitCopy.get(j))) {
				if (j + 3 < contentSplitCopy.size()) {
					// eg. from 9am to 4pm
					if (isTimeString(contentSplitCopy.get(j + 1))
							&& isToKeyword(contentSplitCopy.get(j + 2))
							&& isTimeString(contentSplitCopy.get(j + 3))) {
						endTime = contentSplitCopy.get(j + 3);
						hasPreposition = true;
					}
				} else if (j + 4 < contentSplitCopy.size()) {
					// eg. from 9am today to 4pm tomorrow
					// eg. from 9am 23/10/2014 to 10pm 24/10/2014
					if (isTimeString(contentSplitCopy.get(j + 1))
							&& (isSpecificDateString(contentSplitCopy
									.get(j + 2))
									|| isBlurredDateString(contentSplitCopy
											.get(j + 2)) || isWeekDayKeyword(contentSplitCopy
										.get(j + 2)))
							&& isToKeyword(contentSplitCopy.get(j + 3))
							&& isTimeString(contentSplitCopy.get(j + 4))) {
						endTime = contentSplitCopy.get(j + 4);
						hasPreposition = true;

						// eg. from Sat 3pm to 9pm
					} else if ((isSpecificDateString(contentSplitCopy
							.get(j + 1))
							|| isBlurredDateString(contentSplitCopy.get(j + 1)) || isWeekDayKeyword(contentSplitCopy
								.get(j + 1)))
							&& isTimeString(contentSplitCopy.get(j + 2))
							&& isToKeyword(contentSplitCopy.get(j + 3))
							&& isTimeString(contentSplitCopy.get(j + 4))) {
						endTime = contentSplitCopy.get(j + 4);
						hasPreposition = true;
					}
				} else if (j + 5 < contentSplitCopy.size()) {
					// eg. from 23/10/2014 2pm to 25/10/2014 5pm
					// eg. from today 3pm to tomorrow 9am
					// eg. from Sat 3pm to Sun 9am
					if ((isSpecificDateString(contentSplitCopy.get(j + 1))
							|| isBlurredDateString(contentSplitCopy.get(j + 1)) || isWeekDayKeyword(contentSplitCopy
								.get(j + 1)))
							&& isTimeString(contentSplitCopy.get(j + 2))
							&& isToKeyword(contentSplitCopy.get(j + 3))
							&& (isSpecificDateString(contentSplitCopy
									.get(j + 4))
									|| isBlurredDateString(contentSplitCopy
											.get(j + 4)) || isWeekDayKeyword(contentSplitCopy
										.get(j + 4)))
							&& isTimeString(contentSplitCopy.get(j + 5))) {
						endTime = contentSplitCopy.get(j + 5);
						hasPreposition = true;

						// eg. from 3pm this Fri to 10am next Mon
					} else if (isTimeString(contentSplitCopy.get(j + 1))
							&& (isThisKeyword(contentSplitCopy.get(j + 2)) || isNextKeyword(contentSplitCopy
									.get(j + 2)))
							&& isWeekDayKeyword(contentSplitCopy.get(j + 3))
							&& isToKeyword(contentSplitCopy.get(j + 4))
							&& isTimeString(contentSplitCopy.get(j + 5))) {
						endTime = contentSplitCopy.get(j + 5);
						hasPreposition = true;
					}
				} else if (j + 7 < contentSplitCopy.size()) {
					// eg. from this Fri 3pm to next Mon 10am
					if ((isThisKeyword(contentSplitCopy.get(j + 1)) || isNextKeyword(contentSplitCopy
							.get(j + 1)))
							&& isWeekDayKeyword(contentSplitCopy.get(j + 2))
							&& isTimeString(contentSplitCopy.get(j + 3))
							&& isToKeyword(contentSplitCopy.get(j + 4))
							&& (isThisKeyword(contentSplitCopy.get(j + 5)) || isNextKeyword(contentSplitCopy
									.get(j + 5)))
							&& isWeekDayKeyword(contentSplitCopy.get(j + 6))
							&& isTimeString(contentSplitCopy.get(j + 7))) {
						endTime = contentSplitCopy.get(j + 7);
						hasPreposition = true;
					}
				}

			} else if (isDeadlineKeyword(contentSplitCopy.get(j))) {
				if (j + 1 < contentSplitCopy.size()) {
					// eg. by 10pm
					if (isTimeString(contentSplitCopy.get(j + 1))) {
						endTime = contentSplitCopy.get(j + 1);
						hasPreposition = true;
						// eg. by tomorrow 2pm
						// eg. by 25/10/2014 2pm
						// eg. by Fri 5pm
					} else if (isSpecificDateString(contentSplitCopy.get(j + 1))
							|| isBlurredDateString(contentSplitCopy.get(j + 1))
							|| isWeekDayKeyword(contentSplitCopy.get(j + 1))) {
						if (j + 2 < contentSplitCopy.size()) {
							if (isTimeString(contentSplitCopy.get(j + 2))) {
								endTime = contentSplitCopy.get(j + 2);
								hasPreposition = true;
							}
						}
						// eg. by next Mon 2pm
					} else if (isThisKeyword(contentSplitCopy.get(j + 1))
							|| isNextKeyword(contentSplitCopy.get(j + 1))) {
						if (j + 3 < contentSplitCopy.size()) {
							if (isWeekDayKeyword(contentSplitCopy.get(j + 2))
									&& isTimeString(contentSplitCopy.get(j + 3))) {
								endTime = contentSplitCopy.get(j + 3);
								hasPreposition = true;
							}
						}
					}
				}
			}
		}

		j = 0;
		if (hasPreposition == false) {
			for (; j < contentSplitCopy.size(); j++) {
				if (isTimeString(contentSplitCopy.get(j))) {
					endTime = contentSplitCopy.get(j);
				}
			}
		}

		return endTime;

	}

	private static String removeStartAndEndTime(String startTime,
			String endTime, String content) {

		String[] contentSplit = content.trim().split("\\s+");
		ArrayList<String> contentSplitCopy = new ArrayList<String>();
		for (int i = 0; i < contentSplit.length; i++) {
			contentSplitCopy.add(contentSplit[i]);
		}

		if (startTime.equals(NULL_VALUE) == false) {
			contentSplitCopy.remove(startTime);
		}
		if (endTime.equals(NULL_VALUE) == false) {
			contentSplitCopy.remove(endTime);
		}

		String newContent = "";
		for (int k = 0; k < contentSplitCopy.size(); k++) {
			newContent += contentSplitCopy.get(k);
			newContent += " ";
		}

		newContent = newContent.trim();
		return newContent;

	}

	private static String findStartDate(String content) {

		String startDate = "";

		String[] contentSplit = content.trim().split("\\s+");
		ArrayList<String> contentSplitCopy = new ArrayList<String>();
		for (int i = 0; i < contentSplit.length; i++) {
			contentSplitCopy.add(contentSplit[i]);
		}

		boolean hasPrep = false;
		int j = 0;
		for (; j < contentSplitCopy.size(); j++) {
			if (isFromKeyword(contentSplitCopy.get(j))) {
				// eg. from this Fri to next Mon
				if (j + 2 < contentSplitCopy.size()) {
					if ((isThisKeyword(contentSplitCopy.get(j + 1)) || isNextKeyword(contentSplitCopy
							.get(j + 1)))
							&& isWeekDayKeyword(contentSplitCopy.get(j + 2))) {
						startDate = contentSplitCopy.get(j + 1) + " "
								+ contentSplitCopy.get(j + 2);
						hasPrep = true;
					}
				}
			}
		}

		j = 0;
		for (; j < contentSplitCopy.size(); j++) {
			if (isFromKeyword(contentSplitCopy.get(j))) {
				// eg. from today to tomorrow
				// eg. from Sat to Sun
				// eg. from 24/10/2014 to 25/10/2014
				if (j + 1 < contentSplitCopy.size()) {
					if (isSpecificDateString(contentSplitCopy.get(j + 1))
							|| isBlurredDateString(contentSplitCopy.get(j + 1))
							|| isWeekDayKeyword(contentSplitCopy.get(j + 1))) {
						startDate = contentSplitCopy.get(j + 1);
						hasPrep = true;
					}
				}
			}
		}

		j = 0;
		for (; j < contentSplitCopy.size(); j++) {
			if (isOnKeyword(contentSplitCopy.get(j))) {
				// eg. on 24/10/2014
				// eg. on Sat
				if (j + 1 < contentSplitCopy.size()) {
					if (isSpecificDateString(contentSplitCopy.get(j + 1))
							|| isWeekDayKeyword(contentSplitCopy.get(j + 1))) {
						startDate = contentSplitCopy.get(j + 1);
						hasPrep = true;
					}
				}
			}
		}

		j = 0;
		for (; j < contentSplitCopy.size(); j++) {
			if (isDeadlineKeyword(contentSplitCopy.get(j))) {
				// eg. by next Mon
				if (j + 2 < contentSplitCopy.size()) {
					if ((isThisKeyword(contentSplitCopy.get(j + 1)) || isNextKeyword(contentSplitCopy
							.get(j + 1)))
							&& isWeekDayKeyword(contentSplitCopy.get(j + 2))) {
						startDate = contentSplitCopy.get(j + 1) + " "
								+ contentSplitCopy.get(j + 2);
						hasPrep = true;
					}
				}
			}
		}

		j = 0;
		for (; j < contentSplitCopy.size(); j++) {
			if (isDeadlineKeyword(contentSplitCopy.get(j))) {
				// eg. by Fri
				// eg. by tomorrow
				// eg. by 24/10/2014
				if (j + 1 < contentSplitCopy.size()) {
					if (isSpecificDateString(contentSplitCopy.get(j + 1))
							|| isWeekDayKeyword(contentSplitCopy.get(j + 1))
							|| isBlurredDateString(contentSplitCopy.get(j + 1))) {
						startDate = contentSplitCopy.get(j + 1);
						hasPrep = true;
					}
				}
			}
		}

		if (hasPrep == false) {
			for (int k = 0; k < contentSplitCopy.size(); k++) {
				// eg. do it today
				if (isTodayKeyword(contentSplitCopy.get(k))) {
					startDate = "today";
					// eg. do it tomorrow
				} else if (isTomorrowKeyword(contentSplitCopy.get(k))) {
					startDate = "tomorrow";
					// eg. do it next Fri
				} else if (isThisKeyword(contentSplitCopy.get(k))
						|| isNextKeyword(contentSplitCopy.get(k))) {
					if (k + 1 < contentSplitCopy.size()) {
						if (isWeekDayKeyword(contentSplitCopy.get(k + 1))) {
							startDate = contentSplitCopy.get(k) + " "
									+ contentSplitCopy.get(k + 1);
						}
					}
					// eg. do it Fri
				} else if (isWeekDayKeyword(contentSplitCopy.get(k))) {
					startDate = contentSplitCopy.get(k);
					// eg. do it 24/10/2014
				} else if (isSpecificDateString(contentSplitCopy.get(k))) {
					startDate = contentSplitCopy.get(k);
				}
			}
		}

		return startDate;

	}

	private static String findEndDate(String content) {

		String endDate = "";

		String[] contentSplit = content.trim().split("\\s+");
		ArrayList<String> contentSplitCopy = new ArrayList<String>();
		for (int i = 0; i < contentSplit.length; i++) {
			contentSplitCopy.add(contentSplit[i]);
		}

		boolean hasPrep = false;
		int j = 0;
		for (; j < contentSplitCopy.size(); j++) {
			if (isFromKeyword(contentSplitCopy.get(j))) {
				// eg. from this Fri to next Mon
				if (j + 5 < contentSplitCopy.size()) {
					if ((isThisKeyword(contentSplitCopy.get(j + 1)) || isNextKeyword(contentSplitCopy
							.get(j + 1)))
							&& isWeekDayKeyword(contentSplitCopy.get(j + 2))) {
						if (isToKeyword(contentSplitCopy.get(j + 3))
								&& (isThisKeyword(contentSplitCopy.get(j + 4)) || isNextKeyword(contentSplitCopy
										.get(j + 4)))
								&& isWeekDayKeyword(contentSplitCopy.get(j + 5))) {
							endDate = contentSplitCopy.get(j + 4) + " "
									+ contentSplitCopy.get(j + 5);
							hasPrep = true;
						}
					}

				}
			}
		}

		j = 0;
		for (; j < contentSplitCopy.size(); j++) {
			if (isFromKeyword(contentSplitCopy.get(j))) {
				// eg. from today to tomorrow
				// eg. from Sat to Sun
				// eg. from 24/10/2014 to 25/10/2014
				if (j + 3 < contentSplitCopy.size()) {
					if (isSpecificDateString(contentSplitCopy.get(j + 1))
							|| isBlurredDateString(contentSplitCopy.get(j + 1))
							|| isWeekDayKeyword(contentSplitCopy.get(j + 1))) {
						if (isToKeyword(contentSplitCopy.get(j + 2))
								&& (isSpecificDateString(contentSplitCopy
										.get(j + 3))
										|| isBlurredDateString(contentSplitCopy
												.get(j + 3)) || isWeekDayKeyword(contentSplitCopy
											.get(j + 3)))) {

							endDate = contentSplitCopy.get(j + 3);
							hasPrep = true;
						}
					}
				}
			}
		}

		j = 0;
		for (; j < contentSplitCopy.size(); j++) {
			if (isOnKeyword(contentSplitCopy.get(j))) {
				// eg. on 24/10/2014
				// eg. on Sat
				if (j + 1 < contentSplitCopy.size()) {
					if (isSpecificDateString(contentSplitCopy.get(j + 1))
							|| isWeekDayKeyword(contentSplitCopy.get(j + 1))) {
						endDate = contentSplitCopy.get(j + 1);
						hasPrep = true;
					}
				}
			}
		}

		j = 0;
		for (; j < contentSplitCopy.size(); j++) {
			if (isDeadlineKeyword(contentSplitCopy.get(j))) {
				// eg. by next Mon
				if (j + 2 < contentSplitCopy.size()) {
					if ((isThisKeyword(contentSplitCopy.get(j + 1)) || isNextKeyword(contentSplitCopy
							.get(j + 1)))
							&& isWeekDayKeyword(contentSplitCopy.get(j + 2))) {
						endDate = contentSplitCopy.get(j + 1) + " "
								+ contentSplitCopy.get(j + 2);
						hasPrep = true;
					}
				}
			}
		}

		j = 0;
		for (; j < contentSplitCopy.size(); j++) {
			if (isDeadlineKeyword(contentSplitCopy.get(j))) {
				// eg. by Fri
				// eg. by tomorrow
				// eg. by 24/10/2014
				if (j + 1 < contentSplitCopy.size()) {
					if (isSpecificDateString(contentSplitCopy.get(j + 1))
							|| isWeekDayKeyword(contentSplitCopy.get(j + 1))
							|| isBlurredDateString(contentSplitCopy.get(j + 1))) {
						endDate = contentSplitCopy.get(j + 1);
						hasPrep = true;
					}
				}
			}
		}

		if (hasPrep == false) {
			for (int k = 0; k < contentSplitCopy.size(); k++) {
				// eg. do it today
				if (isTodayKeyword(contentSplitCopy.get(k))) {
					endDate = "today";
					// eg. do it tomorrow
				} else if (isTomorrowKeyword(contentSplitCopy.get(k))) {
					endDate = "tomorrow";
					// eg. do it next Fri
				} else if (isThisKeyword(contentSplitCopy.get(k))
						|| isNextKeyword(contentSplitCopy.get(k))) {
					if (k + 1 < contentSplitCopy.size()) {
						if (isWeekDayKeyword(contentSplitCopy.get(k + 1))) {
							endDate = contentSplitCopy.get(k) + " "
									+ contentSplitCopy.get(k + 1);
						}
					}
					// eg. do it Fri
				} else if (isWeekDayKeyword(contentSplitCopy.get(k))) {
					endDate = contentSplitCopy.get(k);
					// eg. do it 24/10/2014
				} else if (isSpecificDateString(contentSplitCopy.get(k))) {
					endDate = contentSplitCopy.get(k);
				}
			}
		}

		return endDate;

	}

	private static boolean isAtKeyword(String s) {
		if (s.equalsIgnoreCase("at")) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isOnKeyword(String s) {
		if (s.equalsIgnoreCase("on")) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isFromKeyword(String s) {
		if (s.equalsIgnoreCase("from")) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isToKeyword(String s) {
		if (s.equalsIgnoreCase("to")) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isDeadlineKeyword(String s) {
		if (s.equalsIgnoreCase("by")) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isTodayKeyword(String s) {
		if (s.equalsIgnoreCase("today")) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isTomorrowKeyword(String s) {
		if (s.equalsIgnoreCase("tomorrow")) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isThisKeyword(String s) {
		if (s.equalsIgnoreCase("this")) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isNextKeyword(String s) {
		if (s.equalsIgnoreCase("next")) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isTimeString(String s) {
		if ((s.contains("am") || s.contains("pm") || s.contains("AM") || s
				.contains("PM")) && (s.length() >= 3)) {
			int strLength = s.length();
			if (s.substring(strLength - 2, strLength).equalsIgnoreCase("am")
					|| s.substring(strLength - 2, strLength).equalsIgnoreCase(
							"pm")) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	private static boolean isSpecificDateString(String s) {
		if (s.length() == 10 && s.charAt(2) == '/' && s.charAt(5) == '/') {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isBlurredDateString(String s) {
		if (s.equalsIgnoreCase("today") || s.equalsIgnoreCase("tomorrow")) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isWeekDayKeyword(String s) {
		if (s.equalsIgnoreCase("Monday") || s.equalsIgnoreCase("Mon")
				|| s.equalsIgnoreCase("Tuesday") || s.equalsIgnoreCase("Tues")
				|| s.equalsIgnoreCase("Wednesday") || s.equalsIgnoreCase("Wed")
				|| s.equalsIgnoreCase("Thursday")
				|| s.equalsIgnoreCase("Thurs") || s.equalsIgnoreCase("Friday")
				|| s.equalsIgnoreCase("Fri") || s.equalsIgnoreCase("Saturday")
				|| s.equalsIgnoreCase("Sat") || s.equalsIgnoreCase("Sunday")
				|| s.equalsIgnoreCase("Sun")) {
			return true;
		} else {
			return false;
		}
	}

	private static String convertTimeToFourDigit(String s) {
		String time = "";

		if (s.contains("am") || s.contains("AM")) {
			if (s.length() == 3) {
				// eg. 9am
				time = "0" + s.substring(0, 1) + "00";
			} else if (s.length() == 4) {
				if (s.equals("12am") || s.equals("12AM")) {
					time = "0000";
				} else {
					// eg. 10am
					time = s.substring(0, 2) + "00";
				}
			} else if (s.length() > 4) {
				if (s.length() == 6) {
					// eg. 0:45am, 1:30am, 8:45am
					String partFirst = s.substring(0, 1);
					String partSecond = s.substring(s.length() - 4,
							s.length() - 2);
					time = "0" + partFirst + partSecond;
				} else if (s.length() == 7) {
					String firstPart = s.substring(0, 2);
					if (firstPart.equals("12")) {
						// eg. 12:30am
						String secondPart = s.substring(s.length() - 4,
								s.length() - 2);
						time = "00" + secondPart;
					} else {
						// eg. 10:30am, 11:35am
						String secondPart = s.substring(s.length() - 4,
								s.length() - 2);
						time = firstPart + secondPart;
					}
				}
			}
		} else if (s.contains("pm") || s.contains("PM")) {
			if (s.length() == 3) {
				// eg. 9pm
				String strDigit = s.substring(0, 1);
				int value = Integer.parseInt(strDigit);
				value = value + 12;
				time = "" + value + "00";
			} else if (s.length() == 4) {
				if (s.equals("12pm") || s.equals("12PM")) {
					time = "1200";
				} else {
					// eg. 11pm
					String strDigit = s.substring(0, 2);
					int value = Integer.parseInt(strDigit);
					value = value + 12;
					time = "" + value + "00";
				}
			} else if (s.length() > 4) {
				if (s.length() == 6) {
					// 1:30pm, 9:40pm
					String partFirst = s.substring(0, 1);
					String partSecond = s.substring(s.length() - 4,
							s.length() - 2);
					int value = Integer.parseInt(partFirst);
					value = value + 12;
					time = "" + value + partSecond;
				} else if (s.length() == 7) {
					String firstPart = s.substring(0, 2);
					// eg. 12:35pm
					if (firstPart.equals("12")) {
						String secondPart = s.substring(s.length() - 4,
								s.length() - 2);
						time = firstPart + secondPart;
					} else {
						// eg. 11:30pm, 10:55pm
						String secondPart = s.substring(s.length() - 4,
								s.length() - 2);
						int value = Integer.parseInt(firstPart);
						value = value + 12;
						time = "" + value + secondPart;
					}
				}
			}

		}

		return time;

	}
	
	private static String getCurrentDate(){
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		Calendar cal = Calendar.getInstance();
		return dateFormat.format(cal.getTime()); 
	}
	
	private static String getDay(String format) {
		String day = format.substring(0, 2);
		return day;	
	}
	
	private static String getMonth(String format) {
		String month = format.substring(3, 5);
		return month;	
	}
	
	private static String getYear(String format) {
		String year = format.substring(6, 10);
		return year;	
	}
	
	private static String convertTodayAndTomorrow(String s){
		
		if(s.equalsIgnoreCase("today")){
			String date = getCurrentDate();
			s = date;
		}else if(s.equalsIgnoreCase("tomorrow")){
			String date = getCurrentDate();
			String dayStr = getDay(date);
			String monthStr = getMonth(date);
			String yearStr = getYear(date);
			
			int day = Integer.parseInt(dayStr);
			int month = Integer.parseInt(monthStr);
			int year = Integer.parseInt(yearStr);
			
			if(month == 2){
				if(year % 4 == 0){
					// today is 29/02/2012
					if(day + 1 > 29){
						month = 3;
						day = 1;
					}else{
						day = day + 1;
					}
				}else if(year % 4 != 0){
					if(day + 1 > 28){
						month = 3;
						day = 1;
					}else{
						day = day + 1;
					}
				}
			}else if(month == 1 || month == 3 || month == 5 || month == 7 
					|| month == 8 || month == 10){
				
				if(day + 1 > 31){
					month = month + 1;
					day = 1;
				}else{
					day = day + 1;
				}
				
			}else if(month == 4 || month == 6 || month == 9 || month == 11){
				
				if(day + 1 > 30){
					month = month + 1;
					day = 1;
				}else{
					day = day + 1;
				}
			}else if(month == 12){
				
				if(day + 1 > 31){
					year = year + 1;
					month = 1;
					day = 1;
				}else{
					day = day + 1;
				}
			}
			
			if(day < 10){
				dayStr = "0" + day;
			}else{
				dayStr = "" + day;
			}
			
			if(month < 10){
				monthStr = "0" + month;
			}else{
				monthStr = "" + month;
			}
			
			yearStr = "" + year;
			s = dayStr + "/" + monthStr + "/" + yearStr;
			
		}
		
		return s;
			
	}
	
	private static boolean hasTitleSpecifier(String content){
		if(content.contains("-t")){
			return true;
		}else{
			return false;
		}
	}
	
	private static String findTitleWithSpecifier(String content){
		String[] contentSplit = content.trim().split("\\s+");
		String title = "";
		ArrayList<String> contentSplitCopy = new ArrayList<String>();
		for(int i=0; i<contentSplit.length; i++){
			contentSplitCopy.add(contentSplit[i]);
		}
		
		int indexOfTitleSpecifier = contentSplitCopy.indexOf("-t");
		int nextIndex = indexOfTitleSpecifier + 1;
		while(indexOfTitleSpecifier + 1 < contentSplitCopy.size()){
			if((contentSplitCopy.get(nextIndex).equals("-st")==false)&&
					(contentSplitCopy.get(nextIndex).equals("-et")==false)&&
					(contentSplitCopy.get(nextIndex).equals("-sd")==false)&&
					(contentSplitCopy.get(nextIndex).equals("-ed")==false)){
				title += contentSplitCopy.get(nextIndex);
				title += " ";
				contentSplitCopy.remove(nextIndex);
			}else{
				break;
			}
		}
		
		title = title.trim();
		return title;			
	}
	
	private static String removeTitleWithSpecifier(String content){		
		String newContent = "";
		
		String[] contentSplit = content.trim().split("\\s+");
		ArrayList<String> contentSplitCopy = new ArrayList<String>();
		for(int i=0; i<contentSplit.length; i++){
			contentSplitCopy.add(contentSplit[i]);
		}
		
		if(contentSplitCopy.contains("-t")){
			int indexOfTitleSpecifier = contentSplitCopy.indexOf("-t");
			int nextIndex = indexOfTitleSpecifier + 1;
			while(indexOfTitleSpecifier + 1 < contentSplitCopy.size()){
				if((contentSplitCopy.get(nextIndex).equals("-st")==false)&&
						(contentSplitCopy.get(nextIndex).equals("-et")==false)&&
						(contentSplitCopy.get(nextIndex).equals("-sd")==false)&&
						(contentSplitCopy.get(nextIndex).equals("-ed")==false)){
					contentSplitCopy.remove(nextIndex);
				}else{
					break;
				}
			}
		}
						
		for(int j=0; j<contentSplitCopy.size(); j++){
			newContent += contentSplitCopy.get(j);
			newContent += " ";
		}
		
		newContent = newContent.trim();
		content = newContent;
		content = content.replace("-t", "");
		content = content.trim();
		return content;
	}
	
	private static boolean hasStartDateSpecifier(String content){
		if(content.contains("-sd")){
			return true;
		}else{
			return false;
		}
	}
	
	private static boolean hasEndDateSpecifier(String content){
		if(content.contains("-ed")){
			return true;
		}else{
			return false;
		}
	}

	private static String findStartDateWithSpecifier(String content){
		String[] contentSplit = content.trim().split("\\s+");
		String date = "";
		for(int i=0; i<contentSplit.length; i++){
			if(contentSplit[i].equalsIgnoreCase("-sd")){
				date = contentSplit[i+1];
				break;
			}
		}
		return date;
	}
	
	private static String findEndDateWithSpecifier(String content){
		String[] contentSplit = content.trim().split("\\s+");
		String date = "";
		for(int i=0; i<contentSplit.length; i++){
			if(contentSplit[i].equalsIgnoreCase("-ed")){
				date = contentSplit[i+1];
				break;
			}
		}
		return date;
	}
	
	private static String removeStartDate(String content){
		String date = findStartDateWithSpecifier(content);
		content = content.replace("-sd", "");
		content = content.replace(date, "");
		content = content.trim();
		return content;
	}
	
	private static String removeEndDate(String content){
		String date = findEndDateWithSpecifier(content);
		content = content.replace("-ed", "");
		content = content.replace(date, "");
		content = content.trim();
		return content;
	}
	
	private static boolean hasStartTimeSpecifier(String content){
		if(content.contains("-st")){
			return true;
		}else{
			return false;
		}	
	}
	
	
	private static boolean hasEndTimeSpecifier(String content){
		if(content.contains("-et")){
			return true;
		}else{
			return false;
		}			
	}
	
	private static String findStartTimeWithSpecifier(String content){
		String[] contentSplit = content.trim().split("\\s+");
		String startTime = "";
		for(int i=0; i<contentSplit.length; i++){
			if(contentSplit[i].equalsIgnoreCase("-st")){
				startTime = contentSplit[i+1];
				break;
			}
		}
		return startTime;
	}
	
	private static String findEndTimeWithSpecifier(String content){
		String[] contentSplit = content.trim().split("\\s+");
		String endTime = "";
		for(int i=0; i<contentSplit.length; i++){
			if(contentSplit[i].equalsIgnoreCase("-et")){
				endTime = contentSplit[i+1];
				break;
			}
		}
		return endTime;		
	}
	
	private static String removeStartTime(String content){
		String startTime = findStartTimeWithSpecifier(content);
		content = content.replace("-st", "");
		content = content.replace(startTime, "");
		content = content.trim();
		return content;
	}
	
	private static String removeEndTime(String content){
		String endTime = findEndTimeWithSpecifier(content);
		content = content.replace("-et", "");
		content = content.replace(endTime, "");
		content = content.trim();
		return content;
	}
	
}