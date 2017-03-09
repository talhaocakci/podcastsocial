package com.javathlon;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class SongSelectionList extends Activity {
    private ArrayList<String> mAudioIds;
    String[] projectionForAudio = new String[]{
            android.provider.MediaStore.Audio.Media._ID,
            android.provider.MediaStore.Audio.Media.DISPLAY_NAME
    };
    ListView g1;
    String songName;
    private ArrayAdapter<String> aa;
    int image_ID;
    int mediaID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        mAudioIds = new ArrayList<String>();
        Uri audios = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor managedCursorForAudio = managedQuery(audios,
                projectionForAudio,
                null,
                null,
                null);
        getColumnData(managedCursorForAudio, "audio");
//        g1 = (ListView) findViewById(R.id.audioGallery);
//        int resID=R.layout.audioplayout;
//        aa = new IconicAdapter(this);
//        g1.setAdapter(aa);
//        
//        g1.setOnItemClickListener(new OnItemClickListener() {
//            public void onItemClick(AdapterView parent, View v, int position, long id) {
//            	Intent i= new Intent(SongSelectionList.this,Main.class);
//                i.putExtra("position", position);
//                i.putExtra("mediaID", mediaID);
//                startActivity(i);
//            }
//        }); 
    }

    private void getColumnData(Cursor cur, String type) {
        if (cur.moveToFirst()) {

            String mediaName;
            do {
//            	if(type.equalsIgnoreCase("video")){
//                	mediaID = cur.getInt( cur.getColumnIndex(MediaStore.Video.Thumbnails._ID) );
//                	
//                    Uri uri = Uri.withAppendedPath( MediaStore.Video.Thumbnails.EXTERNAL_CONTENT_URI,
//                            Integer.toString(mediaID) );
//                    mVideoIds.add(loadThumbnailImage( uri.toString() ,type));
//            	}else
                if (type.equalsIgnoreCase("audio")) {
                    mediaID = cur.getInt(cur.getColumnIndex(MediaStore.Audio.Media._ID));

                    songName = cur.getString(cur.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));

                    mAudioIds.add(songName);
                }

            } while (cur.moveToNext());

        }
    }

    class IconicAdapter extends ArrayAdapter {
        Activity context;

        IconicAdapter(Activity context) {
            super(context, R.layout.audioplayout, mAudioIds);

            this.context = context;
        }

        public View getView(int position, View convertView,
                            ViewGroup parent) {
            View row = convertView;

            if (row == null) {
                LayoutInflater inflater = context.getLayoutInflater();

                row = inflater.inflate(R.layout.audioplayout, null);
            }
            ImageView icon = (ImageView) row.findViewById(R.id.audioTrackIcon);
            icon.setImageResource(R.drawable.audiotrackicon);
            TextView label = (TextView) row.findViewById(R.id.audioTrackName);
            label.setText((String) mAudioIds.get(position));

            return (row);
        }
    }

}
