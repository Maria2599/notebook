package com.example.notebook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    //l null de l default bta3 l cursor
    public DBHelper(Context context) {
        super(context, "Usernotes.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create Table Userdetails(title TEXT primary key, notes TEXT)");
    }

    @Override
    //lw ha3ml upgrade ba5lih yshil l old version bl new version
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists Userdetails");
    }

    //check if insertion is done
    public Boolean insertdata(String title, String notes){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //create content of values of title and notes
        contentValues.put("title", title);
        contentValues.put("notes", notes);
        
        long results =0;
        
        if (!title.isEmpty() && !notes.isEmpty()) {
            //if they are not empty insert it in data base
             results = DB.insert("Userdetails", null, contentValues);
        }
            //if insertion has faild return false
            if (results == -1) {
                return false;
            } else {
                return true;
            }
    }

    //check if update is done
    public Boolean updatedata(String title, String notes){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("notes", notes);
        //check if the title is written
        Cursor cursor = DB.rawQuery("Select * from Userdetails where title = ? ", new String[]{title});

        if(cursor.getCount() > 0){
        //update the note whose title is entered
        long results = DB.update("Userdetails", contentValues, "title=?", new String[] {title});

        //if insertion has faild return false
        if(results == -1) {
            return false;
        }else {
            return true;
        }
        }else {
            return false;
        }
    }

    //check if deletion is done
    public Boolean deletedata(String title){
        SQLiteDatabase DB = this.getWritableDatabase();
        //check if the title is written
        Cursor cursor = DB.rawQuery("Select * from Userdetails where title = ? ", new String[]{title});

        if(cursor.getCount() > 0){
            //delete the note whose title is entered
            long results = DB.delete("Userdetails", "title=?", new String[] {title});

            //if insertion has faild return false
            if(results == -1) {
                return false;
            }else {
                return true;
            }
        }else {
            return false;
        }
    }

    //check if the delete of all note is done
    public Boolean deletealldata(){
        SQLiteDatabase DB = this.getWritableDatabase();
        //select all notes
        Cursor cursor = DB.rawQuery("Select * from Userdetails ", null);

        if(cursor.getCount() > 0){
            //delete the note whose title is entered
            long results = DB.delete("Userdetails", null, null);

            //if insertion has faild return false
            if(results == -1) {
                return false;
            }else {
                return true;
            }
        }else {
            return false;
        }
    }

    //retrieve data from database
    public Cursor viewdata(String title){
        SQLiteDatabase DB = this.getWritableDatabase();
        //whose title is entered
        Cursor cursor = DB.rawQuery("Select * from Userdetails where title = ?", new String[]{title});

        return cursor;
    }

    //retrieve all data from database
    public Cursor viewalldata(){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Userdetails ", null);

        return cursor;
    }
}
