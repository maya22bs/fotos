package com.fotos.fotos;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;


import java.util.Arrays;

public class SplashLoginActivity extends Activity {

    private CallbackManager callbackManager;

    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    private static final String TAG = "FBLogin";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        // Init FB SDK, must do this before anything else
        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();

        final AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken newAccessToken) {
                updateWithToken(newAccessToken);
            }
        };

        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.splashlogin);

        LoginButton authButton = (LoginButton) findViewById(R.id.authButton);
        authButton.setReadPermissions(Arrays.asList("user_photos", "user_friends"));

        authButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                // Save the access token
                AccessToken.setCurrentAccessToken(loginResult.getAccessToken());
                Log.d(TAG, "Button Success");
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "Button Cancel");
            }

            @Override
            public void onError(FacebookException e) {
                Log.e(TAG, "Button Error");
            }
        });


        updateWithToken(AccessToken.getCurrentAccessToken());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void updateWithToken(AccessToken currentAccessToken) {

        if (currentAccessToken != null) {
            Log.d(TAG, "signed in");

            new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
               /* Create an Intent that will start the Menu-Activity. */
               Intent mainIntent = new Intent(SplashLoginActivity.this,MainActivity.class);
               SplashLoginActivity.this.startActivity(mainIntent);
               SplashLoginActivity.this.finish();
           }
            }, 0);
        }
        else {
            Log.d(TAG, "NOT signed in");
        }
    }
}