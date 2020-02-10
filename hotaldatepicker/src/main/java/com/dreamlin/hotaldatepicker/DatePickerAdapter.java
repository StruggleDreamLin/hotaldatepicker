package com.dreamlin.hotaldatepicker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * <p> Title: DatePickerAdapter </p>
 * <p> Description: </p>
 *
 * @author: dreamlin
 * @date: 2020-02-06
 * @version: V1.0.0
 * Created by dreamlin on 2020-02-06.
 */
public class DatePickerAdapter extends RecyclerView.Adapter<DatePickerAdapter.ViewHolder> {

    protected List<Calendar> calendars;
    protected DateModel dateModel;
    protected DateView.DrawParameters drawParameters;
    private DatePickListener pickListener;


    public DatePickerAdapter(DateModel dateModel, DateView.DrawParameters drawParameters, DatePickListener listener) {
        this.dateModel = dateModel;
        this.drawParameters = drawParameters;
        calendars = new ArrayList<>();
        //定义起始的日期 范围
        int year = dateModel.startYear;
        int month = dateModel.startMonth;
        for (int i = 0; i < dateModel.monthCount; i++) {
            Calendar cloneMonth = (Calendar) Calendar.getInstance().clone();
            if (month + i > 12) {
                cloneMonth.set(Calendar.YEAR, year + 1);
                cloneMonth.set(Calendar.MONTH, (month + i - 1) % 12);
            } else {
                cloneMonth.set(Calendar.MONTH, month + i - 1);
            }
            cloneMonth.set(Calendar.DAY_OF_MONTH, 1);
            calendars.add(cloneMonth);
        }
        this.pickListener = listener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_month_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder.itemView instanceof DateView) {

            DateView itemView = (DateView) holder.itemView;
            itemView.setDrawParameters(drawParameters);
            itemView.setCalendar(calendars.get(position));
            itemView.setDateParameters(dateModel);
            itemView.setListener(pickListener);
            itemView.setAdapter(this);
        }
    }

    @Override
    public int getItemCount() {
        return calendars == null ? 0 : calendars.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public static class DateModel {
        public int startYear = Calendar.getInstance().get(Calendar.YEAR); //开始的年份
        public int startMonth = Calendar.getInstance().get(Calendar.MONTH) + 1; //开始的月份 0-11 + 1
        public int monthCount = 12; //最多显示几个月
        public int mostSelectNum = 100; //最后可以选择多少天
        public int leastSelectNum = 2; //最后可以选择多少天
        public String defTag; //默认标签
        public String beginTag; //第一个选中标签
        public String endTag; //最后一个标签
        public String invalidTips = "包含无效日期"; //点击无效日期时的提示
        public SelectDays selectDays = new SelectDays(); //选择的日期
        public List<PickerDay> invalidDays = new ArrayList<>(); //无效的日期
        public List<PickerDay> tags = new ArrayList<>(); //添加了标签的日期

    }

}
