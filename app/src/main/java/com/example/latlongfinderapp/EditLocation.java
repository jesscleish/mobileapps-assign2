package com.example.latlongfinderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

public class EditLocation extends AppCompatActivity {
    private DBHelper db = new DBHelper(this);
    private EditText searchBar, name, address, latitude, longitude;
    private Button searchBtn, saveBtn, discardBtn;
    private TextView id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_location);

        searchBar = (EditText) findViewById(R.id.edit_loc_searchTxt);
        searchBtn = (Button) findViewById(R.id.editLocSearch_btn);

        saveBtn = (Button) findViewById(R.id.edit_save_btn);
        discardBtn = (Button) findViewById(R.id.edit_discard_btn);

        id = (TextView) findViewById(R.id.edt_db_id);

        name = (EditText) findViewById(R.id.loc_name_editTxt);
        address = (EditText) findViewById(R.id.loc_addr_editTxt);
        latitude = (EditText) findViewById(R.id.loc_lat_editTxt);
        longitude = (EditText) findViewById(R.id.loc_long_editTxt);

        discardBtn.setOnClickListener(view -> {
            finish(); //ends this activity and goes back to setting screen
        });

        saveBtn.setOnClickListener(view -> {
            saveLocation();
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

    private void saveLocation(){

        String addrText, nameTxt, latTxt, lonTxt ="";
        String idTxt="";
        idTxt = id.getText().toString();
        addrText = address.getText().toString();
        nameTxt = name.getText().toString();
        latTxt = latitude.getText().toString();
        lonTxt = longitude.getText().toString();

        if (!idTxt.equals("")) { //user has not searched db
            //check if any fields are blank
            if (!addrText.equals("") && !addrText.equals("address")
                    && !nameTxt.equals("") && !nameTxt.equals("location name/label")
                    && !latTxt.equals("") && !latTxt.equals("latitude")
                    && !lonTxt.equals("") && !lonTxt.equals("longitude")) {
                db.updateLoc(Integer.parseInt(idTxt), nameTxt, addrText, latTxt, lonTxt);
            } else {
                Toast.makeText(this, "Please fill out all fields before saving!", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, "Please search the database for a location!", Toast.LENGTH_SHORT).show();
        }

    }

}