package jakevin.com.jkcalendar;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Calendar;
import java.util.zip.Inflater;

import jakevin.com.jkcalendar_lib.ui.JKCalendar;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((JKCalendar) findViewById(R.id.main_calendar)).step1("2014", "10", "2014", "12");
        ((JKCalendar) findViewById(R.id.main_calendar)).step2(new CalendarConfig()
                        .setLAYOUT_COLOR(Color.BLUE)
                        .setHIDE_COLOR(Color.RED)
                        .setOPEN_BIG_CALENDAR("V")
                        .setCLOSE_BIG_CALENDAR("X")
                        .setPRE_MONTH_ARROW("<")
                        .setNEXT_MONTH_ARROW(">"));
        ((JKCalendar) findViewById(R.id.main_calendar)).setBelowView(LayoutInflater.from(this).inflate(R.layout.layout,null));
        ((JKCalendar) findViewById(R.id.main_calendar)).setOnCalendarListener(new JKCalendar.OnCalendarListener() {
            @Override
            public void onCalendarSelected(Calendar calendar) {
                Toast.makeText(MainActivity.this,calendar.getTime().toString(),Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
