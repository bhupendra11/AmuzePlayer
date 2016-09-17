package bhupendrashekhawat.me.android.amuzeplayer;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Bhupendra Shekhawat on 17/9/16.
 */

public class MediaManager {
    // SDCard Path
    final String MEDIA_PATH = "/storage/";
    final String SONG_PATH = "songPath";
    final String SONG_TITLE = "songTitle";
    private ArrayList<HashMap<String, String>> songsList = new ArrayList<HashMap<String, String>>();
    private final String LOG_TAG = MediaManager.class.getSimpleName();

    // Constructor
    public MediaManager() {

    }

    /**
     * Function to read all mp3 files from sdcard
     * and store the details in ArrayList
     */
    public ArrayList<HashMap<String, String>> getPlayList() {
        Log.d(LOG_TAG, "Inside getPlayList()");
       /* File home = Environment.getExternalStorageDirectory();

        if (home.listFiles(new FileExtensionFilter()).length > 0) {
            for (File file : home.listFiles(new FileExtensionFilter())) {
                HashMap<String, String> song = new HashMap<String, String>();
                song.put(SONG_TITLE, file.getName().substring(0, (file.getName().length() - 4)));
                song.put(SONG_PATH, file.getPath());

                // Adding each song to SongList
                songsList.add(song);
            }
        }
        // return songs list array
        return songsList;*/

        System.out.println(MEDIA_PATH);
        if (MEDIA_PATH != null) {
            File home = new File(MEDIA_PATH);
            File[] listFiles = home.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    Log.d(LOG_TAG, "File Path : " + file.getAbsolutePath());
                   // System.out.println(file.getAbsolutePath());
                    if (file.isDirectory()) {
                        scanDirectory(file);
                    } else {
                        addSongToList(file);
                    }
                }
            }
        }
        // return songs list array
        System.out.println("SongList size = " +songsList.size());
        return songsList;
    }

    private void scanDirectory(File directory) {
        Log.d(LOG_TAG, "Inside scanDirectory()");
        if (directory != null) {
            File[] listFiles = directory.listFiles();
            if (listFiles != null && listFiles.length > 0) {
                for (File file : listFiles) {
                    if (file.isDirectory()) {
                        scanDirectory(file);
                    } else {
                        addSongToList(file);
                    }

                }
            }
        }
    }

    private void addSongToList(File song) {
        if (song.getName().endsWith(".mp3")) {
            HashMap<String, String> songMap = new HashMap<String, String>();
            songMap.put(SONG_TITLE,
                    song.getName().substring(0, (song.getName().length() - 4)));
            songMap.put(SONG_PATH, song.getPath());

            // Adding each song to SongList
            songsList.add(songMap);
        }
    }




    /**
     * Class to filter files which are having .mp3 extension
     */
    /*class FileExtensionFilter implements FilenameFilter {
        public boolean accept(File dir, String name) {

            Log.d("File Name ", name);
            return (name.endsWith(".mp3") || name.endsWith(".MP3"));
        }
    }*/
}
