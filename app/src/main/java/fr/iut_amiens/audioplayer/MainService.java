package fr.iut_amiens.audioplayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class MainService extends Service {

    public static final String ACTION_SHOW = "fr.iut_amiens.audioplayer.ACTION_SHOW";

    public static final String ACTION_PLAY = "fr.iut_amiens.audioplayer.ACTION_PLAY";

    public static final String ACTION_STOP = "fr.iut_amiens.audioplayer.ACTION_STOP";

    public static final String ACTION_CHANGE = "fr.iut_amiens.audioplayer.ACTION_CHANGE";

    public static final String EXTRA_AUDIO_FILE = "fr.iut_amiens.audioplayer.EXTRA_AUDIO_FILE";

    private AudioFile audioFile = AudioFile.GOING_HIGHER;

    private MediaPlayer player = null;

    public void play() {
        if (player == null) {
            Log.d("SERVICE", "start music " + audioFile);
            player = MediaPlayer.create(this, audioFile.getResourceId());
            player.start();
        }
    }

    public void stop() {
        if (player != null) {
            Log.d("SERVICE", "stop music");
            player.stop();
            player.release();
            player = null;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder(this);
    }

    @Override
    public void onCreate() {
        Log.d("SERVICE", "create");
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        Log.d("SERVICE", "destroy");
        stop();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("SERVICE", "received intent " + intent);
        switch (intent.getAction()) {
            case ACTION_SHOW:
                break;
            case ACTION_PLAY:
                play();
                break;
            case ACTION_STOP:
                stop();
                break;
            case ACTION_CHANGE:
                audioFile = (AudioFile) intent.getSerializableExtra(EXTRA_AUDIO_FILE);
                if (player != null) {
                    stop();
                    play();
                }
                break;
        }
        startForeground(1, getNotification());
        return START_NOT_STICKY;
    }

    private Notification getNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_audiotrack_white_24dp)
                .setContentTitle("Audio Player")
                .setContentText(audioFile.getTitle())
                .setContentIntent(PendingIntent.getActivity(this, 1, new Intent(this, MainActivity.class), 0));

        if (player == null) {
            builder.addAction(R.drawable.ic_play_arrow_white_24dp, "play", PendingIntent.getService(this, 2, new Intent(this, MainService.class).setAction(ACTION_PLAY), 0));
        } else {
            builder.addAction(R.drawable.ic_pause_white_24dp, "pause", PendingIntent.getService(this, 3, new Intent(this, MainService.class).setAction(ACTION_STOP), 0));
        }

        builder.addAction(R.drawable.ic_skip_previous_white_24dp, "back", PendingIntent.getService(this, 4, new Intent(this, MainService.class).setAction(ACTION_CHANGE).putExtra(EXTRA_AUDIO_FILE, audioFile.previous()), PendingIntent.FLAG_UPDATE_CURRENT));

        builder.addAction(R.drawable.ic_skip_next_white_24dp, "next", PendingIntent.getService(this, 5, new Intent(this, MainService.class).setAction(ACTION_CHANGE).putExtra(EXTRA_AUDIO_FILE, audioFile.next()), PendingIntent.FLAG_UPDATE_CURRENT));

        return builder.build();
    }
}
