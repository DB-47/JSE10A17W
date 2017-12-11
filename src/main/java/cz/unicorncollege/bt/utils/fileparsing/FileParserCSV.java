package cz.unicorncollege.bt.utils.fileparsing;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import java.util.ArrayList;
import java.util.List;

import cz.unicorncollege.bt.model.MeetingCentre;
import cz.unicorncollege.bt.model.MeetingRoom;
import cz.unicorncollege.bt.utils.Choices;
import cz.unicorncollege.bt.utils.Convertors;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class FileParserCSV {


    public static final String DEFAULT_FILE_PATH = "importData.csv";
    public static final char ODDELOVAC = ',';
    public static final String EMPTY_CELL = "";
    public static final String KEYWORD_CANCEL = "!cancel";


    public static Map<String, MeetingCentre> importData() {

        Map<String, MeetingCentre> allMeetingCentres = new LinkedHashMap<>();

        try {
            System.out.println("**************************************************");
            System.out.println("-> Import will lead to loss of temporary in app data");
            System.out.println("-> Proceed with caution...");
            System.out.println("-> You can type !cancel in case you triggered this accidentally");
            System.out.println("**************************************************");

            String locationFilter = Choices.getInput("Enter path of imported file: ");
            if (locationFilter.equalsIgnoreCase(KEYWORD_CANCEL)) {
                System.out.println();
                System.out.println("**************************************************");
                System.out.println("-> You canceled successfully data import");
                System.out.println("**************************************************");
                System.out.println();
            } else {
                allMeetingCentres = writeRawDataToWorkData(nactiCSVdoKolekce(locationFilter, ODDELOVAC));
                System.out.println();
                System.out.println("**************************************************");
                System.out.println("-> Data was imported. " + allMeetingCentres.size() + " objects of MeetingCentres was loaded");
                System.out.println("**************************************************");
                System.out.println();
            }

        } catch (IOException ex) {
            System.out.println("**************************************************");
            System.out.println("-> Attention! Invalid filepath given, repeat choice No. 3");
            System.out.println("**************************************************");
        }

        return allMeetingCentres;
    }


    public static void saveData(List<String[]> output) {

        try {
            writeListToCSV(output, DEFAULT_FILE_PATH, ODDELOVAC);
            System.out.println();
            System.out.println("**************************************************");
            System.out.println("-> Data was saved correctly.");
            System.out.println("**************************************************");
            System.out.println();
        } catch (IOException ex) {
            System.err.println(ex);
        }

    }


    public static Map<String, MeetingCentre> loadDataFromFile() {

        Map<String, MeetingCentre> mcs = new LinkedHashMap<>();

        try {
            List<String[]> radky = nactiCSVdoKolekce(DEFAULT_FILE_PATH, ODDELOVAC);
            mcs = writeRawDataToWorkData(radky);
            System.out.println();
            System.out.println("**************************************************");
            System.out.println("-> Data was loaded correctly.");
            System.out.println("**************************************************");
            System.out.println();
        } catch (IOException ex) {
            System.out.println();
            System.out.println("**************************************************");
            System.out.println("-> Attention! Default data file is missing, no data was loadaded");
            System.out.println("**************************************************");
            System.out.println();
        }

        return mcs;
    }

    private static List<String[]> nactiCSVdoKolekce(String file, char separator) throws FileNotFoundException, IOException {
        List<String[]> radky = new ArrayList<>();

        FileReader fr = new FileReader(file);
        CSVReader csvr = new CSVReader(fr, separator);
        String[] nextLine;
        while ((nextLine = csvr.readNext()) != null) {
            radky.add(nextLine);
        }

        return radky;
    }

    private static void writeListToCSV(List<String[]> enterRows, String target, char separator) throws IOException {

        CSVWriter writer = new CSVWriter(new FileWriter(target), separator);
        for (String[] row : enterRows) {
            writer.writeNext(row);
        }
        writer.close();

    }

    private static Map<String, MeetingCentre> writeRawDataToWorkData(List<String[]> enterRows) {
        Map<String, MeetingCentre> mcs = new LinkedHashMap<>();
        for (String[] row : enterRows) {
            // 
            int voidCells = distinctRoomsFromCentres(row);

            if (voidCells == 5) {
                //We found metadata row
            } else if (voidCells == 3) {
                //We found MC
                mcs.put(row[1], new MeetingCentre(new LinkedHashMap<String, MeetingRoom>(), row[0], row[1], row[2]));
            } else if (voidCells == 0) {
                // We found MR
                if (mcs.containsKey(row[5])) {
                    mcs.get(row[5]).getMeetingRooms().put(row[1], new MeetingRoom(
                            Integer.parseInt(row[3]),
                            Convertors.convertWordToBoolean(row[4], "YES", "NO"),
                            mcs.get(row[5]),
                            row[0],
                            row[1],
                            row[2]
                    ));
                } else {
                    System.out.println("Your file contains room, that belongs to non-existing MC");
                    System.out.println("This room will be excluded from import");
                }
            }

        }
        return mcs;
    }

    private static int distinctRoomsFromCentres(String[] radek) {
        int voidCells = 0;
        for (int i = 0; i < radek.length; i++) {
            if (radek[i].equals(EMPTY_CELL)) {
                voidCells++;
            }
        }
        return voidCells;
    }



}
