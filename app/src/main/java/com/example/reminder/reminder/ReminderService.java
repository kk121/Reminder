package com.example.reminder.reminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;

public class ReminderService extends WakeReminderIntentService {

  public ReminderService () {
    super("ReminderService");
  }

  @Override
  void doReminderWork (Intent intent) {
    Log.d("ReminderService", "Doing work.");
    Long rowId = intent.getExtras().getLong(RemindersDbAdapter.KEY_ROWID);

    NotificationManager mgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    Intent notificationIntent = new Intent(this, ReminderEditActivity.class);
    notificationIntent.putExtra(RemindersDbAdapter.KEY_ROWID, rowId);

    PendingIntent pi = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT);

    Notification.Builder builder = new Notification.Builder(this);
    builder.setAutoCancel(true);
    builder.setTicker(getString(R.string.notify_new_task_message));
    builder.setContentTitle(getString(R.string.notify_new_task_title));
    builder.setContentText(getString(R.string.notify_new_task_message));
    builder.setSmallIcon(android.R.drawable.stat_sys_warning);
    builder.setContentIntent(pi);
    builder.setOngoing(true);
    builder.setNumber(100);
    builder.build();

    int id = (int) ((long) rowId);
    mgr.notify(id, builder.getNotification());


  }
}
