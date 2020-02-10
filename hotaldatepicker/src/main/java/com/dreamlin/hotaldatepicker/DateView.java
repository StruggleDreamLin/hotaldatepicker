package com.dreamlin.hotaldatepicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.dreamlin.hotaldatepicker.common.Utils;

import java.util.Calendar;
import java.util.List;

/**
 * <p> Title: DateView </p>
 * <p> Description: </p>
 *
 * @author: dreamlin
 * @date: 2020-02-05
 * @version: V1.0.0
 * Created by dreamlin on 2020-02-05.
 */
public class DateView extends View implements DatePickListener {

    static final String[] weeks = new String[]{
            "日", "一", "二", "三", "四", "五", "六"
    };

    Paint mLinePaint, mMonthYearPaint, mDatePaint, mSelectedBgPaint, mInvalidBgPaint, mTipsPaint, mTipsBgPaint;
    Bitmap startBitmap, endBitmap, startOnlyBitmap;
    Drawable startBg;
    Drawable startBgOnly;
    Drawable endBg;

    int monthAndYearTextSize = (int) Utils.sp2px(14);
    int dateTextSize = (int) Utils.sp2px(12);
    int tipsTextSize = (int) Utils.sp2px(11);

    int lineColor = Color.parseColor("#EFEFEF");
    int montAndYearTextColor = Color.BLACK;
    int dateTextNormalColor = Color.parseColor("#959595");
    int dateTextSelectColor = Color.WHITE;
    int dateBgColor = Color.parseColor("#42B3FE");
    int invalidBgColor = Color.parseColor("#DEDEDE");
    int invalidTextColor = Color.parseColor("#959595");
    int tipsTextColor = Color.parseColor("#A8A8A8");
    int tipsBgColor = Color.parseColor("#DEDEDE");

    float itemWidth = Utils.dp2px(30);
    float itemHeight = Utils.dp2px(50);

    boolean isAutoMeasure = true;//是否自动测量
    boolean beforeSelect = false;//今天之前的日期是否可选

    private Calendar mCalendar;
    private Calendar firstDayCalendar;
    int dayOfMonth = 30;
    private int startYear;
    private int startMonth;

    private String defTag;
    private String invalidTips;
    private SelectDays selectDays;
    private List<PickerDay> invalidDays;
    private List<PickerDay> tags;
    private DatePickListener mListener;
    private DatePickerAdapter mAdapter;
    private int mostSelectDays; //最多选择天数

    public DateView(Context context) {
        super(context);
        init(context);
    }

    public DateView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(Utils.dp2px(1));

        mMonthYearPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMonthYearPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mMonthYearPaint.setStrokeCap(Paint.Cap.ROUND);
        mMonthYearPaint.setStrokeJoin(Paint.Join.ROUND);
        mMonthYearPaint.setStrokeWidth(1);

        mDatePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mSelectedBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mInvalidBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTipsPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTipsBgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        startBg = context.getResources().getDrawable(R.drawable.date_start_select);
        startBgOnly = context.getResources().getDrawable(R.drawable.date_start);
        endBg = context.getResources().getDrawable(R.drawable.date_end);
    }

    public void setDrawParameters(DrawParameters drawParameters) {

        isAutoMeasure = drawParameters.isAutoMeasure;
        beforeSelect = drawParameters.beforeSelect;
        itemWidth = drawParameters.itemWidth;
        itemHeight = drawParameters.itemHeight;

        monthAndYearTextSize = drawParameters.monthAndYearTextSize;
        dateTextSize = drawParameters.dateTextSize;
        tipsTextSize = drawParameters.tipsTextSize;

        lineColor = drawParameters.lineColor;
        montAndYearTextColor = drawParameters.montAndYearTextColor;
        dateTextNormalColor = drawParameters.dateTextNormalColor;
        dateTextSelectColor = drawParameters.dateTextSelectColor;
        invalidBgColor = drawParameters.invalidBgColor;
        invalidTextColor = drawParameters.invalidTextColor;
        tipsTextColor = drawParameters.tipsTextColor;
        tipsBgColor = drawParameters.tipsBgColor;

        if (drawParameters.startBg != null)
            startBg = drawParameters.startBg;
        if (drawParameters.startBgOnly != null)
            startBgOnly = drawParameters.startBgOnly;
        if (drawParameters.endBg != null)
            endBg = drawParameters.endBg;

        mLinePaint.setColor(lineColor);

        mMonthYearPaint.setTextSize(monthAndYearTextSize);
        mMonthYearPaint.setColor(montAndYearTextColor);

        mDatePaint.setColor(dateTextNormalColor);
        mDatePaint.setTextSize(dateTextSize);

        mSelectedBgPaint.setColor(dateBgColor);

        mInvalidBgPaint.setColor(invalidBgColor);

        mTipsPaint.setColor(tipsTextColor);
        mTipsPaint.setTextSize(tipsTextSize);

        mTipsBgPaint.setColor(tipsBgColor);

//        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (isAutoMeasure) {
            itemWidth = getMeasuredWidth() / 7;
            itemHeight = itemWidth;
        }
        startBitmap = Utils.loadDrawable(startBg, (int) itemWidth, (int) itemHeight);
        startOnlyBitmap = Utils.loadDrawable(startBgOnly, (int) itemWidth, (int) itemHeight);
        endBitmap = Utils.loadDrawable(endBg, (int) itemWidth, (int) itemHeight);
        int offsetY = drawWeeks() ? (int) (Utils.dp2px(24) + mDatePaint.getFontSpacing()) : 0;
        int weekDay = firstDayCalendar.get(Calendar.DAY_OF_WEEK);
        dayOfMonth = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        int allItemCount = ((weekDay - 1) << 1) + 1 + dayOfMonth;
        int mode = allItemCount % 7;
        int lineCount = allItemCount / 7;
        if (mode != 0) {
            lineCount += 1;
        }
        int height = (int) (offsetY + lineCount * itemHeight);
        int resolveHeight = resolveSize(height, heightMeasureSpec);
        int resolveWidth = resolveSize((int) (itemWidth * 7), widthMeasureSpec);
        setMeasuredDimension(resolveWidth, resolveHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawWeeks(canvas);

        drawMonthAndYear(canvas);

        drawCell(canvas);
    }

    protected void drawWeeks(Canvas canvas) {

        if (drawWeeks()) {
            for (int i = 0; i < weeks.length; i++) {
                float weekWidth = mDatePaint.measureText(weeks[i]);
                float textX = i * itemWidth + (itemWidth - weekWidth) / 2;
                float textY = Utils.dp2px(12) + mDatePaint.getFontSpacing();
                canvas.drawText(weeks[i], textX, textY, mDatePaint);
            }
        }
    }

    protected void drawMonthAndYear(Canvas canvas) {

        int offsetY = drawWeeks() ? (int) (Utils.dp2px(24) + mDatePaint.getFontSpacing()) : 0;
        canvas.drawLine(0, Utils.dp2px(1) + offsetY, getWidth(), Utils.dp2px(1) + offsetY, mLinePaint);
        float offset = Utils.dp2px(7);
        String text = getMonthAndYear();
        float textWidth = mMonthYearPaint.measureText(text);
        int centerX = getWidth() >> 1;
        canvas.drawText(text, centerX - textWidth / 2, mMonthYearPaint.getFontSpacing() + offset + offsetY, mMonthYearPaint);

        canvas.drawLine(0, Utils.dp2px(1) + mMonthYearPaint.getFontSpacing() + offset * 2 + offsetY, getWidth(),
                Utils.dp2px(1) + mMonthYearPaint.getFontSpacing() + offset * 2 + offsetY, mLinePaint);
    }

    protected void drawCell(Canvas canvas) {

        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH) + 1;
        int weekDay = firstDayCalendar.get(Calendar.DAY_OF_WEEK);
        int offsetY = drawWeeks() ? (int) (Utils.dp2px(24) + mDatePaint.getFontSpacing()) : 0;
        int currentX = (int) ((weekDay - 1) * itemWidth);
        int currentY = (int) (Utils.dp2px(16) + mMonthYearPaint.getFontSpacing()) + offsetY;

        for (int i = 1; i <= dayOfMonth; i++) {
            //背景
            if (selectDays.hasSelected()) {
                if (selectDays.inSelected(year, month, i)) {
                    if (selectDays.isFirst(year, month, i)) {
                        if (selectDays.getLast() != null)
                            canvas.drawBitmap(startBitmap, currentX, currentY, mSelectedBgPaint);
                        else
                            canvas.drawBitmap(startOnlyBitmap, currentX, currentY, mSelectedBgPaint);
                    } else if (selectDays.isLast(year, month, i)) {
                        canvas.drawBitmap(endBitmap, currentX, currentY, mSelectedBgPaint);
                    } else {
                        RectF rectF = new RectF(currentX, currentY, currentX + itemWidth, currentY + itemHeight);
                        canvas.drawRect(rectF, mSelectedBgPaint);
                    }
                } else if (selectDays.getFirst().after(year, month, i)) {
                    //draw invalid items bg
                    canvas.drawRect(currentX, currentY, currentX + itemWidth,
                            currentY + itemHeight, mInvalidBgPaint);
                }
            }
            //日期
            String text = String.valueOf(i);
            if (isToday(year, month, i)) {
                text = "今天";
            } else if (isTomorrow(year, month, i)) {
                text = "明天";
            } else if (isAcquired(year, month, i)) {
                text = "后天";
            }
            float textWidth = mDatePaint.measureText(text);
            float fontSpacing = mDatePaint.getFontSpacing();
            float textX = currentX + (itemWidth - textWidth) / 2;
            float textY = currentY + (itemHeight + fontSpacing) / 2;

            if (selectDays.inSelected(year, month, i)) {
                mDatePaint.setColor(Color.WHITE);
                String tag;
                float behaviorXWidth, behaviorX, behaviorY;
                if (selectDays.isFirst(year, month, i)) {
                    textY -= itemHeight / 4;
                    tag = "入住";
                    behaviorXWidth = mDatePaint.measureText(tag);
                    behaviorX = currentX + (itemWidth - behaviorXWidth) / 2;
                    behaviorY = currentY + itemHeight * 3 / 4 + mDatePaint.getFontSpacing() / 2;
                    canvas.drawText(tag, behaviorX, behaviorY, mDatePaint);
                } else {
                    if (selectDays.getLast() != null && selectDays.isLast(year, month, i)) {
                        tag = "退房";
                        behaviorXWidth = mDatePaint.measureText(tag);
                        behaviorX = currentX + (itemWidth - behaviorXWidth) / 2;
                        behaviorY = currentY + itemHeight * 3 / 4 + mDatePaint.getFontSpacing() / 2;
                        textY -= itemHeight / 4;
                        canvas.drawText(tag, behaviorX, behaviorY, mDatePaint);
                    }
                }
            } else if (isInvalidDay(year, month, i)) {
                mDatePaint.setColor(invalidTextColor);
            } else {
                mDatePaint.setColor(dateTextNormalColor);
            }

            //绘制标签
            String tag = getTag(year, month, i);
            float tagWidth;
            float tagX, tagY;
            if (!TextUtils.isEmpty(tag)) {
                if (!(selectDays.isFirst(year, month, i) || selectDays.isLast(year, month, i))) {
                    textY -= itemHeight / 4;
                    tagWidth = mDatePaint.measureText(tag);
                    tagX = currentX + (itemWidth - tagWidth) / 2;
                    tagY = currentY + itemHeight * 3 / 4 + mDatePaint.getFontSpacing() / 2;
                    canvas.drawText(tag, tagX, tagY, mDatePaint);
                }
            } else {
                if (!TextUtils.isEmpty(defTag)) {
                    if (!(selectDays.isFirst(year, month, i) || selectDays.isLast(year, month, i))) {
                        textY -= itemHeight / 4;
                        tagWidth = mDatePaint.measureText(defTag);
                        tagX = currentX + (itemWidth - tagWidth) / 2;
                        tagY = currentY + itemHeight * 3 / 4 + mDatePaint.getFontSpacing() / 2;
                        canvas.drawText(defTag, tagX, tagY, mDatePaint);
                    }
                }
            }
            canvas.drawText(text, textX, textY, mDatePaint);

            //绘制提示
            if (selectDays.hasSelected() && selectDays.getLast() == null &&
                    selectDays.getFirst().year == year && selectDays.getFirst().month == month) {
                String monthAndYear = getMonthAndYear();
                float monthYearWidth = mMonthYearPaint.measureText(monthAndYear);
                float tipsX = (getWidth() + monthYearWidth) / 2;
                float tipsY = mMonthYearPaint.getFontSpacing() / 2 + Utils.dp2px(7) + offsetY;
                String tips = "请选择退房日期";
                Rect tipsBounds = new Rect();
                mTipsPaint.getTextBounds(tips, 0, tips.length(), tipsBounds);
                float tipsWidth = tipsBounds.right - tipsBounds.left;
                float tipsHeight = tipsBounds.bottom - tipsBounds.top;
                RectF rectF = new RectF(tipsX, tipsY, tipsX + tipsWidth + (int) Utils.dp2px(6),
                        tipsY + mTipsPaint.getFontSpacing() + Utils.dp2px(2));
                canvas.drawRoundRect(rectF, Utils.dp2px(5), Utils.dp2px(5), mTipsBgPaint);
                canvas.drawText(tips, tipsX + (int) Utils.dp2px(3), tipsY + Utils.dp2px(1) + tipsHeight, mTipsPaint);
            }

            currentX += itemWidth;
            if ((i + weekDay - 1) % 7 == 0) {
                currentX = 0;
                currentY += itemHeight;
            }
        }
    }

    /**
     * 获取显示年月
     *
     * @return
     */
    protected String getMonthAndYear() {
        return String.format("%d年 %d月", mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH) + 1);
    }

    /**
     * 获取对应日期的tag
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    protected String getTag(int year, int month, int day) {
        if (tags != null && tags.size() > 0) {
            for (int i = 0; i < tags.size(); i++) {
                if (tags.get(i).compareTo(year, month, day) == 0) {
                    return tags.get(i).tag;
                }
            }
        }
        return null;
    }

    /**
     * 判断是否是无效日期
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    protected boolean isInvalidDay(int year, int month, int day) {
        for (int i = 0; i < invalidDays.size(); i++) {
            if (invalidDays.get(i).compareTo(year, month, day) == 0)
                return true;
        }
        return false;
    }

    /**
     * 判断即将选择部分是否包含无效日期
     *
     * @return
     */
    protected boolean containInvalidDay(int year, int month, int day) {
        if (selectDays.getFirst() == null)
            return false;
        if (selectDays.getFirst() != null && selectDays.getLast() != null)
            return false;
        for (int i = 0; i < invalidDays.size(); i++) {
            if (invalidDays.get(i).after(selectDays.getFirst()) &&
                    invalidDays.get(i).before(year, month, day))
                return true;
        }
        return false;
    }

    /**
     * 判断是否需要画星期几
     *
     * @return
     */
    protected boolean drawWeeks() {
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH) + 1;

        return year == startYear && month == startMonth;
    }

    /**
     * 判断是否当天
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    protected boolean isToday(int year, int month, int day) {
        month -= 1;
        return Calendar.getInstance().get(Calendar.YEAR) == year &&
                Calendar.getInstance().get(Calendar.MONTH) == month &&
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == day;
    }

    /**
     * 判断是否明天
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    protected boolean isTomorrow(int year, int month, int day) {
        month -= 1;
        int maxDay = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
        if (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == maxDay) { //如果今天是当月最后一天

            if (month == 12) {
                return (mCalendar.get(Calendar.YEAR) == year - 1) && month == 1 && day == 1;
            } else if (month < 12) {
                return (Calendar.getInstance().get(Calendar.YEAR) == year) && (Calendar.getInstance().get(Calendar.MONTH)
                        == month - 1) && day == 1;
            } else {
                throw new IllegalArgumentException("无效参数");
            }
        } else {
            return Calendar.getInstance().get(Calendar.YEAR) == year &&
                    Calendar.getInstance().get(Calendar.MONTH) == month &&
                    (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == day - 1);
        }
    }

    /**
     * 判断是否后天
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    protected boolean isAcquired(int year, int month, int day) {
        month -= 1;
        int maxDay = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
        int minus = maxDay - Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        if (minus < 2) { //如果今天是当月最后一天

            if (month == 12) {
                return (Calendar.getInstance().get(Calendar.YEAR) == year - 1) && month == 1 && day == 2 - minus;
            } else if (month < 12) {
                return (Calendar.getInstance().get(Calendar.YEAR) == year) && (Calendar.getInstance().get(Calendar.MONTH)
                        == month - 1) && day == 2 - minus;
            } else {
                throw new IllegalArgumentException("无效参数");
            }
        } else {
            return Calendar.getInstance().get(Calendar.YEAR) == year &&
                    Calendar.getInstance().get(Calendar.MONTH) == month &&
                    (Calendar.getInstance().get(Calendar.DAY_OF_MONTH) == day - 2);
        }
    }

    /**
     * 是否在今天之前
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    protected boolean beforeToday(int year, int month, int day) {
        month -= 1;
        Calendar calendar = Calendar.getInstance();
        return year < calendar.get(Calendar.YEAR) ||
                (year == calendar.get(Calendar.YEAR) && month < calendar.get(Calendar.MONTH)) ||
                (year == calendar.get(Calendar.YEAR) && month == calendar.get(Calendar.MONTH) &&
                        day < calendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {

            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_MOVE:
                calculatePointerDateIndex(event.getX(), event.getY());
                break;

        }
        return true;
    }

    protected int getDayRange(PickerDay first, int year, int month, int day) {
        return first == null ? 0 : first.rangeDays(year, month, day);
    }

    /**
     * @param x
     * @param y
     */
    protected void calculatePointerDateIndex(float x, float y) {
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH) + 1;
        int weekDay = firstDayCalendar.get(Calendar.DAY_OF_WEEK);
        int offsetY;
        if (drawWeeks()) {
            offsetY = (int) (Utils.dp2px(40) + mMonthYearPaint.getFontSpacing() + mDatePaint.getFontSpacing());
        } else {
            offsetY = (int) (Utils.dp2px(16) + mMonthYearPaint.getFontSpacing());
        }
        int row = (int) ((y - offsetY) / itemHeight) + 1;
        int column = (int) (x / itemWidth) + 1;
        if (row == 1) {
            if (column < weekDay)
                return;
        }
        int dayIndex = (row - 1) * 7 + column - weekDay + 1;

        //先看点击的是不是无效
        if (isInvalidDay(year, month, dayIndex)) {
            toast(invalidTips);
            return;
        }


        //如果选择范围内包含无效日期
        if (containInvalidDay(year, month, dayIndex)) {
            toast(invalidTips);
            return;
        }

        //如果选择的日期长度超过最大长度限制
        if (selectDays.hasSelected()) {
            if (getDayRange(selectDays.getFirst(), year, month, dayIndex) > mostSelectDays) {
                toast(String.format("最多可选择%d天", mostSelectDays));
                return;
            }
        }

        //如果点击的日期是今天之前的，不做任何处理
        if (!beforeSelect && beforeToday(year, month, dayIndex))
            return;
        if (selectDays.hasSelected()) {
            if (selectDays.inSelected(year, month, dayIndex)) {
                if (selectDays.getLast() == null) {
                    onFirstSelectCancel(selectDays.getFirst());
                    selectDays.setFirst(null);

                } else {
                    selectDays.getFirst().setYear(year);
                    selectDays.getFirst().setMonth(month);
                    selectDays.getFirst().setDay(dayIndex);
                    onFirstSelected(selectDays.getFirst());
                    selectDays.setLast(null);
                }
                invalidate();
            } else {
                if (selectDays.getLast() == null) {
                    if (!selectDays.getFirst().after(year, month, dayIndex)) {
                        selectDays.setLast(new PickerDay(year, month, dayIndex));
                        onRangeSelected(selectDays.getFirst(), selectDays.getLast());
                    }
                } else {
                    selectDays.getFirst().setYear(year);
                    selectDays.getFirst().setMonth(month);
                    selectDays.getFirst().setDay(dayIndex);
                    selectDays.setLast(null);
                    onFirstSelected(selectDays.getFirst());
                }
                invalidate();
            }
        } else {
            selectDays.setFirst(new PickerDay(year, month, dayIndex));
            onFirstSelected(selectDays.getFirst());
            invalidate();
        }
    }

    protected void toast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }


    public void setCalendar(Calendar mCalendar) {
        this.mCalendar = mCalendar;
        dayOfMonth = mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        this.firstDayCalendar = (Calendar) mCalendar.clone();
        this.firstDayCalendar.set(Calendar.DAY_OF_MONTH, 1);
    }

    public void setListener(DatePickListener mListener) {
        this.mListener = mListener;
    }

    public void setAdapter(DatePickerAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    @Override
    public void onFirstSelected(PickerDay pickerDay) {
        if (mListener != null) mListener.onFirstSelected(selectDays.getFirst());
        notifyAdapter();
    }

    @Override
    public void onRangeSelected(PickerDay first, PickerDay last) {
        if (mListener != null)
            mListener.onRangeSelected(selectDays.getFirst(), selectDays.getLast());
        notifyAdapter();
    }

    @Override
    public void onFirstSelectCancel(PickerDay pickerDay) {
        if (mListener != null) mListener.onFirstSelectCancel(selectDays.getFirst());
        notifyAdapter();
    }

    private void notifyAdapter() {
        if (mAdapter != null) mAdapter.notifyDataSetChanged();
    }

    public void setDateParameters(DatePickerAdapter.DateModel dateModel) {
        this.defTag = dateModel.defTag;

        this.startYear = dateModel.startYear;

        this.startMonth = dateModel.startMonth;

        this.beforeSelect = dateModel.beforeTodaySelect;

        this.selectDays = dateModel.selectDays;

        this.invalidDays = dateModel.invalidDays;

        this.invalidTips = dateModel.invalidTips;

        this.tags = dateModel.tags;

        this.mostSelectDays = dateModel.mostSelectNum;
    }

    static class DrawParameters {

        int monthAndYearTextSize = (int) Utils.sp2px(14);
        int dateTextSize = (int) Utils.sp2px(12);
        int tipsTextSize = (int) Utils.sp2px(11);

        int lineColor = Color.parseColor("#EFEFEF");
        int montAndYearTextColor = Color.BLACK;
        int dateTextNormalColor = Color.parseColor("#959595");
        int dateTextSelectColor = Color.WHITE;
        int dateBgColor = Color.parseColor("#42B3FE");
        int invalidBgColor = Color.parseColor("#DEDEDE");
        int invalidTextColor = Color.parseColor("#959595");
        int tipsTextColor = Color.parseColor("#A8A8A8");
        int tipsBgColor = Color.parseColor("#DEDEDE");

        float itemWidth = Utils.dp2px(60);
        float itemHeight = Utils.dp2px(50);

        Drawable startBg;
        Drawable startBgOnly;
        Drawable endBg;

        boolean isAutoMeasure = true;//是否自动测量
        boolean beforeSelect = false;//今天之前的日期是否可选


    }
}
