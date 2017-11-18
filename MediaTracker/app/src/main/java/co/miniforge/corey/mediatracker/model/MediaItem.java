package co.miniforge.corey.mediatracker.model;

import android.util.Log;
import android.webkit.MimeTypeMap;

import org.json.JSONObject;

import java.security.MessageDigest;

import co.miniforge.corey.mediatracker.media_store.Md5IdHelper;

/**
 * Created by corey on 10/20/17.
 */

public class MediaItem {

    public String id;
    public String title;
    public String description;
    public String url;
    public MediaItemType mediaItemType;
    //We need a field that stores what kind of media item this is
    //We will do this by creating an enum that can identify the type of item
    public MediaItemType type = MediaItemType.Generic;

    public MediaItem(JSONObject jsonObject) {
        try {
            //Generate id based on the object instance (should work :D)
            this.id = jsonObject.getString("id");
            this.title = jsonObject.getString("title");
            this.description = jsonObject.getString("description");
            this.url = jsonObject.getString("url");

            this.type = getTypeForObject((MediaItemType) jsonObject.get("type"));
        } catch (Exception e) {
            Log.e("toJSONError", String.format("There was an error: %s", e.getMessage()));
        }
    }
    public MediaItem(MediaItemType mediaItemType){

        this.mediaItemType = mediaItemType;
    }
    public MediaItem(JSONObject jsonObject, MediaItemType mediaItemType) {
        try {
            //Generate id based on the object instance (should work :D)
            this.id = jsonObject.getString("id");
            this.title = jsonObject.getString("title");
            this.description = jsonObject.getString("description");
            this.url = jsonObject.getString("url");
            this.mediaItemType = mediaItemType;

            this.type = getTypeForObject((MediaItemType) jsonObject.get("type"));
        } catch (Exception e) {
            Log.e("toJSONError", String.format("There was an error: %s", e.getMessage()));
        }
    }

    public MediaItem() {
        this.id = Md5IdHelper.idForObject(this);
        this.title = "defaultTitle";
        this.description = "defaultDescription";
        this.url = "defaultUrl";
    }

    public MediaItemType getTypeForObject(MediaItemType value) {
        switch (value) {
            case Generic:
                break;
        }

        return MediaItemType.Generic;
    }

    public String getStringForType(MediaItemType type) {
        switch (type) {
            case Movie:
                return "Movie";
            case TV:
                return "TV";
            case YouTube:
                return "YouTube";
            default:
                return "Generic";
        }
    }

    public MediaItemType getTypeForString(String value) {
        switch (value) {
            case "TV":
                return MediaItemType.TV;
            case "Movie":
                return MediaItemType.Movie;
            case "YouTube":
                return MediaItemType.YouTube;
            default:
                return MediaItemType.Generic;
        }
    }

    public JSONObject toJson() {
        JSONObject mediaItem = new JSONObject();

        try {
            mediaItem.put("id", this.id);
            mediaItem.put("title", this.title);
            mediaItem.put("description", this.description);
            mediaItem.put("url", this.url);

            mediaItem.put("type", this.type);
        } catch (Exception e) {
            Log.e("toJSONError", String.format("There was an error: %s", e.getMessage()));
        }

        return mediaItem;
    }
}
