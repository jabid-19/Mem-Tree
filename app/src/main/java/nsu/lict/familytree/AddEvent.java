package nsu.lict.familytree;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by arfan on 3/4/18.
 */

public class AddEvent extends AppCompatActivity {
    TextView addEvent;
    ImageView iVMyStory;
    ImageView iVOtherStory;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addevent);
        Intent intent = getIntent();
    }

    public void makeMyStory(View view){
        Intent intent = new Intent(AddEvent.this, MyStory.class);
        startActivity(intent);
    }
    public void makeOthersStory(View view){
        Intent intent = new Intent(AddEvent.this, OthersStory.class);
        startActivity(intent);

    }
}
