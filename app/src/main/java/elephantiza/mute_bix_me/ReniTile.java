package elephantiza.mute_bix_me;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Icon;
import android.media.AudioManager;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;

public class ReniTile extends TileService {

    BroadcastReceiver receiver;

    @Override
    public void onCreate() {
        super.onCreate();
        receiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {
                AudioManager audioManager = (AudioManager) context.getSystemService(AUDIO_SERVICE);
                if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
                    Globals.soundLevel = Globals.SoundLevel.SOUND;

                    Tile tile = getQsTile();
                    tile.setState(Tile.STATE_ACTIVE);
                    tile.setIcon(Icon.createWithResource(ReniTile.this, R.drawable.reni_contour));
                    tile.updateTile();
                } else if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE) {
                    Globals.soundLevel = Globals.SoundLevel.VIB;

                    Tile tile = getQsTile();
                    tile.setState(Tile.STATE_INACTIVE);
                    tile.setIcon(Icon.createWithResource(ReniTile.this, R.drawable.reni_vib_contour));
                    tile.updateTile();
                } else {
                    Globals.soundLevel = Globals.SoundLevel.MUTE;

                    Tile tile = getQsTile();
                    tile.setState(Tile.STATE_INACTIVE);
                    tile.setIcon(Icon.createWithResource(ReniTile.this, R.drawable.reni_contour));
                    tile.updateTile();
                }
            }
        };
        IntentFilter filter = new IntentFilter(AudioManager.RINGER_MODE_CHANGED_ACTION);
        registerReceiver(receiver, filter);
    }

    @Override
    public void onClick() {
        super.onClick();
        if (Globals.soundLevel == Globals.SoundLevel.MUTE) {
            Globals.soundLevel = Globals.SoundLevel.SOUND;
            Globals.restoreAll(getApplicationContext());

            Tile tile = getQsTile();
            tile.setState(Tile.STATE_ACTIVE);
            tile.setIcon(Icon.createWithResource(this, R.drawable.reni_contour));
            tile.updateTile();
        } else if (Globals.soundLevel == Globals.SoundLevel.SOUND) {
            Globals.soundLevel = Globals.SoundLevel.VIB;
            Globals.vibAll(getApplicationContext());

            Tile tile = getQsTile();
            tile.setState(Tile.STATE_INACTIVE);
            tile.setIcon(Icon.createWithResource(this, R.drawable.reni_vib_contour));
            tile.updateTile();
        } else {
            Globals.soundLevel = Globals.SoundLevel.MUTE;
            Globals.muteAll(getApplicationContext());

            Tile tile = getQsTile();
            tile.setState(Tile.STATE_INACTIVE);
            tile.setIcon(Icon.createWithResource(this, R.drawable.reni_contour));
            tile.updateTile();
        }
    }

    @Override
    public void onTileRemoved(){
        unregisterReceiver(receiver);
        super.onTileRemoved();
    }

    @Override
    public void onDestroy(){
        unregisterReceiver(receiver);
        super.onDestroy();
    }
}