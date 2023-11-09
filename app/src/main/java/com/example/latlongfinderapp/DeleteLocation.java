package com.example.latlongfinderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class DeleteLocation extends AppCompatActivity {

    private DBHelper db = new DBHelper(this);
    private EditText searchBar;
    private TextView id, name, address, latitude, longitude;
    private Button searchBtn, backBtn, deleteBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_location);

        // setting views to actual elements constructed
        searchBar = (EditText) findViewById(R.id.delete_search_query);
        searchBtn = (Button) findViewById(R.id.deleteSearchBtn);
        backBtn = (Button) findViewById(R.id.delete_back_btn);
        deleteBtn = (Button) findViewById(R.id.deleteLoc_btn);

        id = (TextView) findViewById(R.id.delete_db_id);
        name = (TextView) findViewById(R.id.delete_name_txt);
        address = (TextView) findViewById(R.id.delete_addr_txt);
        latitude = (TextView) findViewById(R.id.delete_lat_txt);
        longitude = (TextView) findViewById(R.id.delete_lon_txt);

        backBtn.setOnClickListener(view -> {
            finish(); //ends this activity and goes back to setting screen
        });

        deleteBtn.setOnClickListener(view -> {
            deleteLocation();
            finish();
        });

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

                    id.setText(dbID);
                    name.setText(dbName);
                    address.setText(dbAddress);
                    latitude.setText(dbLat);
                    longitude.setText(dbLong);
                }
            }
            else //query not in db
            {
                id.setText("");
                name.setText("");
                address.setText("");
                latitude.setText("");
                longitude.setText("");
                Toast.makeText(this, "Address or location label not in database", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void deleteLocation(){

        String idTxt="";
        idTxt = id.getText().toString();


        if (!idTxt.equals("")) { //user has not searched db
           db.deleteLoc(Integer.parseInt(idTxt));
        }
        else
        {
            Toast.makeText(this, "Please search the database for a location to delete!", Toast.LENGTH_SHORT).show();
        }

    }
}