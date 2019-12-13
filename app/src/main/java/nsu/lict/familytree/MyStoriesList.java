package nsu.lict.familytree;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import nsu.lict.familytree.controler.DBController;
import nsu.lict.familytree.settings.SettingsConstant;

public class MyStoriesList extends AppCompatActivity {
    private ListView listView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_story_list);
        getSupportActionBar().setTitle("Own Story");

        Intent intent = getIntent();
        listView = findViewById(R.id.listViewMyStories);

        final DBController dbController = new DBController(this);


        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                dbController.getAllHeaders(true) );

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = (String) adapterView.getItemAtPosition(i);
                String idString = selectedItem.split(" ")[0];
                final int id = Integer.parseInt(idString.replace("#",""));



                // Display the selected item text on TextView
                //Toast.makeText(getApplicationContext(),selectedItem,Toast.LENGTH_SHORT).show();

                Intent newIntent = new Intent(MyStoriesList.this,StoryViewActivity.class);
                newIntent.putExtra("id",id);
                newIntent.putExtra("source","self");
                startActivity(newIntent);
            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = (String) adapterView.getItemAtPosition(i);
                String idString = selectedItem.split(" ")[0];
                final int id = Integer.parseInt(idString.replace("#",""));
                Log.d("DELETE ID = "," "+id);
                new AlertDialog.Builder(MyStoriesList.this)
                        .setTitle("Warning")
                        .setMessage("Do you really want to delete this entry?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {

                                dbController.deleteEntry(id);

                                Toast.makeText(getApplicationContext(), SettingsConstant.DELETE_MSG, Toast.LENGTH_SHORT).show();

                                finish();
                                startActivity(getIntent());
                            }})
                        .setNegativeButton(android.R.string.no, null).show();
                return true;
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
