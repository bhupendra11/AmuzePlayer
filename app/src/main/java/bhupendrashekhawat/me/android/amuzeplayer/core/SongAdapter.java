package bhupendrashekhawat.me.android.amuzeplayer.core;

import android.app.Activity;
import android.content.Context;
import android.os.Trace;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import bhupendrashekhawat.me.android.amuzeplayer.R;
import bhupendrashekhawat.me.android.amuzeplayer.models.Track;

/**
 * Created by Bhupendra Shekhawat on 19/9/16.
 */

public class SongAdapter extends ArrayAdapter<Track>{
    public SongAdapter(Activity context, List<Track> moviePosters){

        super(context,0, moviePosters);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Track track = getItem(position);
        ViewHolder viewHolder ;

        Log.e("DEBUG", "Track name : "+track.getmTitle());

        if (convertView == null) {
            Log.e("DEBUG", "Inside convertView = null");
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.song_list_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.trackTitleView = (TextView) convertView.findViewById(R.id.track_title_view);
            viewHolder.trackAlbumView = (TextView) convertView.findViewById(R.id.track_album_view);
            viewHolder.albumArtView = (ImageView) convertView.findViewById(R.id.album_art_small);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }


        //viewHolder.albumArtView.setAdjustViewBounds(true);
        //viewHolder.albumArtView.setPadding(0,0,0,0);
        viewHolder.albumArtView.setImageBitmap(track.getmAlbumArt());

        viewHolder.trackTitleView.setText(track.getmTitle());
        viewHolder.trackAlbumView.setText(track.getmAlbum());


        return convertView;
    }

    // Implement viewHolder inorder to avoid expensive findViewVyId calls by every list item

    static class ViewHolder{
        ImageView albumArtView;
        TextView trackTitleView;
        TextView trackAlbumView;


    }
}
