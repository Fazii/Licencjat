package com.nowakowski.krzysztof95.navigationdrawerapp.transform;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;

/**
 * Created by Krzysztof on 2017-03-25.
 */

public class DateTransform {

    public String countDownToEvent(String date){
        String countdown;
        DateTime date_time = DateTime.parse(date,
                DateTimeFormat.forPattern("HH:mm:ss yyyy.MM.dd"));


        LocalDateTime localDateTime = new LocalDateTime();


        String currentDateTime = (localDateTime.hourOfDay().get() + ":" + localDateTime.minuteOfHour().get() + ":"
                + localDateTime.secondOfMinute().get() + " " + localDateTime.year().get()+ "." + localDateTime.monthOfYear().get() + "." + localDateTime.dayOfMonth().get());

        DateTime currnet_date_time = DateTime.parse(currentDateTime,
                DateTimeFormat.forPattern("HH:mm:ss yyyy.MM.dd"));

        Period diff = new Period(currnet_date_time, date_time);

        int minutes = diff.getMinutes();
        int hours = diff.getHours();
        int days = diff.getDays();


        if(minutes >= 0 && hours >=0 && days >=0) {
            if (days == 0) {
                if (hours == 0) {
                    if (minutes == 1) {
                        countdown = "Zaczyna się za minutę";
                    } else if (minutes >= 2 % 10 && minutes <= 4 % 10) {
                        countdown = "Zaczyna się za " + minutes + " minuty";
                    } else {
                        countdown = "Zaczyna się za " + minutes + " minut";
                    }
                } else if (hours == 1) {
                    countdown = "Zaczyna się za " + hours + " godzinę";
                } else if (hours % 10 >= 2 && hours % 10 <= 4) {
                    countdown = "Zaczyna się za " + hours + " godziny";
                } else {
                    countdown = "Zaczyna się za " + hours + " godzin";
                }
            } else if (days == 1) {
                countdown = "Zaczyna się za " + days + " dzień";
            } else {
                countdown = "Zaczyna się za " + days + " dni";
            }
        } else {
            if (days == 0) {
                if (hours == 0) {
                    if (minutes == -1) {
                        countdown = "Zaczęło się minutę temu";
                    } else if (Math.abs(minutes) >= 2 % 10 &&  Math.abs(minutes) <= 4 % 10) {
                        countdown = "Zaczęło się " + Math.abs(minutes) + " minuty temu";
                    } else {
                        countdown = "Zaczęło się " + Math.abs(minutes) + " minut temu";
                    }
                } else if (hours == -1) {
                    countdown = "Zaczęło się " + Math.abs(hours) + " godzinę temu";
                } else if (hours % 10 >= -2 && hours % 10 <= -4) {
                    countdown = "Zaczęło się " + Math.abs(hours) + " godziny temu";
                } else {
                    countdown = "Zaczęło się " + Math.abs(hours) + " godzin temu";
                }
            } else if (days == -1) {
                countdown = "Zaczęło się " + Math.abs(days) + " dni temu";
            } else {
                countdown = "Zaczęło się " + Math.abs(days) + " dni temu";
            }
        }


        return countdown;
    }

    public String countDownToDate(String date){
        String countdown;
        DateTime date_time = DateTime.parse(date,
                DateTimeFormat.forPattern("HH:mm:ss yyyy.MM.dd"));


        LocalDateTime localDateTime = new LocalDateTime();


        String currentDateTime = (localDateTime.hourOfDay().get() + ":" + localDateTime.minuteOfHour().get() + ":"
                + localDateTime.secondOfMinute().get() + " " + localDateTime.year().get()+ "." + localDateTime.monthOfYear().get() + "." + localDateTime.dayOfMonth().get());

        DateTime currnet_date_time = DateTime.parse(currentDateTime,
                DateTimeFormat.forPattern("HH:mm:ss yyyy.MM.dd"));

        Period diff = new Period(date_time, currnet_date_time);

        int minutes = diff.getMinutes();
        int hours = diff.getHours() - 2;
        int days = diff.getDays();

        if(days == 0){
            if(hours == 0){
                if(minutes == 1){
                    countdown = "Dodano minutę temu";
                } else if(minutes >=2%10 && minutes <=4%10) {
                    countdown = "Dodano " + minutes + " minuty temu";
                }
                else {
                    countdown = "Dodano " + minutes + " minut temu";
                }
            } else if(hours == 1){
                countdown = "Dodano godzinę temu";
            } else if(hours%10 >=2 && hours%10 <=4) {
                countdown = "Dodano " + hours + " godziny temu";
            } else {
                countdown = "Dodano " + hours + " godzin temu";
            }
        } else if(days == 1){
            countdown = "Dodano "+ days + " dzień temu";
        } else {
            countdown = "Dodano "+ days + " dni temu";
        }


        return countdown;
    }
}
