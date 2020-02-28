package com.dreamlin.hotaldatepicker;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.Nullable;

import java.util.Calendar;
import java.util.Date;

/**
 * <p> Title: PickerDay </p>
 * <p> Description: </p>
 * <p>
 * author: dreamlin
 * date: 2020-02-05
 * version: V1.0.0
 * Created by dreamlin on 2020-02-05.
 */
public class PickerDay implements Comparable<PickerDay>, Parcelable {

    int year;
    int month;
    int day;
    String tag;

    protected PickerDay(Parcel in) {
        year = in.readInt();
        month = in.readInt();
        day = in.readInt();
        tag = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(year);
        dest.writeInt(month);
        dest.writeInt(day);
        dest.writeString(tag);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PickerDay> CREATOR = new Creator<PickerDay>() {
        @Override
        public PickerDay createFromParcel(Parcel in) {
            return new PickerDay(in);
        }

        @Override
        public PickerDay[] newArray(int size) {
            return new PickerDay[size];
        }
    };

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public String getTag() {
        return tag;
    }

    public PickerDay(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public PickerDay(int year, int month, int day, String tag) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.tag = tag;
    }

    public PickerDay(Calendar calendar) {
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 只比较年月日
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(PickerDay o) {
        if (o == null)
            throw new IllegalArgumentException("无效的比较参数");
        return compareTo(o.year, o.month, o.day);
    }

    public int compareTo(int year, int month, int day) {
        if (this.year == year && this.month == month && this.day == day)
            return 0;
        else if (this.year < year || (this.year == year && this.month < month) ||
                (this.year == year && this.month == month && this.day < day)) {
            return -1;
        }
        return 1;
    }

    /**
     * 只比较年月日
     *
     * @param obj
     * @return
     */
    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof PickerDay) {
            return compareTo((PickerDay) obj) == 0;
        }
        return false;
    }

    public boolean before(PickerDay pickerDay) {
        return compareTo(pickerDay) == -1;
    }

    public boolean before(int year, int month, int day) {
        return compareTo(year, month, day) == -1;
    }

    public boolean after(PickerDay pickerDay) {
        return compareTo(pickerDay) == 1;
    }

    public boolean after(int year, int month, int day) {
        return compareTo(year, month, day) == 1;
    }

    public int rangeDays(int year, int month, int day) {
        int days;
        Date firstDate = new Date(this.year, this.month - 1, this.day, 12, 0, 0);
        Date lastDate = new Date(year, month - 1, day, 12, 0, 0);
        long interval = firstDate.getTime() - lastDate.getTime();
        days = (int) (interval / (24 * 3600 * 1000L));
        return Math.abs(days);
    }

    public int rangeDays(PickerDay pickerDay) {
        return rangeDays(pickerDay.year, pickerDay.month, pickerDay.day);
    }

}
