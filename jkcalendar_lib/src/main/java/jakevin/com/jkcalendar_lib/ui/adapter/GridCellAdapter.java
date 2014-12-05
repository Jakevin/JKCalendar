package jakevin.com.jkcalendar_lib.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import jakevin.com.jkcalendar_lib.R;
import jakevin.com.jkcalendar_lib.ui.JKCalendar;

/**
 * 日曆專用Adapter
 *
 * Created by jakevin on 14/11/11.
 */
public class GridCellAdapter extends ArrayAdapter<Calendar> implements View.OnClickListener {

    private static final String tag = "GridCellAdapter";
    private final Context mContext;

    private final List<String> list;
    private ArrayList<Calendar> calendars;

    private static final int DAY_OFFSET = 1;
    private int thisMonthStartIndex = 0;
    private int thisMonthEndIndex = 0;
    private final String[] weekdays = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private final String[] months = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11","12"};

    private Calendar _calendar;

//    private final int month, year;
    private int daysInMonth, prevMonthDays;
    private int currentDayOfMonth;
    private int currentWeekDay;

    private final HashMap eventsPerMonthMap;
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MMM-yyyy",Locale.US);

    private int color_hide = Color.GRAY;
    private int color_high = Color.BLACK;

    static class ViewHolder {
        public View ciricleView;
        public TextView gridcell;
        public TextView num_events_per_day;
        public TextView event_dot;
    }

    // Days in Current Month
    public GridCellAdapter(Context context, int textViewResourceId, ArrayList<Calendar> items, Calendar calendar) {
        super(context, textViewResourceId, items);
        this.mContext = context;
        this.list = new ArrayList<String>();
        this._calendar = calendar;
        this.calendars = items;

//        this.month = month;
//        this.year = year;
//        //JKLog.d(tag, "==> Passed in Date FOR Month: " + month + " " + "Year: " + year);
//        Calendar calendar = Calendar.getInstance();

        setCurrentDayOfMonth(calendar.get(Calendar.DAY_OF_MONTH));
        setCurrentWeekDay(calendar.get(Calendar.DAY_OF_WEEK));
//        //JKLog.d(tag, "New Calendar:= " + calendar.getTime().toString());
//        //JKLog.d(tag, "CurrentDayOfWeek :" + getCurrentWeekDay());
//        //JKLog.d(tag, "CurrentDayOfMonth :" + getCurrentDayOfMonth());

        // Print Month
        printMonth(_calendar.get(Calendar.MONTH) + 1, _calendar.get(Calendar.YEAR));

        // Find Number of Events
        eventsPerMonthMap = findNumberOfEventsPerMonth(_calendar.get(Calendar.YEAR), _calendar.get(Calendar.MONTH));

        color_hide = Color.GRAY;
        color_high = mContext.getResources().getColor(android.R.color.holo_blue_light);
    }

    public void setColor(int high,int hide){
        color_hide = hide;
        color_high = high;
    }

    private String getMonthAsString(int i) {
        return months[i];
    }

    private String getWeekDayAsString(int i) {
        return weekdays[i];
    }

    public static int getNumberOfDaysOfMonth(Calendar c) {
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public Calendar getItem(int position) {
        return calendars.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    /**
     * Prints Month
     *
     * @param mm
     * @param yy
     */
    private void printMonth(int mm, int yy) {
//        //JKLog.d(tag, "==> printMonth: mm: " + mm + " " + "yy: " + yy);
        // The number of days to leave blank at
        // the start of this month.
        int trailingSpaces = 0;
        int leadSpaces = 0;
        int daysInPrevMonth = 0;
        int prevMonth = 0;
        int prevYear = 0;
        int nextMonth = 0;
        int nextYear = 0;

        int currentMonth = mm - 1;
        String currentMonthName = getMonthAsString(currentMonth);
        daysInMonth = getNumberOfDaysOfMonth(_calendar);

//        //JKLog.d(tag, "Current Month: " + " " + currentMonthName + " having " + daysInMonth + " days.");

        // Gregorian Calendar : MINUS 1, set to FIRST OF MONTH
        GregorianCalendar cal = new GregorianCalendar(yy, currentMonth, 1);
        //JKLog.d(tag, "Gregorian Calendar:= " + cal.getTime().toString());

        Calendar prevC = Calendar.getInstance();

        if (currentMonth == 11) {
            prevMonth = currentMonth - 1;
            prevC.set(Calendar.MONTH,prevMonth);
            daysInPrevMonth = getNumberOfDaysOfMonth(prevC);
            nextMonth = 0;
            prevYear = yy;
            nextYear = yy + 1;
            //JKLog.d(tag, "*->PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
        } else if (currentMonth == 0) {
            prevMonth = 11;
            prevYear = yy - 1;
            nextYear = yy;
            prevC.set(Calendar.MONTH,prevMonth);
            daysInPrevMonth = getNumberOfDaysOfMonth(prevC);
            nextMonth = 1;
            //JKLog.d(tag, "**--> PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
        } else {
            prevMonth = currentMonth - 1;
            nextMonth = currentMonth + 1;
            nextYear = yy;
            prevYear = yy;
            prevC.set(Calendar.MONTH,prevMonth);
            daysInPrevMonth = getNumberOfDaysOfMonth(prevC);
            //JKLog.d(tag, "***---> PrevYear: " + prevYear + " PrevMonth:" + prevMonth + " NextMonth: " + nextMonth + " NextYear: " + nextYear);
        }

        // Compute how much to leave before before the first day of the
        // month.
        // getDay() returns 0 for Sunday.
        int currentWeekDay = cal.get(Calendar.DAY_OF_WEEK) - 1;
        trailingSpaces = currentWeekDay;

        //JKLog.d(tag, "Week Day:" + currentWeekDay + " is " + getWeekDayAsString(currentWeekDay));
        //JKLog.d(tag, "No. Trailing space to Add: " + trailingSpaces);
        //JKLog.d(tag, "No. of Days in Previous Month: " + daysInPrevMonth);

        if (cal.isLeapYear(cal.get(Calendar.YEAR)) && mm == 1) {
            ++daysInMonth;
        }

        // Trailing Month days
        for (int i = 0; i < trailingSpaces; i++) {
//            //JKLog.d(tag, "PREV MONTH:= " + prevMonth + " => " + getMonthAsString(prevMonth) + " " + String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i));
            list.add("GREY-"+ String.valueOf((daysInPrevMonth - trailingSpaces + DAY_OFFSET) + i)  + "-" + getMonthAsString(prevMonth) + "-" + prevYear);
        }

        //紀錄Month是第幾格開始
        thisMonthStartIndex = list.size();

        // Current Month Days
        for (int i = 1; i <= daysInMonth; i++) {
            //JKLog.d(currentMonthName, String.valueOf(i) + " " + getMonthAsString(currentMonth) + " " + yy);
            if (i == getCurrentDayOfMonth()) {
                list.add("BLACK-" + String.valueOf(i) +  "-" + getMonthAsString(currentMonth) + "-" + yy);
            } else {
                list.add("WHITE-" + String.valueOf(i) +  "-" + getMonthAsString(currentMonth) + "-" + yy);
            }
        }

        //紀錄Month是第幾格結束
        thisMonthEndIndex = list.size();

        // Leading Month days
        for (int i = 0; i < list.size() % 7; i++) {
            //JKLog.d(tag, "NEXT MONTH:= " + getMonthAsString(nextMonth));
            list.add( "GREY-" + String.valueOf(i + 1) +"-" + getMonthAsString(nextMonth) + "-" + nextYear);
        }
    }

    /**
     * NOTE: YOU NEED TO IMPLEMENT THIS PART Given the YEAR, MONTH, retrieve
     * ALL entries from a SQLite database for that month. Iterate over the
     * List of All entries, and get the dateCreated, which is converted into
     * day.
     *
     * @param year
     * @param month
     * @return
     */
    private HashMap findNumberOfEventsPerMonth(int year, int month) {
        HashMap map = new HashMap<String, Integer>();
        return map;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
//            convertView = LayoutInflater.from(mContext).inflate(JKCalendar.getResourseIdByName(mContext.getPackageName(),"layout","calendar_day_gridcell"), parent, false);
//            holder = new ViewHolder();
//            holder.gridcell = (TextView) convertView.findViewById(JKCalendar.getResourseIdByName(mContext.getPackageName(),"id","calendar_day_gridcell"));
//            holder.ciricleView = (ImageView) convertView. findViewById(JKCalendar.getResourseIdByName(mContext.getPackageName(),"id","calendar_day_circle"));
//            holder.num_events_per_day = (TextView) convertView.findViewById(JKCalendar.getResourseIdByName(mContext.getPackageName(),"id","num_events_per_day"));

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.calendar_day_gridcell, parent, false);

            holder = new ViewHolder();
            holder.gridcell = (TextView) convertView.findViewById(R.id.calendar_day_gridcell);
            holder.ciricleView = (ImageView) convertView. findViewById(R.id.calendar_day_circle);
            holder.num_events_per_day = (TextView) convertView.findViewById(R.id.num_events_per_day);


            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }


        //JKLog.d(tag, "Current Day: " + getCurrentDayOfMonth());
        String[] day_color = list.get(position).split("-");
        String theday = day_color[1];
        String themonth = day_color[2];
        String theyear = day_color[3];
        if ((!eventsPerMonthMap.isEmpty()) && (eventsPerMonthMap != null)) {
            if (eventsPerMonthMap.containsKey(theday)) {
                Integer numEvents = (Integer) eventsPerMonthMap.get(theday);
                holder.num_events_per_day.setText(numEvents.toString());
            }
        }

        // Set the Day GridCell
        holder.gridcell.setText(theday);
        //JKLog.d(tag, "Setting GridCell " + theday + "-" + themonth + "-" + theyear);

        if (day_color[0].equals("GREY")) {
            holder.gridcell.setTextColor(color_hide);
        }
        if (day_color[0].equals("BLACK")) {
            GradientDrawable drawable = new GradientDrawable();
            drawable.setColor(Color.WHITE);
            drawable.setShape(GradientDrawable.RADIAL_GRADIENT);
            drawable.setSize((int)(holder.ciricleView.getWidth()*0.8), (int)(holder.ciricleView.getWidth()*0.8));

            holder.ciricleView.setBackgroundDrawable(drawable);
            holder.gridcell.setTextColor(color_high);
        }else{
            holder.ciricleView.setBackgroundDrawable(null);
        }
        if (day_color[0].equals("WHITE")) {
            holder.gridcell.setTextColor(Color.WHITE);
        }

        //表示這個月內
        if (position>=thisMonthStartIndex && position<thisMonthEndIndex) {
//            if (position%7==0 || position%7==6) {
//                holder.gridcell.setTextColor(mContext.getResources().getColor(R.color.accupass_blue));
//            }
            holder.ciricleView.setTag("00".substring(0,2-theday.length())+theday + "-" + themonth + "-" + theyear);

            holder.ciricleView.setOnClickListener(this);
        }

        return convertView;
    }

    @Override
    public void onClick(View view) {

        SimpleDateFormat df2 = new SimpleDateFormat("dd-MM-yyy");
        Calendar calendarData = Calendar.getInstance();

        try {
            calendarData.setTime(df2.parse((String) view.getTag()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

//        Calendar calendarData = (Calendar) view.getTag();

        if(iEventDateClick!=null){
            iEventDateClick.onClick(calendarData);
        }

    }

    public int getCurrentDayOfMonth() {
        return currentDayOfMonth;
    }

    private void setCurrentDayOfMonth(int currentDayOfMonth) {
        this.currentDayOfMonth = currentDayOfMonth;
    }

    public void setCurrentWeekDay(int currentWeekDay) {
        this.currentWeekDay = currentWeekDay;
    }

    public int getCurrentWeekDay() {
        return currentWeekDay;
    }

    IEventDateClick iEventDateClick;

    public void setiEventDateClick(IEventDateClick iEventDateClick) {
        this.iEventDateClick = iEventDateClick;
    }

    public interface IEventDateClick{
        public void onClick(Calendar calendarData);
    }
}
