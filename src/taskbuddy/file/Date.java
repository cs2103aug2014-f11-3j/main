package taskbuddy.file;

/**
 * Stores and manipulates dates from user input. An object of this class is to
 * be used in the <code>Task</code> class.
 * 
 * @author Soh Yong Sheng
 *
 */
public class Date {
    /**
     * Refers to the actual date of the month, e.g. the <code>date</code> field
     * for "31 December 2014" is 31. This field must be an integer from 1 to 31
     * inclusive.
     */
    int date;

    /**
     * Refers to the actual month of the year, e.g. the <code>month</code> field
     * for "31 December 2014" is 12. This field must be an integer from 1 to 12
     * inclusive.
     */
    int month;

    /**
     * Refers to the year, e.g. the <code>year</code> field for
     * "31 December 2014" is 2014. This field must be an integer. A negative
     * year refers to "B.C.", e.g. -2000 means 2000 B.C., whereas a positive
     * year means "A.D." or "C.E.", e.g. 2014 means 2014 A.D.
     */
    int year;

    /**
     * Constructor for <code>Date</code> class which initialises all 3 fields
     * date, month, and year. This emphasises the fact that you cannot create
     * something like a year-less date. If a default month/year is needed,
     * please feed in the default month/year into the constructor.
     * 
     * @param date
     *            date of the month
     * @param month
     *            month of the year
     * @param year
     *            year
     */
    public Date(int date, int month, int year) {
        this.date = date;
        this.month = month;
        this.year = year;
    }

    /**
     * Gets and returns the date of the month as an <code>integer</code>
     * 
     * @return date of the month
     */
    public int getDate() {
        return date;
    }

    /**
     * Sets the date of the month
     * 
     * @param date
     *            date of the month
     */
    public void setDate(int date) {
        this.date = date;
    }

    /**
     * Gets and returns the month of the year as an <code>integer</code>
     * 
     * @return month of the year
     */
    public int getMonth() {
        return month;
    }

    /**
     * Sets the month of the year
     * 
     * @param month
     *            month of the year
     */
    public void setMonth(int month) {
        this.month = month;
    }

    /**
     * Gets and returns the year as an <code>integer</code>
     * 
     * @return year
     */
    public int getYear() {
        return year;
    }

    /**
     * Sets the year
     * 
     * @param year
     */
    public void setYear(int year) {
        this.year = year;
    }

}
