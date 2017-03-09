package com.javathlon.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.javathlon.CatalogData;
import com.javathlon.PodcastData;
import com.javathlon.PurchasedPodcastItem;
import com.javathlon.memsoft.MemsoftUtil;
import com.javathlon.model.ListenStatistic;
import com.javathlon.model.Note;
import com.javathlon.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DBAccessor {

    // Database fields
    public static final String KEY_ID = "_id";
    public static final String KEY_PODCASTID = "podcast_id";
    public static final String KEY_CATALOGID = "catalog_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_SONGSPID = "song_sp_id";
    public static final String KEY_BEGINSEC = "begin_sec";
    public static final String KEY_ENDSEC = "end_sec";
    public static final String KEY_BEGINEND = "beginend";
    public static final String KEY_SONGPATH = "songpath";
    public static final String KEY_NOTETEXT = "note_text";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_CREATEDATE = "create_date";
    public static final String KEY_BUCKETNAME = "bucket_name";
    public static final String KEY_LASTLISTENDATEMIL = "last_listen_date_mil";
    public static final String KEY_LASTLISTENDATE = "last_listen_date";
    private static final String DB_TABLE_NOTE = "podcast_notes";
    private static final String KEY_LISTEN_STATISTICS = "listen_statistics";
    private static final String KEY_USER = "user";

    public static final String KEY_LISTENCOUNT = "listen_count";


    public static final String KEY_FULLDEVICEPATH = "full_device_path";
    public static final String KEY_FILENAME = "file_name";
    public static final String KEY_DOWNLINK = "download_link";

    private static final String DB_TABLE_PODCAST = "podcast";


    private Context context;
    private static SQLiteDatabase db;
    private static DatabaseHelper dbHelper = null;

    public DBAccessor(Context context) {
        this.context = context;

    }

    public DBAccessor open() throws SQLException {
        if (dbHelper == null) {
            dbHelper = new DatabaseHelper(context);
            db = dbHelper.getWritableDatabase();
        }
        return this;
    }

    public synchronized void close() {
        if (dbHelper != null) {
            dbHelper.close();
            db.close();
            dbHelper = null;
            db = null;
        }
    }


    /**
     * Create a new todo If the todo is successfully created return the new
     * rowId for that note, otherwise return a -1 to indicate failure.
     */

    public long insertListenStatistic(long podcastId, long startPost, long endPos) {
        ContentValues values = new ContentValues();
        values.put("podcast_id", podcastId);
        values.put("start_pos", startPost);
        values.put("end_pos", endPos);
        values.put("create_date", MemsoftUtil.getTimeAsString());
        return db.insert(KEY_LISTEN_STATISTICS, null, values);
    }

    public long insertUser(User user) {
        ContentValues values = new ContentValues();
        values.put("display_name", user.getDisplayName());
        values.put("email", user.getEmail());
        values.put("gender", user.getGender());
        values.put("register_date", MemsoftUtil.getTimeAsString());
        values.put("fb_id", user.getFbToken());
        values.put("google_id", user.getGoogleToken());
        values.put("min_age", user.getMinAge());
        values.put("location", user.getLocation());
        values.put("locale", user.getLanguage());

        return db.insert(KEY_USER, null, values);
    }

    public long getUserIdWithEmail(String email) {
        String sql = "select _id from " + KEY_USER + " where email= '" + email + "'";
        Cursor mCursor = db.rawQuery(sql, null);
        long id = -1;
        if (mCursor.moveToFirst()) {
            id = mCursor.getLong(mCursor.getColumnIndex("_id"));
        }
        return id;
    }

    public User getAnyUser() {
        String sql = "select * from " + KEY_USER + "";
        Cursor mCursor = db.rawQuery(sql, null);
        User user = new User();
        long id = -1;
        if (mCursor.moveToFirst()) {
            user.setUserId(mCursor.getLong(mCursor.getColumnIndex("_id")));
            user.setDisplayName(mCursor.getString(mCursor.getColumnIndex("display_name")));
            user.setEmail(mCursor.getString(mCursor.getColumnIndex("email")));
            user.setGender(mCursor.getString(mCursor.getColumnIndex("gender")));
            user.setMinAge(mCursor.getInt(mCursor.getColumnIndex("min_age")));
            user.setLanguage(mCursor.getString(mCursor.getColumnIndex("locale")));
            user.setLocation(mCursor.getString(mCursor.getColumnIndex("location")));
            user.setGoogleToken(mCursor.getString(mCursor.getColumnIndex("google_id")));
            user.setFbToken(mCursor.getString(mCursor.getColumnIndex("fb_id")));
            user.setRegisterDate(mCursor.getString(mCursor.getColumnIndex("register_date")));
        }
        return user;
    }

    public boolean updateUserSocialId(long userId, User.SOCIAL_TYPES type, String id) {

        ContentValues values = new ContentValues();
        if (User.SOCIAL_TYPES.FACEBOOK.equals(type))
            values.put("fb_id", id);
        else if (User.SOCIAL_TYPES.GOOGLE.equals(type)) {
            values.put("google_id", id);
        }

        return db.update("user", values, "_id" + "=" + userId, null) > 0;

    }

    public List<ListenStatistic> getListenPositionsOfPodcast(long podcastId) {
        String sql = "select * from " + KEY_LISTEN_STATISTICS + " where podcast_id= " + podcastId;
        Cursor mCursor = db.rawQuery(sql, null);
        List<ListenStatistic> statisticList = new ArrayList<ListenStatistic>();
        if (mCursor.moveToFirst()) {
            do {
                ListenStatistic stat = new ListenStatistic();
                stat.setPodcastId(podcastId);
                stat.setStartPos(mCursor.getLong(mCursor.getColumnIndex("start_pos")));
                stat.setEndPos(mCursor.getLong(mCursor.getColumnIndex("end_pos")));
                stat.setCreateDate(mCursor.getString(mCursor.getColumnIndex("create_date")));
                statisticList.add(stat);
            } while (mCursor.moveToNext());
        }
        return statisticList;
    }

    public long createNote(long podcast_id, int song_sp_id, int begin_sec, int end_sec, String songpath, String notetext, String author, String create_date, long last_listen_date_mil, String last_listen_date) {
        ContentValues values = createNoteTableContentValues(podcast_id, song_sp_id, begin_sec,
                end_sec, songpath, notetext, author, create_date, last_listen_date_mil, last_listen_date);

        return db.insert(DB_TABLE_NOTE, null, values);
    }

    public long createPodcastCatalogItem(CatalogData data, String isMain) {

        ContentValues values = new ContentValues();
        values.put("rss_url", data.rss);
        values.put("name", data.name);
        values.put("image", data.image);
        values.put("image_small", data.imageSmall);
        values.put("author", data.author);
        values.put("summary", data.summary);
        values.put("bucket_name", data.bucketName);

        CatalogData catalogData = getPodcastCatalogById(data.id);

        if (catalogData.id != 0L) {
            db.update("podcast_catalog", values, "_id=" + catalogData.id, null);
            return data.id;
        } else {
            values.put("create_date", MemsoftUtil.getTimeAsString());
            values.put("isMainCatalog", isMain);
            values.put("last_rss_download_date", data.lastRssUpdate);
            return db.insert("podcast_catalog", null, values);
        }


    }

    public List<CatalogData> getDownloadedItemInfoByCatalog() {
        String sql = "select count(c._id) as countitem, c.name as name, c.rss_url as rss, c.image image, c._id id  from podcast_catalog c inner join podcast p" +
                " on c._id = p.catalog_id where p.full_device_path is not null and p.full_device_path <> '' " +
                " group by c._id, c.name, c.rss_url, c.image, c._id";
        Cursor mCursor = db.rawQuery(sql, null);
        List<CatalogData> itemList = new ArrayList<CatalogData>();
        if (mCursor.moveToFirst()) {
            do {
                CatalogData item = new CatalogData();
                int count = mCursor.getInt(mCursor.getColumnIndex("countitem"));
                String name = mCursor.getString(mCursor.getColumnIndex("name"));
                String rss = mCursor.getString(mCursor.getColumnIndex("rss"));
                String image = mCursor.getString(mCursor.getColumnIndex("image"));
                long id = mCursor.getLong(mCursor.getColumnIndex("id"));
                item.id = id;
                item.image = image;
                item.name = name;
                item.rss = rss;
                item.trackCount = count;
                itemList.add(item);
            } while (mCursor.moveToNext());
        }
        return itemList;
    }

    public List<PodcastData> getPodcastsByCatalogId(long catalogId, String needDownloaded) {

        String where = "";
        if (needDownloaded != null && needDownloaded.equals("y"))
            where = " and full_device_path <> ''";

        String sql = "select * from podcast where catalog_id =" + catalogId + where;
        Cursor mCursor = db.rawQuery(sql, null);

        List<PodcastData> podcastList = new ArrayList<PodcastData>();
        if (mCursor.moveToFirst()) {
            do {
                PodcastData data = new PodcastData();
                data.id = mCursor.getLong(mCursor.getColumnIndex("_id"));
                data.editionTitle = mCursor.getString(mCursor.getColumnIndex("name"));
                data.setIsDownloaded(mCursor.getString(mCursor.getColumnIndex("is_downloaded")));
                data.progressSecond = mCursor.getString(mCursor.getColumnIndex("progress_second"));
                data.url = mCursor.getString(mCursor.getColumnIndex("download_link"));
                data.duration = mCursor.getLong(mCursor.getColumnIndex("duration"));
                data.publishDateLong = mCursor.getLong(mCursor.getColumnIndex("publish_date"));
                data.size = mCursor.getLong(mCursor.getColumnIndex("size"));
                data.durationString = mCursor.getString(mCursor.getColumnIndex("duration_string"));
                data.devicePath = mCursor.getString(mCursor.getColumnIndex("full_device_path"));
                data.progressPercentage = mCursor.getInt(mCursor.getColumnIndex("progress_percentage"));
                data.catalogId = (int) catalogId;

                podcastList.add(data);
            } while (mCursor.moveToNext());
        }
        return podcastList;

    }

    public List<PodcastData> getPodcastsByFilter(long catalogId, String keyword, String downloaded, int progress) {


        String sql = "select * from podcast where catalog_id = " + catalogId;
        if (keyword != null && !keyword.equals(""))
            sql += " and (description like " + "'%" + keyword + "%' OR name like '%" + keyword + "%') ";
        if (downloaded != null) {
            sql += " and is_downloaded = '" + downloaded + "'";
        }
        if (progress != -1)
            if (progress > 0)
                sql += " and  progress_percentage >= " + progress;
            else
                sql += " and progress_percentage = " + 0;

        List<PodcastData> dataList = new ArrayList<PodcastData>();
        Cursor mCursor = db.rawQuery(sql, null);
        if (mCursor.moveToFirst()) {
            do {
                PodcastData data = new PodcastData();
                data.id = mCursor.getLong(mCursor.getColumnIndex("_id"));
                data.editionTitle = mCursor.getString(mCursor.getColumnIndex("name"));
                data.setIsDownloaded(mCursor.getString(mCursor.getColumnIndex("is_downloaded")));
                data.progressSecond = mCursor.getString(mCursor.getColumnIndex("progress_second"));
                data.url = mCursor.getString(mCursor.getColumnIndex("download_link"));
                data.duration = mCursor.getLong(mCursor.getColumnIndex("duration"));
                data.publishDateLong = mCursor.getLong(mCursor.getColumnIndex("publish_date"));
                data.size = mCursor.getLong(mCursor.getColumnIndex("size"));
                data.durationString = mCursor.getString(mCursor.getColumnIndex("duration_string"));
                data.devicePath = mCursor.getString(mCursor.getColumnIndex("full_device_path"));
                data.progressPercentage = mCursor.getInt(mCursor.getColumnIndex("progress_percentage"));
                data.description = mCursor.getString(mCursor.getColumnIndex("description"));
                data.catalogId = (int) catalogId;

                dataList.add(data);
            } while (mCursor.moveToNext());
        }
        return dataList;
    }


    public PodcastData getPodcastByUrl(String url) {
        String sql = "";
        if (url.startsWith("http"))
            sql = "select * from podcast where download_link ='" + url + "'";
        else
            sql = "select * from podcast where full_device_path ='" + url + "'";
        PodcastData data = null;
        Cursor mCursor = db.rawQuery(sql, null);
        if (mCursor.moveToFirst()) {
            do {
                data = new PodcastData();
                data.id = mCursor.getLong(mCursor.getColumnIndex("_id"));
                data.editionTitle = mCursor.getString(mCursor.getColumnIndex("name"));
                data.setIsDownloaded(mCursor.getString(mCursor.getColumnIndex("is_downloaded")));
                data.progressSecond = mCursor.getString(mCursor.getColumnIndex("progress_second"));
                data.url = mCursor.getString(mCursor.getColumnIndex("download_link"));
                data.duration = mCursor.getLong(mCursor.getColumnIndex("duration"));
                data.publishDateLong = mCursor.getLong(mCursor.getColumnIndex("publish_date"));
                data.size = mCursor.getLong(mCursor.getColumnIndex("size"));
                data.durationString = mCursor.getString(mCursor.getColumnIndex("duration_string"));
                data.devicePath = mCursor.getString(mCursor.getColumnIndex("full_device_path"));
                data.progressPercentage = mCursor.getInt(mCursor.getColumnIndex("progress_percentage"));
                data.description = mCursor.getString(mCursor.getColumnIndex("description"));
                data.catalogId = mCursor.getInt(mCursor.getColumnIndex("catalog_id"));


            } while (mCursor.moveToNext());
        }
        return data;
    }

    public PodcastData getPodcastById(long id) {
        String sql = "select * from podcast where _id = " + id;
        PodcastData data = null;
        Cursor mCursor = db.rawQuery(sql, null);
        if (mCursor.moveToFirst()) {
            do {
                data = new PodcastData();
                data.id = mCursor.getLong(mCursor.getColumnIndex("_id"));
                data.editionTitle = mCursor.getString(mCursor.getColumnIndex("name"));
                data.setIsDownloaded(mCursor.getString(mCursor.getColumnIndex("is_downloaded")));
                data.progressSecond = mCursor.getString(mCursor.getColumnIndex("progress_second"));
                data.url = mCursor.getString(mCursor.getColumnIndex("download_link"));
                data.duration = mCursor.getLong(mCursor.getColumnIndex("duration"));
                data.publishDateLong = mCursor.getLong(mCursor.getColumnIndex("publish_date"));
                data.size = mCursor.getLong(mCursor.getColumnIndex("size"));
                data.durationString = mCursor.getString(mCursor.getColumnIndex("duration_string"));
                data.devicePath = mCursor.getString(mCursor.getColumnIndex("full_device_path"));
                data.progressPercentage = mCursor.getInt(mCursor.getColumnIndex("progress_percentage"));
                data.description = mCursor.getString(mCursor.getColumnIndex("description"));


            } while (mCursor.moveToNext());
        }
        return data;
    }


    public void bulkInsertListenStatisctics(List<ListenStatistic> stats) {
        db.beginTransaction();
        for (ListenStatistic stat : stats) {
            ContentValues values = new ContentValues();
            values.put("podcast_id", stat.getPodcastId());
            values.put("start_pos", stat.getStartPos());
            values.put("end_pos", stat.getEndPos());
            values.put("create_date", MemsoftUtil.getTimeAsString());
            db.insert(KEY_LISTEN_STATISTICS, null, values);
        }
        db.endTransaction();
    }

    public void savePurchasedPodcastItem(Long podcastId, Date validTill) {
        PurchasedPodcastItem item = getPurchasedPodcastItem(podcastId);
        if (item == null) {
            ContentValues values = new ContentValues();
            values.put("podcast_id", podcastId);
            values.put("valid_till", MemsoftUtil.dateToString(validTill));
            db.insert("purchased_podcasts", null, values);
        } else {
            ContentValues values = new ContentValues();
            values.put("podcast_id", podcastId);
            values.put("valid_till", MemsoftUtil.dateToString(validTill));
            db.update("purchased_podcasts", values, "podcast_id=" + podcastId, null);
        }

    }

    public PurchasedPodcastItem getPurchasedPodcastItem(Long podcastId) {

        String sql = "select * from purchased_podcasts where podcast_id = " + podcastId;
        PurchasedPodcastItem purchasedPodcastItem = null;
        Cursor mCursor = db.rawQuery(sql, null);
        if (mCursor.moveToFirst()) {
            purchasedPodcastItem = new PurchasedPodcastItem();
            purchasedPodcastItem.setId(mCursor.getLong(mCursor.getColumnIndex("_id")));
            purchasedPodcastItem.setPodcastId(mCursor.getLong(mCursor.getColumnIndex("podcast_id")));
            purchasedPodcastItem.setValidTill(MemsoftUtil.getTimeFromString(mCursor.getString(mCursor.getColumnIndex("valid_till"))));
        }
        return purchasedPodcastItem;
    }

    public void bulkInsertPodcastData(List<PodcastData> dataList, long catalogId) {


        String sql = "INSERT INTO " + DB_TABLE_PODCAST + " (" +

                KEY_CATALOGID + ", " +
                KEY_FULLDEVICEPATH + ", " +
                KEY_FILENAME + ", " +
                KEY_DOWNLINK + ", " +
                KEY_LASTLISTENDATEMIL + ", " +
                KEY_LASTLISTENDATE + ", " +
                KEY_CREATEDATE + ", " + KEY_NAME + ", is_downloaded, progress_second, duration, publish_date, size, duration_string" +
                ", description)" + " VALUES (?,?,?,?,?,?,?, ?, ? ,?, ? , ?, ?, ?, ?);";
        SQLiteStatement statement = db.compileStatement(sql);
        db.beginTransaction();
        String currentDate = MemsoftUtil.getTimeAsString();
        for (int i = 0; i < dataList.size(); i++) {
            PodcastData data = dataList.get(i);
            statement.clearBindings();
            statement.bindLong(1, catalogId);
            statement.bindString(2, "");
            statement.bindString(3, data.editionTitle);
            statement.bindString(4, data.url);
            statement.bindLong(5, 0L);
            statement.bindString(6, "");
            statement.bindString(7, currentDate);
            statement.bindString(8, data.editionTitle);
            statement.bindString(9, "n");
            statement.bindLong(10, 0);
            statement.bindLong(11, data.duration);
            statement.bindLong(12, data.publishDateLong);
            statement.bindLong(13, data.size);
            statement.bindString(14, data.durationString == null ? "" : data.durationString);
            statement.bindString(15, data.description == null ? "" : data.description);
            statement.execute();
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public boolean updatePodcastCatalogRSSDownload(long catalogId, String last_rss_download_date) {
        ContentValues values = new ContentValues();
        values.put("last_rss_download_date", last_rss_download_date);

        return db.update("podcast_catalog", values, "_id" + "=" + catalogId, null) > 0;
    }

    public boolean updatePodcastProgressSecond(PodcastData data, long progressSecond) {
        ContentValues values = new ContentValues();
        values.put("progress_second", progressSecond);
        int progress = (int) (progressSecond * 100 / data.duration);
        values.put("progress_percentage", progress);

        return db.update("podcast", values, "_id" + "=" + data.id, null) > 0;
    }

    public boolean updatePodcastCatalogToMain(long catalogId, String isMain) {
        ContentValues values = new ContentValues();
        values.put("isMainCatalog", isMain);

        return db.update("podcast_catalog", values, "_id" + "=" + catalogId, null) > 0;
    }

    public boolean updatePodcastIsDownloaded(long podcastId, String isDownloaded, String devicePath) {
        ContentValues values = new ContentValues();
        values.put("is_downloaded", isDownloaded);
        values.put("full_device_path", devicePath);

        return db.update("podcast", values, "_id" + "=" + podcastId, null) > 0;
    }

    public boolean updatePodcastIsSubscribed(long catalogId, String isSubscribed) {
        ContentValues values = new ContentValues();
        values.put("is_subscribed", isSubscribed);
        return db.update("podcast_catalog", values, "_id" + "=" + catalogId, null) > 0;
    }

    public boolean updatePodcastIsDownloadedByCatalogId(long catalogId, String isDownloaded) {
        ContentValues values = new ContentValues();
        values.put("is_downloaded", "n");
        values.put("full_device_path", "");

        return db.update("podcast", values, "catalog_id" + "=" + catalogId, null) > 0;
    }


    public CatalogData getPodcastCatalogByRss(String rss) {
        String sql = "select * from podcast_catalog where rss_url ='" + rss + "'";
        Cursor mCursor = db.rawQuery(sql, null);
        CatalogData data = new CatalogData();
        data.id = 0L;
        if (mCursor.moveToFirst()) {

            data.id = mCursor.getLong(mCursor.getColumnIndex("_id"));
            data.name = mCursor.getString(mCursor.getColumnIndex("name"));
            data.image = mCursor.getString(mCursor.getColumnIndex("image"));
            data.author = mCursor.getString(mCursor.getColumnIndex("author"));
            data.lastRssUpdate = mCursor.getString(mCursor.getColumnIndex("last_rss_download_date"));
            data.createDate = mCursor.getString(mCursor.getColumnIndex("create_date"));

        }
        return data;
    }

    public CatalogData getPodcastCatalogById(long id) {
        String sql = "select * from podcast_catalog where _id =" + id;
        Cursor mCursor = db.rawQuery(sql, null);
        CatalogData data = new CatalogData();
        data.id = 0L;
        if (mCursor.moveToFirst()) {

            data.id = mCursor.getLong(mCursor.getColumnIndex("_id"));
            data.name = mCursor.getString(mCursor.getColumnIndex("name"));
            data.image = mCursor.getString(mCursor.getColumnIndex("image"));
            data.author = mCursor.getString(mCursor.getColumnIndex("author"));
            data.lastRssUpdate = mCursor.getString(mCursor.getColumnIndex("last_rss_download_date"));
            data.createDate = mCursor.getString(mCursor.getColumnIndex("create_date"));

        }
        return data;
    }

    public List<CatalogData> getMainCatalogList() {
        String sql = "select * from podcast_catalog where isMainCatalog ='y'";
        Cursor mCursor = db.rawQuery(sql, null);
        List<CatalogData> catalogList = new ArrayList<CatalogData>();
        if (mCursor.moveToFirst()) {
            do {
                CatalogData data = new CatalogData();
                data.id = mCursor.getLong(mCursor.getColumnIndex("_id"));
                data.name = mCursor.getString(mCursor.getColumnIndex("name"));
                data.image = mCursor.getString(mCursor.getColumnIndex("image"));
                data.author = mCursor.getString(mCursor.getColumnIndex("author"));
                data.rss = mCursor.getString(mCursor.getColumnIndex("rss_url"));
                data.lastRssUpdate = mCursor.getString(mCursor.getColumnIndex("last_rss_download_date"));
                data.isSubscribed = mCursor.getString(mCursor.getColumnIndex("is_subscribed"));
                catalogList.add(data);
            } while (mCursor.moveToNext());
        }
        return catalogList;
    }


    public List<CatalogData> getSubscribedCatalogList() {
        String sql = "select * from podcast_catalog where is_subscribed ='y' order by create_date desc";
        Cursor mCursor = db.rawQuery(sql, null);
        List<CatalogData> catalogList = new ArrayList<CatalogData>();
        if (mCursor.moveToFirst()) {
            do {
                CatalogData data = new CatalogData();
                data.id = mCursor.getLong(mCursor.getColumnIndex("_id"));
                data.name = mCursor.getString(mCursor.getColumnIndex("name"));
                data.image = mCursor.getString(mCursor.getColumnIndex("image"));
                data.author = mCursor.getString(mCursor.getColumnIndex("author"));
                data.rss = mCursor.getString(mCursor.getColumnIndex("rss_url"));
                data.lastRssUpdate = mCursor.getString(mCursor.getColumnIndex("last_rss_download_date"));
                data.isSubscribed = mCursor.getString(mCursor.getColumnIndex("is_subscribed"));
                catalogList.add(data);
            } while (mCursor.moveToNext());
        }
        return catalogList;
    }

    public Cursor getLastPlayedNotesListResult() {
        String sql = "select n.*, p.name name from podcast p inner join podcast_notes n on p._id = n.podcast_id order by n.last_listen_date_mil desc";
        Cursor mCursor = db.rawQuery(sql, null);
        return mCursor;
    }

    public Cursor getLastPlayedFilesListResult() {
        String sql = "Select * from podcast where last_listen_date_mil is not null and last_listen_date_mil <>  0 order by last_listen_date_mil desc";
        Cursor mCursor = db.rawQuery(sql, null);
        return mCursor;
    }


    public Cursor executeQuery(String sql) {
        Log.e("SQL:", sql);
        Cursor mCursor = db.rawQuery(sql, null);
        return mCursor;
    }

    public boolean executeInsertQuery(String sql) {
        try {
            db.execSQL(sql);
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }
    }

    /**
     * Update the todo
     */


    public boolean updateNoteRemoteLink(long noteId, String shareLink) {
        ContentValues values = new ContentValues();
        values.put("upload_link", shareLink);

        return db.update(DB_TABLE_NOTE, values, KEY_ID + "=" + noteId, null) > 0;
    }

    public boolean updateLastListenDate(long rowId, String last_listen_date, String last_listen_date_mil) {
        ContentValues values = new ContentValues();
        values.put(KEY_LASTLISTENDATEMIL, last_listen_date_mil);
        values.put(KEY_LASTLISTENDATE, last_listen_date);
        return db.update(DB_TABLE_NOTE, values, KEY_ID + "=" + rowId, null) > 0;
    }

    public long createPodcastEntry(String podcast_id, long catalog_id, String full_device_path,
                                   String file_name, String download_link, String last_listen_date,
                                   String last_listen_date_mil, String create_date) {
        ContentValues values = new ContentValues();
        values.put(KEY_PODCASTID, podcast_id);
        values.put(KEY_CATALOGID, catalog_id);
        values.put(KEY_FULLDEVICEPATH, full_device_path);
        values.put(KEY_FILENAME, file_name);
        values.put(KEY_DOWNLINK, download_link);
        values.put(KEY_LASTLISTENDATEMIL, last_listen_date_mil);
        values.put(KEY_LASTLISTENDATE, last_listen_date);
        values.put(KEY_CREATEDATE, create_date);
        return db.insert(DB_TABLE_PODCAST, null, values);
    }

    public int getPodcastCount() {
        String sql = "Select * from podcast";
        Cursor mCursor = null;
        int i = 0;
        try {
            mCursor = this.executeQuery(sql);
            i = mCursor.getCount();
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            mCursor.close();
        }

        return i;
    }

    public long updatePodcastEntry(String full_device_path, String last_listen_date,
                                   String last_listen_date_mil) {
        ContentValues values = new ContentValues();
        values.put(KEY_LASTLISTENDATEMIL, last_listen_date_mil);
        values.put(KEY_LASTLISTENDATE, last_listen_date);
        return db.update(DB_TABLE_PODCAST, values, KEY_FULLDEVICEPATH + "= '" + full_device_path + "'", null);
    }

    public long updateDownloadLink(long id, String downlink) {
        ContentValues values = new ContentValues();
        values.put(KEY_DOWNLINK, downlink);
        return db.update(DB_TABLE_PODCAST, values, KEY_PODCASTID + "= " + id, null);
    }

    /**
     * Deletes todo
     */

    public boolean deleteNote(long rowId) {
        return db.delete(DB_TABLE_NOTE, KEY_ID + "=" + rowId, null) > 0;
    }


    /**
     * Return a Cursor over the list of all todo in the database
     *
     * @return Cursor over all notes
     */

    public List<Note> fetchAllNotes(String path) {


        String sql = "select * from " + DB_TABLE_NOTE + " where path ='" + path + "'";

        List<Note> noteList = new ArrayList<Note>();
        Cursor mCursor = db.rawQuery(sql, null);
        if (mCursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setAudioPath(mCursor.getString(mCursor.getColumnIndex(KEY_SONGPATH)));
                note.setAuthor(mCursor.getString(mCursor.getColumnIndex(KEY_AUTHOR)));
                note.setBeginSec(mCursor.getInt(mCursor.getColumnIndex(KEY_BEGINSEC)));
                note.setCreateDate(mCursor.getString(mCursor.getColumnIndex(KEY_CREATEDATE)));
                note.setEndSec(mCursor.getInt(mCursor.getColumnIndex(KEY_ENDSEC)));
                note.setId(mCursor.getLong(mCursor.getColumnIndex(KEY_PODCASTID)));
                note.setLastListenDate(mCursor.getString(mCursor.getColumnIndex(KEY_LASTLISTENDATE)));
                note.setLastListenDateMil(mCursor.getLong(mCursor.getColumnIndex(KEY_LASTLISTENDATEMIL)));

                noteList.add(note);
            } while (mCursor.moveToNext());
        }


        return noteList;
    }

    public List<Note> fetchAllNotes(long podcastId) {
        String sql = "select * from " + DB_TABLE_NOTE + " where podcast_id = " + podcastId;

        List<Note> noteList = new ArrayList<Note>();
        Cursor mCursor = db.rawQuery(sql, null);
        if (mCursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setAudioPath(mCursor.getString(mCursor.getColumnIndex(KEY_SONGPATH)));
                note.setAuthor(mCursor.getString(mCursor.getColumnIndex(KEY_AUTHOR)));
                note.setBeginSec(mCursor.getInt(mCursor.getColumnIndex(KEY_BEGINSEC)));
                note.setCreateDate(mCursor.getString(mCursor.getColumnIndex(KEY_CREATEDATE)));
                note.setEndSec(mCursor.getInt(mCursor.getColumnIndex(KEY_ENDSEC)));
                note.setId(mCursor.getLong(mCursor.getColumnIndex(KEY_ID)));
                note.setLastListenDate(mCursor.getString(mCursor.getColumnIndex(KEY_LASTLISTENDATE)));
                note.setLastListenDateMil(mCursor.getLong(mCursor.getColumnIndex(KEY_LASTLISTENDATEMIL)));
                note.setText(mCursor.getString(mCursor.getColumnIndex("note_text")));
                note.setPodcastId(mCursor.getLong(mCursor.getColumnIndex(KEY_PODCASTID)));
                noteList.add(note);
            } while (mCursor.moveToNext());


        }
        return noteList;
    }

    /**
     * Return a Cursor positioned at the defined todo
     */

    public Cursor fetchNote(long rowId, String path) throws SQLException {
//		Log.e("fetchNote","fetchNote "+(rowId+1));
        Log.e("rowId", "fetchNote " + (rowId));
        Log.e("path", "fetchNote " + path);
        String sql = "Select * from " + DB_TABLE_NOTE + " where song_sp_id='" + rowId + "' and songpath='" + path + "'";
        Log.e("Sql:::::", sql);
        Cursor mCursor;
        try {
            mCursor = db.rawQuery(sql, null);
        } catch (Exception e) {
            // TODO: handle exception
            mCursor = db.rawQuery(sql, null);

        }

//		Cursor mCursor = db.query(true, DB_TABLE_NOTE, new String[] { KEY_ID,
//				KEY_PODCASTID,KEY_SONGSPID, KEY_BEGINSEC, KEY_ENDSEC,KEY_BEGINEND,KEY_SONGPATH,KEY_NOTETEXT,KEY_AUTHOR,KEY_CREATEDATE,KEY_LASTLISTENDATEMIL,KEY_LASTLISTENDATE }, KEY_SONGSPID + "="
//				+ (rowId) +" AND "+KEY_SONGPATH +" = '"+path+"'", null, null, null, null, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    private ContentValues createNoteTableContentValues(long podcast_id, int song_sp_id, int begin_sec, int end_sec, String songpath, String notetext, String author, String create_date, Long last_listen_date_mil, String last_listen_date) {
        ContentValues values = new ContentValues();
        values.put(KEY_PODCASTID, podcast_id);
        values.put(KEY_SONGSPID, song_sp_id);
        values.put(KEY_BEGINSEC, begin_sec);
        values.put(KEY_ENDSEC, end_sec);

        values.put(KEY_SONGPATH, songpath);
        values.put(KEY_NOTETEXT, notetext);
        values.put(KEY_AUTHOR, author);
        values.put(KEY_CREATEDATE, create_date);
        values.put(KEY_LASTLISTENDATEMIL, last_listen_date_mil);
        values.put(KEY_LASTLISTENDATE, last_listen_date);
        return values;
    }


}
