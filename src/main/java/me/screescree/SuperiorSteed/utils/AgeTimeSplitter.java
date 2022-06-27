package me.screescree.SuperiorSteed.utils;

public class AgeTimeSplitter {
    private int seconds;
    private int minutes;
    private int hours;
    private int days;
    private int months;
    private int years;

    public AgeTimeSplitter(long ticks) {
        int fullSeconds = (int) (ticks / 20);
        seconds = fullSeconds % 60;
        int fullMinutes = fullSeconds / 60;
        minutes = fullMinutes % 60;
        int fullHours = fullMinutes / 60;
        hours = fullHours % 24;
        int fullDays = fullHours / 24;
        days = fullDays % 30;
        int fullMonths = fullDays / 30;
        months = fullMonths % 12;
        years = fullMonths / 12;
    }

    public String formatString() {
        return String.format("%d years, %d months, %d days, %d hours, %d minutes, %d seconds", years, months % 12, days % 30, hours % 24, minutes % 60, seconds % 60);
    }

    public long getTicks() {
        return (seconds * 20) + (minutes * 1200) + (hours * 72000) + (days * 1728000) + (months * 51840000) + (years * 622080000);
    }

    public long getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public long getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public long getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public long getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public long getMonths() {
        return months;
    }

    public void setMonths(int months) {
        this.months = months;
    }

    public long getYears() {
        return years;
    }

    public void setYears(int years) {
        this.years = years;
    }
}
