package bhupendrashekhawat.me.android.amuzeplayer.core;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import bhupendrashekhawat.me.android.amuzeplayer.R;
import bhupendrashekhawat.me.android.amuzeplayer.models.Track;
import bhupendrashekhawat.me.android.amuzeplayer.utils.Utilities;

public class MainActivity extends AppCompatActivity  implements ActivityCompat.OnRequestPermissionsResultCallback{
    // Songs list
    public ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
    public ArrayList<Track> trackList = new ArrayList<Track>();
    private final String LOG_TAG = MediaManager.class.getSimpleName();
    private Utilities utils;
    private SongAdapter songAdapter;

    final String SONG_TITLE = "songTitle";
    final String SONG_INDEX = "songIndex";

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

        //getting all songs from SDCard
        this.songsList = mediaManager.getPlayList();

        Log.d(LOG_TAG, "After calling MediaManager");

        // looping through playlist
        for (int i = 0; i < songsList.size(); i++) {
            // creating new HashMap
            HashMap<String, String> song = songsList.get(i);

            // adding HashList to ArrayList
            songsListData.add(song);

            //add Track infor to trackList for everysong
            Track track = utils.getTrackInfo(this, i,songsList);
            trackList.add(i, track);

            Log.e(LOG_TAG , "TrackName : "+track.getmTitle()+" Album : "+track.getmAlbum() +"Artist : "+track.getmArtist() );


        }

        Log.e(LOG_TAG, "TrackList size = "+trackList.size());

        //Add adapter to the track items
        songAdapter = new SongAdapter(this, new ArrayList<Track>());
        songAdapter.addAll(trackList);
        songAdapter.notifyDataSetChanged();
        listView.setAdapter(songAdapter);



        // Adding menuItems to ListView
        /*final ListAdapter adapter = new SimpleAdapter(this, songsListData,
                R.layout.song_list_item, new String[] {SONG_TITLE}, new int[] {
                R.id.song_item });

        listView.setAdapter(adapter);*/

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                int songIndex = position;
                HashMap<String, String> song = songsListData.get(songIndex);
                Toast.makeText(getApplicationContext(), "Click on "+ song.get(SONG_TITLE)
                        , Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext() , PlayActivity.class);
                intent.putExtra(SONG_INDEX, songIndex);
                startActivity(intent);
            }
        });


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




}
