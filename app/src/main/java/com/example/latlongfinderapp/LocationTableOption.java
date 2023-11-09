package com.example.latlongfinderapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class LocationTableOption extends AppCompatActivity {

    private Button newBtn, editBtn, deleteBtn, backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_table_option);

        newBtn = (Button) findViewById(R.id.add_new_loc_btn);
        editBtn = (Button) findViewById(R.id.edit_loc_btn);
        deleteBtn = (Button) findViewById(R.id.del_loc_btn);
        backBtn = (Button) findViewById(R.id.setting_bck_btn);


        backBtn.setOnClickListener(view -> {
            finish(); //ends this activity and goes back to home screen
        });


        newBtn.setOnClickListener(view -> {
            Intent intent = new Intent(LocationTableOption.this, NewLocation.class);
            startActivity(intent);
        });

        editBtn.setOnClickListener(view -> {
            Intent intent = new Intent(LocationTableOption.this, EditLocation.class);
            startActivity(intent);
        });

        deleteBtn.setOnClickListener(view -> {
            Intent intent = new Intent(LocationTableOption.this, DeleteLocation.class);
            startActivity(intent);
        });

    }
}