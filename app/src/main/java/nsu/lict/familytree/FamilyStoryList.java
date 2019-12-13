package nsu.lict.familytree;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import nsu.lict.familytree.controler.DBController;

public class FamilyStoryList extends AppCompatActivity implements SearchView.OnQueryTextListener{

    ListView listView;
    SearchView searchView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_storylist);

        listView = findViewById(R.id.listViewFamilyStories);
        searchView = findViewById(R.id.searchViewFamilyStories);

        DBController dbController = new DBController(this);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                dbController.getAllHeaders(false) );

        listView.setAdapter(arrayAdapter);
        listView.setTextFilterEnabled(true);


        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Search Here");



        searchView.setFocusableInTouchMode(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedItem = (String) adapterView.getItemAtPosition(i);
                String idString = selectedItem.split(" ")[0];
                final int id = Integer.parseInt(idString.replace("#",""));



                // Display the selected item text on TextView
                //Toast.makeText(getApplicationContext(),selectedItem,Toast.LENGTH_SHORT).show();

                Intent newIntent = new Intent(FamilyStoryList.this,StoryViewActivity.class);
                newIntent.putExtra("id",id);
                newIntent.putExtra("source","family");
                startActivity(newIntent);
            }
        });




    }

    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            listView.clearTextFilter();
        } else {
            listView.setFilterText(newText.toString());
        }
        return true;
    }

    public boolean onQueryTextSubmit(String query) {
        return false;
    }
}
