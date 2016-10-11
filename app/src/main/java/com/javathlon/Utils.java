package com.javathlon;

import java.io.File;

public class Utils {


    /*audios*/
    public final static String mp3 = "mp3";
    public final static String wav = "wav";
    public final static String m4a = "m4a";
    public final static String aac = "aac";
    public final static String flac = "flac";
    public final static String mid = "mid";
    public final static String xmf = "xmf";
    public final static String mxmf = "mxmf";
    public final static String rtttl = "rtttl";
    public final static String rtx = "rtx";
    public final static String ota = "ota";
    public final static String imy = "imy";
    public final static String ogg = "ogg";

    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }
}