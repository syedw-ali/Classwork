package assignment1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.temboo.Library.Google.Calendar.GetAllCalendars;
import com.temboo.Library.Google.Calendar.GetAllCalendars.GetAllCalendarsInputSet;
import com.temboo.Library.Google.Calendar.GetAllCalendars.GetAllCalendarsResultSet;
import com.temboo.Library.Google.OAuth.*;
import com.temboo.Library.Google.OAuth.FinalizeOAuth.FinalizeOAuthInputSet;
import com.temboo.Library.Google.OAuth.FinalizeOAuth.FinalizeOAuthResultSet;
import com.temboo.Library.Google.OAuth.InitializeOAuth.InitializeOAuthInputSet;
import com.temboo.Library.Google.OAuth.InitializeOAuth.InitializeOAuthResultSet;
import com.temboo.core.TembooException;
import com.temboo.core.TembooSession;


public class main {

	public static void main(String[] args) throws TembooException, ParseException {
		// Instantiate the Choreo, using a previously instantiated TembooSession object, eg:
		TembooSession session = new TembooSession("dudeitssunny", "myFirstApp", "5420c8af75f84c2a8807fd35314fb75c");
		
		/*  *********************************
		 *  Begin OAuth handshake for user 1
		    ********************************* */
		// Instantiate the Choreo, using a previously instantiated TembooSession object, eg:

		GetAllCalendars getAllCalendarsChoreo = new GetAllCalendars(session);

		// Get an InputSet object for the choreo
		GetAllCalendarsInputSet getAllCalendarsInputs = getAllCalendarsChoreo.newInputSet();

		// Set credential to use for execution
		getAllCalendarsInputs.setCredential("Assignment1");

		// Set inputs
		getAllCalendarsInputs.set_RefreshToken("1/AVl22nzcL5NbTfoJ6G4UgOSBTGnWS91tM_wL2dPecns");

		// Execute Choreo
		GetAllCalendarsResultSet getAllCalendarsResults = getAllCalendarsChoreo.execute(getAllCalendarsInputs);		
		
		System.out.println("User1");
		
		JSONObject json = (JSONObject)new JSONParser().parse(getAllCalendarsResults.getOutputs().get("Response"));
		System.out.println(json.get("items").getClass());
		
		Gson gson = new Gson();
		GoogleCalendar test = gson.fromJson(getAllCalendarsResults.getOutputs().get("Response"), GoogleCalendar.class);
		System.out.println(getAllCalendarsResults.get_Response());
		
		
		//System.out.println(getAllCalendarsResults.getOutputs().get("Response"));

		
		
		//System.out.println(json_2.get(1)); 
		//HashMap allCalc_user1 = json.get("items");
		
		/*  *********************************
		 *  Begin OAuth handshake for user 2
		    ********************************* */
		
		// Instantiate the Choreo, using a previously instantiated TembooSession object, eg:

		GetAllCalendars getAllCalendarsChoreo_user2 = new GetAllCalendars(session);

		// Get an InputSet object for the choreo
		GetAllCalendarsInputSet getAllCalendarsInputs_user2 = getAllCalendarsChoreo_user2.newInputSet();

		// Set credential to use for execution
		getAllCalendarsInputs_user2.setCredential("Assignment1");

		// Set inputs
		getAllCalendarsInputs_user2.set_RefreshToken("1/QmR9b3e5R0TNAxSYViA3qDAHcOizbTCMIexlif2QAdg");

		// Execute Choreo
		GetAllCalendarsResultSet getAllCalendarsResults_user2 = getAllCalendarsChoreo_user2.execute(getAllCalendarsInputs_user2);		
		//System.out.println("User2");
		//System.out.println(getAllCalendarsResults_user2.get_Response());
		
		
		
	}

}
