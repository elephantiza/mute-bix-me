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

/**
 * Implementation of App Widget functionality.
 */
public class ReniWidget extends AppWidgetProvider {

    private static final String ReniOnClick = "ReniOnClick";

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        if (intent.getAction().equals(ReniOnClick)){

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.reni_widget);
            ComponentName reniWidget = new ComponentName(context, ReniWidget.class);

            Log.d("Clicked", " I have just clicked Reni");
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
        }

        //if (AudioManager.RINGER_MODE_CHANGED_ACTION.equals(intent.getAction()))
        Log.d ("Action",intent.getAction() );
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.reni_widget);
        ComponentName reniWidget = new ComponentName(context, ReniWidget.class);
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
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.reni_widget);
        ComponentName reniWidget = new ComponentName(context, ReniWidget.class);

        views.setOnClickPendingIntent(R.id.reni_widget_button, getPendingSelfIntent(context, ReniOnClick));

        appWidgetManager.updateAppWidget(reniWidget, views);
    }

    @Override
    public void onEnabled(Context context) {
        Log.d ("On Enabled", "I am here" );

        IntentFilter filter = new IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION);
        context.getApplicationContext().registerReceiver(this, filter);
    }

    protected PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}

