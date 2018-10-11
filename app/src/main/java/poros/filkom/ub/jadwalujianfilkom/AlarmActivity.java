package poros.filkom.ub.jadwalujianfilkom;

import android.app.Activity;
import android.os.Bundle;
import java.util.Calendar;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AlarmActivity extends Activity {
    /** Called when the activity is first created. */
    /* Author J M */
    TimePicker TimePicker;
    DatePicker DatePicker;
    Button Setalarm;

    TimePickerDialog timePickerDialog;

    final static int RQS_1 = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        DatePicker =(DatePicker)findViewById(R.id.datePicker1);
        TimePicker=(TimePicker)findViewById(R.id.timePicker1);
        Calendar now = Calendar.getInstance();

        DatePicker.init(
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH),
                null);

        TimePicker.setCurrentHour(now.get(Calendar.HOUR_OF_DAY));
        TimePicker.setCurrentMinute(now.get(Calendar.MINUTE));

        Setalarm = (Button) findViewById(R.id.Setalarm);
        Setalarm.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                Calendar current = Calendar.getInstance();

                Calendar cal = Calendar.getInstance();
                cal.set(DatePicker.getYear(),
                        DatePicker.getMonth(),
                        DatePicker.getDayOfMonth(),
                        TimePicker.getCurrentHour(),
                        TimePicker.getCurrentMinute(),
                        00);

                if(cal.compareTo(current) <= 0){
                    //The set Date/Time already passed
                    Toast.makeText(getApplicationContext(),
                            "Invalid Date/Time",
                            Toast.LENGTH_LONG).show();
                }else{
                    setAlarm(cal);
                }

            }});

    }


    private void setAlarm(Calendar targetCal) {

        //
        Toast.makeText(AlarmActivity.this, "Alarm is set at " + targetCal.getTime(),
                Toast.LENGTH_LONG).show();
        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getBaseContext(), RQS_1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(),pendingIntent);

    }

}