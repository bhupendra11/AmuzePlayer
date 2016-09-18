package bhupendrashekhawat.me.android.amuzeplayer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Handler;
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
import java.util.Random;

import static bhupendrashekhawat.me.android.amuzeplayer.R.id.btnForward;
import static bhupendrashekhawat.me.android.amuzeplayer.R.id.btnPlay;

public class PlayActivity extends AppCompatActivity  {

    // Media Player
    private MediaPlayer mp;
    private MediaManager mediaManager;
    private ImageButton btnPlay;
    private TextView songTitleLabel;
    private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
    private ImageView blurAlbumArt;
    private SeekBar songProgressBar;
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
        songTitleLabel = (TextView) findViewById(R.id.song_title_label);
        // Getting all songs list
        songsList = mediaManager.getPlayList();
        blurBitmap = new BlurBitmap();
        blurAlbumArt = (ImageView) findViewById(R.id.album_art_blur);
        songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);



        //Get the blurred Image for background behind the album art
        Bitmap inputBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.album_art_placeholder);  //getResources().getDrawable(R.drawable.album_art_placeholder);
        Bitmap bluredAlbumartBitmap = BlurBitmap.blurImage(getApplicationContext(), inputBitmap);
        blurAlbumArt.setImageBitmap(bluredAlbumartBitmap);


        Intent intent = getIntent();
        final int songIndex = intent.getIntExtra(SONG_INDEX,0);

        Toast.makeText(getApplicationContext(), "Song Selected is " +songIndex,  Toast.LENGTH_SHORT).show();


        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playSong(songIndex);
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




}
