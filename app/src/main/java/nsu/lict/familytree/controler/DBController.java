package nsu.lict.familytree.controler;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import nsu.lict.familytree.models.StoryModel;
import nsu.lict.familytree.settings.SettingsConstant;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by amdadulbariimad on 14/4/18.
 */

public class DBController extends SQLiteOpenHelper {


    public DBController(Context context) {
        super(context, SettingsConstant.DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //if(!checkDB()){
            sqLiteDatabase.execSQL(
                    "create table Story " +
                            "(id integer primary key, title text,image_path text,place text, keywords text,story text,actor text,clan text)"
            );
        sqLiteDatabase.execSQL(
                "create table User " +
                        "(id integer primary key, name text,email text,password text,clan text,image_path text)"
        );


            Log.d("Database Creation","Success");
        //}
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //TODO jokhon next version kora hobe lol
    }

    public boolean checkDB(){
        SQLiteDatabase checkDB;
        try {
            checkDB = SQLiteDatabase.openDatabase(SettingsConstant.DB_NAME, null,
                    SQLiteDatabase.OPEN_READONLY);
            checkDB.close();
            return true;
        } catch (SQLiteException e) {
            return false;
        }
    }


    public boolean insertStory(String title,String image_path,String place,String keywords,String story,String actor){

        SharedPreferences prefs = SettingsConstant.APP_CONTEXT.getSharedPreferences(SettingsConstant.SHARED_PREF_NAME, MODE_PRIVATE);
        String email = prefs.getString("email", null);
        String currentUserClan= getClan(email);

        try{
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("title", title);
            contentValues.put("image_path",image_path);
            contentValues.put("place",place);
            contentValues.put("keywords",keywords);
            contentValues.put("story",story);
            contentValues.put("actor",actor);
            contentValues.put("clan",currentUserClan);
            sqLiteDatabase.insert(SettingsConstant.STORY_TABLE,null,contentValues);
            Log.d("Data Insertion","Success");
            return true;
        }catch (Exception e){
            return false;
        }
    }


    public ArrayList<String> getAllHeaders(boolean isSelf) {
        ArrayList<String> arraylist = new ArrayList<String>();

        SharedPreferences prefs = SettingsConstant.APP_CONTEXT.getSharedPreferences(SettingsConstant.SHARED_PREF_NAME, MODE_PRIVATE);
        String email = prefs.getString("email", null);

        String currentUserName = getName(email);
        String currentUserClan= getClan(email);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        if(isSelf){
            res =  db.rawQuery( "select * from "+SettingsConstant.STORY_TABLE +" where actor =  '"+currentUserName+"'" , null );
        }
        else{
            res =  db.rawQuery( "select * from "+SettingsConstant.STORY_TABLE + " where actor <> '"+currentUserName+"' and clan = '"+currentUserClan+"'" , null );
        }

        res.moveToFirst();
        String actor;
        String title;
        int id;
        while(res.isAfterLast() == false){
            actor ="";
            title="";
            id=0;
            actor = res.getString(res.getColumnIndex("actor"));
            title = res.getString(res.getColumnIndex("title"));
            id = res.getInt(res.getColumnIndex("id"));
            if(isSelf){
                arraylist.add("#"+id+" "+title);
            }
            else{
                arraylist.add("#"+id+" actor: "+actor+"  : "+title);
            }

            res.moveToNext();
        }
        return arraylist;
    }


    public StoryModel getStory(int id){
        StoryModel storyModel = new StoryModel();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;

        res =  db.rawQuery( "select * from "+SettingsConstant.STORY_TABLE + " where id = "+id, null );

        res.moveToFirst();

        while(res.isAfterLast() == false){
            storyModel.setId(res.getInt(res.getColumnIndex("id")));
            storyModel.setActor(res.getString(res.getColumnIndex("actor")));
            storyModel.setTitle(res.getString(res.getColumnIndex("title")));
            storyModel.setImage_path(res.getString(res.getColumnIndex("image_path")));
            storyModel.setKeywords(res.getString(res.getColumnIndex("keywords")));
            storyModel.setStory(res.getString(res.getColumnIndex("story")));
            storyModel.setPlace(res.getString(res.getColumnIndex("place")));

            res.moveToNext();
        }
        return storyModel;

    }


    public boolean deleteEntry(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        db.rawQuery("DELETE FROM "+SettingsConstant.STORY_TABLE+" WHERE id = "+id,null).moveToFirst();
        return true;
    }

    public boolean UpdateMyStory(StoryModel storyModel){
        SQLiteDatabase db = this.getReadableDatabase();
        db.rawQuery("UPDATE  "+SettingsConstant.STORY_TABLE+" SET title = '"+storyModel.getTitle()+"' , image_path  = '"+storyModel.getImage_path()+"' , place = '"+storyModel.getPlace()+
                "' , keywords = '"+storyModel.getKeywords()+"' , story = '"+storyModel.getStory()+"' where id =  "+storyModel.getId(),null).moveToFirst();

        return true;
    }



    public boolean addUser(String name,String email,String password,String clan){
        try{
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("name", name);
            contentValues.put("email", email);
            contentValues.put("password",password);
            contentValues.put("clan",clan);
            contentValues.put("image_path","");

            sqLiteDatabase.insert(SettingsConstant.USER_TABLE,null,contentValues);
            Log.d("Data Insertion","Success");
            return true;
        }catch (Exception e){
            return false;
        }
    }


    public boolean validateUser(String email,String password){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery( "select * from "+SettingsConstant.USER_TABLE +" where email =  '"+email+"'", null );
        res.moveToFirst();
        String originalPass="";
        while(res.isAfterLast() == false){
            originalPass = res.getString(res.getColumnIndex("password"));
            res.moveToNext();
        }

        if(originalPass.equals(password)){
            return true;
        }
        else{
            return false;
        }
    }

    public String getName(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery( "select * from "+SettingsConstant.USER_TABLE +" where email =  '"+email+"'", null );
        res.moveToFirst();
        String name ="";
        while(res.isAfterLast() == false){
            name = res.getString(res.getColumnIndex("name"));
            res.moveToNext();
        }

        return name;
    }

    public String getClan(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery( "select * from "+SettingsConstant.USER_TABLE +" where email =  '"+email+"'", null );
        res.moveToFirst();
        String clan ="";
        while(res.isAfterLast() == false){
            clan = res.getString(res.getColumnIndex("clan"));
            res.moveToNext();
        }
        return clan;
    }

    public boolean updatePP(String image_path,String email){
        SQLiteDatabase db = this.getReadableDatabase();
        db.rawQuery("UPDATE  "+SettingsConstant.USER_TABLE+" SET image_path = '"+ image_path+"' WHERE email = '"+email+"'",null).moveToFirst();
        return true;
    }

    public String getPPPath(String email){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res;
        res =  db.rawQuery( "select * from "+SettingsConstant.USER_TABLE +" where email =  '"+email+"'", null );
        res.moveToFirst();
        String image_path ="";
        while(res.isAfterLast() == false){
            image_path = res.getString(res.getColumnIndex("image_path"));
            res.moveToNext();
        }

        return image_path;
    }



}