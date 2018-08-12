package elephantiza.mute_bix_me;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;

import static android.content.Context.AUDIO_SERVICE;

public class Globals {
    public enum SoundLevel {SOUND, VIB, MUTE};
    public static SoundLevel soundLevel = SoundLevel.SOUND;
    public static int[] streams = {
        AudioManager.STREAM_RING,
        AudioManager.STREAM_NOTIFICATION,
        AudioManager.STREAM_SYSTEM,
        AudioManager.STREAM_MUSIC
    };


    public static void muteAll(Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

            context.startActivity(intent);
        }

        else {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
            audioManager.setStreamVolume(14, 0, 0);
        }
    }

    public static void restoreAll(Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

            context.startActivity(intent);
        }

        else {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            SharedPreferences sharedPrefs = context.getSharedPreferences("elephantiza.sharedprefs", Context.MODE_PRIVATE);

            int music = sharedPrefs.getInt("music", 0);
            int bixby = sharedPrefs.getInt("bixby", 0);

            audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, music, 0);
            audioManager.setStreamVolume(14, bixby, 0);
        }
    }

    public static void vibAll(Context context) {
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && !notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(
                    android.provider.Settings
                            .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

            context.startActivity(intent);
        }

        else {
            AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

            SharedPreferences sharedPrefs = context.getSharedPreferences("elephantiza.sharedprefs", Context.MODE_PRIVATE);

            sharedPrefs.edit().putInt("music", audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)).apply();
            sharedPrefs.edit().putInt("bixby", audioManager.getStreamVolume(14)).apply();

            audioManager.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
            audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
            audioManager.setStreamVolume(14, 0, 0);
        }
    }
}
