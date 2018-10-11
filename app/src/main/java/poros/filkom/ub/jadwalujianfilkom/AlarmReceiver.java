package poros.filkom.ub.jadwalujianfilkom;


import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.sax.StartElementListener;
import android.telephony.SmsManager;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "SEKARANG JADWAL KAMU UTSS!!!!! Cek Aplikasi Jadwal Ujian FILKOM UB untuk mengetahui lokasi Ujian kamu!!", Toast.LENGTH_LONG).show();
        Vibrator vib=(Vibrator)context.getSystemService(context.VIBRATOR_SERVICE);    //for Vibration
        vib.vibrate(2000);

        Intent i=new Intent(context,song.class);  //song class contain media song
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }
}