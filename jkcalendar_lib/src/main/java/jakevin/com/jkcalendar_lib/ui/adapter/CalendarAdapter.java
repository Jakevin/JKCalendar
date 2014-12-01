package jakevin.com.jkcalendar_lib.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import jakevin.com.jkcalendar_lib.ui.JKCalendar;

/**
 * Gallery月曆用的Adapter
 *
 * Created by jakevin on 14/11/20.
 */
public class CalendarAdapter extends BaseAdapter {

    protected LayoutInflater layoutInflater;
    Context mContext;

    ArrayList<Boolean> clicks;

    ArrayList<Calendar> calendars;

    public CalendarAdapter(Context context, ArrayList<Calendar> calendars){
        this.layoutInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.calendars = calendars;

        clicks = new ArrayList<Boolean>();
        for(int i = 0 ; i < calendars.size() ; i++){
            clicks.add(false);
        }

    }

    private class ViewHodler{
        TextView dayOfMonth;
        TextView dayOfWeek;
    }

    public void setClickIndex(int position){
        for(int i = 0 ; i < clicks.size() ; i++){
            clicks.set(i,false);
        }
        if(position>=0){
            clicks.set(position,true);
        }
    }

    @Override
    public int getCount() {
        return calendars.size();
    }

    @Override
    public Calendar getItem(int i) {
        return calendars.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHodler viewHodler;

        if (convertView == null) {
            convertView = layoutInflater.inflate(JKCalendar.getResourseIdByName(mContext.getPackageName(),"layout","calender_adpter_layout"), null);

            viewHodler = new ViewHodler();

            viewHodler.dayOfMonth = (TextView) convertView.findViewById(JKCalendar.getResourseIdByName(mContext.getPackageName(),"id","day_of_month"));

            viewHodler.dayOfWeek = (TextView) convertView.findViewById(JKCalendar.getResourseIdByName(mContext.getPackageName(),"id","day_of_week"));

            convertView.setTag(viewHodler);
        }

        viewHodler = (ViewHodler) convertView.getTag();

        convertView.setLayoutParams(new Gallery.LayoutParams(convertDipToPixels(mContext,50), convertDipToPixels(mContext,60)));

        Calendar tempCalendar = calendars.get(position);

        viewHodler.dayOfMonth.setText(String.format("%02d",tempCalendar.get(Calendar.DAY_OF_MONTH)));

        viewHodler.dayOfMonth.setTag(tempCalendar);

        SimpleDateFormat parserSDF = new SimpleDateFormat("EEE", Locale.ENGLISH);

        viewHodler.dayOfWeek.setText(parserSDF.format(tempCalendar.getTime()));

        if(clicks.get(position)){
            viewHodler.dayOfWeek.setTextColor(mContext.getResources().getColor(android.R.color.holo_blue_light));
            viewHodler.dayOfMonth.setTextColor(mContext.getResources().getColor(android.R.color.holo_blue_light));
        }else{
            viewHodler.dayOfWeek.setTextColor(mContext.getResources().getColor(android.R.color.darker_gray));
            viewHodler.dayOfMonth.setTextColor(mContext.getResources().getColor(android.R.color.darker_gray));
        }

        return convertView;
    }

    public static int convertDipToPixels(Context context, float dips) {
        return (int) (dips * context.getResources().getDisplayMetrics().density + 0.5f);
    }
}
