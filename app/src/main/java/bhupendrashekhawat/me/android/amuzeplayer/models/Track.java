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


    private long mId;
    private String mTitle;
    private String mAlbum;
    private String mArtist;
    private Bitmap mAlbumArt;

    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }


    public String getAlbum() {
        return mAlbum;
    }

    public void setAlbum(String mAlbum) {
        this.mAlbum = mAlbum;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getArtist() {
        return mArtist;
    }

    public void setArtist(String mArtist) {
        this.mArtist = mArtist;
    }

    public Bitmap getAlbumArt() {
        return mAlbumArt;
    }

    public void setAlbumArt(Bitmap mAlbumArt) {
        this.mAlbumArt = mAlbumArt;
    }



    public Track(long id,String title, String album, String artist , Bitmap albumArt){
        this.mId = id;
        this.mTitle = title;
        this.mAlbum = album;
        this.mArtist = artist;
        this.mAlbumArt = albumArt;

    }


    protected Track(Parcel in) {
        mId = in.readLong();
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
        dest.writeLong(mId);
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