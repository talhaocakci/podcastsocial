import android.util.Log;

import com.javathlon.ApplicationSettings;
import com.javathlon.apiclient.PodcastModernAPI;
import com.javathlon.download.PodcastModernClient;
import com.javathlon.model.User;

import junit.framework.TestSuite;

import org.junit.Before;
import org.junit.Test;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ocakcit on 18/10/2016.
 */

public class PodcastModernUtilTest extends TestSuite {

    private static final String login = "your@login";
    private static final String pass = "pass";
    PodcastModernAPI api;

    @Before
    public void beforeTest() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApplicationSettings.podcastModernServerUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(PodcastModernAPI.class);
    }

    @Test
    public void test()
    {
        User user = new User();
        user.setEmail("dfsdf@fsdfsd.com");

        PodcastModernClient.saveUser(user, ApplicationSettings.appId, new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                Log.d("app", "success");
                System.out.println("success");
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Log.d("app", "failure");
                System.out.println("failure");
                System.out.println("failure");

            }
        });

    }
}