package bhupendrashekhawat.me.android.amuzeplayer.core;


import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import bhupendrashekhawat.me.android.amuzeplayer.models.Track;

import static bhupendrashekhawat.me.android.amuzeplayer.R.id.songList;

/**
 * Created by Bhupendra Shekhawat on 17/9/16.
 */

public class MediaManager {
    // SDCard Path
    final String MEDIA_PATH = "/storage/";
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


    public ArrayList<Track> getSongList(Context context) {
        Log.d(LOG_TAG, "Inside getSongList()");
        ArrayList<Track> songList = new ArrayList<Track>();

        ContentResolver musicResolver = context.getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, null);

        //Iterate over the result

        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int albumColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);

            long albumId = musicCursor.getLong(musicCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));


            final Uri ART_CONTENT_URI = Uri.parse("content://media/external/audio/albumart");
            Uri albumArtUri = ContentUris.withAppendedId(ART_CONTENT_URI, albumId);

            //add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String thisAlbum = musicCursor.getString(albumColumn);

                //Get bitmap Album art for the album
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), albumArtUri);
                } catch (Exception exception) {
                    Log.e(LOG_TAG, "Cannot get Album art for song " + thisTitle);
                }

                songList.add(new Track(thisId, thisTitle, thisAlbum, thisArtist, bitmap));
                //ong id,String title, String album, String artist , Bitmap albumArt

            }
            while (musicCursor.moveToNext());
        }

        return songList;

    }




  /*  public ArrayList<HashMap<String, String>> getPlayList(Context context) {
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
*/







}
