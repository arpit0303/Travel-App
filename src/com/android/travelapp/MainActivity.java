package com.android.travelapp;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;


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
				// TODO Auto-generated method stub
				Intent intent;
				intent = new Intent(MainActivity.this, PlacesActivity.class);
				
				switch (position) {
				case 1:
					intent.putExtra("mounments",places.getSelectedItem().toString());
					startActivity(intent);
					break;
				case 2:
					intent.putExtra("hotels",places.getSelectedItem().toString());
					startActivity(intent);
					break;
				case 3:
					intent.putExtra("restaurants",places.getSelectedItem().toString());
					startActivity(intent);
					break;
				case 4:
					intent.putExtra("restaurants",places.getSelectedItem().toString());
					startActivity(intent);
					break;

				default:
					break;
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
    }


    @Override
    protected void onResume() {
    	// TODO Auto-generated method stub
    	 places.setSelection(0);
    	super.onResume();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
