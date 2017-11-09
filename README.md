# Twitter VPlay 
## Twitter Video Share Without using the composer .

This is the extension of the Twitter4j library . This library used to share the video along with the text silently that means without using the composer. 

**IMPORTANT NOTE : Please download the above project and run it , if u have any doubt in souce refer MainActivity.class in the app folder and refer gradle too avoid unnecessary error**

## SETUP 

**NOTE : Before you add this dependency , please add Twitter sdk to your project ...**

###### First : Add the dependency to the gradle
```
compile 'com.ko.twitter.vplay.core:twittervideoshare:1.0.1'
```
or maven 

```
<dependency>
  <groupId>com.ko.twitter.vplay.core</groupId>
  <artifactId>twittervideoshare</artifactId>
  <version>1.0.1</version>
  <type>pom</type>
</dependency>

```

That 's all u have added the library successfully to your project .

###### Second : How to do twitter video share silently 

**Please add strict mode in the onCreate() to avoid network exception while doing twitter video share** 

```
if (android.os.Build.VERSION.SDK_INT > 16) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
}

```

**Install Mutli dex support to your project**

```
 @Override
    public void attachBaseContext(Context base) {
        MultiDex.install(base);
        super.attachBaseContext(base);
}

```
**After login from the twitter sdk , get the active session from it** 

```
final TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
```

**Setup confiuration builder for the twiiter video share** 

```
 private com.ko.twitter.vplay.core.Twitter twitter;
 private ConfigurationBuilder configurationBuilder;
 private com.ko.twitter.vplay.core.auth.AccessToken accessTokenForTwitter;
 
 // Set the token and secret from the login user using twitter sdk  to the instance for the library 
 
 accessTokenForTwitter = new com.ko.twitter.vplay.core.auth.AccessToken(session.getAuthToken().token, session.getAuthToken().secret);
 
 configurationBuilder = new ConfigurationBuilder();
 configurationBuilder.setDebugEnabled(true);
 configurationBuilder.setUseSSL(true);
 configurationBuilder.setOAuthConsumerKey(TWITTER_KEY);
 configurationBuilder.setOAuthConsumerSecret(TWITTER_SECRET);
 
 TwitterFactory twitterFactory = new TwitterFactory(configurationBuilder.build());

 // Setting that auth token to instance for the twitter4j variable from the library  
 
 twitter = twitterFactory.getInstance();
 twitter.setOAuthAccessToken(accessTokenForTwitter);
 
```

**Finally send the file to uploadMediaChunked**

```
 twitter.uploadMediaChunked("video", fileInputStream);
```

**NOTE : Do the above syntax in async task , to avoid lag in your UI Thread**












