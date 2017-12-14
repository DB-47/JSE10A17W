package cz.unicorncollege.controller;

import java.util.ArrayList;
import java.util.List;
import cz.unicorncollege.bt.model.MeetingCentre;
import cz.unicorncollege.bt.utils.Choices;
import cz.unicorncollege.bt.utils.fileparsing.FileParserCSV;
import cz.unicorncollege.bt.utils.fileparsing.FileParserJSON;
import cz.unicorncollege.bt.utils.Convertors;
import cz.unicorncollege.bt.utils.HashGenerators;
import cz.unicorncollege.bt.utils.fileparsing.FileParserXML;
import java.util.Map;

/**
 * Main controller class. Contains methods to communicate with user and methods
 * to work with files.
 *
 * @author UCL, DB-47
 */
public class MainController {

    private MeetingController controll;
    private ReservationController controllR;

    public MainController() {
        controll = new MeetingController();
        controll.init();
        controllR = new ReservationController(controll);
    }

    public static void main(String[] argv) {
        MainController instance = new MainController();
        instance.run();
    }

    private void run() {
        List<String> choices = new ArrayList<>();
        choices.add("List all Meeting Centres");
        choices.add("Add new Meeting Centre");
        choices.add("Import Data (XML)");
        choices.add("Exit and Save (XML)");
        choices.add("Exit");
        choices.add("Reservations ");
        choices.add("Export resrvations into JSON (WIP)");
        while (true) {
            switch (Choices.getChoice("Select an option: ", choices)) {
                case 1:
                    controll.listAllMeetingCentres();
                    break;
                case 2:
                    controll.addMeetingCentre();
                    break;
                case 3:
                    Map<String, MeetingCentre> temp = FileParserCSV.importData();
                    if (temp.isEmpty()) {
                        System.out.println("**************************************************");
                        System.out.println("-> No in app data were affected...");
                        System.out.println("**************************************************");
                    } else {
                        controll.setMeetingCentres(temp);
                        controll.listAllMeetingCentres();
                    }
                    break;
                case 4:
                    FileParserXML.saveData(controll.getMeetingCentres());
                    return;
                case 5:
                    String initialMD5 = controll.getInitialMD5();
                    String finalMD5 = HashGenerators.getMD5(controll.provideDataForHashGenerator());
                    System.out.println(initialMD5);
                    System.out.println(finalMD5);
                    if (initialMD5.equals(finalMD5)) {
                        System.out.println("**************************************************");
                        System.out.println("-> Exited correctly without changes...");
                        System.out.println("**************************************************");
                        return;
                    } else {
                        System.out.println("**************************************************");
                        System.out.println("-> You have made changes to in-app data...");
                        System.out.println("-> Are you sure you want to discard changes...");
                        System.out.println("**************************************************");
                        String decision = Choices.getInput("Write YES or y or a or true to discard changes or anyhting else to return back: ");
                        if (Convertors.convertWordToBoolean(decision)) {
                            return;
                        } else {
                            break;
                        }
                    }
                case 6:
                    controllR.showReservationMenu();
                    break;
                case 7:
                    FileParserJSON.exportDataToJSON(controllR);
                    break;
            }
        }
    }
    
}
