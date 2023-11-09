package com.example.latlongfinderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private DBHelper db = new DBHelper(this);
    private EditText searchBar;
    private TextView name, address, latitude, longitude;
    private Button searchBtn, settingBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setting views to actual elements constructed
        searchBar = (EditText) findViewById(R.id.search_field);
        searchBtn = (Button) findViewById(R.id.search_btn);
        settingBtn = (Button) findViewById(R.id.settings);

        name = (TextView) findViewById(R.id.main_name_displayTxt);
        address = (TextView) findViewById(R.id.main_address_displayTxt);
        latitude = (TextView) findViewById(R.id.main_lat_displayTxt);
        longitude = (TextView) findViewById(R.id.main_long_displayTxt);

        //Search button listener
        searchBtn.setOnClickListener(view -> {
            String query = searchBar.getText().toString();
            int result = db.getFirstID(query); //uses query text to get ID of first matching record

            if (result != -1) //query result found
            {
                Cursor response =  db.getLocation(result);
                if (response.getCount() == 0)
                {
                    Toast.makeText(this, "Error fetching address with ID", Toast.LENGTH_SHORT).show();
                }
                else {
                    response.moveToNext();
                    String dbID = response.getString(0);
                    String dbAddress = response.getString(2);
                    String dbName = response.getString(1);
                    String dbLat = response.getString(3);
                    String dbLong = response.getString(4);

                    name.setText(dbName);
                    address.setText(dbAddress);
                    latitude.setText(dbLat);
                    longitude.setText(dbLong);
                }
            }
            else //query not in db
            {
                name.setText("");
                address.setText("");
                latitude.setText("");
                longitude.setText("");
                Toast.makeText(this, "Address or location label not in database", Toast.LENGTH_SHORT).show();
            }

        });

        settingBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, LocationTableOption.class);
            startActivity(intent);
        });

    }
}