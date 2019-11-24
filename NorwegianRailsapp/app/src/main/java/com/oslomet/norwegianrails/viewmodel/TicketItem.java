package com.oslomet.norwegianrails.viewmodel;

public class TicketItem {
    public String id;
    public String from;
    public String to;
    public String time_from;
    public String time_to;
    public String track;
    public String travel_time;
    public String price;
    public String date;


    public TicketItem(String id, String from, String to, String time_from,
                      String time_to,  String travel_time,String track,String price, String date) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.time_from = time_from;
        this.time_to=time_to;
        this.track=track;
        this.travel_time=travel_time;
        this.price = price;
        this.date = date;
    }
    public String getId(){
        return id;
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
        return travel_time;
    }
    public String getPrice (){
        return price;
    }
    public String getDate(){
        return date;
    }
}
