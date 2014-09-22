package taskbuddy.file;

import static org.junit.Assert.*;

import org.junit.Test;

public class TimeTest {
    Time testTime;
    int hour;
    int minute;

    public void setup() {
        hour = 23;
        minute = 59;
        testTime = new Time(hour, minute);
    }

    @Test
    public void testTime() {
        setup();        
        assertEquals(
                "minute field not initialised properly by constructor, i.e. minute is not 59",
                minute, testTime.getMinute());
        assertEquals(
                "hour field not initialised properly by constructor, i.e. hour is not 23",
                hour, testTime.getHour());
    }
    
    @Test
    public void testSetTime() throws Exception {
        setup();
        hour = 00;
        minute = 00;
        testTime.setMinute(minute);
        testTime.setHour(hour);
        
        assertEquals(
                "minute field not set properly, i.e. minute is not 00",
                minute, testTime.getMinute());
        assertEquals(
                "hour field not set properly by constructor, i.e. hour is not 00",
                hour, testTime.getHour());
    }

}
