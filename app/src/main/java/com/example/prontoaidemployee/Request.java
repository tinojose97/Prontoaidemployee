package com.example.prontoaidemployee;

public class Request {

    String DateBook;
    String TimeBook;
    String LocBook;

    public String getDate(){return  DateBook;}

    public void setDate(String date){DateBook=date;}

    public String getTime(){return  TimeBook;}

    public void setTime(String time){ TimeBook=time;}

    public String getLocation() { return LocBook;   }

    public void setLocation(String location) {     LocBook = location; }


    public Request(String date, String time, String location ) {


        DateBook=date;
        TimeBook=time;
        LocBook = location;


    }
}