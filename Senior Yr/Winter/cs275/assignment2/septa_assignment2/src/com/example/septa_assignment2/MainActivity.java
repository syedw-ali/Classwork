package com.example.septa_assignment2;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.BufferedReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity implements OnItemSelectedListener {

	private Spinner source_station;
	private Spinner destination_station;
	private Button mapButton;
	private List<String> stations_list;
	private ArrayAdapter<String> dataAdapter;
	private TextView availableTrains_text;
	private ListView availableTrains_listView; 
	private List<Map<String, String>> nextTrain_data;
	private SimpleAdapter availableTrains_adapter;
	private String source_station_selected;
	private String destination_station_selected;
	private String mapURL;
	private List<HashMap<String, String>> coordinates;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		source_station = (Spinner) findViewById(R.id.source_station);
		destination_station = (Spinner) findViewById(R.id.destination_station);
		availableTrains_text = (TextView) findViewById(R.id.available_trains_text);
		availableTrains_listView = (ListView) findViewById(R.id.availableTrains_list);
		mapButton = (Button) findViewById(R.id.openMaps_button);
		stations_list = new ArrayList<String>();
		new getStationListAsyncTask().execute();
		
		source_station_selected = "";
		destination_station_selected = "";

		dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, stations_list);
		dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		source_station.setAdapter(dataAdapter);
		destination_station.setAdapter(dataAdapter);
		
		source_station.setOnItemSelectedListener(new StationsSelectedListener());
		destination_station.setOnItemSelectedListener(new StationsSelectedListener());
		
		availableTrains_text.setEnabled(false);
		
		nextTrain_data = new ArrayList<Map<String, String>>();
		
		availableTrains_adapter = new SimpleAdapter(this, nextTrain_data,
		                                          android.R.layout.simple_list_item_2,
		                                          new String[] {"Train", "Times"},
		                                          new int[] {android.R.id.text1,
		                                                     android.R.id.text2});
		
		
		Map<String,String> datum = new HashMap<String,String>();
		datum.put("Train","Select a destination");
		datum.put("Times","N/A");
		nextTrain_data.add(datum);
		
		availableTrains_listView.setAdapter(availableTrains_adapter);
		availableTrains_listView.setOnItemLongClickListener(new TrainOnClickListener());
		
		mapURL = "";
		coordinates = new ArrayList<HashMap<String, String>> ();
		mapButton.setOnClickListener(new OpenImageViewClickListener());
	}
	public class OpenImageViewClickListener implements View.OnClickListener{
		

		@Override
		public void onClick(View v) {
			switch(v.getId()){
			case R.id.openMaps_button:
				
				
				// Make the static google maps image url
				String url = "http://maps.googleapis.com/maps/api/staticmap?zoom=13&size=600x300&maptype=roadmap";
				
				for(int i =0; i < coordinates.size(); i++){
					//39.73740,-75.55360
					url = url + "&markers=color:blue%7Clabel:a%7C" + coordinates.get(i).get("lat") +","+ coordinates.get(i).get("long");
				}
				
				url = url + "&sensor=false";
				
				Log.w("Septa", "Image url : " + url);
				Intent goToMapView = new Intent(MainActivity.this, TrainMapActivity.class);
				goToMapView.putExtra("url", url);
				startActivity(goToMapView);
			break;
			}
		}
		
	}
	public class StationsSelectedListener implements OnItemSelectedListener {

	    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
	    	
	    	Log.w("Septa", "In onItemSelected");
	    	
	    	if(!parent.getItemAtPosition(pos).toString().equals(" Station Name")){
		    	switch(parent.getId()){
		    	case R.id.source_station:
		    		Log.w("Septa", "Source station selected :" + parent.getItemAtPosition(pos).toString());
		    		source_station_selected = parent.getItemAtPosition(pos).toString();
		    		break;
		    	case R.id.destination_station:
		    		Log.w("Septa", "Destination station selected :" + parent.getItemAtPosition(pos).toString());
		    		destination_station_selected = parent.getItemAtPosition(pos).toString();
		    		
		    		
		    		break;
		    	}
	    	}
	    	
	    	if( !destination_station_selected.toString().equals(" Station Name") &&
	    			!source_station_selected.toString().equals(" Station Name") &&
	    			source_station_selected.toString().length() > 0 &&
	    			destination_station_selected.toString().length() > 0 )
	    		new getAvailableTrainsAsyncTask().execute();
	    }

	    public void onNothingSelected(AdapterView parent) {
	    	source_station_selected = "";
	    	destination_station_selected = "";
	    }
	}
	
	
	public class TrainOnClickListener implements OnItemLongClickListener{

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View v, int pos, long arg3) {{
			String selectedItem = parent.getItemAtPosition(pos).toString();
			// String manipulation to get the train number
			String trainNo = selectedItem.substring(selectedItem.indexOf("TrainNo")+8, selectedItem.indexOf(","));
			String trainName = selectedItem.substring(selectedItem.indexOf("Train=")+6, selectedItem.indexOf("}"));
			Log.w("Septa", "here trainName=" + trainName);
		
		
			new getTrainDetailsAsyncTask().execute(trainNo,trainName);

		
			return false;
		}

		
	}}
	
	private class getNextTrainsDataAsyncTask extends AsyncTask<String, Integer, String>{

		@Override
		protected String doInBackground(String... params) {
			
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet("http://www3.septa.org/hackathon/TrainView/");
			
			try{
				HttpResponse response = httpclient.execute(httpget);
				String responseStr = EntityUtils.toString(response.getEntity());
				
				
				return responseStr;
			} catch(ClientProtocolException e){
				Log.w("Septa",e.getStackTrace().toString());
			} catch (IOException e){
				Log.w("Septa", e.getStackTrace().toString());
			}
			return null;
		}
		
		protected void onPostExecute(String result) {
			try {
				JSONArray jsonObj = new JSONArray(result);
				Log.w("Septa","result=" + result);
				// clear out old train coordinates
				coordinates.clear();
				
				for(int i=0; i < jsonObj.length(); i++){
					String trainNo = jsonObj.getJSONObject(i).getString("trainno");
					int trainNum = Integer.parseInt(trainNo);
					
					Log.w("Septa", "trainNum="+trainNo);
					
					for(int j=0; j < nextTrain_data.size(); j++){
						Map<String,String> routeTrains = nextTrain_data.get(j);
						
						Log.w("Septa", "routeTrains.get(\"trainNo\")="+routeTrains.get("TrainNo"));
						if(routeTrains.get("TrainNo").equals(trainNo)){
							Log.w("Septa", "MATCH FOUND!");
							
							// we have train coming up that we are interestetd in!
							String lat = jsonObj.getJSONObject(i).getString("lat");
							String longi = jsonObj.getJSONObject(i).getString("lon");
							Log.w("Septa", "Lat=" + lat + " Longi = " + longi);
							HashMap<String, String> thisTrain_coors = new HashMap<String,String>();
							thisTrain_coors.put("lat",lat);
							thisTrain_coors.put("long",longi);
							
							coordinates.add(thisTrain_coors);
						}
					}
				}
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	}
	
	private class getTrainDetailsAsyncTask extends AsyncTask<String,Integer, Map<String,String>>{

		protected Map<String,String> doInBackground(String... params) {
			String trainNo = params[0];
			Log.w("Septa", "params[0]=" +params[0]);
			String trainName = params[1];
			Log.w("Septa", "params[1]=" + params[1]);
			
			HttpClient httpclient = new DefaultHttpClient();
			
			// Make a request to more details of the train
		
				String url = "http://www3.septa.org/hackathon/RRSchedules/"+Uri.encode(trainNo)+"/";
				
				Log.w("Septa", url);
				
				HttpGet httpget = new HttpGet(url);
				
				try{
					HttpResponse response = httpclient.execute(httpget);
					String responseStr = EntityUtils.toString(response.getEntity());
					Map<String,String> returnData = new HashMap<String,String>();
					
					returnData.put("train", trainName);
					returnData.put("trainNo",trainNo);
					returnData.put("data",responseStr);
					
					return returnData;
				} catch(ClientProtocolException e){
					Log.w("Septa",e.getStackTrace().toString());
				} catch (IOException e){
					Log.w("Septa", e.getStackTrace().toString());
				}
			
			
			return null;
		}
		
		protected void onPostExecute(Map<String,String> result) {
			Log.w("Septa", "Alert dialog:" );
			
			String trainName = result.get("train");
			String trainNo = result.get("trainNo");
			String data = result.get("data");
			String station,scheduled_time, actual_time, message = "";
			
			
			JSONArray json;
			try {
				Log.w("Septa", "JSON data" + data);
				json = new JSONArray(data);
				int lastStation_i = -1;
				for(int i=0; (i < json.length()) && lastStation_i == -1; i++){
					Log.w("Septa", "Station: " + json.getJSONObject(i).getString("station"));
					if(json.getJSONObject(i).getString("act_tm").equals("na")){
						lastStation_i = i-1;
					}
				}
				
				// This is the last station the train was at
				JSONObject jsonObj_lastStation = json.getJSONObject(lastStation_i);
				if(lastStation_i != -1){
					 station = jsonObj_lastStation.getString("station");
					 scheduled_time = jsonObj_lastStation.getString("sched_tm");
					 actual_time = jsonObj_lastStation.getString("act_tm");
				} else {
					 station = "Train finished it's route";
					 scheduled_time = "n/a";
					 actual_time = "n/a";
				}
				message = "Last known station \nStation: " + station + "\n" + "Scheduled departure time: " + scheduled_time + "\n" +
							"Actual departure time: " + actual_time ;
				
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
				alertDialogBuilder.setTitle(trainName + " (" + trainNo + ")");
				alertDialogBuilder
				.setMessage(message)
				.setCancelable(false)
				.setPositiveButton("Cool!",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						// if this button is clicked, close
						// current activity
						dialog.cancel();
					}
				  });

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
		}
	}
	/**
	 * AsyncTask to pull the next available trains for the source and destination stations
	 * @author sunnypatel
	 *
	 */
	private class getAvailableTrainsAsyncTask extends AsyncTask<String, Integer, String>{

		@Override
		protected String doInBackground(String... arg0) {
			HttpClient httpclient = new DefaultHttpClient();
			
			// Make a request to get next trains available if source station and destination station are selected
			if(source_station_selected.length()>0 && destination_station_selected.length()>0){
				String url = "http://www3.septa.org/hackathon/NextToArrive/"+Uri.encode(source_station_selected)+"/"+Uri.encode(destination_station_selected)+"/";
				
				//Log.w("Septa", url);
				
				HttpGet httpget = new HttpGet(url);
				
				try{
					HttpResponse response = httpclient.execute(httpget);
					String responseStr = EntityUtils.toString(response.getEntity());
					return responseStr;
				} catch(ClientProtocolException e){
					Log.w("Septa",e.getStackTrace().toString());
				} catch (IOException e){
					Log.w("Septa", e.getStackTrace().toString());
				}
			} else {
				Log.e("Septa", "Attempted to get next trains without selecting both source station and dest. station");
			}
			return null;
		}
		
		protected void onPostExecute(String result) {
			//Log.w("Got next trains", result);
			// we got the json for the next trains for this route
			String train_name;
			String train_no;
			String depart_time;
			String arrival_time;
			
			try {
				JSONArray json = new JSONArray(result);
				List<Map<String,String>> newTrainList = new ArrayList<Map<String,String>>();
				
				for(int i=0; i < json.length(); i++){
					Log.w("Got next trains","inside for loop");
					JSONObject train_jsonObj = json.getJSONObject(i);
					train_no = train_jsonObj.getString("orig_train");
					train_name = train_jsonObj.getString("orig_line");
					depart_time = train_jsonObj.getString("orig_departure_time");
					arrival_time = train_jsonObj.getString("orig_arrival_time");
					
					String train_times_info = "Departure time: " + depart_time + "  |  Arrival time: " + arrival_time;
					Map<String,String> datum = new HashMap<String,String>();
					datum.put("Train", train_name);
					datum.put("Times", train_times_info);
					datum.put("TrainNo", train_no);
					
					newTrainList.add(datum);
					Log.w("Got next trains","added: " + datum.get("Train"));
					Log.w("Got next trains","added: " + datum.get("Times"));
				}
				
				updateAvailableTrainsList(newTrainList);
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
	}
	
	protected void updateAvailableTrainsList(List<Map<String,String>> updatedTrainList){
		nextTrain_data.clear();
		nextTrain_data.addAll(updatedTrainList);
		
		availableTrains_adapter.notifyDataSetChanged();
		new getNextTrainsDataAsyncTask().execute();
	}
	
	/**
	 * This AsyncTask will be used to grab the csv of train stations
	 * and populate the source and destination spinners
	 * @author sunnypatel
	 *
	 */
	private class getStationListAsyncTask extends
			AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... params) {
			Log.w("Starting", "getting csv");

			try {
				InputStream input = new URL("http://www3.septa.org/hackathon/Arrivals/station_id_name.csv").openStream();
				String responseStr = getStringFromInputStream(input);

				return responseStr;
			} catch (IOException e) {
				Log.w("", e.getStackTrace().toString());
			}

			return null;
		}

		protected void onPostExecute(String result) {
			//Log.w("Get stations CSV", result);
			String[] station_data = result.split("\n");
			int i = 0;
			while (i < station_data.length) {

				String[] line = station_data[i].split(",");
				dataAdapter.add(line[1]);
				i++;
			}

		}
	}

	public void onItemSelected(AdapterView<?> parentView, View v, int position,long id) {
		
		switch (v.getId()) {
		case R.id.source_station:
			String station = stations_list.get(position).toString();
			dataAdapter.notifyDataSetChanged();
			break;
		case R.id.destination_station:
			
			break;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// convert InputStream to String
	private static String getStringFromInputStream(InputStream is) {

		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();

		String line;
		try {

			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				Log.e("Septa", line);
				sb.append(line + "\n");
			}

		} catch (IOException e) {
			// e.printStackTrace();
			Log.e("Septa", "getStringFromInputStream IOException");
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					Log.e("Septa", "getStringFromInputStream IOException 2");
				}
			}
		}

		return sb.toString();

	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
	}
}
