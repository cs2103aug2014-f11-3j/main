package taskbuddy.logic;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

//Author: andrew
public class Task {

    // @formatter:off
    private static final String EMPTY_STRING        = "";
    private static final String DELIMITER           = " | ";
    private static final String TASK_ID             = "Task ID: ";
    private static final String TITLE               = "Title: ";
    private static final String DESCRIPTION         = "Description: ";
    private static final String START               = "Start: ";
    private static final String END                 = "End: ";
    private static final String PRIORITY            = "Priority: ";
    private static final String IS_COMPLETE         = "Completed: ";
    private static final String IS_FLOATING         = "Floating task: ";
    private static final String GOOGLE_CALENDAR_ID  = "Google Calendar ID: ";
    // @formatter:on

    private static String nullValue = "padding value";
    private StringProperty __title;
    private StringProperty __description;
    private ObjectProperty<Calendar> __startTime;
    private ObjectProperty<Calendar> __endTime;
    private IntegerProperty __priorityFlag;
    private BooleanProperty __completionFlag;
    private BooleanProperty __floatingTask;
    private String __googleID;
    private IntegerProperty __taskId;

    // bundle strings
    private String user_description = "description";
    private String user_endDate = "endDate";
    private String user_start = "startTime";
    private String user_endTime = "endTime";
    private String user_title = "title";
    private String user_flag = "flag";
    private String user_priority = "priority";
    private String user_googleID = "GoogleID";

    public SimpleDateFormat formatter;
    public static final String DATABASE_DATE_TIME_FORMATTER = "d-M-yyyy 'at' "
            + "HH:mm";

    // basic constructors
    public Task() {
    }

    public Task(String title) {
        this.__title = new SimpleStringProperty(title);
        this.__completionFlag = new SimpleBooleanProperty(false);
    }

    // basic accessors
    public String getTitle() {
        return this.__title.get();
    }

    public StringProperty titleProperty(){
    	return this.__title;
    }
    
    public String getDescription() {
        return this.__description.get();
    }
    
    public StringProperty descriptionProperty(){
    	return this.__description;
    }

    public Calendar getStartTime() {
        return this.__startTime.get();
    }

    public Calendar getEndTime() {
        return this.__endTime.get();
    }
    
    public StringProperty startDateProperty(){
    	Calendar start = this.getStartTime();
    	StringProperty startDate = new SimpleStringProperty(parseDate(start));
    	return startDate;
    }
    
    public StringProperty dueDateProperty(){
    	Calendar due = this.getEndTime();
    	StringProperty dueDate = new SimpleStringProperty(parseDate(due));
    	return dueDate;
    }
    
    private String parseDate(Calendar cal){
		String s = cal.getTime().toString();
		String[] calInfo = s.split(" ");
		String time = calInfo[3].substring(0,5);
		String toReturn = calInfo[0] + " " + calInfo[1] + " " + calInfo[2] + ", " + time;
		return toReturn;
	}

    public int getPriority() {
        return this.__priorityFlag.get();
    }

    public boolean getCompletionStatus() {
        return this.__completionFlag.get();
    }

    public boolean isFloatingTask() {
        return this.__floatingTask.get();
    }

    public String getGID() {
        return this.__googleID;
    }

    /**
     * Gets the task ID, which is the unique identifier of a task. The task ID
     * is used for task manipulation such as task addition and deletion. For
     * example <code>delete(int taskId):void</code> deletes a task given a
     * specified, valid task ID.
     * 
     * @return the task ID
     * 
     * @author Soh Yong Sheng
     * 
     */
    public int getTaskId() {
        return __taskId.get();
    }
    
    public IntegerProperty idProperty(){
    	return this.__taskId;
    }

    // basic mutators
    public void setTitle(String nextTitle) {
        this.__title = new SimpleStringProperty(nextTitle);
    }

    public void setGID(String newID) {
        this.__googleID = newID;
    }

    public void setDescription(String nextDescription) {
        if (!nextDescription.equals(nullValue)) {
            this.__description = new SimpleStringProperty(nextDescription);
        } else {
            this.__description = new SimpleStringProperty("nil");
        }
    }

    public void setStartTime(Calendar nextStart) {
        this.__startTime = new SimpleObjectProperty<Calendar>(nextStart);
    }

    public void setStartTime(String startDate, String startTime) {

        if (!startDate.equals(nullValue) && !startTime.equals(nullValue)) {
            int date = Integer.parseInt(startDate.substring(0, 2));
            int month = Integer.parseInt(startDate.substring(3, 5));
            month--;
            int year = Integer.parseInt(startDate.substring(6));
            int hour = Integer.parseInt(startTime.substring(0, 2));
            int minute = Integer.parseInt(startTime.substring(2));
            Calendar start = Calendar.getInstance();
            start.set(year, month, date, hour, minute);
            this.__startTime = new SimpleObjectProperty<Calendar>(start);
        } else if (!startDate.equals(nullValue)) {
            int date = Integer.parseInt(startDate.substring(0, 2));
            int month = Integer.parseInt(startDate.substring(3, 5));
            month--;
            int year = Integer.parseInt(startDate.substring(6));
            Calendar start = Calendar.getInstance();
            start.set(year, month, date);
            this.__startTime = new SimpleObjectProperty<Calendar>(start);

        } else if (!startTime.equals(nullValue)) {
            Calendar start = Calendar.getInstance();
            int year = start.get(Calendar.YEAR);
            int month = start.get(Calendar.MONTH);
            int date = start.get(Calendar.DATE);
            int hour = Integer.parseInt(startTime.substring(0, 2));
            int minute = Integer.parseInt(startTime.substring(2));
            start.set(year, month, date, hour, minute);
            this.__startTime = new SimpleObjectProperty<Calendar>(start);

        } else {
            Calendar now = Calendar.getInstance();
            this.__startTime = new SimpleObjectProperty<Calendar>(now);
        }
    }

    public void setEndTime(Calendar nextEnd) {
        this.__endTime = new SimpleObjectProperty<Calendar>(nextEnd);
    }

    public void setEndTime(String endDate, String endTime) {
        if (!endDate.equals(nullValue) && !endTime.equals(nullValue)) {
            int date = Integer.parseInt(endDate.substring(0, 2));
            int month = Integer.parseInt(endDate.substring(3, 5));
            month--;
            int year = Integer.parseInt(endDate.substring(6));
            int hour = Integer.parseInt(endTime.substring(0, 2));
            int minute = Integer.parseInt(endTime.substring(2));
            Calendar ending = Calendar.getInstance();
            ending.set(year, month, date, hour, minute);
            this.__endTime = new SimpleObjectProperty<Calendar>(ending);
        } else if (!endDate.equals(nullValue)) {
            int date = Integer.parseInt(endDate.substring(0, 2));
            int month = Integer.parseInt(endDate.substring(3, 5));
            month--;
            int year = Integer.parseInt(endDate.substring(6));
            int hour = 23;
            int minute = 59;
            Calendar ending = Calendar.getInstance();
            ending.set(year, month, date, hour, minute);
            this.__endTime = new SimpleObjectProperty<Calendar>(ending);

        } else if (!endTime.equals(nullValue)) {
            Calendar ending = Calendar.getInstance();
            int year = ending.get(Calendar.YEAR);
            int month = ending.get(Calendar.MONTH);
            int date = ending.get(Calendar.DATE);
            int hour = Integer.parseInt(endTime.substring(0, 2));
            int minute = Integer.parseInt(endTime.substring(2));
            ending.set(year, month, date, hour, minute);
            this.__endTime = new SimpleObjectProperty<Calendar>(ending);

        } else {
            Calendar ending = Calendar.getInstance();
            // todo stub
            // find appropriate setting for null value
            this.__endTime = new SimpleObjectProperty<Calendar>(ending);
        }
    }

    public void setPriority(int nextPriority) {
        this.__priorityFlag = new SimpleIntegerProperty(nextPriority);
    }

    public void setCompletion(boolean nextStatus) {
        this.__completionFlag = new SimpleBooleanProperty(nextStatus);
    }

    public void setFloating(boolean nextFloatStat) {
        this.__floatingTask = new SimpleBooleanProperty(nextFloatStat);
    }

    /**
     * Sets a unique task ID for every task. Unique task ID is used for task
     * manipulation.
     * 
     * @param __taskId
     *            task ID
     * 
     * @author Soh Yong Sheng
     * 
     */
    public void setTaskId(int __taskId) {
        this.__taskId = new SimpleIntegerProperty(__taskId);
    }

    public void checkFloating(Task task) {
        if (task.getEndTime() == null) {
            task.setFloating(true);
        } else {
            task.setFloating(false);
        }
    }

    // other class methods
    public Bundle getTaskInfo() {
        Bundle toDisplay = new Bundle();
        toDisplay.putString(user_title, this.getTitle());
        toDisplay.putObject(user_flag, this.getCompletionStatus());
        toDisplay.putString(user_endTime, this.getEndTime().toString());
        toDisplay.putString(user_description, this.getDescription());
        toDisplay.putString(user_start, this.getStartTime().toString());
        toDisplay.putString(user_priority, Integer.toString(this.getPriority()));
        toDisplay.putString(user_googleID, this.__googleID);
        return toDisplay;
    }

    /**
     * Returns a string showing the date and time of the argument
     * <code>Calendar</code> object. For example, the date returned may be
     * "1-1-1 at 1:1", as opposed to having leading zeroes like 01-01-0001 at
     * 01:01.
     * 
     * @param cal
     *            the <code>Calendar</code> object to be converted into a string
     * @return a string showing the date and time of the argument
     *         <code>Calendar</code> object.
     */
    public String displayDateTime(Calendar cal) {
        Date date = cal.getTime();

        // Do not change this formatter - this is for Database's log file. You
        // need to at least tell me how you've changed it if you want to change
        // it, or write your method for your own formatter.
        formatter = new SimpleDateFormat(DATABASE_DATE_TIME_FORMATTER);
        return formatter.format(date);
    }

    /**
     * Returns a string showing the date of the argument <code>Calendar</code>
     * object. For example, the date returned may be "1-1-1", as opposed to
     * having leading zeroes like 01-01-0001 at 01:01.
     * 
     * @param cal
     *            the <code>Calendar</code> object to be converted into a string
     * @return a string showing the date and time of the argument
     *         <code>Calendar</code> object.
     * 
     * @author Pee Choon Hian
     */
    public String displayDate(Calendar cal) {
        Date date = cal.getTime();

        // Do not change this formatter - this is for GoogleCalendarManager. You
        // need to at least tell me how you've changed it if you want to change
        // it, or write your method for your own formatter.
        formatter = new SimpleDateFormat("d/M/yyyy");
        return formatter.format(date);
    }

    /**
     * Returns a string showing the time of the argument <code>Calendar</code>
     * object.
     * 
     * @param cal
     *            the <code>Calendar</code> object to be converted into a string
     * @return a string showing the date and time of the argument
     *         <code>Calendar</code> object.
     * 
     * @author Pee Choon Hian
     */
    public String displayTime(Calendar cal) {
        Date date = cal.getTime();

        // Do not change this formatter - this is for GoogleCalendarManager. You
        // need to at least tell me how you've changed it if you want to change
        // it, or write your method for your own formatter.
        formatter = new SimpleDateFormat("HH:mm");
        return formatter.format(date);
    }

    /**
     * @return a list of all string-ified fields of this task
     * 
     * @author Soh Yong Sheng
     * 
     */
    public ArrayList<String> taskFields() {
        ArrayList<String> taskFields = new ArrayList<String>();

        // Do not change this string conversion format - this is for Database's
        // log file. You need to at least tell me how you've changed it if you
        // want to change it, or write your method for your own formatter.
        taskFields.add(this.displayTaskId());
        taskFields.add(this.displayTitle());
        taskFields.add(this.displayDescription());
        taskFields.add(this.displayStart());
        taskFields.add(this.displayEnd());
        taskFields.add(this.displayPriority());
        taskFields.add(this.displayIsComplete());
        taskFields.add(this.displayIsFloating());
        taskFields.add(this.displayGoogleId());

        return taskFields;
    }

    /**
     * Converts a <code>Task</code> object and its attributes to a string for
     * logging into a text file.
     * 
     * @return a string containing all the information of a <code>Task</code>
     *         object.
     * 
     * @author Soh Yong Sheng
     * 
     */
    public String displayTask() {

        String result = EMPTY_STRING;
        for (String aField : this.taskFields()) {
            int lastFieldIndex = this.taskFields().size() - 1;
            if (this.taskFields().indexOf(aField) == lastFieldIndex) {
                result = result + aField;
            } else {
                result = result + aField + DELIMITER;
            }
        }
        return result;
    }

    /**
     * @return Google ID of this task as a string
     * 
     * @author Soh Yong Sheng
     * 
     */
    public String displayGoogleId() {
        return GOOGLE_CALENDAR_ID + this.getGID();
    }

    /**
     * @return "true" if this task is floating, "false" otherwise
     * 
     * @author Soh Yong Sheng
     * 
     */
    public String displayIsFloating() {
        return IS_FLOATING + Boolean.toString(this.isFloatingTask());
    }

    /**
     * @return "true" if this task is completed, "false" otherwise
     * 
     * @author Soh Yong Sheng
     * 
     */
    public String displayIsComplete() {
        return IS_COMPLETE + Boolean.toString(this.getCompletionStatus());
    }

    /**
     * @return a non-zero integer ranking the priority of this task
     * 
     * @author Soh Yong Sheng
     * 
     */
    public String displayPriority() {
        return PRIORITY + Integer.toString(this.getPriority());
    }

    /**
     * @return the end date and time of this task
     * 
     * @author Soh Yong Sheng
     * 
     */
    public String displayEnd() {
        String displayEnd = END + this.displayDateTime(this.getEndTime());
        return displayEnd;
    }

    /**
     * @return the end date of this task. This method is used by
     *         GooglCalendarManager, please do not change.
     * 
     * @author Pee Choon Hian
     * 
     */
    public String displayEndDate() {
        return this.displayDate(this.getEndTime());
    }

    /**
     * @return the end time of this task. This method is used by
     *         GooglCalendarManager, please do not change.
     * 
     * @author Pee Choon Hian
     * 
     */
    public String displayEndTime() {
        return this.displayTime(this.getEndTime());
    }

    /**
     * @return the start date and time of this task
     * 
     * @author Soh Yong Sheng
     * 
     */
    public String displayStart() {
        return START + this.displayDateTime(this.getStartTime());
    }

    /**
     * @return the start date of this task. This method is used by
     *         GooglCalendarManager, please do not change.
     * 
     * @author Pee Choon Hian
     * 
     */
    public String displayStartDate() {
        return this.displayDate(this.getStartTime());
    }

    /**
     * @return the start time of this task. This method is used by
     *         GooglCalendarManager, please do not change.
     * 
     * @author Pee Choon Hian
     * 
     */
    public String displayStartTime() {
        return this.displayTime(this.getStartTime());
    }

    /**
     * @return the description of this task
     * 
     * @author Soh Yong Sheng
     * 
     */
    public String displayDescription() {
        return DESCRIPTION + this.__description;
    }

    /**
     * @return the title of this task
     * 
     * @author Soh Yong Sheng
     * 
     */
    public String displayTitle() {
        return TITLE + this.__title;
    }

    /**
     * @return the task ID
     * 
     * @author Soh Yong Sheng
     * 
     */
    public String displayTaskId() {
        return TASK_ID + this.__taskId;
    }
}
