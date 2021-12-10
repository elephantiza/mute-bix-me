package elephantiza.mute_bix_me;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.util.Log;
import android.widget.RemoteViews;

import static android.content.Context.AUDIO_SERVICE;

import java.util.Objects;

/**
 * Implementation of App Widget functionality.
 */
public class ReniWidget extends AppWidgetProvider {

    private static final String ReniOnClick = "ReniOnClick";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("ReniWidget", Thread.currentThread().getStackTrace()[2].getMethodName());
        Log.d("ReniWidget", "onReceive: Action " + intent.getAction());
        super.onReceive(context, intent);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.reni_widget);
        ComponentName reniWidget = new ComponentName(context, ReniWidget.class);

        switch (Objects.requireNonNull(intent.getAction())) {
            case ReniOnClick:
                Log.d("ReniWidget", "ReniOnClick action");
                if (Globals.soundLevel == Globals.SoundLevel.MUTE) {
                    views.setImageViewResource(R.id.reni_widget_button, R.drawable.reni);
                    Globals.soundLevel = Globals.SoundLevel.SOUND;
                    Globals.restoreAll(context);
                }
                else if (Globals.soundLevel == Globals.SoundLevel.SOUND) {
                    views.setImageViewResource(R.id.reni_widget_button, R.drawable.reni_vib);
                    Globals.soundLevel = Globals.SoundLevel.VIB;
                    Globals.vibAll(context);
                }
                else {
                    views.setImageViewResource(R.id.reni_widget_button, R.drawable.reni_shh);
                    Globals.soundLevel = Globals.SoundLevel.MUTE;
                    Globals.muteAll(context);
                }
                appWidgetManager.updateAppWidget(reniWidget, views);
                break;

            case AudioManager.RINGER_MODE_CHANGED_ACTION:
                Log.d("ReniWidget", "RINGER_MODE_CHANGED_ACTION");
                AudioManager audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
                if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
                    views.setImageViewResource(R.id.reni_widget_button, R.drawable.reni);
                    Globals.soundLevel = Globals.SoundLevel.SOUND;
                } else if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE) {
                    views.setImageViewResource(R.id.reni_widget_button, R.drawable.reni_vib);
                    Globals.soundLevel = Globals.SoundLevel.VIB;
                } else {
                    views.setImageViewResource(R.id.reni_widget_button, R.drawable.reni_shh);
                    Globals.soundLevel = Globals.SoundLevel.MUTE;
                }
                appWidgetManager.updateAppWidget(reniWidget, views);
                break;
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.d("ReniWidget", Thread.currentThread().getStackTrace()[2].getMethodName());
        super.onUpdate(context, appWidgetManager, appWidgetIds);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.reni_widget);
        views.setOnClickPendingIntent(R.id.reni_widget_button, getPendingSelfIntent(context, ReniOnClick));
        ComponentName reniWidget = new ComponentName(context, ReniWidget.class);
        appWidgetManager.updateAppWidget(reniWidget, views);
    }

    @Override
    public void onEnabled(Context context) {
        Log.d("ReniWidget", Thread.currentThread().getStackTrace()[2].getMethodName());
        super.onEnabled(context);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.reni_widget);
        views.setOnClickPendingIntent(R.id.reni_widget_button, getPendingSelfIntent(context, ReniOnClick));

        IntentFilter filter = new IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION);
        filter.addAction(ReniOnClick);
        context.getApplicationContext().registerReceiver(this, filter);
    }

    @Override
    public void onDisabled(Context context) {
        Log.d("ReniWidget", Thread.currentThread().getStackTrace()[2].getMethodName());
        context.getApplicationContext().unregisterReceiver(this);
        super.onDisabled(context);

    }

    private PendingIntent getPendingSelfIntent(Context context, String action) {
        Log.d("ReniWidget", Thread.currentThread().getStackTrace()[2].getMethodName());
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}
