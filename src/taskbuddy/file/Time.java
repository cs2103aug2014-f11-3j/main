package taskbuddy.file;

/**
 * Stores and manipulates time in 24 hrs format, as opposed to a.m./p.m. format,
 * keyed in by user. An object of this class is to be used in the
 * <code>Task</code> object. There is no 'seconds' field for this class because
 * a user keying a time for the task deadline is usually not concerned with
 * extending the deadline's precision to seconds. That is, a user typically keys
 * in "submit assignment by 2359 hrs", not something like
 * "submit assignment by 2359 hrs 59 s".
 * 
 * @author Soh Yong Sheng
 *
 */
public class Time {
    /**
     * Stores hour of the time keyed in by user. For example, the hour field of
     * time 2359 hrs is '23'. This field must be an integer from 00 to 23
     * inclusive.
     */
    int hour;

    /**
     * Stores minute of the time keyed in by user. For example, the minute field
     * of time 2359 hrs is '59'. This field must be an integer from 00 to 59
     * inclusive.
     */
    int minute;

    /**
     * Constructor for the <code>Time</code> object that initialises the hour
     * and minute fields. Initialisation of hour and minute fields emphasise the
     * fact that you can't have an (hour/minute)-less time.
     * 
     * @param hour
     * @param minute
     */
    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    /**
     * Retrieves and returns the hour field of the time as an integer.
     * 
     * @return hour
     */
    public int getHour() {
        return hour;
    }

    /**
     * Sets the hour of the time. The hour must be a positive integer from 00 to
     * 23 inclusive.
     * 
     * @param hour
     */
    public void setHour(int hour) {
        this.hour = hour;
    }

    /**
     * Retrieves and returns the minute field of the time as an integer.
     * 
     * @return minute
     */
    public int getMinute() {
        return minute;
    }

    /**
     * Sets the minute of the time. The hour must be a positive integer from 00
     * to 59 inclusive.
     * 
     * @param minute
     */
    public void setMinute(int minute) {
        this.minute = minute;
    }

}
