package unipi.samuele.calugi.voxelgo.handlers;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import static unipi.samuele.calugi.voxelgo.utils.VoxelUtils.*;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import unipi.samuele.calugi.voxelgo.R;
import unipi.samuele.calugi.voxelgo.activities.MainActivity;

public class NotificationHandler {

    /**
     * Il NotificationHandler si occupa di gestire tutto il sistema delle notifiche dell'applicazione.
     */

    // Singleton pattern
    private static NotificationHandler notificationHandler;

    public static NotificationHandler getInstance(Application application) {
        if (notificationHandler == null) {
            notificationHandler = new NotificationHandler(application);
        }
        return notificationHandler;
    }

    private final Context context;

    private NotificationHandler(Application application) {
        context = application.getApplicationContext();

        // Registro e creo un canale per le notifiche
        NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL, NOTIFICATION_NAME, NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("VoxelGO notification channel");
        NotificationManager notificationManager = application.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    /**
     * Metodo utilizzato per mostrare le notifiche a schermo
     *
     * @param notificationTitle titolo della notifica
     * @param notificationContent contenuto della notifica
     */
    public void showNotification(CharSequence notificationTitle, CharSequence notificationContent) {

        // Intent utilizzato per lanciare la classe MainActivity quando l'utente clicca sulla notifica
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        // NotificationBuilder utilizzato per personalizzare la notifica e per registrla nel NotificationChannel
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL)
                // Icona della notifica
                .setSmallIcon(R.drawable.icon_collectible_name)
                // Titolo della notifica
                .setContentTitle(notificationTitle)
                // Contenuto della notifica
                .setContentText(notificationContent)
                // Priorità della notifica
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Intent che viene lanciato quando l'utente clicca sulla notifica
                .setContentIntent(pendingIntent)
                // Una volta che l'utente ha cliccato sulla notifica, quest'ultima si cancella
                .setAutoCancel(true);

        // Mostro la notifica a schermo
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(0, builder.build());
    }
}
