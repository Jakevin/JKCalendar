package jakevin.com.jkcalendar_lib.ui;

import android.content.Context;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import jakevin.com.jkcalendar_lib.ui.adapter.CalendarAdapter;
import jakevin.com.jkcalendar_lib.ui.adapter.GridCellAdapter;

/**
 * Created by jakevin on 14/11/29.
 */
public class JKCalendar extends RelativeLayout {

    private Context mContext;

    //展開月曆Layout
    private LinearLayout calendarLayout;

    //展開月曆的月份
    private TextView calendar_month_in_layout;
    private View pre_month_in_layout;
    private TextView pre_month_icon_in_layout;
    private View next_month_in_layout;
    private TextView next_month_icon_in_layout;
    private TextView calendar_close;


    //月份顯示
    private TextView monthText;
    private TextView preMonth;
    private TextView nextMonth;

    //左右轉動月曆
    private Gallery dateViewpager;

    //Gallery月曆用的Adapter
    CalendarAdapter calendarsAdapter;

    //展開的月曆
    private GridView calendarView;

    //展開月曆的Adapter
    private GridCellAdapter adapter;

    //展開月曆
    private TextView calendar_open;

    //存放所有要顯示的日期
    ArrayList<Calendar> calendars = new ArrayList<Calendar>();

    //今天的日期
    private Calendar _calendar;

    private int day, month, year;

    int today_index = 0;

    OnCalendarListener onCalendarListener;

    public JKCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }


    public JKCalendar(Context context) {
        super(context);
        mContext = context;
    }

    public void init(String startYear, String startMonth, String endYear, String endMonth) {

        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM");

        Calendar startCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();

        try {
            startCalendar.setTime(df2.parse(startYear + "-" + startMonth));
            endCalendar.setTime(df2.parse(endYear + "-" + endMonth));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        endCalendar.set(Calendar.DAY_OF_MONTH, endCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));

        addView(LayoutInflater.from(mContext).inflate(getResourseIdByName(mContext.getPackageName(),"layout","calendar_main_layout"), null));

        //Today Calendar
        _calendar = Calendar.getInstance(Locale.getDefault());

        _calendar.set(Calendar.HOUR_OF_DAY, 00);
        _calendar.set(Calendar.MINUTE, 00);
        _calendar.set(Calendar.SECOND, 00);
        _calendar.set(Calendar.MILLISECOND, 00);

        //這個月是幾月
        month = _calendar.get(Calendar.MONTH) + 1;

        //今天是幾號
        day = _calendar.get(Calendar.DAY_OF_MONTH);

        //今年是幾年
        year = _calendar.get(Calendar.YEAR);

        long aDayInMilliSecond = 60 * 60 * 24 * 1000;     //一天的毫秒數
        long dayDiff = (endCalendar.getTimeInMillis() - startCalendar.getTimeInMillis()) / aDayInMilliSecond;

        if (dayDiff == 0 && endCalendar.get(Calendar.DAY_OF_YEAR) != _calendar.get(Calendar.DAY_OF_YEAR)) {
            dayDiff += 1;
        }

        for (int i = 0; i <= dayDiff; i++) {
            Calendar calendar = Calendar.getInstance(Locale.getDefault());
            calendar.setTime(startCalendar.getTime());

            calendar.add(Calendar.DAY_OF_MONTH, +i);

            if (calendar.get(Calendar.DAY_OF_YEAR) == _calendar.get(Calendar.DAY_OF_YEAR)) {
                today_index = i;
            }
            calendars.add(calendar);
        }

        initView();

        //初始化展開的月曆
        initializeCalendar();

        //初始化左右轉動的月曆
        setHeaderView();
    }


    int currentIndex = 0;

    private void setHeaderView() {

        calendarsAdapter = (new CalendarAdapter(mContext, calendars));

        dateViewpager.setAdapter(calendarsAdapter);

        //只讓最後停下來的地方有Callback
//        dateViewpager.setCallbackDuringFling(false);
        dateViewpager.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                currentIndex = i;

                Calendar dateCalendar = calendars.get(i);

                monthText.setText(String.format("%tb", dateCalendar.getTime()));

                int pre2 = i - 2;
                int next2 = i + 2;

                if (pre2 >= 0) {
                    Calendar preDateCalendar = calendars.get(pre2);
                    if (preDateCalendar.get(Calendar.MONTH) != dateCalendar.get(Calendar.MONTH)) {
                        Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in);
                        preMonth.setText(String.format("%tb", preDateCalendar.getTime()));
                        preMonth.startAnimation(animation);
                    } else {
                        Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_out);
                        preMonth.setText("");
                        preMonth.startAnimation(animation);
                    }
                }

                if (next2 < calendars.size()) {
                    Calendar nextDateCalendar = calendars.get(next2);
                    if (nextDateCalendar.get(Calendar.MONTH) != dateCalendar.get(Calendar.MONTH)) {
                        Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_in);
                        nextMonth.setText(String.format("%tb", nextDateCalendar.getTime()));
                        nextMonth.startAnimation(animation);
                    } else {
                        Animation animation = AnimationUtils.loadAnimation(mContext, android.R.anim.fade_out);
                        nextMonth.setText("");
                        nextMonth.startAnimation(animation);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dateViewpager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                //手指放下時，所有顏色都回歸正常
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    calendarsAdapter.setClickIndex(-1);
                    calendarsAdapter.notifyDataSetChanged();
                }

                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                int index = 0;
                                while (true) {
                                    index = currentIndex;
                                    Thread.sleep(300);
                                    if (index == currentIndex) {
                                        handler.sendEmptyMessage(index);
                                        break;
                                    } else {
                                    }
                                }
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }

                return false;
            }
        });

        //預設今天的活動為主
        dateViewpager.setSelection(today_index);

        //取得今天活動列表
        handler.sendEmptyMessage(today_index);


        //展開日歷
        calendar_open.setText("V");
        calendar_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                refreshCalendar(calendars.get(dateViewpager.getSelectedItemPosition()));
                calendarLayout.setVisibility(View.VISIBLE);

                Animation in_Anim = AnimationUtils.loadAnimation(mContext, getResourseIdByName(mContext.getPackageName(),"anim","slide_in_top"));

                in_Anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        calendarLayout.clearAnimation();

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                calendarLayout.startAnimation(in_Anim);
            }
        });
    }

    public void initializeCalendar() {


        //for這個月使用的ARRAYLIST
        ArrayList<Calendar> tempCalendarDatas = new ArrayList<Calendar>();

        for (Calendar calendar : calendars) {
            if (calendar.get(Calendar.MONTH) == calendars.get(today_index).get(Calendar.MONTH)) {
                tempCalendarDatas.add(calendar);
            }
        }

        // Initialised
        adapter = new GridCellAdapter(mContext, getResourseIdByName(mContext.getPackageName(),"id","calendar_day_gridcell"), tempCalendarDatas, calendars.get(today_index));
        adapter.notifyDataSetChanged();
        calendarView.setAdapter(adapter);

        adapter.setiEventDateClick(listen);

        setGridViewHeightBasedOnChildren(calendarView, 7);

        calendarLayout.setVisibility(View.GONE);
    }

    /**
     * 重新整理大月歷
     *
     * @param selectCalendar
     */
    public void refreshCalendar(Calendar selectCalendar) {
        //設置月份
        calendar_month_in_layout.setText(String.format("%tb", selectCalendar.getTime()));

        //關閉月曆
        calendar_close.setText("X");
        calendar_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeCal(0, false);
                calendar_close.setOnClickListener(null);
            }
        });

        //回上個月
        int pre_diffYear = Math.abs(selectCalendar.get(Calendar.YEAR) - calendars.get(0).get(Calendar.YEAR));
        if ((pre_diffYear * 12 + selectCalendar.get(Calendar.MONTH)) > calendars.get(0).get(Calendar.MONTH)) {
            pre_month_icon_in_layout.setVisibility(View.VISIBLE);
            pre_month_in_layout.setVisibility(View.VISIBLE);
            pre_month_icon_in_layout.setText("<");

            pre_month_in_layout.setTag(selectCalendar);
            pre_month_in_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(((Calendar) view.getTag()).getTime());
                    calendar.add(Calendar.MONTH, -1);
                    refreshCalendar(calendar);
                }
            });
        } else {
            pre_month_icon_in_layout.setVisibility(View.GONE);
            pre_month_in_layout.setVisibility(View.GONE);
        }

        //去下個月
        int diffMonth = getDiffOfMonth(selectCalendar.getTime(), calendars.get(calendars.size() - 1).getTime());
        if (diffMonth > 0) {
            next_month_icon_in_layout.setVisibility(View.VISIBLE);
            next_month_in_layout.setVisibility(View.VISIBLE);
            next_month_icon_in_layout.setText(">");

            next_month_in_layout.setTag(selectCalendar);
            next_month_in_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(((Calendar) view.getTag()).getTime());
                    calendar.add(Calendar.MONTH, 1);
                    refreshCalendar(calendar);
                }
            });
        } else {
            next_month_icon_in_layout.setVisibility(View.GONE);
            next_month_in_layout.setVisibility(View.GONE);
        }


        ArrayList<Calendar> tempCalendarDatas = new ArrayList<Calendar>();

        for (Calendar calendar : calendars) {
            if (calendar.get(Calendar.MONTH) == selectCalendar.get(Calendar.MONTH)) {
                tempCalendarDatas.add(calendar);
            }
        }

        // Initialised
        adapter = new GridCellAdapter(mContext, getResourseIdByName(mContext.getPackageName(),"id","calendar_day_gridcell"), tempCalendarDatas, selectCalendar);

        adapter.notifyDataSetChanged();

        calendarView.setAdapter(adapter);

        adapter.setiEventDateClick(listen);

        setGridViewHeightBasedOnChildren(calendarView, 7);
    }

    private void initView() {
        //初始化VIEW
        dateViewpager = (Gallery) findViewById(getResourseIdByName(mContext.getPackageName(),"id","date_viewpager"));

        calendar_month_in_layout = (TextView) findViewById(getResourseIdByName(mContext.getPackageName(),"id","calendar_month_in_layout"));
        pre_month_icon_in_layout = (TextView) findViewById(getResourseIdByName(mContext.getPackageName(),"id","pre_month_icon_in_layout"));
        pre_month_in_layout = findViewById(getResourseIdByName(mContext.getPackageName(),"id","pre_month_in_layout"));
        next_month_icon_in_layout = (TextView) findViewById(getResourseIdByName(mContext.getPackageName(),"id","next_month_icon_in_layout"));
        next_month_in_layout = findViewById(getResourseIdByName(mContext.getPackageName(),"id","next_month_in_layout"));

        calendar_close = (TextView) findViewById(getResourseIdByName(mContext.getPackageName(),"id","calendar_close"));

        calendar_open = (TextView) findViewById(getResourseIdByName(mContext.getPackageName(),"id","calendar_open"));

        monthText = (TextView) findViewById(getResourseIdByName(mContext.getPackageName(),"id","calendar_month"));
        preMonth = (TextView) findViewById(getResourseIdByName(mContext.getPackageName(),"id","calendar_pre_month"));
        nextMonth = (TextView) findViewById(getResourseIdByName(mContext.getPackageName(),"id","calendar_next_month"));

        calendarView = (GridView) findViewById(getResourseIdByName(mContext.getPackageName(),"id","calendar"));

        calendarLayout = (LinearLayout) findViewById(getResourseIdByName(mContext.getPackageName(),"id","calendar_layout"));
    }

    public GridCellAdapter.IEventDateClick listen = new GridCellAdapter.IEventDateClick() {
        @Override
        public void onClick(Calendar calendar) {

            long aDayInMilliSecond = 60 * 60 * 24 * 1000;     //一天的毫秒數
            long dayDiff = (calendar.getTimeInMillis() - (_calendar.getTimeInMillis())) / aDayInMilliSecond;

            if (dayDiff == 0 && calendar.get(Calendar.DAY_OF_YEAR) != _calendar.get(Calendar.DAY_OF_YEAR)) {
                dayDiff += 1;
            }

            dateViewpager.setSelection((int) (today_index + dayDiff));

            closeCal((int) (today_index + dayDiff), true);

        }
    };

    private void closeCal(final int index, final boolean hasSend) {
        Animation out_Anim = AnimationUtils.loadAnimation(mContext, getResourseIdByName(mContext.getPackageName(),"anim","slide_out_top"));
        out_Anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                calendarLayout.setVisibility(View.GONE);
                calendarLayout.clearAnimation();
                if (hasSend)
                    handler.sendEmptyMessage(index);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        calendarLayout.startAnimation(out_Anim);
    }

    android.os.Handler handler = new android.os.Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Calendar dateCalendar = calendars.get(msg.what);

            if (onCalendarListener != null) {
                onCalendarListener.onCalendarSelected(dateCalendar);
            }

            calendarsAdapter.setClickIndex(msg.what);

            calendarsAdapter.notifyDataSetChanged();
        }
    };

    /**
     * 重新計算GridView的高度
     *
     * @param gridView
     */
    public static void setGridViewHeightBasedOnChildren(GridView gridView, int columns) {

        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount() / columns; i++) {
            View listItem = listAdapter.getView(i, null, gridView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight + (listAdapter.getCount() - 1);
        gridView.setLayoutParams(params);
        gridView.requestLayout();
    }

    /**
     * 获取量日期之间相差的月数
     *
     * @param starDate
     * @param endDate
     * @return
     */
    public static int getDiffOfMonth(Date starDate, Date endDate) {
        Calendar star = Calendar.getInstance();
        star.setTime(starDate);
        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        int diffYear = star.get(Calendar.YEAR) - end.get(Calendar.YEAR);
        int diffMonth = star.get(Calendar.MONTH) - end.get(Calendar.MONTH);
        return Math.abs(diffYear * 12 + diffMonth);
    }

    public static int getResourseIdByName(String packageName, String className,
                                          String name) {
        Class r = null;
        int id = 0;
        try {
            r = Class.forName(packageName + ".R");

            Class[] classes = r.getClasses();
            Class desireClass = null;

            for (int i = 0; i < classes.length; i++) {
                if (classes[i].getName().split("\\$")[1].equals(className)) {
                    desireClass = classes[i];
                    break;
                }
            }

            if (desireClass != null)
                id = desireClass.getField(name).getInt(desireClass);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return id;

    }

    public void setOnCalendarListener(OnCalendarListener onCalendarListener) {
        this.onCalendarListener = onCalendarListener;
    }

    public interface OnCalendarListener {
        public void onCalendarSelected(Calendar calendar);
    }
}
