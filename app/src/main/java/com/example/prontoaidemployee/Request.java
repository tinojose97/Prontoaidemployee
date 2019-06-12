package com.example.prontoaidemployee;

public class Request {

    String DateBook;
    String TimeBook;
    String LocBook;
    String Job;

    public String getDate(){return  DateBook;}

    public void setDate(String date){DateBook=date;}

    public String getTime(){return  TimeBook;}

    public void setTime(String time){ TimeBook=time;}

    public String getLocation() { return LocBook;   }

    public void setLocation(String location) {     LocBook = location; }

    public String getJob() { return Job;   }

    public void setJob(String job) {     Job= job; }


    public Request(String date, String time, String location, String job ) {


        DateBook=date;
        TimeBook=time;
        LocBook = location;
        Job=job;


    }
}