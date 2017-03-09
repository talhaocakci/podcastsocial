package com.javathlon.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.javathlon.R;
import com.javathlon.model.Note;
import com.javathlon.upload.Mp3Cropper;
import com.nhaarman.listviewanimations.ArrayAdapter;

import java.util.List;

/**
 * Created by talha on 20.07.2015.
 */
public class NoteAdapter extends ArrayAdapter<Note> {
    private Context context;
    private List<Note> noteList;
    private Activity activity;
    private boolean isDownloaded = false;


    public NoteAdapter(Context context, List<Note> objects, Activity activity, String isDownloaded) {
        super(objects);
        this.context = context;
        this.noteList = objects;
        this.activity = activity;
        if (isDownloaded != null && isDownloaded.equals("y"))
            this.isDownloaded = true;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View noteView = layoutInflater.inflate(R.layout.note_item, viewGroup, false);
        TextView noteText = (TextView) noteView.findViewById(R.id.noteText);
        TextView noteUploadBtn = (TextView) noteView.findViewById(R.id.uploadNoteBtn);
        TextView whatsAppText = (TextView) noteView.findViewById(R.id.whatsapp);
        holder.noteText = noteText;
        holder.uploadNoteButton = noteUploadBtn;
        holder.whatsAppText = whatsAppText;
        if (isDownloaded) {
            holder.whatsAppText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Note note = NoteAdapter.this.noteList.get(i);
                    String filePath = note.getAudioPath();
                    int sec = note.getBeginSec() / 1000;
                    new Mp3Cropper(context, Mp3Cropper.CropResultType.MP3).execute(note.getPodcastId(), sec, note.getId());
                }
            });
            holder.uploadNoteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Note note = NoteAdapter.this.noteList.get(i);
                    int sec = note.getBeginSec() / 1000;
                    new Mp3Cropper(context, Mp3Cropper.CropResultType.URL).execute(note.getPodcastId(), sec, note.getId());
                }
            });
        } else {
            noteUploadBtn.setVisibility(View.GONE);
            whatsAppText.setVisibility(View.GONE);
        }
        noteText.setText(noteList.get(i).getText());
        noteView.setTag(holder);
        return noteView;
    }

    private static class ViewHolder {

        public TextView uploadNoteButton;
        public TextView noteText;
        public TextView beginSecondText;
        public TextView whatsAppText;
    }
}
