package com.dreamlin.hotaldatepicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * <p> Title: DatePickerWrapper </p>
 * <p> Description: </p>
 *
 * author: dreamlin
 * date: 2020-02-06
 * version: V1.0.0
 * Created by dreamlin on 2020-02-06.
 */
public class DatePickerView extends RecyclerView {

    private DatePickerAdapter mAdapter;
    private DatePickerAdapter.DateModel dateModel;
    private DateView.DrawParameters drawParameters = new DateView.DrawParameters();

    public DatePickerView(@NonNull Context context) {
        this(context, null);
    }

    public DatePickerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DatePickerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        readAttrs(context, attrs);
    }

    private void readAttrs(@NonNull Context context, @Nullable AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DatePickerView);
        drawParameters.isAutoMeasure = typedArray.getBoolean(R.styleable.DatePickerView_autoMeasure, true);
        drawParameters.beforeSelect = typedArray.getBoolean(R.styleable.DatePickerView_canSelectBeforeDay, false);

        drawParameters.itemWidth = typedArray.getDimension(R.styleable.DatePickerView_itemWidth, drawParameters.itemWidth);
        drawParameters.itemHeight = typedArray.getDimension(R.styleable.DatePickerView_itemHeight, drawParameters.itemHeight);

        drawParameters.monthAndYearTextSize = (int) typedArray.getDimension(R.styleable.DatePickerView_monthAndYearTextSize, drawParameters.monthAndYearTextSize);
        drawParameters.dateTextSize = (int) typedArray.getDimension(R.styleable.DatePickerView_dateTextSize, drawParameters.monthAndYearTextSize);
        drawParameters.tipsTextSize = (int) typedArray.getDimension(R.styleable.DatePickerView_tipsTextSize, drawParameters.monthAndYearTextSize);

        drawParameters.lineColor = typedArray.getColor(R.styleable.DatePickerView_lineColor, drawParameters.lineColor);
        drawParameters.montAndYearTextColor = typedArray.getColor(R.styleable.DatePickerView_montAndYearTextColor, drawParameters.montAndYearTextColor);
        drawParameters.dateSelectBgColor = typedArray.getColor(R.styleable.DatePickerView_dateSelectBgColor, drawParameters.dateSelectBgColor);
        drawParameters.dateNormalBgColor = typedArray.getColor(R.styleable.DatePickerView_dateNormalBgColor, drawParameters.dateNormalBgColor);
        drawParameters.dateTextNormalColor = typedArray.getColor(R.styleable.DatePickerView_dateNormalTextColor, drawParameters.dateTextNormalColor);
        drawParameters.dateTextSelectColor = typedArray.getColor(R.styleable.DatePickerView_dateSelectTextColor, drawParameters.dateTextSelectColor);
        drawParameters.invalidBgColor = typedArray.getColor(R.styleable.DatePickerView_invalidBgColor, drawParameters.invalidBgColor);
        drawParameters.invalidTextColor = typedArray.getColor(R.styleable.DatePickerView_invalidTextColor, drawParameters.invalidTextColor);
        drawParameters.tipsTextColor = typedArray.getColor(R.styleable.DatePickerView_tipsTextColor, drawParameters.tipsTextColor);
        drawParameters.tipsBgColor = typedArray.getColor(R.styleable.DatePickerView_tipsBgColor, drawParameters.tipsBgColor);

        drawParameters.startBg = typedArray.getDrawable(R.styleable.DatePickerView_startBg);
        drawParameters.startBgOnly = typedArray.getDrawable(R.styleable.DatePickerView_startBgOnly);
        drawParameters.endBg = typedArray.getDrawable(R.styleable.DatePickerView_endBg);

        typedArray.recycle();
    }

    public void setParams(DatePickerAdapter.DateModel dateModel, DatePickListener listener) {
        mAdapter = new DatePickerAdapter(dateModel, drawParameters, listener);
        this.dateModel = dateModel;
        setLayoutManager(new LinearLayoutManager(getContext()));
        setAdapter(mAdapter);
    }

    public void updateTags(List<PickerDay> tags) {
        this.dateModel.tags = tags;
        mAdapter.notifyDataSetChanged();
    }

    public void updateInvalidDays(List<PickerDay> invalidDays) {
        this.dateModel.invalidDays = invalidDays;
        mAdapter.notifyDataSetChanged();
    }

    public void update() {
        mAdapter.notifyDataSetChanged();
    }

}
