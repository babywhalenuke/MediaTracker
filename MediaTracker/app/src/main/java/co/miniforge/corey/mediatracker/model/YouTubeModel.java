package co.miniforge.corey.mediatracker.model;

import org.json.JSONObject;

import co.miniforge.corey.mediatracker.model.MediaItem;

/**
 * Created by jeff on 11/18/2017.
 */

public class YouTubeModel extends MediaItem {
    public long viewCount;
    public int upvotes;
    public int downvotes;

    public YouTubeModel(JSONObject jsonObject, long viewCount, int upvotes, int downvotes){
        super(jsonObject);
        this.viewCount = viewCount;
        this.upvotes = upvotes;
        this.downvotes = downvotes;
    }

}
