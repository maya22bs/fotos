package com.fotos.fotos.facebookAccess;

/**
 * Created by t-yoadat on 9/3/2015.
 */
public class Friend {
    private String name;
    private String id;

    public Friend(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public String getId() {
        return this.id;
    }
}
