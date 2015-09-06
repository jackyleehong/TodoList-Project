package example.com.todolist;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        //this will update the UI with message
        TaskAddingActivity inst = TaskAddingActivity.instance();
        inst.setAlarmText("Alarm! Wake up! Wake up!");

        //this will sound the alarm tone
        //this will sound the alarm once, if you wish to
        //raise alarm in loop continuously then use MediaPlayer and setLooping(true)

       // Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
          Uri alarmUri = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
      // Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);

        Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();
       /* MediaPlayer mediaPlayer = MediaPlayer.create(context,alarmUri);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();*/

        //this will send a notification message
        ComponentName comp = new ComponentName(context.getPackageName(),
                AlarmService.class.getName());
        context.startService( (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}