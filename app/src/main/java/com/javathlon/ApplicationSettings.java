package com.javathlon;

/**
 * Created by ocakcit on 13/11/15.
 */
public class ApplicationSettings {

    public static Boolean isProxyOpen = false;
    public static String proxyAddress = "proxy.pozitron.com";
    public static String proxyHost = "3128";
    public static MemberMode memberMode = MemberMode.FULL;
    public static Long appId = 1L;
    public static String podcastModernServerUrl = "http://107.170.25.76:8080/PodcastModern/";
    public enum MemberMode {
        FREE,
        JAVACORE,
        FULL
    }
}
