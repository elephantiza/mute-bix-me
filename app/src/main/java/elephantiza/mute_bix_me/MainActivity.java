package elephantiza.mute_bix_me;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import static elephantiza.mute_bix_me.Globals.SoundLevel;

public class MainActivity extends AppCompatActivity {

    private AudioManager audioManager;
    BroadcastReceiver receiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageButton reniButton = findViewById(R.id.reni_main_button);

        receiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                AudioManager audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
                if (audioManager.getRingerMode() == audioManager.RINGER_MODE_NORMAL) {
                    reniButton.setBackground(getDrawable(R.drawable.reni));
                    Globals.soundLevel = SoundLevel.SOUND;
                }
                else if (audioManager.getRingerMode() == audioManager.RINGER_MODE_VIBRATE) {
                    reniButton.setBackground(getDrawable(R.drawable.reni_vib));
                    Globals.soundLevel = SoundLevel.VIB;
                }
                else {
                    reniButton.setBackground(getDrawable(R.drawable.reni_shh));
                    Globals.soundLevel = SoundLevel.MUTE;
                }
            }
        };
        IntentFilter filter = new IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION);
        registerReceiver(receiver,filter);

        reniButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //TODO Change Reni when this happens
                if (Globals.soundLevel == SoundLevel.MUTE) {
                    reniButton.setBackground(getDrawable(R.drawable.reni));
                    Globals.soundLevel = SoundLevel.SOUND;
                    Globals.restoreAll(getApplicationContext());
                }
                else if (Globals.soundLevel == SoundLevel.SOUND) {
                    reniButton.setBackground(getDrawable(R.drawable.reni_vib));
                    Globals.soundLevel = SoundLevel.VIB;
                    Globals.vibAll(getApplicationContext());
                }
                else {
                    reniButton.setBackground(getDrawable(R.drawable.reni_shh));
                    Globals.soundLevel = SoundLevel.MUTE;
                    Globals.muteAll(getApplicationContext());
                }
            }
        });
    }

    @Override
    public void onDestroy(){
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
