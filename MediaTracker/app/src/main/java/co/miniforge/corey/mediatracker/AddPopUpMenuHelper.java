package co.miniforge.corey.mediatracker;

import android.view.MenuItem;
import android.widget.PopupMenu;

import co.miniforge.corey.mediatracker.model.MediaItem;
import co.miniforge.corey.mediatracker.model.MediaItemType;

/**
 * Created by jeff on 11/18/2017.
 */

public class AddPopUpMenuHelper  implements PopupMenu.OnMenuItemClickListener {

    MyListActivity activity;

    public AddPopUpMenuHelper(MyListActivity activity){
        this.activity = activity;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.movie:
                activity.addMediaItem(new MediaItem(MediaItemType.Movie));
                break;
            case R.id.tv:
                activity.addMediaItem(new MediaItem(MediaItemType.TV));
                break;
            case R.id.youTube:
                activity.addMediaItem(new MediaItem(MediaItemType.YouTube));
                break;
            default:
                activity.addMediaItem(new MediaItem(MediaItemType.Generic));
                break;

        }
        return true;
    }
}


