package example.com.todolist;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

public class AlarmReceiver extends BroadcastReceiver {
    public CheckBox reminderchk;
     Ringtone ringtone;
    Button finishringtone;
    @Override
    public void onReceive(final Context context, Intent intent) {
        //this will update the UI with message
        MainActivity inst = MainActivity.instance();
        reminderchk = (CheckBox)inst.findViewById(R.id.reminderChk);
        finishringtone = (Button)inst.findViewById(R.id.finishRingtone);
        inst.setAlarmText("You have the task todo!!!");


        //this will sound the alarm tone
        //this will sound the alarm once, if you wish to
        //raise alarm in loop continuously then use MediaPlayer and setLooping(true)

         Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        // Uri alarmUri = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
      // Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);

        ringtone = RingtoneManager.getRingtone(context, alarmUri);
        ringtone.play();
        finishringtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ringtone.stop();
            }
        });






        //this will send a notification message
        ComponentName comp = new ComponentName(context.getPackageName(),
                AlarmService.class.getName());

        context.startService( (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }


}
