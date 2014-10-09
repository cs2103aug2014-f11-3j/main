package taskbuddy.parser;

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
 *   
 *  2.Time must be this format: 0930, 1700, 2359
 * 	
 */

public class Parser {

	private static Scanner scanner = new Scanner(System.in);
	private static ArrayList<String> commandSplit = new ArrayList<String>();
	private static final int COMMAND_TYPE_INDEX = 0;
	private static final int DESCRIPTION_INDEX = 1;
	private static final int DATE_INDEX = 2;
	private static final int START_TIME_INDEX = 3; 
	private static final int END_TIME_INDEX = 4;
	private static final int TITLE_INDEX = 5;
	private static final String NULL_VALUE = "padding value";
	
	// use bundle instead of arraylist
	private Bundle userInputs = new Bundle();
	private String user_command = "command";
	private String user_description = "description";
	private String user_date = "date";
	private String user_start = "starttime";
	private String user_end = "endtime";
	private String user_title = "title";
	
	public static void main(String[] args) {
		while (true) {
			System.out.print("command:");
			String userCommand = scanner.nextLine();
			String commandType = getFirstWord(userCommand);
			commandSplit.add(COMMAND_TYPE_INDEX, commandType);
			if(commandType.equalsIgnoreCase("undo")||commandType.equalsIgnoreCase("redo")){
				
				commandSplit.add(DESCRIPTION_INDEX, NULL_VALUE);
				commandSplit.add(DATE_INDEX, NULL_VALUE);
				commandSplit.add(START_TIME_INDEX, NULL_VALUE);
				commandSplit.add(END_TIME_INDEX, NULL_VALUE);
				commandSplit.add(TITLE_INDEX, NULL_VALUE);
				
				//pass the arraylist to Logic
//				arrayOutput(commandSplit);   //for testing
				CommandParser commandParser = new CommandParser();
				commandParser.userInputs(commandSplit);
				
			}else if(commandType.equalsIgnoreCase("add")){
				
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
					
					commandSplit.add(DESCRIPTION_INDEX, description);
					commandSplit.add(DATE_INDEX, date);
					commandSplit.add(START_TIME_INDEX, startTime);
					commandSplit.add(END_TIME_INDEX, endTime);
					commandSplit.add(TITLE_INDEX, title);
					
					//pass the arraylist to Logic
//					arrayOutput(commandSplit);   //for testing
					CommandParser commandParser = new CommandParser();
					commandParser.userInputs(commandSplit);
													
				}else{
					showInvalidMessage();
				}
				
				
							
				
			}else if(commandType.equalsIgnoreCase("edit")){
				
				String contentToEdit = removeFirstWord(userCommand);
				if(hasTitleSpecifier(contentToEdit)){
					String title = findTitle(contentToEdit);
					contentToEdit = removeTitle(contentToEdit);
					String date = "";
					String startTime = "";
					String endTime = "";
					String description = "";
//					commandSplit.add(TITLE_INDEX, title);
					
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
					
					commandSplit.add(DESCRIPTION_INDEX, description);
					commandSplit.add(DATE_INDEX, date);
					commandSplit.add(START_TIME_INDEX, startTime);
					commandSplit.add(END_TIME_INDEX, endTime);
					commandSplit.add(TITLE_INDEX, title);
					
					//pass the arraylist to Logic
//					arrayOutput(commandSplit);   //for testing
					CommandParser commandParser = new CommandParser();
					commandParser.userInputs(commandSplit);
									
				}else{
					showInvalidMessage();
				}
				
				
				
				
			}else if(commandType.equalsIgnoreCase("display")){
				
				String contentToDisplay = removeFirstWord(userCommand);
				if(contentToDisplay.isEmpty()){
					commandSplit.add(DESCRIPTION_INDEX, NULL_VALUE);
					commandSplit.add(DATE_INDEX, NULL_VALUE);
					commandSplit.add(START_TIME_INDEX, NULL_VALUE);
					commandSplit.add(END_TIME_INDEX, NULL_VALUE);
					commandSplit.add(TITLE_INDEX, NULL_VALUE);
					
					//pass the arraylist to Logic
//					arrayOutput(commandSplit);   //for testing
					CommandParser commandParser = new CommandParser();
					commandParser.userInputs(commandSplit);
					
				}else if(contentToDisplay.equalsIgnoreCase("all")){
					commandSplit.add(DESCRIPTION_INDEX, "all");
					commandSplit.add(DATE_INDEX, NULL_VALUE);
					commandSplit.add(START_TIME_INDEX, NULL_VALUE);
					commandSplit.add(END_TIME_INDEX, NULL_VALUE);
					commandSplit.add(TITLE_INDEX, NULL_VALUE);
					
					//pass the arraylist to Logic
//					arrayOutput(commandSplit);   //for testing
					CommandParser commandParser = new CommandParser();
					commandParser.userInputs(commandSplit);
					
				}else if(hasDateSpecifier(contentToDisplay)){
					commandSplit.add(DESCRIPTION_INDEX, NULL_VALUE);
					commandSplit.add(DATE_INDEX, removeFirstWord(contentToDisplay));	
					commandSplit.add(START_TIME_INDEX, NULL_VALUE);
					commandSplit.add(END_TIME_INDEX,NULL_VALUE);
					commandSplit.add(TITLE_INDEX, NULL_VALUE);
					
					//pass the arraylist to Logic
//					arrayOutput(commandSplit);   //for testing
					CommandParser commandParser = new CommandParser();
					commandParser.userInputs(commandSplit);
					
				}else{
					showInvalidMessage();
				}
				
				
							
			}else if(commandType.equalsIgnoreCase("delete")){
				
				String contentToDelete = removeFirstWord(userCommand);
				if(hasTitleSpecifier(contentToDelete)){			
					commandSplit.add(DESCRIPTION_INDEX, NULL_VALUE);
					commandSplit.add(DATE_INDEX, NULL_VALUE);
					commandSplit.add(START_TIME_INDEX, NULL_VALUE);
					commandSplit.add(END_TIME_INDEX, NULL_VALUE);
					commandSplit.add(TITLE_INDEX, removeFirstWord(contentToDelete));
					
					//pass the arraylist to Logic
//					arrayOutput(commandSplit);   //for testing
					CommandParser commandParser = new CommandParser();
					commandParser.userInputs(commandSplit);
					
				}else{
					showInvalidMessage();
				}			
				
				
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
	
	// print out for testing
	private static void arrayOutput(ArrayList<String> splitString){
		System.out.println("Command type is "+splitString.get(0));
		System.out.println("Description is "+splitString.get(1));
		System.out.println("Date is "+splitString.get(2));
		System.out.println("Start time is "+splitString.get(3));
		System.out.println("End time is "+splitString.get(4));
		System.out.println("Title is "+splitString.get(5));
	}
	
	
}
