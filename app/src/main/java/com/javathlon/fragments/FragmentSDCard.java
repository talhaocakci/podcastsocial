package com.javathlon.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.javathlon.CommonStaticClass;
import com.javathlon.R;
import com.javathlon.Utils;
import com.javathlon.adapters.BrowseListAdapter;
import com.javathlon.player.PlayerScreen;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FragmentSDCard extends Fragment {
    public static final int ADDTO_PLAYLIST = 15, ADDTO_QUICK = 16;
    public static BrowseListAdapter fileListArrayAdapter;
    public static List<String> fileString1 = new ArrayList<String>();
    public static List<String> fileStringWithPath = new ArrayList<String>();

    public static ListView sdcardFileListView;
    public static String baseFolder = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static String currentPath = baseFolder;

    Button upDir;

    public static void initWithPath(Context context, String currentSelection) {
        String path = currentSelection;

        File checkFile = new File(path);
        if (checkFile.isDirectory()) {

            File f[] = checkFile.listFiles();
            FilesInDir(f);
            if (FragmentSDCard.fileString1.size() >= 0) {
                FragmentSDCard.currentPath = path;
                FragmentSDCard.fileListArrayAdapter.notifyDataSetChanged();
            }
        } else if (checkFile.isFile()) {

            if (getTypeDescription(checkFile) == TYPE.AUDIO) {
                Intent i = new Intent(context, PlayerScreen.class);
                i.putExtra("mediapath", path);
                i.putExtra("sppos", -1);
                i.putExtra("fromBackground", false);
                i.putExtra("filelabel", checkFile.getName().substring(0, checkFile.getName().lastIndexOf(".")));
                CommonStaticClass.streaming = false;
                context.startActivity(i);
            } else {
                /*Toast.makeText(con,
                        "Please mark song position correctly",
                        Toast.LENGTH_SHORT).show();*/
            }

        }

    }

    public static TYPE getTypeDescription(File f) {
        String extension = Utils.getExtension(f);
        TYPE type = null;

        if (extension != null) {
            if (extension.equals(Utils.mp3) || extension.equals(Utils.ogg)) {
                type = TYPE.AUDIO;
            }
        }
        return type;
    }

    public static void FilesInDir(File[] file1) {
        FragmentSDCard.fileStringWithPath.clear();
        FragmentSDCard.fileString1.clear();
        int i = 0;
        String filePath = "";
        String fileName;

        if (file1 != null) {
            while (i != file1.length) {
                filePath = file1[i].getAbsolutePath();
                fileName = file1[i].getName();
                if (file1[i].isDirectory()
                        || getTypeDescription(file1[i]) == TYPE.AUDIO) {
                    if (fileName.indexOf(".") != 0) {
                        if (!filePath.equals("/mnt/sdcard/Music") && !filePath.equals("/mnt/sdcard/podmark")) {
                            FragmentSDCard.fileStringWithPath.add(filePath);
                            FragmentSDCard.fileString1.add(fileName);
                        } else {
                            FragmentSDCard.fileStringWithPath.add(0, filePath);
                            FragmentSDCard.fileString1.add(0, fileName);
                        }
                    }
                }
                i++;
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.layout_localfiles, container, false);
        fileListArrayAdapter = new BrowseListAdapter(this.getActivity(), fileString1, fileStringWithPath);
        upDir = (Button) view.findViewById(R.id.upDir);
        sdcardFileListView = (ListView) view.findViewById(R.id.localfileslist);
        sdcardFileListView.setAdapter(fileListArrayAdapter);
        sdcardFileListView.setOnItemClickListener(new ItemClickListener());
        sdcardFileListView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {
                // TODO Auto-generated method stub
                menu.setHeaderTitle("Access folder quickly");
                menu.add(0, ADDTO_QUICK, 0, "Add to Quick Access");
                menu.add(0, ADDTO_PLAYLIST, 0, "Add to Playlist");
            }
        });

        upDir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String folderPath = FragmentSDCard.currentPath;
                String upLoc = FragmentSDCard.baseFolder;
                if (!folderPath.equalsIgnoreCase("...")) {
                    upLoc = folderPath.substring(0, folderPath.lastIndexOf("/"));
                    if (upLoc.length() <= 0) {

                        folderPath = FragmentSDCard.baseFolder;
                    } else {

                    }
                }
                FragmentSDCard.initWithPath(view.getContext(), upLoc);
            }
        });

        initWithPath(view.getContext(), baseFolder);


        return view;
    }


}

class ItemClickListener implements AdapterView.OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> adapterView, View v, int pos, long id) {
        String currentSelection = (String) adapterView.getAdapter().getItem(pos);
        pos = pos + 1;
        FragmentSDCard.initWithPath(v.getContext(), FragmentSDCard.currentPath + "/" + currentSelection);

    }


}