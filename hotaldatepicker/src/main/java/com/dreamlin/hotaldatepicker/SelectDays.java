package com.dreamlin.hotaldatepicker;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * <p> Title: SelectDays </p>
 * <p> Description: </p>
 *
 * author: dreamlin
 * date: 2020-02-05
 * version: V1.0.0
 * Created by dreamlin on 2020-02-05.
 */
public class SelectDays implements Parcelable {

    PickerDay first;
    PickerDay last;

    public SelectDays() {
    }

    protected SelectDays(Parcel in) {
        first = in.readParcelable(PickerDay.class.getClassLoader());
        last = in.readParcelable(PickerDay.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(first, flags);
        dest.writeParcelable(last, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SelectDays> CREATOR = new Creator<SelectDays>() {
        @Override
        public SelectDays createFromParcel(Parcel in) {
            return new SelectDays(in);
        }

        @Override
        public SelectDays[] newArray(int size) {
            return new SelectDays[size];
        }
    };

    public PickerDay getFirst() {
        return first;
    }

    public void setFirst(PickerDay first) {
        this.first = first;
    }

    public PickerDay getLast() {
        return last;
    }

    public void setLast(PickerDay last) {
        this.last = last;
    }

    public boolean hasSelected() {
        return first != null;
    }

    /**
     * 将要选择
     * @return
     */
    public boolean beSelected() {
        return first != null && last == null;
    }

    /**
     * 判断某天是否被选中
     *
     * @param pickerDay
     * @return
     */
    public boolean inSelected(PickerDay pickerDay) {
        if (first == null)
            return false;
        else if (last == null)
            return isFirst(pickerDay);
        return pickerDay.before(last) && pickerDay.after(first);
    }

    public boolean inSelected(int year, int month, int day) {
        if (first == null)
            return false;
        else if (last == null)
            return isFirst(year, month, day);
        return (last.after(year, month, day) || last.compareTo(year, month, day) == 0) &&
                (first.before(year, month, day) || first.compareTo(year, month, day) == 0);
    }

    public boolean isFirst(int year, int month, int day) {
        if (first == null)
            return false;
        return first.compareTo(year, month, day) == 0;
    }

    public boolean isFirst(PickerDay pickerDay) {
        if (first == null)
            return false;
        return first.equals(pickerDay);
    }

    public boolean isLast(int year, int month, int day) {
        if (last == null)
            return false;
        return last.compareTo(year, month, day) == 0;
    }

    public boolean isLast(PickerDay pickerDay) {
        if (last == null)
            return false;
        return last.equals(pickerDay);
    }

    public int getRangeDays() {
        int days = 0;
        if (first != null && last != null) {
            days = first.rangeDays(last);
        }
        return days;
    }
}
