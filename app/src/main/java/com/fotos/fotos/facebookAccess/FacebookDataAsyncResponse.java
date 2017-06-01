package com.fotos.fotos.facebookAccess;

import java.util.List;

/**
 * Created by t-yoadat on 9/3/2015.
 */
public interface FacebookDataAsyncResponse {
    void GetFriendListResponce(List<Friend> friendList);

    void GetUserPhotosResponse(String id, String name, List<Photo> friendList);
}
