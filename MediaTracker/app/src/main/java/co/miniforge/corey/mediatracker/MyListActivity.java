package co.miniforge.corey.mediatracker;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

import co.miniforge.corey.mediatracker.media_recycler.MediaRecyclerAdapter;
import co.miniforge.corey.mediatracker.media_store.MediaStorageUtil;
import co.miniforge.corey.mediatracker.model.MediaItem;

public class MyListActivity extends AppCompatActivity {
    public static String mediaExtra = "mediaExtra";

    RecyclerView media_list_recycler;

    FloatingActionButton add_media_item_button;

    MediaStorageUtil storageUtil;

    List<MediaItem> mediaItems = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        storageUtil = new MediaStorageUtil(getApplicationContext());
        locateViews();
        bindData();
        handleIntent();
    }


    public void deleteMediaItem(MediaItem item){
        for(int i = 0; i < mediaItems.size(); i++){
            if(mediaItems.get(i).id.equals(item.id)){
                mediaItems.remove(i);
                break;
            }
        }
        storageUtil.saveMediaData(mediaItems);
        updateMediaItems(storageUtil.getMediaDataList());
    }
    void handleIntent(){
        if(getIntent() != null){
            Intent intent = getIntent();

            if(intent.hasExtra(mediaExtra)){
                try {
                    JSONObject json = new JSONObject(intent.getStringExtra(mediaExtra));
                    MediaItem item = new MediaItem(json);

                    for(int i = 0; i < mediaItems.size(); i++){
                        if(mediaItems.get(i).id.equals(item.id)){
                            mediaItems.set(i, item);
                            break;
                        }
                    }

                    storageUtil.saveMediaData(mediaItems);
                } catch (Exception e){
                    Log.e("handleIntentErr", String.format("Could not update item from intent: %s", e.getMessage()));
                }
            }
        }
    }

    void locateViews(){
        media_list_recycler = (RecyclerView) findViewById(R.id.media_list_recycler);
        add_media_item_button = (FloatingActionButton) findViewById(R.id.add_media_item_button);
    }

    void bindData(){
        add_media_item_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu menu = new PopupMenu(view.getContext(),view);
                menu.inflate(R.menu.add_menu);
                menu.setOnMenuItemClickListener(new AddPopUpMenuHelper((MyListActivity)view.getContext()));
                menu.show();
                //Create new empty media item
//                MediaItem item = new MediaItem();
//                mediaItems.add(item);
//                storageUtil.saveMediaData(mediaItems);
//                updateMediaItems(mediaItems);
            }
        });

        setUpRecyclerView();

        updateMediaItems(storageUtil.getMediaDataList());

        MediaItem item = new MediaItem();
        Toast.makeText(this, item.toJson().toString(), Toast.LENGTH_SHORT).show();

        //Update list every 10 sec
        final Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                updateMediaItems(storageUtil.getMediaDataList());

                //Update every 10 sec
                handler.postDelayed(this, 10000);
            }
        };
        handler.post(runnable);
    }

    public void updateMediaItems(List<MediaItem> mediaItems){
        this.mediaItems = mediaItems;
        ((MediaRecyclerAdapter)media_list_recycler.getAdapter()).updateList(this.mediaItems);
    }

    public void addMediaItem(MediaItem mediaItem){
        String mediaItemType = mediaItem.mediaItemType.toString();
        mediaItem.id = Integer.toString(mediaItems.size()+1);
        mediaItem.title = "Default " + mediaItemType + " Title";
        mediaItem.description = "Default " + mediaItemType + " Description";
        mediaItems.add(mediaItem);
        storageUtil.saveMediaData(mediaItems);
        updateMediaItems(mediaItems);
    }

    void setUpRecyclerView(){
        MediaRecyclerAdapter adapter = new MediaRecyclerAdapter();
        media_list_recycler.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
        media_list_recycler.setLayoutManager(manager);
    }

    public void sortMedia(String sortBy){
        MediaItemSortHelper sorter = new MediaItemSortHelper();
        switch(sortBy) {
            case "type": mediaItems = sorter.sortByType(mediaItems);
            case "name" : mediaItems = sorter.sortByName(mediaItems);
            default : break;
        }
        storageUtil.saveMediaData(mediaItems);
        updateMediaItems(mediaItems);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_my_list,menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.sortByName :
                sortMedia("name");
                break;
            case R.id.sortByType :
                sortMedia("type");
                break;
        }
        return true;
        //return super.onOptionsItemSelected(item);
    }
}
