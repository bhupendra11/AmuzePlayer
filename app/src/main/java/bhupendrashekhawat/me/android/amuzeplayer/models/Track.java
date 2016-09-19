package bhupendrashekhawat.me.android.amuzeplayer.models;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Bhupendra Shekhawat on 18/9/16.
 *
 * POJO for track info
 */

public class Track implements Parcelable {
    private String mTitle;

    public String getmAlbum() {
        return mAlbum;
    }

    public void setmAlbum(String mAlbum) {
        this.mAlbum = mAlbum;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getmArtist() {
        return mArtist;
    }

    public void setmArtist(String mArtist) {
        this.mArtist = mArtist;
    }

    public Bitmap getmAlbumArt() {
        return mAlbumArt;
    }

    public void setmAlbumArt(Bitmap mAlbumArt) {
        this.mAlbumArt = mAlbumArt;
    }

    private String mAlbum;
    private String mArtist;
    private Bitmap mAlbumArt;

    public Track(String title, String album, String artist , Bitmap albumArt){
        this.mTitle = title;
        this.mAlbum = album;
        this.mArtist = artist;
        this.mAlbumArt = albumArt;

    }


    protected Track(Parcel in) {
        mTitle = in.readString();
        mAlbum = in.readString();
        mArtist = in.readString();
        mAlbumArt = (Bitmap) in.readValue(Bitmap.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTitle);
        dest.writeString(mAlbum);
        dest.writeString(mArtist);
        dest.writeValue(mAlbumArt);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Track> CREATOR = new Parcelable.Creator<Track>() {
        @Override
        public Track createFromParcel(Parcel in) {
            return new Track(in);
        }

        @Override
        public Track[] newArray(int size) {
            return new Track[size];
        }
    };
}