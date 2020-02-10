# hotaldatepicker
an android hotal datepicker

### 效果

![](./capture/datepicker.gif)

### 引用

```groovy
implementation 'com.dreamlin.hotaldatepicker:hotaldatepicker:1.0.0'
```

### 属性

| 属性                 | 说明                                                         |
| -------------------- | ------------------------------------------------------------ |
| autoMeasure          | 是否自动测量宽度高度，默认为true                             |
| canSelectBeforeDay   | 今天之前的日期是否可选，默认为false不可选                    |
| itemWidth            | 日期格子宽度，不开启自动测量指定具体宽度 才有效（autoMeasure = false） |
| itemHeight           | 日期格子高度，不开启自动测量指定具体高度度 有效（autoMeasure = false） |
| monthAndYearTextSize | 年月标题字体大小，单位sp                                     |
| dateTextSize         | 日期字体大小，单位sp                                         |
| tipsTextSize         | 提示字体大小，单位sp                                         |
| lineColor            | 线的颜色                                                     |
| montAndYearTextColor | 年月标题字体颜色                                             |
| dateBgColor          | 日期背景色                                                   |
| dateNormalTextColor  | 未选中的日期字体颜色                                         |
| dateSelectTextColor  | 选中的日期字体颜色                                           |
| invalidBgColor       | 无效日期的背景颜色                                           |
| invalidTextColor     | 无效日期的字体颜色                                           |
| tipsTextColor        | 提示字体颜色                                                 |
| tipsBgColor          | 提示字体背景色                                               |
| startBg              | 单选一个日期的背景drawable                                   |
| startBgOnly          | 多选日期后，选中的第一个日期的背景drawable                   |
| endBg                | 多选日期后，选中的最后一个日期的背景drawable                 |

### 其他可选项

| DateModel      | 属性说明                                      |
| -------------- | --------------------------------------------- |
| startYear      | 开始年份，默认是当前年                        |
| startMonth     | 开始月份，默认是当前月                        |
| monthCount     | 显示几个月，默认12个月                        |
| mostSelectNum  | 最多可以选择多少天，默认100天                 |
| leastSelectNum | 至少需要选择多少天，默认2天                   |
| defTag         | 默认标签，默认为空                            |
| invalidTips    | 选择包含无效日期时的提示，默认为 包含无效日期 |
| selectDays     | 选中的日期                                    |
| invalidDays    | 无效的日期                                    |
| tags           | 标签列表                                      |

