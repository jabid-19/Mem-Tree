package nsu.lict.familytree;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
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
import nsu.lict.familytree.settings.SettingsConstant;

public class OthersStory extends AppCompatActivity {

    DBController dbController;
    private EditText etTitle;
    private EditText etKeyWords;
    private EditText etPlace;
    private EditText etStory;
    private EditText etActor;
    private ImageView ivOtherStory;
    private static int RESULT_LOAD_IMAGE = 1;

    private String picturePath;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_others_story);
        getSupportActionBar().setTitle("Add Others Story");


        etTitle = findViewById(R.id.etTitle);
        etKeyWords = findViewById(R.id.etKeyWords);
        etPlace = findViewById(R.id.etPlace);
        etStory = findViewById(R.id.etStory);
        etActor = findViewById(R.id.etActor);
        ivOtherStory = findViewById(R.id.ivOtherStory);

        ivOtherStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        dbController = new DBController(this);


    }
    public void saveOthersStory(View view){
        dbController.insertStory(etTitle.getText().toString(),picturePath,etPlace.getText().toString(),etKeyWords.getText().toString(),etStory.getText().toString(),etActor.getText().toString());

        finish();
        Toast.makeText(getApplicationContext(), SettingsConstant.SAVE_MSG,Toast.LENGTH_SHORT).show();

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

            ImageView imageView = (ImageView) findViewById(R.id.ivOtherStory);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }
    }
}
