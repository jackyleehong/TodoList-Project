package example.com.todolist;

/**
 * Created by OS on 9/11/2015.
 */
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;

public class NotifyService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationManager nNM = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(context.getApplicationContext(), MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent1, 0);
        Notification mNotify = new Notification.Builder(context)
                .setContentTitle("TodoList")
                .setContentText("You have a reminder")
                .setSmallIcon(R.drawable.todolist)
                .setContentIntent(pIntent)
                .setSound(sound)
                .build();
        nNM.notify(1, mNotify);
    }
}