/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.unicorncollege.bt.utils.fileparsing;

import cz.unicorncollege.bt.model.MeetingCentre;
import cz.unicorncollege.bt.model.MeetingRoom;
import cz.unicorncollege.bt.model.Reservation;
import cz.unicorncollege.bt.utils.Choices;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
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
     * @param data Map containing all meeting centers, from whose will be
     * extracted reservations and all necessary information
     */
    public static void exportDataToJSON(Map<String, MeetingCentre> data) {
        // TODO: ulozeni dat do souboru ve formatu JSON
        String locationFilter = Choices.getInput("Enter name of the file for export: ");

        File exportDataFile = new File(locationFilter);
        FileWriter fw = null;
        try {
            fw = new FileWriter(exportDataFile);
            JSONObject main = new JSONObject();
            JSONArray meetingObject = new JSONArray();

            main.put("schema", "PLUS4U.EBC.MCS.MeetingRoom_Schedule_1.0\n");
            main.put("uri", "ues:UCL-BT:UCL.INF/DEMO_REZERVACE:EBC.MCS.DEMO/MR001/SCHEDULE\n");
            
            for (Map.Entry<String, MeetingCentre> entry : data.entrySet()) {
                String key = entry.getKey();
                MeetingCentre value = entry.getValue();
                for (Map.Entry<String, MeetingRoom> innerEntry : value.getMeetingRooms().entrySet()) {
                    String innerKey = innerEntry.getKey();
                    MeetingRoom innerValue = innerEntry.getValue();
                    //Pokud daný meeting room má nějakou rezervaci, provedeme výpis tohoto centra do JSONu
                    if (!innerValue.getReservations().isEmpty()) {
                    //TODO: Implementuj zápis vnitřních dat
                    }
                }
            }
            main.put("data", meetingObject);
            fw.write(main.toJSONString());

            fw.flush();

            System.out.println("");
            System.out.println("**************************************************");
            System.out.println("Data was exported correctly. The file is here: " + exportDataFile.getAbsolutePath());
            System.out.println("**************************************************");
            System.out.println("");

        } catch (IOException ex) {

            System.out.println("");
            System.out.println("**************************************************");
            System.out.println("Something terrible happend during exporting!");
            System.out.println("**************************************************");
            System.out.println("");

        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException ex) {
                    System.out.println("");
                    System.out.println("**************************************************");
                    System.out.println("Something terrible happend during exporting!");
                    System.out.println("**************************************************");
                    System.out.println("");
                }
            }
        }

    }

}
