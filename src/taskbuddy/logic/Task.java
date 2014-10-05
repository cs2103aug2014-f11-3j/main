package taskbuddy.logic;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;

//Author: andrew
public class Task {

    private static String nullValue = "PADDING_VALUE";
    private String __title;
    private String __description;
    private Calendar __startTime;
    private Calendar __endTime;
    private int __priorityFlag;
    private boolean __completionFlag;
    private boolean __floatingTask;
    private String __googleID;

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
    public ArrayList<String> getTaskInfo() {
        ArrayList<String> toDisplay = new ArrayList<String>();
        toDisplay.add(this.__title);
        toDisplay.add(Boolean.toString(this.__completionFlag));
        toDisplay.add(this.__endTime.toString());
        toDisplay.add(this.__description);
        toDisplay.add(this.__startTime.toString());
        toDisplay.add(Integer.toString(this.__priorityFlag));
        toDisplay.add(this.__googleID);
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
    public String toString(Calendar cal) {
        Date date = cal.getTime();

        // Do not change this formatter - this is for Database's log file. You
        // need to at least tell me how you've changed it if you want to change
        // it, or write your method for your own formatter.
        SimpleDateFormat formatter = new SimpleDateFormat("d-M-y 'at' H:m");

        return formatter.format(date);
    }

    /*
     * TODO Method not tested yet.
     */
    public String toString() {
        // Do not change this string conversion format - this is for Database's
        // log file. You need to at least tell me how you've changed it if you
        // want to change it, or write your method for your own formatter.
        String delimiter = " | ";
        // @formatter:off
        String result = this.__title + delimiter + 
                        Boolean.toString(this.__completionFlag) + delimiter +
                        this.toString(__startTime) + delimiter + 
                        this.toString(__startTime) + delimiter +
                        this.__description +  delimiter + 
                        Integer.toString(this.__priorityFlag) + delimiter +
                        this.__googleID;
        // @formatter:on
        return result;
    }
}
