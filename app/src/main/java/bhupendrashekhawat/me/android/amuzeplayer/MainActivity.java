package bhupendrashekhawat.me.android.amuzeplayer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity  implements ActivityCompat.OnRequestPermissionsResultCallback{
    // Songs list
    public ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
    private final String LOG_TAG = MediaManager.class.getSimpleName();

    final String SONG_TITLE = "songTitle";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean isStoragePermissionGranted = isStoragePermissionGranted();

        Log.d(LOG_TAG, "Storage access permission = " +isStoragePermissionGranted);

        ListView listView = (ListView) findViewById(R.id.songList);
        ArrayList<HashMap<String, String>> songsListData = new ArrayList<HashMap<String, String>>();

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
        }

        // Adding menuItems to ListView
        ListAdapter adapter = new SimpleAdapter(this, songsListData,
                R.layout.song_list_item, new String[] {SONG_TITLE}, new int[] {
                R.id.song_item });

        listView.setAdapter(adapter);


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