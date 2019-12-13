package nsu.lict.familytree;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import nsu.lict.familytree.controler.DBController;
import nsu.lict.familytree.models.StoryModel;
import nsu.lict.familytree.settings.SettingsConstant;

/**
 * Created by arfan on 3/5/18.
 */

public class MyStory extends AppCompatActivity {

    DBController dbController;
    private EditText etTitle;
    private EditText etKeyWords;
    private EditText etPlace;
    private EditText etStory;
    private Button btnSave;
    private ImageView ivMyStory;


    private String picturePath="";
    private static int RESULT_LOAD_IMAGE = 1;
    private String source="";
    private StoryModel storyModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_story);
        getSupportActionBar().setTitle("Add Own Story");

        Intent intent = getIntent();




        etTitle = findViewById(R.id.editTextTitle);
        etKeyWords = findViewById(R.id.editTextTag);
        etPlace = findViewById(R.id.editTextPlace);
        etStory = findViewById(R.id.editTextStory);
        ivMyStory = findViewById(R.id.imageButtonMyStory);
        btnSave = findViewById(R.id.btnSave);

        ivMyStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        if (getIntent().hasExtra("source")) {
            source  = getIntent().getStringExtra("source");
        }

        if(source.equals("view")){
            storyModel = (StoryModel) getIntent().getSerializableExtra("storyModel");
            initialize();
        }

        dbController = new DBController(this);

    }

    private void initialize() {
        etTitle.setText(storyModel.getTitle());
        etStory.setText(storyModel.getStory());
        etPlace.setText(storyModel.getPlace());
        etKeyWords.setText(storyModel.getKeywords());
        if(!storyModel.getImage_path().isEmpty()){
            ivMyStory.setImageBitmap(BitmapFactory.decodeFile(storyModel.getImage_path()));

        }
        btnSave.setVisibility(View.VISIBLE);

        btnSave.setText("Update");

    }

    public void saveMyStory(View view){

        SharedPreferences prefs = SettingsConstant.APP_CONTEXT.getSharedPreferences(SettingsConstant.SHARED_PREF_NAME, MODE_PRIVATE);
        String email = prefs.getString("email", null);

        DBController dbController = new DBController(getApplicationContext());
        String currentUserName = dbController.getName(email);


        if(source.equals("view")){

            StoryModel updateModel = new StoryModel();
            updateModel.setId(storyModel.getId());
            updateModel.setTitle(etTitle.getText().toString());
            updateModel.setStory(etStory.getText().toString());
            updateModel.setPlace(etPlace.getText().toString());
            updateModel.setKeywords(etKeyWords.getText().toString());
            updateModel.setImage_path(picturePath);

            dbController.UpdateMyStory(updateModel);
            finish();
            Toast.makeText(this, SettingsConstant.SAVE_MSG,Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MyStory.this,MyStoriesList.class);
            startActivity(intent);

        }
        else{
            dbController.insertStory(etTitle.getText().toString(),picturePath,etPlace.getText().toString(),etKeyWords.getText().toString(),etStory.getText().toString(),currentUserName);

            Toast.makeText(this, SettingsConstant.SAVE_MSG,Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MyStory.this,MyStoriesList.class);
            startActivity(intent);
        }



    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            picturePath = cursor.getString(columnIndex);


            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.imageButtonMyStory);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
