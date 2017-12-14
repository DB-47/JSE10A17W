/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.unicorncollege.bt.utils.fileparsing;

import cz.unicorncollege.bt.model.MeetingCentre;
import cz.unicorncollege.bt.utils.Choices;
import java.io.File;
import java.util.Map;
import javax.json.JsonObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author DB-47-PG
 */
public class FileParserJSON {
    
    	/**
	 * Method to export data to JSON file
	 * 
	 * @param data 
	 * Map containing all meeting centers, from whose will be extracted
         * reservations and all necessary information
	 */
	public static void exportDataToJSON(Map<String, MeetingCentre> data) {
		// TODO: ulozeni dat do souboru ve formatu JSON

		String locationFilter = Choices.getInput("Enter name of the file for export (.json ending will be added) : ");

		File exportDataFile = null;
                
               // json.put("schema", "PLUS4U.EBC.MCS.MeetingRoom_Schedule_1.0");

		System.out.println();

		if (exportDataFile != null) {
			System.out.println("**************************************************");
			System.out.println("Data was exported correctly. The file is here: " + exportDataFile.getAbsolutePath());
			System.out.println("**************************************************");
		} else {
			System.out.println("**************************************************");
			System.out.println("Something terrible happend during exporting!");
			System.out.println("**************************************************");
		}

		System.out.println();
	}
    
}
