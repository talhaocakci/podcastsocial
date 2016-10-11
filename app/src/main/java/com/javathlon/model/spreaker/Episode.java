package com.javathlon.model.spreaker;

import com.google.gson.annotations.SerializedName;

/**
 * Created by talha on 16.07.2015.
 */
public class Episode {

    @SerializedName("episode_id")
    private long id;
    @SerializedName("title")
    private String title;
    @SerializedName("download_url")
    private String downloadUrl;
    @SerializedName("published_at")
    private String publishDateString;
    @SerializedName("description")
    private String description;
    @SerializedName("length")
    private String length;

    //String description

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getPublishDateString() {
        return publishDateString;
    }

    public void setPublishDateString(String publishDateString) {
        this.publishDateString = publishDateString;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    // {"episode_id":6356402,"show_id":1398145,"user_id":8116815,"permalink":"21-pewdiepie-ve-youtube-girisimciligi","type":"RECORDED","on_air":false,"title":"#21 pewdiepie ve youtube giri\u015fimcili\u011fi","length":1891736,"published_at":"2015-07-16 07:30:19","auto_published_at":"2015-07-16 07:30:00","non_stop":false,"explicit":false,"download_enabled":true,"hidden":false,"ihr_hidden":false,"stream_id":null,"site_url":"http:\/\/www.spreaker.com\/user\/girisimcimuhabbeti\/21-pewdiepie-ve-youtube-girisimciligi","api_url":"http:\/\/api.spreaker.com\/episode\/6356402","download_url":"http:\/\/api.spreaker.com\/download\/episode\/6356402\/girisimci_muhabbeti_bolum_21.mp3","description":"Bu b\u00f6l\u00fcmde enerjimiz \u00e7ok y\u00fcksek! Ge\u00e7ti\u011fimiz haftalarda bulu\u015fmam\u0131za gelen ve bizi gaza getirmi\u015f olan dinleyicilerimize, b\u00f6l\u00fcm\u00fcm\u00fcze misafir olan Naci'ye ve slack kanal\u0131m\u0131zda konu\u015fmalar\u0131m\u0131za kat\u0131lan herkese te\u015fekk\u00fcrler!\n\nAna konumuz video. B\u00f6l\u00fcm\u00fc Youtube \u00fczerinden kazand\u0131\u011f\u0131 para ile (7.5 milyon dolar kazanm\u0131\u015f) herkesin (bizde dahil) diline d\u00fc\u015fm\u00fc\u015f olan Pewdiepie ve yapt\u0131\u011f\u0131 i\u015f hakk\u0131nda konu\u015farak a\u00e7\u0131yoruz. Ard\u0131ndan \u00fclkemizde video i\u00e7erik olu\u015fturarak ba\u015far\u0131l\u0131 bir yere gelmi\u015f olan GiyenBayan (Ezi Akba\u015f) ve e\u015fi AmcaO\u011flu'ndan bahsediyoruz.  Video ile alakal\u0131 konu\u015fmam\u0131z\u0131 Frans\u0131z medya devi Vivendi'nin Dailymotion'\u0131 almas\u0131 ile sonu\u00e7land\u0131r\u0131yoruz.\n\nBir di\u011fer konumuz ise Vestel'in ve di\u011fer yerel telefon \u00fcreticilerinin h\u00fck\u00fcmet ile yapt\u0131\u011f\u0131 g\u00f6r\u00fc\u015fmeler sonucunda yabanc\u0131 telefonlar\u0131n \u00fczerine koyulan ek vergiler - neden bu \u015fekilde bir aksiyon al\u0131nd\u0131, al\u0131nmas\u0131 do\u011fru mu, normal mi? bunu tart\u0131\u015f\u0131yoruz. \n\nB\u00f6l\u00fcm ile alakal\u0131 linkler:\nPewdiepie: https:\/\/www.youtube.com\/user\/PewDiePie\nGiyenBayan: https:\/\/www.youtube.com\/channel\/UC2F-vOoACt3f-gJCd6m8IWg\nAmcaO\u011flu: https:\/\/www.youtube.com\/channel\/UCYeIE19PpVowCrhB4E0JKnQ\n\nVivendi Daily Motion'\u0131n %80'ini sat\u0131n ald\u0131:\nhttp:\/\/techcrunch.com\/2015\/06\/30\/vivendi-buys-80-of-frances-dailymotion-valuing-the-youtube-rival-at-295m\/","published_at_locale":"today @ 10:30 AM","show":{"show_id":1398145,"title":"giri\u015fimci muhabbeti","permalink":"girisimci-muhabbeti","site_url":"http:\/\/www.spreaker.com\/show\/girisimci-muhabbeti","api_url":"http:\/\/api.spreaker.com\/show\/1398145","fb_page_id":null,"fb_page_name":null,"language":"tr","ihr_badge":false,"image":{"image_id":4930979,"type":1,"user_id":8116815,"width":1843,"height":1843,"small_url":"http:\/\/d1bm3dmew779uf.cloudfront.net\/small\/74218a6e08718d5cfdcbe7b427482e19.jpg","medium_url":"http:\/\/d1bm3dmew779uf.cloudfront.net\/medium\/74218a6e08718d5cfdcbe7b427482e19.jpg","large_url":"http:\/\/d1bm3dmew779uf.cloudfront.net\/large\/74218a6e08718d5cfdcbe7b427482e19.jpg","big_url":"http:\/\/d1bm3dmew779uf.cloudfront.net\/big\/74218a6e08718d5cfdcbe7b427482e19.jpg","play_url":"http:\/\/d1bm3dmew779uf.cloudfront.net\/play\/74218a6e08718d5cfdcbe7b427482e19.jpg"}},"status":"READY","styles":[],"tags":[{"tag_id":661196,"name":"giyenbayan"},{"tag_id":661195,"name":"letswatch"},{"tag_id":47406,"name":"pewdiepie"},{"tag_id":3728,"name":"youtube"}],"categories":[{"category_id":31,"name":"Talk","permalink":"talk","level":2}],"category":{"category_id":31,"name":"Talk","permalink":"talk","level":2},"author":{"user_id":8116815,"fullname":"girisimci muhabbeti","type":"REGISTERED","site_url":"http:\/\/www.spreaker.com\/user\/girisimcimuhabbeti","shows_url":"http:\/\/api.spreaker.com\/user\/8116815\/shows\/author","permalink":"girisimcimuhabbeti","profile_name":"broadcaster","occupation":4,"image":{"image_id":4963614,"type":2,"user_id":8116815,"small_url":"http:\/\/d1bm3dmew779uf.cloudfront.net\/small\/695450f4af886a87aa9221f3ed02e67d.jpg","medium_url":"http:\/\/d1bm3dmew779uf.cloudfront.net\/medium\/695450f4af886a87aa9221f3ed02e67d.jpg","large_url":"http:\/\/d1bm3dmew779uf.cloudfront.net\/large\/695450f4af886a87aa9221f3ed02e67d.jpg","play_url":"http:\/\/d1bm3dmew779uf.cloudfront.net\/play\/695450f4af886a87aa9221f3ed02e67d.jpg"},"username":"girisimcimuhabbeti","fb_user_id":"10152826173942689","description":"Giri\u015fimcilik ve teknoloji hakk\u0131nda T\u00fcrk\u00e7e bir podcast. Bar\u0131\u015f Ko\u00e7dur ve Sami Can Tando\u011fdu taraf\u0131ndan sunulmaktad\u0131r.","location":"Istanbul, Istanbul, Turkey","location_latitude":"41.018611907959","location_longitude":"28.964721679688"},"stats":{"plays":169,"plays_streaming":99,"plays_download":70,"likes":1,"messages":0}}
}
