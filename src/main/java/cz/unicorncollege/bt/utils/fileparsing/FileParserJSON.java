/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.unicorncollege.bt.utils.fileparsing;

import cz.unicorncollege.bt.utils.Choices;
import cz.unicorncollege.controller.ReservationController;
import java.io.File;
import javax.json.JsonObject;

/**
 *
 * @author DB-47-PG
 */
public class FileParserJSON {
    
    	/**
	 * Method to export data to JSON file
	 * 
	 * @param controllReservation
	 *            Object of reservation controller to get all reservation and
	 *            other data if needed
	 */
	public static void exportDataToJSON(ReservationController controllReservation) {
		// TODO: ulozeni dat do souboru ve formatu JSON

		String locationFilter = Choices.getInput("Enter name of the file for export: ");

		File exportDataFile = null;
		JsonObject json = null;

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
