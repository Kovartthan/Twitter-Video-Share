package com.twittervplay;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.ko.twitter.vplay.core.OnCallBackVideoShareListener;
import com.ko.twitter.vplay.core.StatusUpdate;
import com.ko.twitter.vplay.core.TwitterFactory;
import com.ko.twitter.vplay.core.UploadedMedia;
import com.ko.twitter.vplay.core.auth.AccessToken;
import com.ko.twitter.vplay.core.auth.RequestToken;
import com.ko.twitter.vplay.core.conf.ConfigurationBuilder;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.DefaultLogger;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterAuthClient;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements OnCallBackVideoShareListener {
    public String TWITTER_KEY;
    public String TWITTER_SECRET;
    public String TWITTER_OAUTH_TOKEN;
    public String TWITTER_OAUTH_SECRET_TOKEN;
    protected TwitterLoginButton loginButton;
    com.ko.twitter.vplay.core.Twitter twitter;
    private ConfigurationBuilder configurationBuilder;
    private com.ko.twitter.vplay.core.auth.AccessToken accessTokenForTwitter;

    @Override
    public void attachBaseContext(Context base) {
        MultiDex.install(base);
        super.attachBaseContext(base);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /****Strict mode enabled for Twitter4j Video sharing , don't disable it ***/
        if (android.os.Build.VERSION.SDK_INT > 16) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        Twitter.initialize(this);
        setContentView(R.layout.activity_main);
        setupTwitter();
        doTwitterLogin();
    }

    private void setupTwitter() {
//        twitterAuthClient = new TwitterAuthClient();
//        Twitter.initialize(twitterConfig);
    }

    private void doTwitterLogin() {
        loginButton = (TwitterLoginButton) findViewById(R.id.login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                doTwitterVideoShareSilently();
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(MainActivity.this, "Login failure due to " + exception.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void doTwitterVideoShareSilently() {
        final TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
        accessTokenForTwitter = new com.ko.twitter.vplay.core.auth.AccessToken(session.getAuthToken().token, session.getAuthToken().secret);
        configurationBuilder = new ConfigurationBuilder();
        configurationBuilder.setDebugEnabled(true);
        configurationBuilder.setUseSSL(true);
        configurationBuilder.setOAuthConsumerKey(TWITTER_KEY);
        configurationBuilder.setOAuthConsumerSecret(TWITTER_SECRET);
        TwitterFactory twitterFactory = new TwitterFactory(configurationBuilder.build());
        twitter = twitterFactory.getInstance();
        twitter.setOAuthAccessToken(accessTokenForTwitter);
        new TweetVideoUploadBackgroundService().execute();
    }


    private class TweetVideoUploadBackgroundService extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            doTweetVideoUpload();
            return null;
        }
    }

    private void doTweetVideoUpload() {
        UploadedMedia.setOnCallBackVideoShareListener(MainActivity.this);
        new TaskDownloadContent().execute("http://techslides.com/demos/sample-videos/small.mp4 ");

    }

    private class TaskDownloadContent extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... voids) {

            try {
                String outputSteamLocation = null;
                File targetFile = null;
                outputSteamLocation = createVideoFile(MainActivity.this, "video").getAbsolutePath();

                URL contentUrl = new URL(voids[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) contentUrl.openConnection();
                urlConnection.connect();

                targetFile = new File(outputSteamLocation);
                FileOutputStream fileOutput = new FileOutputStream(targetFile);
                InputStream inputStream = urlConnection.getInputStream();
                byte[] buffer = new byte[50000];
                int bufferLength = 0;
                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fileOutput.write(buffer, 0, bufferLength);
                }
                fileOutput.close();

                Log.d("tag", "====TaskDownloadContent==targetFile======" + targetFile);
                urlConnection.disconnect();
                return outputSteamLocation;
            } catch (IOException e) {
                Log.d("tag", "====TaskDownloadContent==targetFile======" + e.toString());
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String path) {
            super.onPostExecute(path);
            File videoFolder = new File(MainActivity.this.getExternalCacheDir(), "create_video/video");
            String videoPath = getLatestFileFromDir(videoFolder.getAbsolutePath());
            File file = new File(videoPath);
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            try {
                twitter.uploadMediaChunked("video", fileInputStream);
            } catch (com.ko.twitter.vplay.core.TwitterException e) {
                e.printStackTrace();
            }
        }
    }
    public static String getLatestFileFromDir(String dirPath) {
        File dir = new File(dirPath);
        File[] files = dir.listFiles();
        if (files == null || files.length == 0) {
            return null;
        }

        File lastModifiedFile = files[0];
        for (int i = 1; i < files.length; i++) {
            if (lastModifiedFile.lastModified() < files[i].lastModified()) {
                lastModifiedFile = files[i];
            }
        }
        return lastModifiedFile.getAbsolutePath();
    }

    public static File createVideoFile(Context context, String type) {
        File mapdir = new File(context.getExternalCacheDir(), "create_video");
        mapdir.mkdir();
        File f = null;
        File dir = new File(mapdir, "video");
        dir.mkdir();
        f = new File(dir + File.separator, "video.mp4");
        return f;
    }


    @Override
    public void onSuccess(long mediaId, String media) {
        StatusUpdate statusUpdate = new StatusUpdate("Your uploaded video here ");
        statusUpdate.setMediaIds(mediaId);
        try {
            twitter.updateStatus(statusUpdate);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MainActivity.this, "Check your profile you will find your video", Toast.LENGTH_LONG).show();
                }
            },5000);
        } catch (com.ko.twitter.vplay.core.TwitterException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Status update failure " + e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }


}
