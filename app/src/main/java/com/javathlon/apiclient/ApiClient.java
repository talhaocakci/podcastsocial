package com.javathlon.apiclient;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.javathlon.apiclient.auth.ApiKeyAuth;
import com.javathlon.apiclient.auth.HttpBasicAuth;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import okhttp3.CookieJar;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class ApiClient {

    public boolean proxyOpen = false;
    public String proxyAddress = "proxy.pozitron.com";
    public String proxyHost = "3128";
    public Long appId = 1L;
    public  String podcastModernServerUrl = "http://107.170.25.76:8080";
    //public  String podcastModernServerUrl = "http://192.168.1.101:8080";
    //public String podcastModernServerUrl = "http://10.0.3.2:8080";


    private Map<String, Interceptor> apiAuthorizations;
    private OkHttpClient.Builder okBuilder;
    private Retrofit.Builder adapterBuilder;
    private Context context;

    private static ApiClient client = null;

    private ApiClient() {
        apiAuthorizations = new LinkedHashMap<String, Interceptor>();
    }

    private ApiClient(String[] authNames) {
        this();

    }

    private ApiClient(Context context) {

        this();
        this.context = context;
        createDefaultAdapter();
        addAuthorization("password", new HttpBasicAuth());
        setCredentials("admin", "admin");

    }

    public static ApiClient getApiClient(Context context) {
        if (client == null) {
            client = new ApiClient(context);
        }
        return client;
    }

    /**
     * Basic constructor for single auth name
     *
     * @param authName Authentication name
     */
    public ApiClient(String authName) {
        this(new String[]{authName});
    }

    /**
     * Helper constructor for single api key
     *
     * @param authName Authentication name
     * @param apiKey   API key
     */
    public ApiClient(String authName, String apiKey) {
        this(authName);
        this.setApiKey(apiKey);
    }

    /**
     * Helper constructor for single basic auth or password oauth2
     *
     * @param authName Authentication name
     * @param username Username
     * @param password Password
     */
    public ApiClient(String authName, String username, String password) {
        this(authName);
        this.setCredentials(username, password);


    }

    public class GsonDateDeSerializer implements JsonDeserializer<Date> {

        private SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ");
        private SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm.SSS:ssZZ");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                String j = json.getAsJsonPrimitive().getAsString();
                return parseDate(j);
            } catch (ParseException e) {
                throw new JsonParseException(e.getMessage(), e);
            }
        }

        private Date parseDate(String dateString) throws ParseException {
            if (dateString != null && dateString.trim().length() > 0) {
                try {
                    return format1.parse(dateString);
                } catch (ParseException pe) {
                    try {
                        return format2.parse(dateString);
                    } catch (ParseException pe2) {
                        try {
                            return sdf.parse(dateString);
                        } catch (ParseException p) {
                            return sdf2.parse(dateString);
                        }
                    }
                }
            } else {
                return null;
            }
        }

    }

    public void createDefaultAdapter() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Date.class, new GsonDateDeSerializer())
                .registerTypeAdapter(DateTime.class, new DateTimeTypeAdapter())
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .create();

        okBuilder = new OkHttpClient.Builder();

        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
        okBuilder.cookieJar(cookieJar);

        if (proxyOpen) {
            try {
                putProxy();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        String baseUrl = podcastModernServerUrl;
        if (!baseUrl.endsWith("/"))
            baseUrl = baseUrl + "/";

        adapterBuilder = new Retrofit
                .Builder()
                .baseUrl(baseUrl)


                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonCustomConverterFactory.create(gson));
    }

    public <S> S createService(Class<S> serviceClass) {
        return adapterBuilder
                .client(okBuilder.build())
                .build()
                .create(serviceClass);

    }

    public void putProxy() throws ExecutionException, InterruptedException {
        AsyncTask task = new AsyncTask<Void, Void, OkHttpClient.Builder>() {
            @Override
            protected OkHttpClient.Builder doInBackground(Void... params) {
                okBuilder.proxy(new Proxy(Proxy.Type.HTTP,
                        new InetSocketAddress(proxyAddress,
                                Integer.valueOf(proxyHost))));
                return okBuilder;
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, (Void[]) null).get();
        else
            task.execute((Void[]) null).get();

    }

    /**
     * Helper method to configure the first api key found
     *
     * @param apiKey API key
     */
    private void setApiKey(String apiKey) {
        for (Interceptor apiAuthorization : apiAuthorizations.values()) {
            if (apiAuthorization instanceof ApiKeyAuth) {
                ApiKeyAuth keyAuth = (ApiKeyAuth) apiAuthorization;
                keyAuth.setApiKey(apiKey);
                return;
            }
        }
    }

    /**
     * Helper method to configure the username/password for basic auth or password oauth
     *
     * @param username Username
     * @param password Password
     */
    private void setCredentials(String username, String password) {
        for (Interceptor apiAuthorization : apiAuthorizations.values()) {
            if (apiAuthorization instanceof HttpBasicAuth) {
                HttpBasicAuth basicAuth = (HttpBasicAuth) apiAuthorization;
                basicAuth.setCredentials(username, password);
                return;
            }
        }
    }


    /**
     * Adds an authorization to be used by the client
     *
     * @param authName      Authentication name
     * @param authorization Authorization interceptor
     */
    public void addAuthorization(String authName, Interceptor authorization) {
        if (apiAuthorizations.containsKey(authName)) {
            throw new RuntimeException("auth name \"" + authName + "\" already in api authorizations");
        }
        apiAuthorizations.put(authName, authorization);
        okBuilder.addInterceptor(authorization);
    }

    public Map<String, Interceptor> getApiAuthorizations() {
        return apiAuthorizations;
    }

    public void setApiAuthorizations(Map<String, Interceptor> apiAuthorizations) {
        this.apiAuthorizations = apiAuthorizations;
    }

    public Retrofit.Builder getAdapterBuilder() {
        return adapterBuilder;
    }

    public void setAdapterBuilder(Retrofit.Builder adapterBuilder) {
        this.adapterBuilder = adapterBuilder;
    }

    public OkHttpClient.Builder getOkBuilder() {
        return okBuilder;
    }

    public void addAuthsToOkBuilder(OkHttpClient.Builder okBuilder) {
        for (Interceptor apiAuthorization : apiAuthorizations.values()) {
            okBuilder.addInterceptor(apiAuthorization);

            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okBuilder.addInterceptor(interceptor);
        }
    }

    /**
     * Clones the okBuilder given in parameter, adds the auth interceptors and uses it to configure the Retrofit
     *
     * @param okClient An instance of OK HTTP client
     */
    public void configureFromOkclient(OkHttpClient okClient) {
        this.okBuilder = okClient.newBuilder();
        if (proxyOpen) {
            this.okBuilder.proxy(new Proxy(Proxy.Type.HTTP,
                    new InetSocketAddress(proxyAddress,
                            Integer.valueOf(proxyHost))));
        }
        addAuthsToOkBuilder(this.okBuilder);

    }
}

/**
 * This wrapper is to take care of this case:
 * when the deserialization fails due to JsonParseException and the
 * expected type is String, then just return the body string.
 */
class GsonResponseBodyConverterToString<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final Type type;

    GsonResponseBodyConverterToString(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String returned = value.string();
        try {
            return gson.fromJson(returned, type);
        } catch (JsonParseException e) {
            return (T) returned;
        }
    }
}

class GsonCustomConverterFactory extends Converter.Factory {
    public static GsonCustomConverterFactory create(Gson gson) {
        return new GsonCustomConverterFactory(gson);
    }

    private final Gson gson;
    private final GsonConverterFactory gsonConverterFactory;

    private GsonCustomConverterFactory(Gson gson) {
        if (gson == null) throw new NullPointerException("gson == null");
        this.gson = gson;
        this.gsonConverterFactory = GsonConverterFactory.create(gson);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type.equals(String.class))
            return new GsonResponseBodyConverterToString<Object>(gson, type);
        else
            return gsonConverterFactory.responseBodyConverter(type, annotations, retrofit);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return gsonConverterFactory.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit);
    }
}

/**
 * Gson TypeAdapter for Joda DateTime type
 */
class DateTimeTypeAdapter extends TypeAdapter<DateTime> {

    private final SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZ");
    private final SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm.SSS:ssZZ");
    final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ");
    final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ssZ");

    @Override
    public void write(JsonWriter out, DateTime date) throws IOException {
        if (date == null) {
            out.nullValue();
        } else {
            out.value(formatter.print(date));
        }
    }

    @Override
    public DateTime read(JsonReader reader) throws IOException {

        String dateString = null;

        switch (reader.peek()) {
            case NULL:
                reader.nextNull();
                return null;
            default:
                dateString = reader.nextString();

                if (dateString != null && dateString.trim().length() > 0) {
                    try {
                        return new DateTime(format1.parse(dateString));
                    } catch (ParseException pe) {
                        try {
                            return new DateTime(format2.parse(dateString));
                        } catch (ParseException pe2) {
                            try {
                                return new DateTime(sdf.parse(dateString));
                            } catch (ParseException p) {
                                try {
                                    return new DateTime(sdf2.parse(dateString));
                                } catch (ParseException e) {

                                }
                            }
                        }
                    }
                } else {
                    return null;
                }
                return null;
        }
    }

}

/**
 * Gson TypeAdapter for Joda LocalDate type
 */
class LocalDateTypeAdapter extends TypeAdapter<LocalDate> {

    private final DateTimeFormatter formatter = ISODateTimeFormat.date();

    @Override
    public void write(JsonWriter out, LocalDate date) throws IOException {
        if (date == null) {
            out.nullValue();
        } else {
            out.value(formatter.print(date));
        }
    }

    @Override
    public LocalDate read(JsonReader in) throws IOException {
        switch (in.peek()) {
            case NULL:
                in.nextNull();
                return null;
            default:
                String date = in.nextString();
                return formatter.parseLocalDate(date);
        }
    }
}
