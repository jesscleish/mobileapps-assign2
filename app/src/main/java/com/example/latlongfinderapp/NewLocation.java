package com.example.latlongfinderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class NewLocation extends AppCompatActivity {
    private DBHelper db = new DBHelper(this);
    private EditText name, address, latitude, longitude;

    private Button geoCodeBtn, saveBtn, backBtn;

    public NewLocation() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_location);

        geoCodeBtn = (Button) findViewById(R.id.getGeocoords_btn);
        saveBtn = (Button) findViewById(R.id.new_addNew_loc);
        backBtn = (Button) findViewById(R.id.new_back_btn);

        name = (EditText) findViewById(R.id.new_name_editTxt);
        address = (EditText) findViewById(R.id.new_addr_editTxt);
        latitude = (EditText) findViewById(R.id.new_lat_editTxt);
        longitude = (EditText) findViewById(R.id.new_lon_editTxt);

        backBtn.setOnClickListener(view -> {
            finish(); //ends this activity and goes back to setting screen
        });

        saveBtn.setOnClickListener(view -> {
            saveLocation();
            finish();
        });

        geoCodeBtn.setOnClickListener(view -> {
            String addrText ="";
            addrText = address.getText().toString();

            if (!address.equals("") && !address.equals("address"))
            {
                setLatLong(addrText);
            }
            else
            {
                Toast.makeText(this, "Please enter an address first", Toast.LENGTH_SHORT).show();
            }


        });
    }

    private void setLatLong(String strAddress) {
        //Create coder with Activity context - this
        Geocoder coder = new Geocoder(this);
        List<Address> address;

        try {
            //Get latLng from String
            address = coder.getFromLocationName(strAddress, 5);
            //check for null
            if (address != null) {
                //take first possibility from the all possibilities.
                try {
                    Address location = address.get(0);
                    String lat = Double.toString(location.getLatitude());
                    String lon = Double.toString(location.getLongitude());

                    latitude.setText(lat);
                    longitude.setText(lon);

                } catch (IndexOutOfBoundsException er) {
                    Toast.makeText(this, "Coordinates are not available for this address.", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveLocation(){

        String addrText, nameTxt, latTxt, lonTxt ="";
        addrText = address.getText().toString();
        nameTxt = name.getText().toString();
        latTxt = latitude.getText().toString();
        lonTxt = longitude.getText().toString();

        //check if any fields are blank
        if (!addrText.equals("") && !addrText.equals("address")
        && !nameTxt.equals("") && !nameTxt.equals("location name/label")
        && !latTxt.equals("") && !latTxt.equals("latitude")
        && !lonTxt.equals("") && !lonTxt.equals("longitude")){
            db.addLoc(nameTxt,addrText,latTxt,lonTxt);
        }
        else{
            Toast.makeText(this, "Please fill out all fields before saving!", Toast.LENGTH_SHORT).show();
        }

    }

}