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

public class MainActivity extends AppCompatActivity {

    BroadcastReceiver receiver;
    ImageButton reniButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        reniButton = findViewById(R.id.reni_main_button);
        reniButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { //TODO Change Reni when this happens
                if (Globals.soundLevel == Globals.SoundLevel.MUTE) {
                    reniButton.setBackground(getDrawable(R.drawable.reni));
                    Globals.soundLevel = Globals.SoundLevel.SOUND;
                    Globals.restoreAll(getApplicationContext());
                } else if (Globals.soundLevel == Globals.SoundLevel.SOUND) {
                    reniButton.setBackground(getDrawable(R.drawable.reni_vib));
                    Globals.soundLevel = Globals.SoundLevel.VIB;
                    Globals.vibAll(getApplicationContext());
                } else {
                    reniButton.setBackground(getDrawable(R.drawable.reni_shh));
                    Globals.soundLevel = Globals.SoundLevel.MUTE;
                    Globals.muteAll(getApplicationContext());
                }
            }
        });

        receiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                AudioManager audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
                if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
                    reniButton.setBackground(getDrawable(R.drawable.reni));
                    Globals.soundLevel = Globals.SoundLevel.SOUND;
                } else if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE) {
                    reniButton.setBackground(getDrawable(R.drawable.reni_vib));
                    Globals.soundLevel = Globals.SoundLevel.VIB;
                } else {
                    reniButton.setBackground(getDrawable(R.drawable.reni_shh));
                    Globals.soundLevel = Globals.SoundLevel.MUTE;
                }
            }
        };
        IntentFilter filter = new IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION);
        registerReceiver(receiver,filter);
    }

    @Override
    public void onDestroy(){
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}
