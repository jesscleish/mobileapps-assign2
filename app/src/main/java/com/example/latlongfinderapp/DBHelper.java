package com.example.latlongfinderapp;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DBHelper extends SQLiteOpenHelper {

    private Context context;
    //db name
    private static final String DB_NAME="JessLeishLatLongApp.db";

    //db version
    private static final int DB_VERSION = 1;

    // table name
    private static final String TABLE_NAME = "locationTable";

    //table columns
    private static final String ID_COL = "id";
    private static final String NAME_COL = "locationName";
    private static final String ADDRESS_COL ="address";
    private static final String LATITUDE_COL = "latitude";
    private static final String LONGITUDE_COL = "longitude";
    //constructor
    public DBHelper (@Nullable Context context){
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String  query = "CREATE TABLE " + TABLE_NAME + " (" +
                ID_COL +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME_COL + " TEXT, " +
                ADDRESS_COL + " TEXT, " +
                LATITUDE_COL + " TEXT, " + //stored as text for precision of decimal places
                LONGITUDE_COL + " TEXT)"; //stored as text for precision of decimal places

        db.execSQL(query);
        insertData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion >= newVersion)
            return;
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }



    //uses ID to return full required Location information
    public Cursor getLocation(int id)
    {
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + ID_COL + "='"+id+"'";
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    //insert data from csv if no rows in db
    private void insertData(SQLiteDatabase db)
    {
        Log.d("readData", "reading data");
        String mCSVfile = "data.csv";
        AssetManager manager = context.getAssets();
        InputStream inStream = null;
        try {
            inStream = manager.open(mCSVfile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
        String line = "";
        db.beginTransaction();
        try {
            while ((line = buffer.readLine()) != null) {
                String[] columns = line.split(",");
                Log.d("collength", Integer.toString(columns.length));
                if (columns.length != 4) {
                    Log.d("CSVParser", "Skipping Bad CSV Row");
                    continue;
                }
                ContentValues cv = new ContentValues(3);
                cv.put(ADDRESS_COL, columns[0].trim());
                cv.put(NAME_COL, columns[1].trim());
                cv.put(LATITUDE_COL, columns[2].trim());
                cv.put(LONGITUDE_COL, columns[3].trim());
                db.insert(TABLE_NAME, null, cv);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        db.setTransactionSuccessful();
        db.endTransaction();

    }


    //Add location using strings passed
    public void addLoc(String name, String address, String lat, String lon) {
        //create var for sqlite db and call writeable method
        SQLiteDatabase db = this.getWritableDatabase();

        //contentValues variable
        ContentValues values = new ContentValues();

        values.put(NAME_COL, name);
        values.put(ADDRESS_COL, address);
        values.put(LATITUDE_COL, lat);
        values.put(LONGITUDE_COL, lon);

        long id = db.insert(TABLE_NAME, null, values);
        if (id == -1) {
            Log.d("Error inserting", "ID: " + id);
        }else{
            Log.d("Inserted", "ID: " + id);
        }

    }

    // Update location with values passed where id is the id entered
    public void updateLoc(int id, String name, String address, String lat, String lon) {
        //create var for sqlite db and call writeable method
        SQLiteDatabase db = this.getWritableDatabase();

        //contentValues variable
        ContentValues values = new ContentValues();

        values.put(NAME_COL, name);
        values.put(ADDRESS_COL, address);
        values.put(LATITUDE_COL, lat);
        values.put(LONGITUDE_COL, lon);

        long response = db.update(TABLE_NAME, values, ID_COL + "=?", new String[]{String.valueOf(id)});

        if (response == -1) {
            Log.d("Error updating", "ID: " + id);
        }else {
            Log.d("Updated", "ID: " + id);
        }

    }

    //delete a location from the DB based on ID
    public void deleteLoc(int id)
    {
        SQLiteDatabase db = getWritableDatabase();
        long response = db.delete(TABLE_NAME, ID_COL + "=?", new String[]{String.valueOf(id)});
        if (response == -1) {
            Log.d("Error deleting", "ID: " + id);
        }else {
            Log.d("Deleted", "ID: " + id);
        }
    }

    //trying a different tutorial this time
    Cursor readAllData()
    {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }

        return cursor;
    }

    //Gets first id of row that matches search query either in name column or address column
    public int getFirstID(String search)
    {
        search = (search.replace("'", "''"));
        String query = "SELECT "+ ID_COL+" FROM " + TABLE_NAME + " WHERE ((" + ADDRESS_COL + " LIKE '%"+search+"%') OR (" + NAME_COL + " LIKE '%"+search+"%'))";
        Log.d("getQuery", query);
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);

            while(cursor.moveToNext()) {
                int id = cursor.getInt(0);
                return id;
            }

        }
        return -1;
    }
}

