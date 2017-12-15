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
import cz.unicorncollege.bt.utils.Convertors;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author DB-47-PG
 */
public class FileParserJSON {

    /**
     * Method to export inputData to JSON file
     *
     * @param inputData Map containing all meeting centers, from whose will be
     * extracted reservations and all necessary information into JSON file
     */
    public static void exportDataToJSON(Map<String, MeetingCentre> inputData) {
        // TODO: ulozeni dat do souboru ve formatu JSON
        String locationFilter = Choices.getInput("Enter name of the file for export: ");

        File exportDataFile = new File(locationFilter);
        FileWriter fw = null;
        try {
            fw = new FileWriter(exportDataFile);
            JSONObject main = new JSONObject(new LinkedHashMap());
            JSONArray data = new JSONArray();

            for (Map.Entry<String, MeetingCentre> entryL1 : inputData.entrySet()) {

                String key = entryL1.getKey();
                MeetingCentre value = entryL1.getValue();
                for (Map.Entry<String, MeetingRoom> entryL2 : value.getMeetingRooms().entrySet()) {
                    String innerKey = entryL2.getKey();
                    MeetingRoom innerValue = entryL2.getValue();
                    //Pokud daný meeting room má nějakou rezervaci, provedeme výpis tohoto centra do JSONu
                    if (!innerValue.getReservations().isEmpty()) {
                        JSONObject object = new JSONObject(new LinkedHashMap());
                        object.put("meetingCentre", key);
                        object.put("meetingRoom", innerKey);
                        Map<Date, List<Reservation>> sortedReservations = innerValue.retrieveReservationSortedByDateAndByTime();

                        JSONObject reservationsOfDays = new JSONObject(new LinkedHashMap());
                        for (Map.Entry<Date, List<Reservation>> entryL3 : sortedReservations.entrySet()) {
                            String dateStr = Convertors.convertDateToString(entryL3.getKey());
                            JSONArray reservations = new JSONArray();
                            for (Reservation r : entryL3.getValue()) {
                                JSONObject reservation = new JSONObject(new LinkedHashMap());
                                reservation.put("from", r.getTimeFrom());
                                reservation.put("to", r.getTimeFrom());
                                reservation.put("expectedPersonCount", r.getExpectedPersonCount());
                                reservation.put("videoConference", r.isNeedVideoConference());
                                reservation.put("note", r.getNote());
                                reservations.add(reservation);
                            }
                            reservationsOfDays.put(dateStr, reservations);
                        }
                        object.put("reservations", reservationsOfDays);
                        data.add(object);
                    }
                }
            }

            main.put("schema", "PLUS4U.EBC.MCS.MeetingRoom_Schedule_1.0");
            main.put("uri", "ues:UCL-BT:UCL.INF/DEMO_REZERVACE:EBC.MCS.DEMO/MR001/SCHEDULE");
            main.put("data", data);

            fw.write(main.toJSONString());

            fw.flush();

            System.out.println("");
            System.out.println("**************************************************");
            System.out.println("-> (i) Data was exported correctly. The file is here: " + exportDataFile.getAbsolutePath());
            System.out.println("**************************************************");
            System.out.println("");

        } catch (IOException ex) {

            System.out.println("");
            System.out.println("**************************************************");
            System.out.println("-> (!) Something terrible happend during exporting!");
            System.out.println("-> Error details: " + ex.getMessage());        
            System.out.println("**************************************************");
            System.out.println("");

        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException ex) {
                    System.out.println("");
                    System.out.println("**************************************************");
                    System.out.println("-> (!) Something terrible happend during exporting!");
                    System.out.println("-> Error details: " + ex.getMessage());
                    System.out.println("**************************************************");
                    System.out.println("");
                }
            }
        }

    }

}
