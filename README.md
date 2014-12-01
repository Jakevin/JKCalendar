JKCalendar
==========

Accupass Share Custom Calendar View

How to use?
```java
((JKCalendar) findViewById(R.id.main_calendar)).init("2014", "10", "2014", "12");
((JKCalendar) findViewById(R.id.main_calendar)).setOnCalendarListener(new JKCalendar.OnCalendarListener() {
       @Override
       public void onCalendarSelected(Calendar calendar) {

       }
});
        
```

```xml
<jakevin.com.jkcalendar_lib.ui.JKCalendar
        android:id="@+id/main_calendar"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>
```

感謝：
  Jonney - UI/UX (Accupass)
  Ruth  - PM (Accupass)
  Matt - iOS/back-end (Accupass)
