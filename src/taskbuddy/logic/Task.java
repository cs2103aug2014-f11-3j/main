package taskbuddy.logic;

import java.util.ArrayList;
import java.util.Calendar;

//Author: andrew
public class Task {
	private String __title;
	private String __description;
	private Calendar __startTime;
	private Calendar __endTime;
	private int __priorityFlag;
	private boolean __completionFlag;
	private boolean __floatingTask;
	
	//basic constructors
	public Task(){}
	
	public Task(String title){
		this.__title = title;
		this.__completionFlag = false;
	}
	
	//basic accessors
	public String getTitle(){
		return this.__title;
	}
	
	public String getDescription(){
		return this.__description;
	}
	
	public Calendar getStartTime(){
		return this.__startTime;
	}
	
	public Calendar getEndTime(){
		return this.__endTime;
	}
	
	public int getPriority(){
		return this.__priorityFlag;
	}
	
	public boolean getCompletionStatus(){
		return this.__completionFlag;
	}
	
	public boolean isFloatingTask(){
		return this.__floatingTask;
	}
	
	//basic mutators
	public void setTitle(String nextTitle){
		this.__title = nextTitle;
	}
	
	public void setDescription(String nextDescription){
		this.__description = nextDescription;
	}
	
	public void setStartTime(Calendar nextStart){
		this.__startTime = nextStart;
	}
	
	public void setEndTime(Calendar nextEnd){
		this.__endTime = nextEnd;
	}
	
	public void setEndTime(String endDate, String endTime){
		int date = Integer.parseInt(endDate.substring(0, 2));
		int month = Integer.parseInt(endDate.substring(2, 4));
		int year = Integer.parseInt(endDate.substring(4));
		int hour = Integer.parseInt(endTime.substring(0, 2));
		int minute = Integer.parseInt(endTime.substring(2));
		Calendar ending = Calendar.getInstance();
		ending.set(year, month, date, hour, minute);
		this.__endTime = ending;
	}
	
	public void setEndTime(String endDate){
		int date = Integer.parseInt(endDate.substring(0, 2));
		int month = Integer.parseInt(endDate.substring(2, 4));
		int year = Integer.parseInt(endDate.substring(4));
		Calendar ending = Calendar.getInstance();
		ending.set(year, month, date);
		this.__endTime = ending;
	}
	
	public void setPriority(int nextPriority){
		this.__priorityFlag = nextPriority;
	}
	
	public void setCompletion(boolean nextStatus){
		this.__completionFlag = nextStatus;
	}
	
	public void setFloating(boolean nextFloatStat){
		this.__floatingTask = nextFloatStat;
	}
	
	public void checkFloating(Task task){
		if (task.getEndTime() == null){
			task.setFloating(true);
		} else {
			task.setFloating(false);
		}
	}
	
	//other class methods
	public ArrayList<String> getTaskInfo(){
		ArrayList<String> toDisplay = new ArrayList<String>();
		toDisplay.add(__title);
		toDisplay.add(__description);
		toDisplay.add(__startTime.toString());
		toDisplay.add(__endTime.toString());
		toDisplay.add(Integer.toString(__priorityFlag));
		toDisplay.add(Boolean.toString(__completionFlag));
		return toDisplay;
	}
}

