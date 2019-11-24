package com.oslomet.norwegianrails.viewmodel;

public class SearchItem {

    public String from;
    public String to;
    public String time_from;
    public String time_to;
    public String track;
    public String travelTime;
    public String price;
    public String date;


    public SearchItem(String from, String to, String time_from,
                         String time_to, String track, String travelTime,String price,
                      String date) {
        this.from = from;
        this.to = to;
        this.time_from = time_from;
        this.time_to=time_to;
        this.track=track;
        this.travelTime=travelTime;
        this.price = price;
        this.date = date;
    }
    public String getFrom()
    {
        return from;
    }
    public String getTo()
    {
        return to;
    }
    public String getTimeFrom()
    {
        return time_from;
    }
    public String getTimeTo()
    {
        return time_to;
    }
    public String getTrack()
    {
        return track;
    }
    public String getTravelTime(){
        return travelTime;
    }
    public String getPrice (){
        return price;
    }
    public String getDate(){
        return date;
    }
}

