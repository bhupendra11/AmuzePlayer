package bhupendrashekhawat.me.android.amuzeplayer.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;

import java.util.ArrayList;
import java.util.HashMap;

import bhupendrashekhawat.me.android.amuzeplayer.R;
import bhupendrashekhawat.me.android.amuzeplayer.models.Track;

/**
 * Created by Bhupendra Shekhawat on 18/9/16.
 */

public class Utilities {

    final String SONG_PATH = "songPath";

    private String mTitle;
    private String mAlbum;
    private String mArtist;
    private Bitmap mAlbumArt;

    /**
     * Function to convert milliseconds time to
     * Timer Format
     * Hours:Minutes:Seconds
     * */
    public String milliSecondsToTimer(long milliseconds){
        String finalTimerString = "";
        String secondsString = "";

        // Convert total duration into time
        int hours = (int)( milliseconds / (1000*60*60));
        int minutes = (int)(milliseconds % (1000*60*60)) / (1000*60);
        int seconds = (int) ((milliseconds % (1000*60*60)) % (1000*60) / 1000);
        // Add hours if there
        if(hours > 0){
            finalTimerString = hours + ":";
        }

        // Prepending 0 to seconds if it is one digit
        if(seconds < 10){
            secondsString = "0" + seconds;
        }else{
            secondsString = "" + seconds;}

        finalTimerString = finalTimerString + minutes + ":" + secondsString;

        // return timer string
        return finalTimerString;
    }

    /**
     * Function to get Progress percentage
     * @param currentDuration
     * @param totalDuration
     * */
    public int getProgressPercentage(long currentDuration, long totalDuration){
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage =(((double)currentSeconds)/totalSeconds)*100;

        // return percentage
        return percentage.intValue();
    }

    /**
     * Function to change progress to timer
     * @param progress -
     * @param totalDuration
     * returns current duration in milliseconds
     * */
    public int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double)progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
    }

    public Bitmap getAlbumArt(Context context,int songIndex, ArrayList<HashMap<String, String>> songsList ){
        boolean isSet = false;
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(songsList.get(songIndex).get(SONG_PATH));
        Bitmap outputBitmap = null;

        byte [] data = mmr.getEmbeddedPicture();

        // convert the byte array to a bitmap
        if(data != null)
        {
            outputBitmap= BitmapFactory.decodeByteArray(data, 0, data.length);
        }
        else
        {
            outputBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.def_album_art);
        }

        return outputBitmap;
    }

    //Get Song Info
    public Track getTrackInfo(Context context, int songIndex, ArrayList<HashMap<String, String>> songsList) {

        MediaMetadataRetriever metaRetriever= new MediaMetadataRetriever();
        metaRetriever.setDataSource(songsList.get(songIndex).get(SONG_PATH));
        mArtist = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST) ;
        mTitle= metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
        mAlbum = metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);

        if(mArtist == null)mArtist ="Unknown artist";
        if(mTitle == null)mTitle = "Unknown title";
        if(mAlbum == null)mAlbum = "Unknown album";

        //For extracting AlbumArt
        Bitmap outputBitmap = null;

        byte [] data = metaRetriever.getEmbeddedPicture();

        // convert the byte array to a bitmap
        if(data != null)
        {
            outputBitmap= BitmapFactory.decodeByteArray(data, 0, data.length);
        }
        else
        {
            outputBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.def_album_art);
        }

        mAlbumArt = outputBitmap;

        Track track = new Track(mTitle,mAlbum,mArtist,mAlbumArt);
        return  track;
    }
}