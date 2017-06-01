package com.fotos.fotos.facebookAccess;

import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by t-yoadat on 9/3/2015.
 */
public class FacebookData {
    private AccessToken token = null;

    public FacebookData() {
        token = AccessToken.getCurrentAccessToken();
    }
    public FacebookDataAsyncResponse delegate = null;

    public void GetFriends() {
        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try {

                            List<Friend> friendList = new ArrayList<Friend>();

                            JSONArray friends = response.getJSONObject().getJSONArray("data");
                            for (int i = 0; i < friends.length(); i++) {
                                String name = friends.getJSONObject(i).getString("name");
                                String id = friends.getJSONObject(i).getString("id");

                                //Log.d("FBLogin", name + id);
                                friendList.add(new Friend(name, id));

                            }

                            delegate.GetFriendListResponce(friendList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //JSONObject friends = (JSONObject)response.getJSONObject().get("data");

                    }
                }
        ).executeAsync();
    }

        public void GetUserPhotos(final String id, final String name) {
            Bundle parameters = new Bundle();
            parameters.putString("fields","source,place");

            new GraphRequest(
                    AccessToken.getCurrentAccessToken(),
                    String.format("/%s/photos", id),
                    parameters,
                    HttpMethod.GET,
                    new GraphRequest.Callback() {
                        public void onCompleted(GraphResponse response) {
                            try {

                                List<Photo> photoList = new ArrayList<Photo>();

                                JSONArray friends = response.getJSONObject().getJSONArray("data");
                                for (int i = 0; i < friends.length(); i++) {
                                    String id = friends.getJSONObject(i).getString("id");
                                    String url = friends.getJSONObject(i).getString("source");
                                    //String place = friends.getJSONObject(i).getString("place").toString();

                                    String place = "";

                                    if (friends.getJSONObject(i).has("place")) {
                                        place = friends.getJSONObject(i).getJSONObject("place").getString("name").toString();

                                        //Log.d("FBLogin", name + id);
                                        photoList.add(new Photo(id, name, url, place));
                                    }
                                }

                                delegate.GetUserPhotosResponse(id, name, photoList);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            ).executeAsync();
    }
}
