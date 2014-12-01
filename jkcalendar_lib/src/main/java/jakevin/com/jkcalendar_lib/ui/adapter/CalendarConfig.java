package jakevin.com.jkcalendar_lib.ui.adapter;

import android.graphics.Color;
import android.graphics.Typeface;

/**
 * Created by jakevin on 14/12/1.
 */
public class CalendarConfig {

    private Typeface typeface;

    private String PRE_MONTH_ARROW = "<";

    private String NEXT_MONTH_ARROW = ">";

    private String OPEN_BIG_CALENDAR = "V";

    private String CLOSE_BIG_CALENDAR = "X";

    private int LAYOUT_COLOR = Color.BLACK;

    private int HIDE_COLOR = Color.BLACK;

    private int DATE_VIEW_COLOR = Color.WHITE;

    public CalendarConfig setTypeface(Typeface typeface){
        this.typeface = typeface;
        return this;
    }

    public CalendarConfig setPRE_MONTH_ARROW(String PRE_MONTH_ARROW) {
        this.PRE_MONTH_ARROW = PRE_MONTH_ARROW;
        return this;
    }

    public CalendarConfig setNEXT_MONTH_ARROW(String NEXT_MONTH_ARROW) {
        this.NEXT_MONTH_ARROW = NEXT_MONTH_ARROW;
        return this;
    }

    public CalendarConfig setOPEN_BIG_CALENDAR(String OPEN_BIG_CALENDAR) {
        this.OPEN_BIG_CALENDAR = OPEN_BIG_CALENDAR;
        return this;
    }

    public CalendarConfig setCLOSE_BIG_CALENDAR(String CLOSE_BIG_CALENDAR) {
        this.CLOSE_BIG_CALENDAR = CLOSE_BIG_CALENDAR;
        return this;
    }

    public CalendarConfig setLAYOUT_COLOR(int LAYOUT_COLOR) {
        this.LAYOUT_COLOR = LAYOUT_COLOR;
        return this;
    }

    public CalendarConfig setHIDE_COLOR(int HIDE_COLOR) {
        this.HIDE_COLOR = HIDE_COLOR;
        return this;
    }

    public CalendarConfig setDATE_VIEW(int DATE_VIEW_COLOR) {
        this.DATE_VIEW_COLOR = DATE_VIEW_COLOR;
        return this;
    }

    public Typeface getTypeface() {
        return typeface;
    }

    public String getPRE_MONTH_ARROW() {
        return PRE_MONTH_ARROW;
    }

    public String getNEXT_MONTH_ARROW() {
        return NEXT_MONTH_ARROW;
    }

    public String getOPEN_BIG_CALENDAR() {
        return OPEN_BIG_CALENDAR;
    }

    public String getCLOSE_BIG_CALENDAR() {
        return CLOSE_BIG_CALENDAR;
    }

    public int getLAYOUT_COLOR() {
        return LAYOUT_COLOR;
    }

    public int getHIDE_COLOR() {
        return HIDE_COLOR;
    }

    public int getDATE_VIEW_COLOR() {
        return DATE_VIEW_COLOR;
    }
}
