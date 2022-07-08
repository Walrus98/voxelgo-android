package unipi.samuele.calugi.voxelgo.handlers;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.NoCopySpan;

import static unipi.samuele.calugi.voxelgo.utils.VoxelUtils.*;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import unipi.samuele.calugi.voxelgo.R;
import unipi.samuele.calugi.voxelgo.activities.MainActivity;

public class NotificationHandler {

    private final Context context;

    public NotificationHandler(Application application) {
        context = application.getApplicationContext();

        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL, NOTIFICATION_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("VoxelGO notification channel");
        NotificationManager notificationManager = application.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    public void showNotification(CharSequence notificationTitle, CharSequence notificationContent) {

        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL)
                .setSmallIcon(R.drawable.icon_collectible_name)
                .setContentTitle(notificationTitle)
                .setContentText(notificationContent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(0, builder.build());
    }
}
