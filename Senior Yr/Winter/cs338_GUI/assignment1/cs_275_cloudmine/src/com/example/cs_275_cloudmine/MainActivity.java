package com.example.cs_275_cloudmine;
/*
import com.cloudmine.api.CMApiCredentials;
import com.cloudmine.api.CMObject;
import com.cloudmine.api.SimpleCMObject;
import com.cloudmine.api.rest.CMStore;
import com.cloudmine.api.rest.callbacks.CMObjectResponseCallback;
import com.cloudmine.api.rest.callbacks.ObjectModificationResponseCallback;
import com.cloudmine.api.rest.response.CMObjectResponse;
import com.cloudmine.api.rest.response.ObjectModificationResponse;*/
import java.net.URL;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.temboo.Library.CloudMine.ObjectStorage.ObjectSearch;
import com.temboo.Library.CloudMine.ObjectStorage.ObjectSearch.ObjectSearchInputSet;
import com.temboo.Library.CloudMine.ObjectStorage.ObjectSearch.ObjectSearchResultSet;
import com.temboo.Library.CloudMine.ObjectStorage.ObjectSet;
import com.temboo.Library.CloudMine.ObjectStorage.ObjectSet.ObjectSetInputSet;
import com.temboo.Library.CloudMine.ObjectStorage.ObjectSet.ObjectSetResultSet;
import com.temboo.core.TembooException;
import com.temboo.core.TembooSession;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/*CMApiCredentials.initialize("c1e5c5b5981f4a16b5ec099ab3f9ec5e","951261fdbf1f4ce0a31661ba20e2583d",getApplicationContext());
		
		/*SimpleCMObject cs275 = new SimpleCMObject();
		cs275.add("dept", "cs");
		cs275.add("number", "275");
		cs275.add("title", "Web and Mobile Application Development");
		cs275.save(new ObjectModificationResponseCallback(){
			public void onCompletion(ObjectModificationResponse response){
				Log.i("CloudMine Object", "course was saved: " + response.wasSuccess());
			}
		});
		
		String searchQuery = "[dept = \"cs\",number=\"275\"]";
		
		CMStore store = CMStore.getStore();
		store.loadApplicationObjectsSearch(searchQuery, new CMObjectResponseCallback(){
			public void onCompletion(CMObjectResponse response){
				for(CMObject object:response.getObjects()){
					// only cs275 courses are returned
					SimpleCMObject course = (SimpleCMObject) object;
					Log.i("ClouadMine Object","retrieved course:" + course.getString("title"));
				}
			}
		}); */
		
		


	}

	private class tembooCloudMine_set extends AsyncTask<URL, Void, Void> {

		@Override
		protected Void doInBackground(URL... params) {
			TembooSession session;
			try {
				session = new TembooSession("dudeitssunny", "myFirstApp", "5420c8af75f84c2a8807fd35314fb75c");
				ObjectSet objectSetChoreo = new ObjectSet(session);
				
				ObjectSetInputSet objectSetInputs = objectSetChoreo.newInputSet();
				
				objectSetInputs.set_Data("{\"userojb\":{\"user\":\"bill\",\"key\":\"abc123\"}}");
				objectSetInputs.set_APIKey("951261fdbf1f4ce0a31661ba20e2583d");
				objectSetInputs.set_ApplicationIdentifier("c1e5c5b5981f4a16b5ec099ab3f9ec5e");
				
				ObjectSetResultSet objectSetResults = objectSetChoreo.execute(objectSetInputs);
			} catch (TembooException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		
	}
	
	private class tembooCloudMine_get extends AsyncTask<URL, Void, Void> {

		@Override
		protected Void doInBackground(URL... params) {
			TembooSession session;
			try {
				session = new TembooSession("dudeitssunny", "myFirstApp", "5420c8af75f84c2a8807fd35314fb75c");
				ObjectSearch objectSearchChoreo = new ObjectSearch(session);
				 
				
				// Get an InputSet object for the choreo
				ObjectSearchInputSet objectSearchInputs = objectSearchChoreo.newInputSet();

				// Set inputs
				objectSearchInputs.set_APIKey("951261fdbf1f4ce0a31661ba20e2583d");
				objectSearchInputs.set_Query("[user=\"bill\"]");
				objectSearchInputs.set_ApplicationIdentifier("");

				// Execute Choreo
				ObjectSearchResultSet objectSearchResults = objectSearchChoreo.execute(objectSearchInputs);
		 
				JsonParser jp = new JsonParser();
		    	JsonElement root = jp.parse(objectSearchResults.get_Response());
		    	JsonObject rootobj = root.getAsJsonObject();         // may be Json Array if it's an array, or other type if a primitive
		    	
		    	String key = rootobj.get("success").getAsJsonObject().get("userobj").		                   getAsJsonObject().get("key").getAsString();
		    	System.out.println("Key was " + key);
		    	
		    	System.out.println(objectSearchResults.get_Response());
		 
			} catch (TembooException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
