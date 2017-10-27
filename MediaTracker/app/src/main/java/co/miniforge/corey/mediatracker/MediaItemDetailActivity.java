package co.miniforge.corey.mediatracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import co.miniforge.corey.mediatracker.model.MediaItem;

/**
 * This activity will display the contents of a media item and allow the user to update the contents
 * of the item. When the user clicks the save button, the activity should create an intent that goes
 * back to MyListActivity and puts the MediaItem into the intent (If you are stuck on that, read through
 * the code in MyListActivity)
 */
public class MediaItemDetailActivity extends AppCompatActivity {
    //Locate Views
    EditText title;
    EditText description;
    EditText Url;
    Button buttonSave;
    MediaItem item ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_item_detail);
        item = getIntentExtras();
        locateViews();
        bindFunctionality();
        bindData(item);
    }

    private MediaItem getIntentExtras(){
        Intent intent = getIntent();
        if(intent.hasExtra("mediaExtra")){
            String mediaString = intent.getStringExtra("mediaExtra");
            try {
                JSONObject json = new JSONObject(mediaString);
                MediaItem mediaItem = new MediaItem(json);
                return mediaItem;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
        else{
            return null;
        }

    }

    private void bindFunctionality(){
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO Add onclick listener functionality for button after save.
                MediaItem updatedMediaItem = updateItem(item);
                Intent intent = new Intent(getApplicationContext(),MyListActivity.class);
                String json = updatedMediaItem.toJson().toString();
                intent.putExtra("mediaExtra",json);
                startActivity(intent);
            }
        });
    }

    private void locateViews(){
        title = (EditText) findViewById(R.id.editTextTitle);
        description = (EditText) findViewById(R.id.editTextDesc);
        Url = (EditText) findViewById(R.id.editTextURL);
        buttonSave = (Button) findViewById(R.id.buttonSave);
    }


    private void bindData(MediaItem item){
        title.setText(item.title);
        description.setText(item.description);
        Url.setText(item.url);
    }

    private MediaItem updateItem(MediaItem item){
        String newTitle = title.getText().toString();
        String newDescription = description.getText().toString();
        String newUrl = Url.getText().toString();

        item.title = newTitle;
        item.description = newDescription;
        item.url = newUrl;

        return item;
    }





}
