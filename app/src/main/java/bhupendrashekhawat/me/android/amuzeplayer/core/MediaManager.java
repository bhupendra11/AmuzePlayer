package bhupendrashekhawat.me.android.amuzeplayer.core;


import android.media.MediaMetadataRetriever;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Bhupendra Shekhawat on 17/9/16.
 */

public class MediaManager {
    // SDCard Path
    final String MEDIA_PATH = "/sdcard/";
    final String SONG_PATH = "songPath";
    final String SONG_TITLE = "songTitle";

    //Constants for Song Info
    final String SONG_ALBUM = "songAlbum";


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
                        Log.d(LOG_TAG, "File isDirectory "+file.getAbsolutePath());
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








}
