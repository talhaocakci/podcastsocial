package com.javathlon.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class PodmarkDBTable {
    // Database creation SQL statement
    private static final String CREATE_USER_TABLE = "create table if not exists user  "
            + "(_id INTEGER PRIMARY KEY autoincrement,"
            +" display_name VARCHAR(60), "
            +" email VARCHAR(60), "
            +" gender VARCHAR(1), "
            +" min_age NUMBER, "
            +" fb_id VARCHAR(50), "
            +" google_id VARCHAR(50), "
            +"linked_in id VARCHAR(50), "
            +"twitter id VARCHAR(50), "
            +"swarm id VARCHAR(50), "
            +"instagram id VARCHAR(50), "
            +"register_date VARCHAR(20),"
            +"location VARCHAR(50), "
            +"locale VARCHAR(15))";


    private static final String CREATE_NOTE_TABLE = "create table if not exists podcast_notes  "
            + "(_id INTEGER PRIMARY KEY autoincrement,"
            + "podcast_id NUMBER,"
            + "song_sp_id NUMBER , "
            + "begin_sec NUMBER, "
            + "end_sec NUMBER, "
            + "beginend NUMBER,"
            + "songpath VARCHAR(300),"
            + "note_text TEXT,"
            + "author VARCHAR(60),"
            + "create_date VARCHAR(30),"
            +" upload_link VARCHAR(300) DEFAULT '',  "
            + "last_listen_date_mil NUMBER,"
            + " last_listen_date VARCHAR(30));";

    private static final String CREATE_STATISTICS_TABLE = "create table if not exists listen_statistics "
            + "(_id INTEGER  PRIMARY KEY autoincrement,"
            + "podcast_id VARCHAR(20),"
            + "start_pos NUMBER, "
            + "end_pos NUMBER, "
            + "last_listen_date VARCHAR(20), "
            + "create_date VARCHAR(20));";

    private static final String CREATE_PODCAST_CATALOG_TABLE = "create table if not exists podcast_catalog "
            + "(_id INTEGER  PRIMARY KEY autoincrement,"
            + "rss_url VARCHAR(300),"
            + "name VARCHAR(80),"
            + "image VARCHAR(400),"
            + "image_small VARCHAR(400),"
            + "author VARCHAR(200),"
            + "summary VARCHAR(500),"
            + "infourl VARCHAR(255),"
            + "isMainCatalog VARCHAR(1),"
            + "primaryGenre VARCHAR(255),"
            + "create_date VARCHAR(20),"
            + " is_subscribed VARCHAR(1), "
            + "last_rss_download_date VARCHAR(50),"
            + "last_downloaded_episode_date VARCHAR(50),"
            + " item_count INTEGER);";


    private static final String CREATE_UPLOAD_TRRACKER = "create table if not exists upload_tracker "
            + "(_id INTEGER  PRIMARY KEY autoincrement,"
            + "podcast_id VARCHAR(20),"
            + "uploaded VARCHAR(3));";

    private static final String CREATE_PODCAST_TABLE = "create table if not exists podcast "
            + "(_id INTEGER  PRIMARY KEY autoincrement,"
            + "podcast_id VARCHAR(20),"
            + "catalog_id INTEGER,"
            + "full_device_path VARCHAR(300),"
            + "name VARCHAR(50),"
            + "file_name VARCHAR(50),"
            + "download_link VARCHAR(300),"
            + "is_downloaded VARCHAR(1),"
            + "duration INTEGER, "
            + "duration_string VARCHAR(20), "
            + "size INTEGER, "
            + "publish_date INTEGER,"
            + "progress_second INTEGER,"
            + "progress_percentage INTEGER DEFAULT 0, "
            + "last_listen_date VARCHAR(20),"
            + "last_listen_date_mil VARCHAR(20),"
            + " create_date VARCHAR(20), description TEXT);";

    private static final String CREATE_PLAYLIST_TABLE = "create table if not exists playlist  "
            + "(_id INTEGER PRIMARY KEY autoincrement,"
            + "playlist_name VARCHAR(20), "
            + "isitFile VARCHAR(3),"
            + "songpath TEXT);";

    private static final String CREATE_WATCHLIST_TABLE = "create table if not exists watchlist  "
            + "(_id INTEGER PRIMARY KEY autoincrement,"
            + "_order INTEGER, "
            + "create_date VARCHAR(20),"
            + "songpath VARCHAR(200));";

    private static final String CREATE_DOWNLOADLIST_TABLE = "create table if not exists downloadlist  "
            + "(_id INTEGER PRIMARY KEY autoincrement,"
            + "_order INTEGER, "
            + "create_date VARCHAR(20),"
            + "songpath VARCHAR(200));";

    private static final String CREATE_QUICKLIST_TABLE = "create table if not exists quicklist  "
            + "(_id INTEGER PRIMARY KEY autoincrement,"
            + "isitFile VARCHAR(3),"
            + "songpath TEXT);";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_NOTE_TABLE);
        database.execSQL(CREATE_STATISTICS_TABLE);
        database.execSQL(CREATE_PODCAST_TABLE);
        database.execSQL(CREATE_PLAYLIST_TABLE);
        database.execSQL(CREATE_QUICKLIST_TABLE);
        database.execSQL(CREATE_WATCHLIST_TABLE);
        database.execSQL(CREATE_DOWNLOADLIST_TABLE);
        database.execSQL(CREATE_UPLOAD_TRRACKER);
        database.execSQL(CREATE_PODCAST_CATALOG_TABLE);
        database.execSQL(CREATE_USER_TABLE);
        createInitialItems();
    }

    public static void createInitialItems(){



    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {

        Log.w(PodmarkDBTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS todo");
        onCreate(database);
    }
}
