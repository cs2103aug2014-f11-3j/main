package taskbuddy.parser;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Scanner;

import taskbuddy.logic.Bundle;
import taskbuddy.logic.CommandParser;

/*
 *  User Manual:
 *  1.Specifier must be lower case -d, -s, -e, -t
 *  	-d: date
 *  	-s: start time
 *  	-e: end time 
 *  	-t: title
 *   dd/mm/yyyy
 *  2.Time must be this format: 0930, 1700, 2359
 * 	
 */

public class Parser {

	
	private static Scanner scanner = new Scanner(System.in);
	private static final String NULL_VALUE = "padding value";
	
	private static Bundle userInputs = new Bundle();
	private static String user_command = "command";
	private static String user_description = "description";
	private static String user_endDate = "endDate";
	private static String user_start = "startTime";
	private static String user_endTime = "endTime";
	private static String user_title = "title";
	
	
	public static void main(String[] args) throws ParseException, IOException {
		
		CommandParser commandParser = new CommandParser();
		
		
		while (true) {
			System.out.print("command:");
			String userCommand = scanner.nextLine();
			String commandType = getFirstWord(userCommand);
			
			userInputs.putString(user_command, commandType);
			
			if(isUndoType(commandType)){
				undoDataPadding(userInputs);
				commandParser.parseUserInputs(userInputs);
			
			}else if(isRedoType(commandType)){
				redoDataPadding(userInputs);
				commandParser.parseUserInputs(userInputs);
			
			}else if(isAddType(commandType)){
				addDataPadding(userInputs, userCommand);
				commandParser.parseUserInputs(userInputs);
			
			}else if(isEditType(commandType)){
				editDataPadding(userInputs, userCommand);
				commandParser.parseUserInputs(userInputs);
			
			}else if(isDisplayType(commandType)){
				displayDataPadding(userInputs, userCommand);
				commandParser.parseUserInputs(userInputs);
			
			}else if(isDeleteType(commandType)){
				deleteDataPadding(userInputs, userCommand);
				commandParser.parseUserInputs(userInputs);
			
			}else{
				showInvalidMessage();
			}		
			
		}
		
		
		
	}
	
	
	private static String getFirstWord(String userCommand) {
		String commandTypeString = userCommand.trim().split("\\s+")[0];
		return commandTypeString;
	}

	private static String removeFirstWord(String userCommand) {
		String result = userCommand.replace(getFirstWord(userCommand), "").trim();	
		return result;
	}
	
	private static void showInvalidMessage(){
		System.out.println("Invalid Command Format");
	}
	
	
	private static boolean isUndoType(String commandType){
		if(commandType.equalsIgnoreCase("undo")){
			return true;
		}else{
			return false;
		}
	}
	
	private static boolean isRedoType(String commandType){
		if(commandType.equalsIgnoreCase("redo")){
			return true;
		}else{
			return false;
		}
	}
	
	private static boolean isAddType(String commandType){
		if(commandType.equalsIgnoreCase("add")){
			return true;
		}else{
			return false;
		}
	}
	
	private static boolean isEditType(String commandType){
		if(commandType.equalsIgnoreCase("edit")){
			return true;
		}else{
			return false;
		}
	}
	
	
	private static boolean isDisplayType(String commandType){
		if(commandType.equalsIgnoreCase("display")){
			return true;
		}else{
			return false;
		}
	}
	
	private static boolean isDeleteType(String commandType){
		if(commandType.equalsIgnoreCase("delete")){
			return true;
		}else{
			return false;
		}
	}
	
	private static void undoDataPadding(Bundle b){
		b.putString(user_description, NULL_VALUE);
		b.putString(user_endDate, NULL_VALUE);
		b.putString(user_start, NULL_VALUE);
		b.putString(user_endTime, NULL_VALUE);
		b.putString(user_title, NULL_VALUE);
	}
	
	private static void redoDataPadding(Bundle b){
		b.putString(user_description, NULL_VALUE);
		b.putString(user_endDate, NULL_VALUE);
		b.putString(user_start, NULL_VALUE);
		b.putString(user_endTime, NULL_VALUE);
		b.putString(user_title, NULL_VALUE);
	}
	
	private static void addDataPadding(Bundle b, String userCommand){
		String contentToAdd = removeFirstWord(userCommand);
		if(hasTitleSpecifier(contentToAdd)){
			String title = findTitle(contentToAdd);
			contentToAdd = removeTitle(contentToAdd);
			String date = "";
			String startTime = "";
			String endTime = "";
			String description = "";
			
			if(hasDateSpecifier(contentToAdd)){
				date = findDate(contentToAdd);
				contentToAdd = removeDate(contentToAdd);
				
			}else{
				date = NULL_VALUE;
			}
			
			if(hasStartTimeSpecifier(contentToAdd)){
				startTime = findStartTime(contentToAdd);
				contentToAdd = removeStartTime(contentToAdd);
				
			}else{
				startTime = NULL_VALUE;
			}
			
			if(hasEndTimeSpecifier(contentToAdd)){
				endTime = findEndTime(contentToAdd);
				contentToAdd = removeEndTime(contentToAdd);
			}else{
				endTime = NULL_VALUE;
			}
			
			if(contentToAdd.isEmpty() == false){
				description = contentToAdd.trim();
			}else{
				description = NULL_VALUE;
			}
			
			b.putString(user_description, description);
			//b.putString(user_description, "abc");
			b.putString(user_endDate, date);
			b.putString(user_start, startTime);
			b.putString(user_endTime, endTime);
			b.putString(user_title, title);
											
		}else{
			showInvalidMessage();
		}
	}
	
	
	private static void editDataPadding(Bundle b, String userCommand){
		String contentToEdit = removeFirstWord(userCommand);
		if(hasTitleSpecifier(contentToEdit)){
			String title = findTitle(contentToEdit);
			contentToEdit = removeTitle(contentToEdit);
			String date = "";
			String startTime = "";
			String endTime = "";
			String description = "";
			
			if(hasDateSpecifier(contentToEdit)){
				date = findDate(contentToEdit);
				contentToEdit = removeDate(contentToEdit);
			}else{
				date = NULL_VALUE;
			}
			
			if(hasStartTimeSpecifier(contentToEdit)){
				startTime = findStartTime(contentToEdit);
				contentToEdit = removeStartTime(contentToEdit);
			}else{
				startTime = NULL_VALUE;
			}
			
			if(hasEndTimeSpecifier(contentToEdit)){
				endTime = findEndTime(contentToEdit);
				contentToEdit = removeEndTime(contentToEdit);
			}else{
				endTime = NULL_VALUE;
			}
			
			if(contentToEdit.isEmpty() == false){
				description = contentToEdit.trim();
			}else{
				description = NULL_VALUE;
			}			
			
			b.putString(user_description, description);
			b.putString(user_endDate, date);
			b.putString(user_start, startTime);
			b.putString(user_endTime, endTime);
			b.putString(user_title, title);
									
		}else{
			showInvalidMessage();
		}
				
	}
	
	private static void displayDataPadding(Bundle b, String userCommand){
		String contentToDisplay = removeFirstWord(userCommand);
		if(contentToDisplay.isEmpty()){
			b.putString(user_description, NULL_VALUE);
			b.putString(user_endDate, NULL_VALUE);
			b.putString(user_start, NULL_VALUE);
			b.putString(user_endTime, NULL_VALUE);
			b.putString(user_title, NULL_VALUE);
			
		}else if(contentToDisplay.equalsIgnoreCase("all")){
			b.putString(user_description, "all");
			b.putString(user_endDate, NULL_VALUE);
			b.putString(user_start, NULL_VALUE);
			b.putString(user_endTime, NULL_VALUE);
			b.putString(user_title, NULL_VALUE);
					
		}else if(hasDateSpecifier(contentToDisplay)){
			b.putString(user_description, NULL_VALUE);
			b.putString(user_endDate, removeFirstWord(contentToDisplay));
			b.putString(user_start, NULL_VALUE);
			b.putString(user_endTime, NULL_VALUE);
			b.putString(user_title, NULL_VALUE);
			
		}else{
			showInvalidMessage();
		}
		
	}
	
	private static void deleteDataPadding(Bundle b, String userCommand){
		String contentToDelete = removeFirstWord(userCommand);
		if(hasTitleSpecifier(contentToDelete)){			
			b.putString(user_description, NULL_VALUE);
			b.putString(user_endDate, NULL_VALUE);
			b.putString(user_start, NULL_VALUE);
			b.putString(user_endTime, NULL_VALUE);
			b.putString(user_title, removeFirstWord(contentToDelete));
			
		}else{
			showInvalidMessage();
		}			
			
	}
	
	
	
	private static boolean hasTitleSpecifier(String content){
		if(content.contains("-t")){
			return true;
		}else{
			return false;
		}
	}
		
	
	private static boolean hasDateSpecifier(String content){
		if(content.contains("-d")){
			return true;
		}else{
			return false;
		}
	}
	
	
	private static boolean hasStartTimeSpecifier(String content){
		if(content.contains("-s")){
			return true;
		}else{
			return false;
		}	
	}
	
	
	private static boolean hasEndTimeSpecifier(String content){
		if(content.contains("-e")){
			return true;
		}else{
			return false;
		}			
	}
	
	private static String findDate(String content){
		String[] contentSplit = content.trim().split("\\s+");
		String date = "";
		for(int i=0; i<contentSplit.length; i++){
			if(contentSplit[i].equalsIgnoreCase("-d")){
				date = contentSplit[i+1];
				break;
			}
		}
		return date;
	}
	
	
	private static String findStartTime(String content){
		String[] contentSplit = content.trim().split("\\s+");
		String startTime = "";
		for(int i=0; i<contentSplit.length; i++){
			if(contentSplit[i].equalsIgnoreCase("-s")){
				startTime = contentSplit[i+1];
				break;
			}
		}
		return startTime;
	}
	
	
	private static String findEndTime(String content){
		String[] contentSplit = content.trim().split("\\s+");
		String endTime = "";
		for(int i=0; i<contentSplit.length; i++){
			if(contentSplit[i].equalsIgnoreCase("-e")){
				endTime = contentSplit[i+1];
				break;
			}
		}
		return endTime;		
	}
	
	
	private static String findTitle(String content){
		String[] contentSplit = content.trim().split("\\s+");
		String title = "";
		ArrayList<String> contentSplitCopy = new ArrayList<String>();
		for(int i=0; i<contentSplit.length; i++){
			contentSplitCopy.add(contentSplit[i]);
		}
		
		int indexOfTitleSpecifier = contentSplitCopy.indexOf("-t");
		int nextIndex = indexOfTitleSpecifier + 1;
		while(indexOfTitleSpecifier + 1 < contentSplitCopy.size()){
			if((contentSplitCopy.get(nextIndex).equals("-s")==false)&&
					(contentSplitCopy.get(nextIndex).equals("-e")==false)&&
					(contentSplitCopy.get(nextIndex).equals("-d")==false)){
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
	
	private static String removeDate(String content){
		String date = findDate(content);
		content = content.replace("-d", "");
		content = content.replace(date, "");
		content = content.trim();
		return content;
	}
	
	
	private static String removeStartTime(String content){
		String startTime = findStartTime(content);
		content = content.replace("-s", "");
		content = content.replace(startTime, "");
		content = content.trim();
		return content;
	}
	
	
	private static String removeEndTime(String content){
		String endTime = findEndTime(content);
		content = content.replace("-e", "");
		content = content.replace(endTime, "");
		content = content.trim();
		return content;
	}
	
	private static String removeTitle(String content){		
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
				if((contentSplitCopy.get(nextIndex).equals("-s")==false)&&
						(contentSplitCopy.get(nextIndex).equals("-e")==false)&&
						(contentSplitCopy.get(nextIndex).equals("-d")==false)){
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
	
//	private static void bundleOutput(Bundle b){
//		System.out.println("Command type is "+ (String)b.getItem(user_command));
//		System.out.println("Description is "+ (String)b.getItem(user_description));
//		System.out.println("Date is "+ (String)b.getItem(user_endDate));
//		System.out.println("Start time is "+ (String)b.getItem(user_start));
//		System.out.println("End time is "+ (String)b.getItem(user_endTime));
//		System.out.println("Title is "+ (String)b.getItem(user_title));
//	}
	
	
	
}
