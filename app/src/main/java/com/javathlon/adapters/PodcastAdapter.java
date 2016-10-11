package com.javathlon.adapters;

/**
 * Created by talha on 28.02.2015.
 */

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.javathlon.PodcastData;
import com.javathlon.R;
import com.javathlon.rss.RssListPlayerActivity;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class PodcastAdapter extends BaseAdapter {
    Activity context;
    List<PodcastData> dataList;
    ImageView imageView;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    DownloadManager downloadManager;
    private LayoutInflater mInflater;
    private long downloadReference;

    public PodcastAdapter(Activity context,
                          List<PodcastData> dataList) {

        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.dataList = dataList;
    }



    public View getView(final int position, View convertView,
                        ViewGroup parent) {

        ViewHolder holder;

        convertView = mInflater.inflate(R.layout.podcastitem, null);

        holder = new ViewHolder();

        holder.positionTxt = (TextView) convertView.findViewById(R.id.audioTrackName);
        holder.downloadStatus = (TextView) convertView.findViewById(R.id.downloadpodcastitem);
        holder.icon = (ImageView) convertView.findViewById(R.id.audioTrackIcon);
        holder.duration = (TextView) convertView.findViewById(R.id.podcastDuration);
        holder.pubDate = (TextView) convertView.findViewById(R.id.podcastPublishDate);
        holder.size = (TextView) convertView.findViewById(R.id.podcastSize);
        holder.progress = (TextView) convertView.findViewById(R.id.podcastProgress);

        holder.downloadStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (dataList.get(position).devicePath != null && !dataList.get(position).devicePath.equals(""))
                    return;

                downloadManager = (DownloadManager) view.getContext().getSystemService(Context.DOWNLOAD_SERVICE);

                Uri Download_Uri = Uri.parse(dataList.get(position).url);
               // Uri Download_Uri = Uri.parse("http://www.google.com");
                DownloadManager.Request request = new DownloadManager.Request(Download_Uri);

                //Restrict the types of networks over which this download may proceed.
                request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                //Set whether this download may proceed over a roaming connection.
                request.setAllowedOverRoaming(false);
                //Set the title of this download, to be displayed in notifications (if enabled).
                request.setTitle(dataList.get(position).editionTitle);
                //Set a description of this download, to be displayed in notifications (if enabled)
                request.setDescription(context.getResources().getString(R.string.notificationtitle));
                //Set the local destination for the downloaded file to a path within the application's external files directory

                String fileName = dataList.get(position).editionTitle.replaceAll("[^\\x30-\\x5A\\x61-\\x7A\\x30-\\x39]", "") + ".mp3";
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PODCASTS, fileName);



                dataList.get(position).devicePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PODCASTS).getPath() + "/" + fileName;




                //Enqueue a new download and same the referenceId
                downloadReference = downloadManager.enqueue(request);
                RssListPlayerActivity.storeTable.put(downloadReference, dataList.get(position));


                Toast.makeText(context, R.string.downloadstarted, Toast.LENGTH_SHORT).show();

                Intent i = new Intent("startDownloadProgress");
                LocalBroadcastManager.getInstance(context).sendBroadcast(i);


              /*Intent intent = new Intent();
                intent.setAction(DownloadManager.ACTION_VIEW_DOWNLOADS);
                view.getContext().startActivity(intent);*/
            }
        });

        convertView.setTag(holder);

        String title = (String) dataList.get(position).editionTitle;

        DateFormat formatter = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
        Date d = new Date(dataList.get(position).publishDateLong);
           /* Date now = new Date();
            long millis = now.getTime() - d.getTime() ;
            int days = millis*/
        holder.pubDate.setText(sdf.format(d));

        long sizeInMB = Long.valueOf(dataList.get(position).size) / 1024 / 1024;

        holder.size.setText(sizeInMB + " MB");

        holder.duration.setText(dataList.get(position).durationString);

        // holder.size.setText(Long.valueOf(dataList.get(position).size).toString());

        holder.positionTxt.setText(title);
        if(dataList.get(position).downloadPercentage == 100)

            holder.downloadStatus.setText(R.string.material_ok);
        else if(dataList.get(position).downloadPercentage == 0)
            holder.downloadStatus.setText(R.string.material_downloadindicator);
        else
            holder.downloadStatus.setText(dataList.get(position).downloadPercentage+ "%");

        holder.progress.setText("%" + dataList.get(position).progressPercentage);

        return (convertView);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    static class ViewHolder {
        TextView positionTxt;
        TextView downloadStatus;
        ImageView icon;
        TextView duration;
        TextView pubDate;
        TextView size;
        TextView progress;
    }
}
