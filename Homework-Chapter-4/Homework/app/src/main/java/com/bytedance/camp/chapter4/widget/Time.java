package com.bytedance.camp.chapter4.widget;

class Time {
    private int hours;
    private int minutes;
    private int seconds;
    private int ms;

    Time(int hours, int minutes, int seconds,int ms) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
        this.ms=ms;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }
    public int getMs(){return ms;}
}
