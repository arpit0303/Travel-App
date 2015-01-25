package com.android.travelapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class MainActivity extends ActionBarActivity {

	Spinner places;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        getSupportActionBar().hide();
        
        places = (Spinner) findViewById(R.id.places_spinner);
        
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
        		android.R.layout.simple_list_item_1,
        		getResources().getStringArray(R.array.places));
        
        places.setAdapter(adapter);
        
        places.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(MainActivity.this, PlacesActivity.class);
				intent.putExtra("searchText",places.getSelectedItem().toString());
				if(position != 0){
					startActivity(intent);
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
    }


    @Override
    protected void onResume() {
    	places.setSelection(0);
    	super.onResume();
    }
}
