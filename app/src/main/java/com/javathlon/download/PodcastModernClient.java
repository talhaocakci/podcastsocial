package com.javathlon.download;

import android.os.AsyncTask;

import com.javathlon.ApplicationSettings;
import com.javathlon.apiclient.APIServiceGenerator;
import com.javathlon.apiclient.PodcastModernAPI;
import com.javathlon.memsoft.SecureUrl;
import com.javathlon.model.User;
import com.javathlon.model.podcastmodern.Application;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;

public class PodcastModernClient {

    private static PodcastModernAPI api;

    static {
        api = APIServiceGenerator.createService(PodcastModernAPI.class, "javathlon", "JavathlonSuperUser19871987");
    }


    public static void getPodcastsByApplication(Integer applicationId, Callback<Application> callback)
            throws IOException, ExecutionException, InterruptedException {

          api.getPodcastsOfApp(applicationId.toString()).enqueue(callback);

}

    public static void saveUser(User user, Long applicationId, Callback<Long> callback) {
        api.saveUser(applicationId, user).enqueue(callback);
    }

    public static String getSignedUrl(String folder, String fileKey, boolean isFree) {

        if (fileKey.startsWith("http") || fileKey.startsWith("file://")) {
            return fileKey;
        }

        if (!isFree && (ApplicationSettings.memberMode == ApplicationSettings.MemberMode.FREE)) {
            return null;
        }

        SecureUrl secureURL = null;
        try {
            secureURL = new AsyncTask<String, Void, SecureUrl>() {
                protected SecureUrl doInBackground(String... param) {

                    Call<SecureUrl> app = api.getSecureURL(param[0].toString(), param[1].toString());
                    try {
                        return app.execute().body();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
            }.execute(fileKey, folder).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        if (secureURL != null)
            return secureURL.getUrl();
        else
            return null;
    }
}
