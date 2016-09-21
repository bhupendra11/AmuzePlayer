package bhupendrashekhawat.me.android.amuzeplayer.core;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import bhupendrashekhawat.me.android.amuzeplayer.R;
import bhupendrashekhawat.me.android.amuzeplayer.models.Track;
import bhupendrashekhawat.me.android.amuzeplayer.utils.Utilities;
import bhupendrashekhawat.me.android.amuzeplayer.core.PlayBackService.MusicBinder;

public class MainActivity extends AppCompatActivity  implements ActivityCompat.OnRequestPermissionsResultCallback , MediaController.MediaPlayerControl
    {
    // Songs list
    public ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
    public ArrayList<Track> trackList = new ArrayList<Track>();
    private final String LOG_TAG = MediaManager.class.getSimpleName();
    private Utilities utils;
    private SongAdapter songAdapter;

    final String SONG_TITLE = "songTitle";
    final String SONG_INDEX = "songIndex";

    //For MusicController
    private MusicController controller;

    //For interacting with the playback service
    private PlayBackService playBackService;
    private Intent playIntent;
    private boolean musicBound=false;

    private boolean paused=false, playbackPaused=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean isStoragePermissionGranted = isStoragePermissionGranted();

        Log.d(LOG_TAG, "Storage access permission = " +isStoragePermissionGranted);

        ListView listView = (ListView) findViewById(R.id.songList);
        final ArrayList<HashMap<String, String>> songsListData = new ArrayList<HashMap<String, String>>();
        utils = new Utilities();


        MediaManager mediaManager = new MediaManager();
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(new AudioManager.OnAudioFocusChangeListener() {
                                                        @Override
                                                        public void onAudioFocusChange(int focusChange) {
                                                            playBackService.onAudioFocusChange(focusChange);
                                                        }
                                                    },
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);

        if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            // could not get audio focus.
        }

        //getting all songs from SDCard
       // this.songsList = mediaManager.getPlayList(this);

        Log.d(LOG_TAG, "After calling MediaManager");

        // looping through playlist
        /*for (int i = 0; i < songsList.size(); i++) {
            // creating new HashMap
            HashMap<String, String> song = songsList.get(i);

            // adding HashList to ArrayList
            songsListData.add(song);

            //add Track infor to trackList for everysong
            Track track = utils.getTrackInfo(this, i,songsList);
            trackList.add(i, track);

            Log.e(LOG_TAG , "TrackName : "+track.getTitle()+" Album : "+track.getAlbum() +"Artist : "+track.getArtist() );


        }
*/

        trackList = mediaManager.getSongList(this);

        Collections.sort(trackList, new Comparator<Track>(){
            public int compare(Track a, Track b){
                return a.getTitle().compareTo(b.getTitle());
            }
        });

        Log.e(LOG_TAG, "TrackList size = "+trackList.size());

        //Add adapter to the track items
        songAdapter = new SongAdapter(this, new ArrayList<Track>());
        songAdapter.addAll(trackList);
        songAdapter.notifyDataSetChanged();
        listView.setAdapter(songAdapter);

        //set music controller
        setController();


        // Adding menuItems to ListView
        /*final ListAdapter adapter = new SimpleAdapter(this, songsListData,
                R.layout.song_list_item, new String[] {SONG_TITLE}, new int[] {
                R.id.song_item });

        listView.setAdapter(adapter);*/

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                playBackService.setSong(position);
                playBackService.playSong();
                if(playbackPaused){
                    setController();
                    playbackPaused=false;
                }
                controller.show(0);

               /* int songIndex = position;
                Track track  = trackList.get(songIndex);
                Toast.makeText(getApplicationContext(), "Click on "+ track.getId()
                        , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext() , PlayActivity.class);
                intent.putExtra(SONG_INDEX, songIndex);
                startActivity(intent);*/
            }
        });


    }

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicBinder binder = (MusicBinder)service;
            //get service
            playBackService= binder.getService();
            //pass list
            playBackService.setList(trackList);
            musicBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        if(playIntent==null){
            playIntent = new Intent(this, PlayBackService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            startService(playIntent);
        }
    }

    @Override
    protected void onDestroy() {
        stopService(playIntent);
        playBackService=null;
        super.onDestroy();
    }

    //For android marshmallow and above
   public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(LOG_TAG,"Permission is granted in mm");
                return true;
            } else {

                Log.v(LOG_TAG,"Permission is revoked");
                ActivityCompat.requestPermissions( this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v(LOG_TAG,"Permission is granted");
            return true;
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Log.v(LOG_TAG,"Permission: "+permissions[0]+ "was "+grantResults[0]);
            //resume tasks needing this permission
        }
    }


    /**
     * Methods related to MusicController class
     */
    private void setController(){
        //set the controller up
        controller = new MusicController(this);

        controller.setPrevNextListeners(
           new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playNext();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPrev();
            }
        });


        controller.setMediaPlayer(this);
        controller.setAnchorView(findViewById(R.id.songList));
        controller.setEnabled(true);
    }


    //play next
    private void playNext(){
        playBackService.playNext();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(0);
    }

    //play previous
    private void playPrev(){
        playBackService.playPrev();
        if(playbackPaused){
            setController();
            playbackPaused=false;
        }
        controller.show(0);
    }

    @Override
    protected void onPause(){
        super.onPause();
        paused=true;
    }
    @Override
    protected void onResume(){
        super.onResume();
        if(paused){
            setController();
            paused=false;
        }
    }
    @Override
    protected void onStop() {
        controller.hide();
        super.onStop();
    }


    /**
     Methods which are part of MediaPlayerControl Interface
     */

    @Override
    public void start() {
        playBackService.go();
    }

    @Override
    public void pause() {
        playbackPaused =true;
        playBackService.pausePlayer();
    }

    @Override
    public int getDuration() {
        if(playBackService!=null && musicBound && playBackService.isPng())
        return playBackService.getDur();
        else return 0;
    }

    @Override
    public int getCurrentPosition() {
        if(playBackService!=null && musicBound && playBackService.isPng())
        return playBackService.getPosn();
        else return 0;
    }

    @Override
    public void seekTo(int pos) {
        playBackService.seek(pos);
    }

    @Override
    public boolean isPlaying() {
        if(playBackService!=null && musicBound)
        return playBackService.isPng();
        return false;
    }

    @Override
    public int getBufferPercentage() {
        return 0;
    }

    @Override
    public boolean canPause() {
        return true;
    }

    @Override
    public boolean canSeekBackward() {
        return true;
    }

    @Override
    public boolean canSeekForward() {
        return true;
    }

    @Override
    public int getAudioSessionId() {
        return 0;
    }
}
