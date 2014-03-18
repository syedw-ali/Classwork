package drugiee;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

public class DataLoader {

	private JSONObject drugs_json, drug_brands_json, drug_interaction_json, patient_info_json, patient_drugs_json;

	public DataLoader() {
		loadData();
	}

	public void loadData() {
		try {
			// Load in drugs json
			BufferedReader in = new BufferedReader(new FileReader("Data_files/drugs.json"));

			String line, data = "";

			while ((line = in.readLine()) != null) {
				data = data + line;
			}
			in.close();

			// Begin parsing the json Drugs
			drugs_json = new JSONObject(data);

			// Load in drug brands json
			in = new BufferedReader(new FileReader("Data_files/drug_brands.json"));
			line = "";
			data = "";

			while ((line = in.readLine()) != null) {
				data = data + line;
			}
			in.close();

			// Begin parsing the json drug brands
			drug_brands_json = new JSONObject(data);

			// Begin loading in json Drugs information
			in = new BufferedReader(new FileReader("Data_files/drug_interactions.json"));
			line = "";
			data = "";

			while ((line = in.readLine()) != null) {
				data = data + line;
			}
			in.close();
			// Begin parsing the json Drugs information
			drug_interaction_json = new JSONObject(data);

			
			// Load in drugs json
			in = new BufferedReader(new FileReader("Data_files/patient_info.json"));

			line = "";
			data = "";

			while ((line = in.readLine()) != null) {
				data = data + line;
			}
			in.close();

			// Begin parsing the json patient info
			patient_info_json = new JSONObject(data);

			
			// Load Patient drug
			in = new BufferedReader(new FileReader("Data_files/patient_drugs.json"));

			line = "";
			data = "";

			while ((line = in.readLine()) != null) {
				data = data + line;
			}
			in.close();

			// Begin parsing the json patient info
			patient_drugs_json = new JSONObject(data);		
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * GetPatient json
	 * @param patientName
	 * @return
	 */
	public Vector<JSONObject> getPatients_matching(String patientName){
		// removes any html tags if there are there
		patientName = Jsoup.parse(patientName).text();
		
		Vector<JSONObject> allMatchesFound = new Vector<JSONObject>();
		
		try {
			JSONArray patients = patient_info_json.getJSONArray("patient_info");
			for(int i=0; i < patients.length(); i++){
				if(patients.getJSONObject(i).getString("first_name").contains(patientName)){
					allMatchesFound.add(patients.getJSONObject(i));
				} else if(patients.getJSONObject(i).getString("last_name").contains(patientName)){
					allMatchesFound.add(patients.getJSONObject(i));
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allMatchesFound;
	}
	
	/**
	 * Get all drugs prescribed to this patient id
	 * @param patient_id
	 * @return
	 */
	public Vector<String> getAllPresciptions(String patient_id){
		patient_id = Jsoup.parse(patient_id).text();
		Vector<String> allPrescriptions = new Vector<String>();
		
		try{
			JSONArray prescriptions_json = patient_drugs_json.getJSONArray("patient_drugs");
			
			
			for(int i=0; i < prescriptions_json.length(); i++){
				// Convert patient_id to string
				String patient_id_str = String.valueOf(prescriptions_json.getJSONObject(i).getInt("patient_id"));
				
				
				if(patient_id_str.equals(patient_id)){
					allPrescriptions.add(prescriptions_json.getJSONObject(i).getString("drug_id"));
				}
			}
		} catch (JSONException e){
			e.printStackTrace();
		}
		
		return allPrescriptions;
	}
	/**
	 * Get all drugs prescribed to this patient id
	 * @param patient_id
	 * @return
	 */
	public Vector<JSONObject> getAllPresciptions_JSONObject(String patient_id){
		patient_id = Jsoup.parse(patient_id).text();
		Vector<JSONObject> allPrescriptions = new Vector<JSONObject>();
		
		try{
			JSONArray prescriptions_json = patient_drugs_json.getJSONArray("patient_drugs");
			
			
			for(int i=0; i < prescriptions_json.length(); i++){
				// Convert patient_id to string
				String patient_id_str = String.valueOf(prescriptions_json.getJSONObject(i).getInt("patient_id"));
				
				
				if(patient_id_str.equals(patient_id)){
					allPrescriptions.add(prescriptions_json.getJSONObject(i));
				}
			}
		} catch (JSONException e){
			e.printStackTrace();
		}
		
		return allPrescriptions;
	}
	
	public Vector<String> getAllDrugInteractions(String drug_id){
		drug_id = Jsoup.parse(drug_id).text();
		Vector<String> allInteractions = new Vector<String>();
		
		JSONArray interactions;
		try {
			interactions = drug_interaction_json.getJSONArray("drug_interactions");
			for(int i=0; i < interactions.length(); i++){
				if(interactions.getJSONObject(i).getString("drug_id").equals(drug_id)){
					allInteractions.add(interactions.getJSONObject(i).getString("interaction_drug_id"));
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return allInteractions;
	}
	
	/**
	 * GetPatient json
	 * @param patientName
	 * @return
	 */
	public JSONObject getPatient(String patientName){
		// removes any html tags if there are there
		patientName = Jsoup.parse(patientName).text();
		
		try {
			JSONArray patients = patient_info_json.getJSONArray("patient_info");
			for(int i=0; i < patients.length(); i++){
				String full_name = patients.getJSONObject(i).getString("first_name") + " " + patients.getJSONObject(i).getString("last_name");
				if(full_name.equals(patientName)){
					return patients.getJSONObject(i);
				} 
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public Vector<JSONObject> getBrandName_matching(String drugName) {
		drugName = Jsoup.parse(drugName).text();
		Vector<JSONObject> allBrandsFound = new Vector<JSONObject> ();
		try {
			JSONArray drug_brands = drug_brands_json.getJSONArray("drug_brands");
			
			// search through all drug brands we know looking for one that contains this text
			for (int i = 0; i < drug_brands.length(); i++) {
				if (drug_brands.getJSONObject(i).getString("brand_name").contains(drugName)) {
					 allBrandsFound.add(drug_brands.getJSONObject(i));
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allBrandsFound;
	}

	public Vector<JSONObject> getDrugName_matching(String drugName) {
		drugName = Jsoup.parse(drugName).text();
		Vector<JSONObject> allDrugsFound = new Vector<JSONObject> ();
		
		try {
			JSONArray drugs_array = drugs_json.getJSONArray("drugs");
			for (int i = 0; i < drugs_array.length(); i++) {
				if (drugs_array.getJSONObject(i).getString("name").contains(drugName)) {
					allDrugsFound.add(drugs_array.getJSONObject(i));
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allDrugsFound;
	}
	
	/**
	 * Gets Drug id associated with corresponding drug name or brand name
	 * @param name
	 * @return
	 */
	public String getDrugId_fromName(String name){
		name = Jsoup.parse(name).text();
		
		// Check if the name is a brand name for a drug
		JSONArray brands_array;
		try {
			brands_array = drug_brands_json.getJSONArray("drug_brands");
			for(int i=0; i < brands_array.length(); i++){
				if(brands_array.getJSONObject(i).getString("brand_name").equals(name)){
					return brands_array.getJSONObject(i).getString("drug_id");
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Check if the name is a drug name and grab the drug_id
		JSONArray drugs_array;
		try {
			drugs_array = drugs_json.getJSONArray("drugs");
			for( int i=0; i < drugs_array.length(); i++){
				if(drugs_array.getJSONObject(i).getString("name").equals(name)){
					return drugs_array.getJSONObject(i).getString("drug_id");
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public JSONObject getDrug(String drug_id) {
		if (drug_id == null)
			return null;

	
		try {
			JSONArray drugs_array = drugs_json.getJSONArray("drugs");
			for (int i = 0; i < drugs_array.length(); i++) {
				if (drugs_array.getJSONObject(i).getString("drug_id").equals(drug_id)) {
					return drugs_array.getJSONObject(i);
				}
			}
		} catch(JSONException e){
			e.printStackTrace();
		}
		return null;
	}
		
	public String getDrugInformation(String drug_id) {
		if (drug_id == null)
			return null;

		String formattedInfo = "";
		try {
			JSONArray drugs_array = drugs_json.getJSONArray("drugs");
			for (int i = 0; i < drugs_array.length(); i++) {
				if (drugs_array.getJSONObject(i).getString("drug_id")
						.equals(drug_id)) {
					String description, pharmacology, indication;
					description = drugs_array.getJSONObject(i).getString(
							"description");
					pharmacology = drugs_array.getJSONObject(i).getString(
							"pharmacology");
					indication = drugs_array.getJSONObject(i).getString(
							"indication");

					formattedInfo = "<html>"
							+ "<h2>Description</h2><p>"
							+ description + "</p>"
							+ "<br><h2>Pharmacology</h2><p>" + pharmacology
							+ "</p>" + "<br><h2>Indication</h2><p>"
							+ indication + "</p></html>";

					return formattedInfo;
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
