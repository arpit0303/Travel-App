package com.android.travelapp;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class PlacesActivity extends Activity {

	Double latitude;
	Double longitude;
	String API_KEY = "AIzaSyBQF4rZXj02aXN2TCRtC7KP1Tb9BITMURs";
	String Search;
	GoogleMap map;
	StringBuilder urlString;
	private static final String TAG = PlacesActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_places);

		Search = getIntent().getStringExtra("searchText");

		showMap();
		
		urlString = new StringBuilder(
				"https://maps.googleapis.com/maps/api/place/nearbysearch/json?");

		urlString.append("location=" + Double.toString(latitude) + ","
				+ Double.toString(longitude));
		urlString.append("&radius=5000");
		urlString.append("&type=" + Search);
		urlString.append("&sensor=true&key=" + API_KEY); 
		
		Handler handler = new Handler(Looper.getMainLooper());
	    handler.post(
	        new Runnable()
	        {
	            @Override
	            public void run()
	            {
	            	Toast.makeText(PlacesActivity.this, urlString.toString(), Toast.LENGTH_LONG).show();
	            }
	        }
	    );
		
		
		
		MapSearch ms = new MapSearch();
		ms.execute();
	}

	private void showMap() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm != null) {
			NetworkInfo nf = cm.getActiveNetworkInfo();
			if (nf != null && nf.isConnected()) {
				map = ((MapFragment) getFragmentManager()
						.findFragmentById(R.id.map)).getMap();

				map.setMyLocationEnabled(true);

				LocationManager locMngr = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

				// Create a criteria object to retrieve provider
				Criteria criteria = new Criteria();

				// Get the name of the best provider
				String provider = locMngr.getBestProvider(criteria, true);

				// Get Current Location
				Location myLocation = locMngr.getLastKnownLocation(provider);

				latitude = myLocation.getLatitude();
				longitude = myLocation.getLongitude();
				CameraPosition cameraPosition = new CameraPosition.Builder()
						.target(new LatLng(latitude, longitude)) 
						.zoom(10).build();

				map.animateCamera(CameraUpdateFactory
						.newCameraPosition(cameraPosition));
			} else {
				Toast.makeText(PlacesActivity.this,
						R.string.error_no_internet_connection,
						Toast.LENGTH_LONG).show();
			}
		} else {
			Toast.makeText(PlacesActivity.this, R.string.error_no_network,
					Toast.LENGTH_LONG).show();
		}
	}

	public class MapSearch extends AsyncTask<Void, Void, JSONObject> {

		@Override
		protected JSONObject doInBackground(Void... params) {
			int responseCode = -1;
			JSONObject jsonresponse = null;
			try {
				// connecting to a URL
				
				URL searchurl = new URL(urlString.toString());
				HttpURLConnection connection = (HttpURLConnection) searchurl
						.openConnection();
				connection.connect();

				// Get the response code which said connection is done or not
				responseCode = connection.getResponseCode();
				
				if (responseCode == HttpURLConnection.HTTP_OK) {
					
					InputStream inputstream = connection.getInputStream();
					Reader reader = new InputStreamReader(inputstream);
					int contetntlength = connection.getContentLength();
					char[] charArray = new char[contetntlength];
					reader.read(charArray);
					String ReasponseData = new String(charArray);

					jsonresponse = new JSONObject(ReasponseData);
					
					JSONArray jsonSearchs = jsonresponse.getJSONArray("results");
					for(int i=0;i<jsonSearchs.length();i++)
					{
						JSONObject jsonSr = jsonSearchs.getJSONObject(i);
						JSONObject js = jsonSr.getJSONObject("geometry");
						JSONObject js1 = js.getJSONObject("location");
						Double lat = js1.getDouble("lat");
						Double lng = js1.getDouble("lng");
						
						map.addMarker(new MarkerOptions()
						.position(new LatLng(lat, lng)));
					}
				} else {
					Log.i(TAG, "unsuccessful HTTP Response code: "
							+ responseCode);
				}

			} catch (MalformedURLException e) {
				Log.e(TAG, "Exception caught: ", e);
			} catch (IOException e) {
				Log.e(TAG, "Exception caught: ", e);
			} catch (Exception e) {
				Log.e(TAG, "Exception caught: ", e);
			}
			return jsonresponse;
		}
		
		@Override
		protected void onPostExecute(JSONObject result) {
			super.onPostExecute(result);
			
		}

	}
}
