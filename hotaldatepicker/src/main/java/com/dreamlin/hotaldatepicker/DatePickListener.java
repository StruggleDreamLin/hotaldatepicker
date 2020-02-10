package com.dreamlin.hotaldatepicker;

/**
 * <p> Title: DatePickListener </p>
 * <p> Description: </p>
 *
 * @author: dreamlin
 * @date: 2020-02-06
 * @version: V1.0.0
 * Created by dreamlin on 2020-02-06.
 */
public interface DatePickListener {

    void onFirstSelected(PickerDay pickerDay);

    void onRangeSelected(PickerDay first, PickerDay last);

    void onFirstSelectCancel(PickerDay pickerDay);


}
