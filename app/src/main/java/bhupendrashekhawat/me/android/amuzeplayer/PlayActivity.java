package bhupendrashekhawat.me.android.amuzeplayer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class PlayActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    // Media Player
    private MediaPlayer mp;
    private MediaManager mediaManager;
    private ImageButton btnPlay;
    private ImageButton btnPrevious;
    private ImageButton btnNext;
    private ImageButton btnLoop;
    private ImageButton btnShuffle;

    private TextView songTitleLabel;
    private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
    private ImageView blurAlbumArt;
    private SeekBar songProgressBar;

    private int currentSongIndex =0;
    //  private Utilities utils;

    final String SONG_INDEX = "songIndex";

    private BlurBitmap blurBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        mp = new MediaPlayer();
        mediaManager = new MediaManager();
        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);
        btnNext = (ImageButton) findViewById(R.id.btnNext);
        btnLoop = (ImageButton) findViewById(R.id.btnLoop);
        btnShuffle = (ImageButton) findViewById(R.id.btnShuffle);

        songTitleLabel = (TextView) findViewById(R.id.song_title_label);
        // Getting all songs list
        songsList = mediaManager.getPlayList();
        blurBitmap = new BlurBitmap();
        blurAlbumArt = (ImageView) findViewById(R.id.album_art_blur);
        songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);


        //Listeners
        mp.setOnCompletionListener(this); // Important


        //Get the blurred Image for background behind the album art
        Bitmap inputBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.album_art_placeholder);  //getResources().getDrawable(R.drawable.album_art_placeholder);
        Bitmap bluredAlbumartBitmap = BlurBitmap.blurImage(getApplicationContext(), inputBitmap);
        blurAlbumArt.setImageBitmap(bluredAlbumartBitmap);


        Intent intent = getIntent();
        currentSongIndex = intent.getIntExtra(SONG_INDEX,0);
        // play selected song
        playSong(currentSongIndex);

        Toast.makeText(getApplicationContext(), "Song Selected is " + currentSongIndex ,  Toast.LENGTH_SHORT).show();

        /* Set on click listeners for the playback controls */

        /**
         * Play button click event
         * */
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // check for already playing
                if(mp.isPlaying()){
                    if(mp!=null){
                        mp.pause();
                        // Changing button image to play button
                        btnPlay.setImageResource(R.drawable.btn_play);
                    }
                }else{
                    // Resume song
                    if(mp!=null){
                        mp.start();
                        // Changing button image to pause button
                        btnPlay.setImageResource(R.drawable.btn_pause);
                    }
                }
            }
        });

        /**
         * Previous button click event
         * */
        btnPrevious.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if(currentSongIndex > 0){
                    playSong(currentSongIndex - 1);
                    currentSongIndex = currentSongIndex - 1;
                }else{
                    // play last song
                    playSong(songsList.size() - 1);
                    currentSongIndex = songsList.size() - 1;
                }

            }
        });


        /**
         * Next button click event
         * Plays next song by taking currentSongIndex + 1
         * */
        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // check if next song is there or not
                if(currentSongIndex < (songsList.size() - 1)){
                    playSong(currentSongIndex + 1);
                    currentSongIndex = currentSongIndex + 1;
                }else{
                    // play first song
                    playSong(0);
                    currentSongIndex = 0;
                }

            }
        });

    }



    /**
     * Function to play a song
     * @param songIndex - index of song
     * */
    public void  playSong(int songIndex){
        // Play song
        try {
            mp.reset();
            mp.setDataSource(songsList.get(songIndex).get("songPath"));
            mp.prepare();
            mp.start();
            // Displaying Song title
            String songTitle = songsList.get(songIndex).get("songTitle");
            songTitleLabel.setText(songTitle);

            // Changing Button Image to pause image
            btnPlay.setImageResource(R.drawable.btn_pause);

            // set Progress bar values
            songProgressBar.setProgress(0);
            songProgressBar.setMax(100);


        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

    }
}
