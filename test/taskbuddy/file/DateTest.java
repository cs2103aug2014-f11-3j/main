package taskbuddy.file;

import static org.junit.Assert.*;

import org.junit.Test;

public class DateTest {
    Date testDate;
    int date;
    int month;
    int year;

    public void setup() {
        date = 31;
        month = 12;
        year = 2014;

        testDate = new Date(date, month, year);
    }

    @Test
    public void testDate() {
        setup();
        assertEquals("date is incorrect, i.e. not 31", date, testDate.getDate());
        assertEquals("month is incorrect, i.e. not 12", month,
                testDate.getMonth());
        assertEquals("year is incorrect, i.e. not 2014", year,
                testDate.getYear());
    }

    @Test
    public void testSetDate() throws Exception {
        setup();

        date = 1;
        month = 1;
        year = 9999;
        testDate.setDate(date);
        testDate.setMonth(month);
        testDate.setYear(year);

        assertEquals("date is incorrect, i.e. not 1", date, testDate.getDate());
        assertEquals("month is incorrect, i.e. not 1", month,
                testDate.getMonth());
        assertEquals("year is incorrect, i.e. not 9999", year,
                testDate.getYear());
    }

}
