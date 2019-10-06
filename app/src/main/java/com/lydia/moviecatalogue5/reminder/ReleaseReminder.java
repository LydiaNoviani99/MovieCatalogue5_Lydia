package com.lydia.moviecatalogue5.reminder;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.lydia.moviecatalogue5.BuildConfig;
import com.lydia.moviecatalogue5.R;
import com.lydia.moviecatalogue5.activity.MainActivity;
import com.lydia.moviecatalogue5.model.MovieData;
import com.lydia.moviecatalogue5.response.MovieResponse;
import com.lydia.moviecatalogue5.utils.ApiUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class ReleaseReminder extends BroadcastReceiver {
    public int ID_RELEASE = 100;
    private static final String API_KEY = BuildConfig.TMDB_API_KEY;
    @Override
    public void onReceive(Context context, Intent intent) {
        String message = context.getString(R.string.release_reminder_message);
        getRelease(context,  message);
    }

    private void showNotification(Context context, String title, String message, int id) {
        try {

            String CHANNEL_ID = "channel_2";
            String CHANNEL_NAME = "Release";

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                    .setSound(sound)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
                builder.setChannelId(CHANNEL_ID);
                if (notificationManager != null) {
                    notificationManager.createNotificationChannel(channel);
                }
            }

            Notification notification = builder.build();
            notificationManager.notify(id, notification);
        } catch (Exception e) {
            Log.d("showNotif ", e.getMessage());
        }
    }

    private void getRelease(final Context context  ,String message) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        Date date = new Date();
        String today = dateFormat.format(date);
        new ApiUtils();
        Call<MovieResponse> call = ApiUtils.getApi().getReleaseToday(API_KEY, today, today);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    ArrayList<MovieData> movieData = new ArrayList<>(Objects.requireNonNull(response.body()).getMovies());
                    List<MovieData> listMovies = response.body().getMovies();
                    for (int i = 0; i < listMovies.size(); i++) {
                        String movieTitle = listMovies.get(i).getMovieName();
                        showNotification(context, movieTitle, message, ID_RELEASE);
                    }
                    Log.d("getRelease", "success");
                } else {
                }
            }
            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
               Log.d("getRelease error: " , t.getMessage()) ;
            }

        });
    }

    public void setAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        // TIME SET HERE
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(context, ReleaseReminder.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_RELEASE, intent, 0);

        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }

    public void cancelAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, ReleaseReminder.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, ID_RELEASE, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent.cancel();

        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
    }

}
