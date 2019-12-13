package nsu.lict.familytree;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.Serializable;

import nsu.lict.familytree.R;
import nsu.lict.familytree.controler.DBController;
import nsu.lict.familytree.models.StoryModel;

public class StoryViewActivity extends AppCompatActivity {

    TextView tvTitle;
    TextView tvPlace;
    TextView tvKeywords;
    TextView tvActor;
    TextView tvStory;
    ImageView ivStory;
    Button btnEdit;

    int id=0;
    String source="";
    StoryModel storyModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_story_view);



        String actionBarMsg="Own Story";
        if (getIntent().hasExtra("id")) {
            id  = getIntent().getIntExtra("id",0);
            source = getIntent().getStringExtra("source");
        }


        tvActor = findViewById(R.id.textViewMyStory);
        tvTitle = findViewById(R.id.tvTitle);
        tvPlace = findViewById(R.id.tvPlace);
        tvKeywords = findViewById(R.id.tvTag);
        tvStory = findViewById(R.id.tvStory);
        ivStory = findViewById(R.id.imageViewStory);
        btnEdit = findViewById(R.id.btnEdit);


        DBController dbController = new DBController(getApplicationContext());
        storyModel = dbController.getStory(id);


        if(!source.equals("self")){
            btnEdit.setVisibility(View.INVISIBLE);
            tvActor.setText("Actor: "+storyModel.getActor());
            actionBarMsg="Other Story";
        }

        getSupportActionBar().setTitle(actionBarMsg);

        tvTitle.setText("Title: "+storyModel.getTitle());
        tvPlace.setText("Place: "+storyModel.getPlace());
        tvKeywords.setText("Keywords: "+storyModel.getKeywords());
        tvStory.setText("Story: "+storyModel.getStory());
        ivStory.setImageBitmap(BitmapFactory.decodeFile(storyModel.getImage_path()));

    }

    public void editStory(View view){
        if(source.equals("self")){
            Intent intent = new Intent(StoryViewActivity.this,MyStory.class);
            intent.putExtra("storyModel", (Serializable) storyModel);
            intent.putExtra("source","view");
            startActivity(intent);

        }


    }

}
