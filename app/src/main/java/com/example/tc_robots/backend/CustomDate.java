package com.example.tc_robots.backend;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class CustomDate {
    DateTime date;

    public CustomDate() {
        this.date = new DateTime();
    }

    private String getHoursAndMinutes() {
        return date.getHourOfDay() + ":" + date.getMinuteOfHour();
    }

    public void setDate(DateTime date) {
        this.date = date;
    }

    public String getCorrectTimeInfo() {
        if((date.toLocalDate()).equals(new LocalDate())) {
            return getHoursAndMinutes();
        }
        if((date.toLocalDate()).equals(new LocalDate().minusDays(1))) {
            return "YDA";
        }
        else{
            return "PREV";
        }


    }

}
