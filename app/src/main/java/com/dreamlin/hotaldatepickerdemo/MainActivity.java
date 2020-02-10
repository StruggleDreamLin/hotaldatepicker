package com.dreamlin.hotaldatepickerdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.dreamlin.hotaldatepicker.DatePickListener;
import com.dreamlin.hotaldatepicker.DatePickerAdapter;
import com.dreamlin.hotaldatepicker.DatePickerView;
import com.dreamlin.hotaldatepicker.PickerDay;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DatePickerView dateView = findViewById(R.id.date_wrapper);
        List<PickerDay> invalidDays = new ArrayList<>();
        List<PickerDay> tags = new ArrayList<>();
        invalidDays.add(new PickerDay(2020, 2, 22));
        tags.add(new PickerDay(2020, 2, 12, "呀呀"));
        DatePickerAdapter.DateModel dateModel = new DatePickerAdapter.DateModel();
//        dateModel.beginTag = "";
//        dateModel.endTag = "";
        dateModel.monthCount = 12;
        dateModel.mostSelectNum = 10;
        dateModel.invalidTips = "包含无效日期";
        dateModel.invalidDays = invalidDays;
        dateModel.tags = tags;
//        dateModel.defTag = "￥148";

        dateView.setParams(dateModel, new DatePickListener() {
            @Override
            public void onFirstSelected(PickerDay pickerDay) {
                System.out.println("MainActivity.onFirstSelected");
            }

            @Override
            public void onRangeSelected(PickerDay first, PickerDay last) {
                System.out.println("MainActivity.onRangeSelected");
            }

            @Override
            public void onFirstSelectCancel(PickerDay pickerDay) {
                System.out.println("MainActivity.onFirstSelectCancel");
            }
        });
    }
}
