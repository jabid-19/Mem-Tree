package nsu.lict.familytree;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import nsu.lict.familytree.R;
import nsu.lict.familytree.controler.DBController;
import nsu.lict.familytree.settings.SettingsConstant;

import static com.amitshekhar.utils.Constants.NULL;

public class LandingActivity extends AppCompatActivity {

    private TextView textView;
    private TextView tvClan;
    private ImageView profileImage;
    private static int RESULT_LOAD_IMAGE = 1;
    private String email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        SettingsConstant.APP_CONTEXT = getApplicationContext();
        textView = findViewById(R.id.textViewHello);
        tvClan = findViewById(R.id.tvClan);
        profileImage = findViewById(R.id.imageViewUser);

        SharedPreferences prefs = getSharedPreferences(SettingsConstant.SHARED_PREF_NAME, MODE_PRIVATE);
        email = prefs.getString("email", null);

        DBController dbController = new DBController(getApplicationContext());
        textView.setText("Welcome : "+dbController.getName(email));
        tvClan.setText("Clan# "+dbController.getClan(email));

        if(!dbController.getPPPath(email).isEmpty()){
            profileImage.setImageBitmap(BitmapFactory.decodeFile(dbController.getPPPath(email)));

        }




        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

    }
    public void myStories(View view){
        Intent intent = new Intent(LandingActivity.this, MyStoriesList.class);
        startActivity(intent);

    }

    public void othersStories(View view){
        Intent intent = new Intent(LandingActivity.this, FamilyStoryList.class);
        startActivity(intent);
    }

    public void addEvent(View view) {
        Intent intent = new Intent(LandingActivity.this, AddEvent.class);
        startActivity(intent);
    }

    public void signout(View view){
        finish();
        //Intent intent = new Intent(LandingActivity.this,LogIn.class);
        //startActivity(intent);
        SharedPreferences settings = getApplicationContext().getSharedPreferences(SettingsConstant.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        settings.edit().clear().commit();
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);

            DBController dbController = new DBController(getApplicationContext());
            dbController.updatePP(picturePath,email);

            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.imageViewUser);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }


    }
}
