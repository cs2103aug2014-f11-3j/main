package taskbuddy.logic;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;

//Author: andrew
public class Task {

    // @formatter:off
    private static final String DELIMITER           = " | ";
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
    private String __title;
    private String __description;
    private Calendar __startTime;
    private Calendar __endTime;
    private int __priorityFlag;
    private boolean __completionFlag;
    private boolean __floatingTask;
    private String __googleID;

    //bundle strings
    private String user_description = "description";
	private String user_endDate = "endDate";
	private String user_start = "startTime";
	private String user_endTime = "endTime";
	private String user_title = "title";
    private String user_flag = "flag";
    private String user_priority = "priority";
    private String user_googleID = "GoogleID";
	
    public static SimpleDateFormat formatter;
    
    // basic constructors
    public Task() {
    }

    public Task(String title) {
        this.__title = title;
        this.__completionFlag = false;
    }

    // basic accessors
    public String getTitle() {
        return this.__title;
    }

    public String getDescription() {
        return this.__description;
    }

    public Calendar getStartTime() {
        return this.__startTime;
    }

    public Calendar getEndTime() {
        return this.__endTime;
    }

    public int getPriority() {
        return this.__priorityFlag;
    }

    public boolean getCompletionStatus() {
        return this.__completionFlag;
    }

    public boolean isFloatingTask() {
        return this.__floatingTask;
    }

    public String getGID() {
        return this.__googleID;
    }

    // basic mutators
    public void setTitle(String nextTitle) {
        this.__title = nextTitle;
    }

    public void setGID(String newID) {
        this.__googleID = newID;
    }

    public void setDescription(String nextDescription) {
        if (!nextDescription.equals(nullValue)) {
            this.__description = nextDescription;
        } else {
            this.__description = "nil";
        }
    }

    public void setStartTime(String nextStart) {
        if (!nextStart.equals(nullValue)) {
            // this.__startTime = nextStart;
            // todo stub
            // parse String into Calendar
        } else {
            Calendar now = Calendar.getInstance();
            this.__startTime = now;
        }
    }

    public void setEndTime(Calendar nextEnd) {
        this.__endTime = nextEnd;
    }

    public void setEndTime(String endDate, String endTime) {
        if (!endDate.equals(nullValue) && !endTime.equals(nullValue)) {
            int date = Integer.parseInt(endDate.substring(0, 2));
            int month = Integer.parseInt(endDate.substring(2, 4));
            int year = Integer.parseInt(endDate.substring(4));
            int hour = Integer.parseInt(endTime.substring(0, 2));
            int minute = Integer.parseInt(endTime.substring(2));
            Calendar ending = Calendar.getInstance();
            ending.set(year, month, date, hour, minute);
            this.__endTime = ending;
        } else if (!endDate.equals(nullValue)) {
            int date = Integer.parseInt(endDate.substring(0, 2));
            int month = Integer.parseInt(endDate.substring(2, 4));
            int year = Integer.parseInt(endDate.substring(4));
            int hour = 23;
            int minute = 59;
            Calendar ending = Calendar.getInstance();
            ending.set(year, month, date, hour, minute);
            this.__endTime = ending;
        } else {
            Calendar ending = Calendar.getInstance();
            // todo stub
            // find appropriate setting for null value
            this.__endTime = ending;
        }
    }

    public void setEndTime(String endDate) {
        int date = Integer.parseInt(endDate.substring(0, 2));
        int month = Integer.parseInt(endDate.substring(2, 4));
        int year = Integer.parseInt(endDate.substring(4));
        Calendar ending = Calendar.getInstance();
        ending.set(year, month, date);
        this.__endTime = ending;
    }

    public void setPriority(int nextPriority) {
        this.__priorityFlag = nextPriority;
    }

    public void setCompletion(boolean nextStatus) {
        this.__completionFlag = nextStatus;
    }

    public void setFloating(boolean nextFloatStat) {
        this.__floatingTask = nextFloatStat;
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
        toDisplay.putString(user_title, this.__title);
        toDisplay.putObject(user_flag, this.__completionFlag);
        toDisplay.putString(user_endTime, this.__endTime.toString());
        toDisplay.putString(user_description, this.__description);
        toDisplay.putString(user_start, this.__startTime.toString());
        toDisplay.putString(user_priority, Integer.toString(this.__priorityFlag));
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
        formatter = new SimpleDateFormat("d-M-y 'at' H:m");
        return formatter.format(date);
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

        // Do not change this string conversion format - this is for Database's
        // log file. You need to at least tell me how you've changed it if you
        // want to change it, or write your method for your own formatter.
        ArrayList<String> toDisplay = new ArrayList<String>();
        toDisplay.add(this.displayTitle());
        toDisplay.add(this.displayDescription());
        toDisplay.add(this.displayStart());
        toDisplay.add(this.displayEnd());
        toDisplay.add(this.displayPriority());
        toDisplay.add(this.displayIsComplete());
        toDisplay.add(this.displayIsFloating());
        toDisplay.add(this.displayGoogleId());

        String result = "";
        for (String aField : toDisplay) {
            if (toDisplay.indexOf(aField) == toDisplay.size() - 1) {
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
     * @return a number ranking the priority of this task
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
     * @return the start date and time of this task
     * 
     * @author Soh Yong Sheng
     * 
     */
    public String displayStart() {
        return START + this.displayDateTime(this.getStartTime());
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
}
