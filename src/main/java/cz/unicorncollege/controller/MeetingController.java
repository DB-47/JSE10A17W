package cz.unicorncollege.controller;

import java.util.ArrayList;
import java.util.List;

import cz.unicorncollege.bt.model.MeetingCentre;
import cz.unicorncollege.bt.model.MeetingRoom;
import cz.unicorncollege.bt.model.Reservation;
import cz.unicorncollege.bt.utils.Choices;
import cz.unicorncollege.bt.utils.fileparsing.FileParserCSV;
import cz.unicorncollege.bt.utils.Convertors;
import cz.unicorncollege.bt.utils.HashGenerators;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

public class MeetingController implements Serializable {

    public String getInitialMD5() {
        return initialMD5;
    }

    private Map<String, MeetingCentre> meetingCentres;
    private String initialMD5;

    /**
     * This method fills in data from default file location and creates MD5 hash
     * from initial state of master "meetingCentres" map to assure, user will
     * be warned if he tries to leave changed in-app data without changes
     */
    public void init() {

        meetingCentres = FileParserCSV.loadDataFromFile();
        //We create MD5 from central Map
        //We can surely tell, if someone altered this map or not
        //This can be used for warning user when exiting without save after modifying data
        initialMD5 = HashGenerators.getMD5(provideDataForHashGenerator());

    }

    public void listAllMeetingCentres() {
        printAllMeetingCentres();
        try {
            listAllMeetingCentresProvideChoices();
        } catch (NumberFormatException nfe) {
            System.out.println("Invalid input, type either number, or code like 2-EBC-MC_BRNO, or 2 press enter and type EBC-MC_BRNO");
        }

    }

    private void listAllMeetingCentresProvideChoices() {
        List<String> choices = new ArrayList<>();
        choices.add("Show Details of Meeting Centre with code:");
        choices.add("Edit Meeting Centre with code:");
        choices.add("Delete Meeting Centre with code:");
        choices.add("Go Back to Home");

        while (true) {

            Choices.listChoices(choices);
            String chosenOption = Choices.getInput("Choose option (including code after '-', example 1-M01): ");
            int option = chosenOption.contains("-") ? Integer.parseInt(chosenOption.substring(0, 1)) : Integer.parseInt(chosenOption);
            String code = chosenOption.contains("-") ? chosenOption.substring(2, chosenOption.length()) : "";

            switch (option) {
                case 1:
                    Choices.listChoices(choices);
                    showMeetingCentreDetails(code);
                    break;
                case 2:
                    Choices.listChoices(choices);
                    editMeetingCentre(code);
                    break;
                case 3:
                    Choices.listChoices(choices);
                    deleteMeetingCentre(code);
                    break;

                default:
                    return;
            }

        }
    }

    public void addMeetingCentre() {
        String code;
        String name;
        String descr;
        System.out.println("Add MC wizard started, to stop this wizard, type anytime !cancel");
        code = Choices.getInput("Enter unique code of new MC: ");
        while (meetingCentres.containsKey(code) || code.isEmpty()) {
            System.out.println("You entered used or invalid code, please provide unused one");
            code = Choices.getInput("Enter unique code of new MC again: ");
        }
        if (code.equals("!cancel")) {
            System.out.println("You canceled adding new MC");
        } else {
            do {
                name = Choices.getInput("Enter name of meeting center: ");
            } while (name.isEmpty());
            if (name.equals("!cancel")) {
                System.out.println("You canceled adding new MC");
            } else {
                do {
                    descr = Choices.getInput("Enter description of meeting center: ");
                } while (descr.isEmpty());
                if (descr.equals("!cancel")) {
                    System.out.println("You canceled adding new MC");
                } else {
                    meetingCentres.put(code, new MeetingCentre(new LinkedHashMap<String, MeetingRoom>(), name, code, descr));
                    System.out.println("Meeting centre " + name + " added into in-app data");
                }
            }
        }

    }

    public void showMeetingCentreDetails(String input) {
        String locationFilter = input;

        if (input.isEmpty()) {
            locationFilter = Choices.getInput("Enter now name of MeetingCentre: ");
        }
        if (meetingCentres.containsKey(locationFilter)) {
            System.out.println(" -> " + locationFilter);
            System.out.println("  -> " + meetingCentres.get(locationFilter).getName());
            System.out.println("  -> " + meetingCentres.get(locationFilter).getDescription());

            List<String> choices = new ArrayList<>();
            choices.add("Show meeting rooms");
            choices.add("Add meeting room");
            choices.add("Edit details");
            choices.add("Remove room");
            choices.add("Go Back");

            while (true) {
                switch (Choices.getChoice("Select an option: ", choices)) {
                    case 1:
                        showMeetingRoomDetails(locationFilter);
                        break;
                    case 2:
                        addMeetingRoom(locationFilter);
                        break;
                    case 3:
                        editMeetingRoom(locationFilter);
                        break;
                    case 4:
                        removeMeetingRoom(locationFilter);
                        break;
                    case 5:
                        return;
                    default:
                        break;
                }
            }
        } else {
            System.out.println("");
            System.out.println("This meeting centre is not present in in-app data!");
            System.out.println("");
        }

    }

    public void deleteMeetingCentre(String input) {
        String locationFilter = input;

        if (input.isEmpty()) {
            locationFilter = Choices.getInput("Enter now name of MeetingCentre: ");
        }
        //TODO: doplnit vymazani meeting centra a jeho mistnosti a vypsani potvrzeni o smazani
        if (meetingCentres.containsKey(locationFilter)) {
            System.out.println("");
            System.out.println("Are you sure, you want to erase center:" + locationFilter + "?");
            System.out.println("This can be reverted by typing !cancel, putting negative answer or non-sense");
            System.out.println("");
            String decision = Choices.getInput("Write YES or y or a or true to perform delete action: ");

            if (Convertors.convertWordToBoolean(decision)) {
                meetingCentres.remove(locationFilter);
                System.out.println("");
                System.out.println("This meeting center and children rooms were successfully removed");
                System.out.println("");
            } else {
                System.out.println("");
                System.out.println("This meeting center was not deleted");
                System.out.println("");
            }

        } else {
            System.out.println("");
            System.out.println("This centre is not present in in-app data!");
            System.out.println("No data change was performed");
            System.out.println("");
        }
    }

    public void editMeetingCentre(String input) {

        String locationFilter = input;

        if (input.isEmpty()) {
            locationFilter = Choices.getInput("Enter now name of MeetingCentre: ");
        }

        if (meetingCentres.containsKey(locationFilter)) {

            List<String> choices = new ArrayList<>();
            choices.add("Edit all values");
            choices.add("Edit single value");
            choices.add("Cancel edit");

            switch (Choices.getChoice("Select an option: ", choices)) {
                case 1:
                    alterAllValuesInMeetingCentre(locationFilter);
                    break;
                case 2:
                    alterValueInMeetingCentreWithChoicer(locationFilter);
                    break;
                default:
                    break;
            }
        } else {
            System.out.println("");
            System.out.println("This centre is not present in in-app data!");
            System.out.println("No data was altered");
            System.out.println("");
        }

    }

    private void alterAllValuesInMeetingCentre(String locationFilter) {

        alterMeetingCenterCode(locationFilter);
        alterMeetingCenterName(locationFilter);
        alterMeetingCenterDescription(locationFilter);

    }

    private void alterValueInMeetingCentreWithChoicer(String locationFilter) {
        List<String> choices = new ArrayList<>();
        choices.add("Edit Meeting centre name");
        choices.add("Edit Meeting centre description");
        choices.add("Edit Meeting centre code");
        choices.add("Edit nothing");

        switch (Choices.getChoice("Select an option: ", choices)) {

            case 1:
                alterMeetingCenterName(locationFilter);
                break;
            case 2:
                alterMeetingCenterDescription(locationFilter);
                break;
            case 3:
                alterMeetingCenterCode(locationFilter);
                break;
            case 4:
                return;
            default:
                break;

        }

    }

    private boolean checkCodeAvailability(String id) {
        return meetingCentres.containsKey(id);
    }

    private void alterMeetingCenterCode(String locationFilter) {
        String newMcCode = Choices.getInput("Enter now new name (If you leave this blank, same or you hit same code, no data will be altered)");
        if (meetingCentres.get(locationFilter).getCode().equals(newMcCode) || newMcCode.isEmpty() || checkCodeAvailability(newMcCode)) {
            System.out.println("Input was blank, same, or code was occupied, this parameter was not altered");
        } else {
            MeetingCentre mcTemp = meetingCentres.get(locationFilter);
            mcTemp.setCode(newMcCode);
            Map<String, MeetingRoom> temp = mcTemp.getMeetingRooms();
            for (Map.Entry<String, MeetingRoom> entry : temp.entrySet()) {
                MeetingRoom value = entry.getValue();
                value.setMeetingCentre(meetingCentres.get(locationFilter));
            }

            meetingCentres.remove(locationFilter);
            meetingCentres.put(newMcCode, mcTemp);
            System.out.println("Meeting centre code was altered");
            System.out.println("Meeting rooms belonging to this MC were altered");

        }

    }

    private void alterMeetingCenterName(String locationFilter) {
        String newMcName = Choices.getInput("Enter now new name (If you leave this blank or same, no data will be altered)");
        if (meetingCentres.get(locationFilter).getName().equals(newMcName) || newMcName.isEmpty()) {
            System.out.println("Input was blank or same, this parameter was not altered");
        } else {
            meetingCentres.get(locationFilter).setName(newMcName);
            System.out.println("Meeting centre name altered");
        }
    }

    private void alterMeetingCenterDescription(String locationFilter) {
        String newMcDescr = Choices.getInput("Enter now new description (If you leave this blank or same, no data will be altered)");
        if (meetingCentres.get(locationFilter).getDescription().equals(newMcDescr) || newMcDescr.isEmpty()) {
            System.out.println("Input was blank or same, this parameter was not altered");
        } else {
            meetingCentres.get(locationFilter).setDescription(newMcDescr);
            System.out.println("Meeting centre description altered");
        }
    }

    public void addMeetingRoom(String center) {

        System.out.println("Add room to MC wizard started. To stop this wizard, type anytime !cancel keyword");

        String code;
        String name;
        String descr;
        String video;
        String capacity = "";

        Integer capacityI = null;

        do {
            code = Choices.getInput("Enter meeting room code (required unique): ");
            if (meetingCentres.get(center).getMeetingRooms().containsKey(code)) {
                System.out.println("This room already exists, please enter unique code!");
            }
        } while (code.isEmpty() || meetingCentres.get(center).getMeetingRooms().containsKey(code));
        if (code.equals("!cancel")) {
            System.out.println("Add room wizard stopped");
        } else {
            do {
                name = Choices.getInput("Enter meeting room name (required): ");
            } while (name.isEmpty());
            if (name.equals("!cancel")) {
                System.out.println("Add room wizard stopped");
            } else {
                do {
                    descr = Choices.getInput("Enter meeting room description (required): ");
                } while (descr.isEmpty());
                if (descr.equals("!cancel")) {
                    System.out.println("Add room wizard stopped");
                } else {
                    do {
                        video = Choices.getInput("Enter video capability of room (Required) (Values Y,A,YES,TRUE are considered as yes, others as no): ");
                    } while (video.isEmpty());
                    if (video.equals("!cancel")) {
                        System.out.println("Add room wizard stopped");
                    } else {
                        do {
                            try {
                                capacity = Choices.getInput("Enter meeting room capacity (Only positive numbers are accepted): ");
                                if (capacity.equals("!cancel")) {
                                    break;
                                }
                                capacityI = Integer.parseInt(capacity);
                            } catch (NumberFormatException nfe) {
                            }
                        } while (isGivenCapacityInvalid(capacityI));
                    }
                    if (capacity.equals("!cancel")) {
                        System.out.println("Add room wizard stopped");
                    } else {
                        Boolean videoB = Convertors.convertWordToBoolean(video);
                        meetingCentres.get(center).getMeetingRooms().put(code, new MeetingRoom(capacityI, videoB, meetingCentres.get(center), name, code, descr));
                        System.out.println("New meeting room added into " + center);
                    }
                }
            }
        }

    }

    /*
    This method checks, if Integer is not null. If not, then if it is positive
    Returns true if there null or negative value
    Returns false if there is everything OK :)
     */
    private boolean isGivenCapacityInvalid(Integer number) {
        boolean invalid = true;
        if (number != null) {
            if (number > 0) {
                invalid = false;
            }
        }
        if (invalid) {
            System.out.println("Only positive number can be accepted, please retry or type !cancel to abort creation of MR");
        }
        return invalid;
    }

    public void showMeetingRoomDetails(String center) {
        Map<String, MeetingRoom> temp = meetingCentres.get(center).getMeetingRooms();

        for (Map.Entry<String, MeetingRoom> entry : temp.entrySet()) {
            System.out.println(" -> " + entry.getKey());
            System.out.println("   -> " + entry.getValue().getName());
            System.out.println("   -> " + entry.getValue().getDescription());
            System.out.println("   -> " + entry.getValue().getCapacity());
            System.out.println("   -> " + Convertors.convertBooleanToWord(entry.getValue().HasVideoConference()));
            System.out.println("   -> " + entry.getValue().getMeetingCentre().getName());
            System.out.println("   -> " + entry.getValue().getMeetingCentre().getCode());

        }

    }

    public void editMeetingRoom(String center) {
        String choice = Choices.getInput("Enter code of meeting room to be edited: ");

        if (meetingCentres.get(center).getMeetingRooms().containsKey(choice)) {

            List<String> choices = new ArrayList<>();
            choices.add("Edit all values");
            choices.add("Edit single value");
            choices.add("Cancel edit");

            switch (Choices.getChoice("Select an option: ", choices)) {
                case 1:
                    alterAllValuesInMeetingRoom(center, choice);
                    break;
                case 2:
                    alterValueInMeetingRoomWithChoicer(center, choice);
                    break;
                default:
                    break;
            }

        } else {
            System.out.println("");
            System.out.println("This room is not present in in-app data!");
            System.out.println("No data was altered");
            System.out.println("");
        }
    }

    private void alterValueInMeetingRoomWithChoicer(String center, String choice) {
        List<String> choices = new ArrayList<>();
        choices.add("Edit Meeting room name");
        choices.add("Edit Code of Meeting room");
        choices.add("Edit Meeting room description");
        choices.add("Edit Meeting room capacity");
        choices.add("Edit Meeting room video projection capability");
        choices.add("Edit nothing");

        switch (Choices.getChoice("Select an option: ", choices)) {

            case 1:
                alterMeetingRoomName(center, choice);
                break;
            case 2:
                alterMeetingRoomCode(center, choice);
                break;
            case 3:
                alterMeetingRoomDescription(center, choice);
                break;
            case 4:
                alterMeetingRoomCapacity(center, choice);
                break;
            case 5:
                alterMeetingRoomVideoProjectionCapability(center, choice);
                break;
            case 6:
                return;
            default:
                break;

        }
    }

    private void alterAllValuesInMeetingRoom(String center, String choice) {
        alterMeetingRoomName(center, choice);
        alterMeetingRoomCode(center, choice);
        alterMeetingRoomDescription(center, choice);
        alterMeetingRoomCapacity(center, choice);
        alterMeetingRoomVideoProjectionCapability(center, choice);
    }

    private void alterMeetingRoomName(String center, String choice) {
        String newMrName = Choices.getInput("Enter now new name (If you leave this blank or same, no data will be altered)");
        if (meetingCentres.get(center).getMeetingRooms().get(choice).getName().equals(newMrName) || newMrName.isEmpty()) {
            System.out.println("Input was blank or same, this parameter was not altered");
        } else {
            meetingCentres.get(center).getMeetingRooms().get(choice).setName(newMrName);
            System.out.println("Meeting centre name altered");
        }
    }

    private boolean checkMeetingRoomCodeAvailability(String center, String choice) {
        return meetingCentres.get(center).getMeetingRooms().containsKey(choice);
    }

    private void alterMeetingRoomCode(String center, String choice) {
        String newMrCode = Choices.getInput("Enter now new code (If you leave this blank or same, no data will be altered)");
        System.out.println(meetingCentres.get(center).getMeetingRooms().get(choice).getCode());

        if (meetingCentres.get(center).getMeetingRooms().get(choice).getCode().equals(newMrCode) || newMrCode.isEmpty() || checkMeetingRoomCodeAvailability(center, newMrCode)) {
            System.out.println("Input was blank or same, this parameter was not altered");
        } else {
            MeetingRoom mrTemp = meetingCentres.get(center).getMeetingRooms().get(choice);
            mrTemp.setCode(newMrCode);
            meetingCentres.get(center).getMeetingRooms().remove(choice);
            meetingCentres.get(center).getMeetingRooms().put(newMrCode, mrTemp);
            System.out.println("Meeting room code altered");
        }
    }

    private void alterMeetingRoomDescription(String center, String choice) {
        String newMrDescription = Choices.getInput("Enter now new description (If you leave this blank or same, no data will be altered)");
        if (meetingCentres.get(center).getMeetingRooms().get(choice).getDescription().equals(newMrDescription) || newMrDescription.isEmpty()) {
            System.out.println("Input was blank or same, this parameter was not altered");
        } else {
            meetingCentres.get(center).getMeetingRooms().get(choice).setDescription(newMrDescription);
            System.out.println("Meeting room description altered");
        }
    }

    private void alterMeetingRoomCapacity(String center, String choice) {
        try {
            Integer newMrCapacity = Integer.parseInt(Choices.getInput("Enter now new description (If you leave this blank or same, no data will be altered)"));
            if (meetingCentres.get(center).getMeetingRooms().get(choice).getCapacity() == newMrCapacity) {
                System.out.println("Input was blank or same, this parameter was not altered");
            } else {
                meetingCentres.get(center).getMeetingRooms().get(choice).setCapacity(newMrCapacity);
                System.out.println("Meeting room capacity altered");
            }
        } catch (NumberFormatException nfe) {
            System.out.println("Input was invalid, value not altered");
        }

    }

    private void alterMeetingRoomVideoProjectionCapability(String center, String choice) {
        System.out.println("Accepted keywords: YES,NO,Y,N,A,N,true,false");
        String newVidCap = Choices.getInput("Enter now room video capability (If you leave this blank or same, no data will be altered)");
        boolean newVidCapB = Convertors.convertWordToBoolean(newVidCap);
        if (meetingCentres.get(center).getMeetingRooms().get(choice).HasVideoConference() == newVidCapB || newVidCap.isEmpty()) {
            System.out.println("Input was blank or same, this parameter was not altered");
        } else {
            meetingCentres.get(center).getMeetingRooms().get(choice).setHasVideoConference(newVidCapB);
            System.out.println("Meeting room video capability altered");
        }
    }

    public void removeMeetingRoom(String center) {
        String choice = Choices.getInput("Enter code of meeting room to be erased: ");

        if (meetingCentres.get(center).getMeetingRooms().containsKey(choice)) {
            System.out.println("");
            System.out.println("Are you sure, you want to erase room: " + choice + " ?");
            System.out.println("This can be reverted by typing !cancel, putting negative answer or non-sense");
            System.out.println("");
            String decision = Choices.getInput("Write YES or y or a or true to perform delete action: ");

            if (decision.equalsIgnoreCase("y") || decision.equalsIgnoreCase("a") || decision.equalsIgnoreCase("true") || decision.equalsIgnoreCase("yes")) {
                meetingCentres.get(center).getMeetingRooms().remove(choice);
                System.out.println("");
                System.out.println("This meeting room was successfully removed");
                System.out.println("");
            } else {
                System.out.println("");
                System.out.println("This meeting room was not deleted");
                System.out.println("No data change was performed");
                System.out.println("");
            }

        } else {
            System.out.println("");
            System.out.println("This room is not present in in-app data!");
            System.out.println("No data change was performed");
            System.out.println("");
        }
    }

    public void printAllMeetingCentres() {
        for (Map.Entry<String, MeetingCentre> entry : meetingCentres.entrySet()) {
            System.out.println(entry.getKey());
        }
    }

    //For now is used for gCSV file output
    @Deprecated
    public List<String[]> provideDataForSave() {
        List<String[]> radkyNaExport = new ArrayList<>();

        radkyNaExport.add(new String[]{"MEETING_CENTRES", "", "", "", "", ""});
        for (Map.Entry<String, MeetingCentre> entry : meetingCentres.entrySet()) {
            String mcName = entry.getValue().getName();
            String mcCode = entry.getValue().getCode();
            String mcDescription = entry.getValue().getDescription();
            radkyNaExport.add(new String[]{mcName, mcCode, mcDescription, "", "", ""});
        }
        radkyNaExport.add(new String[]{"MEETING_ROOMS", "", "", "", "", ""});
        for (Map.Entry<String, MeetingCentre> entry : meetingCentres.entrySet()) {
            Map<String, MeetingRoom> tmp = entry.getValue().getMeetingRooms();
            for (Map.Entry<String, MeetingRoom> innerEntry : tmp.entrySet()) {
                String mrName = innerEntry.getValue().getName();
                String mrCode = innerEntry.getValue().getCode();
                String mrDescription = innerEntry.getValue().getDescription();
                String mrCapacity = innerEntry.getValue().getCapacity() + "";
                String mrHasVideoConf = Convertors.convertBooleanToWord(innerEntry.getValue().HasVideoConference());
                String mrMc = innerEntry.getValue().getMeetingCentre().getCode();
                radkyNaExport.add(new String[]{mrName, mrCode, mrDescription, mrCapacity, mrHasVideoConf, mrMc});
            }
        }
        return radkyNaExport;

    }

    /**
     * This method provides list of strings, that can be easily serialized. For
     * this program it is inteded mainly for MD5 generation, so we can tell if
     * user has altered in-app data and inform him, if he exits app.
     * 
     * It could be used just XML output or implement correct serialization,
     * however this would be slower, so simplifying CSV output generator was
     * easier and faster way to do this
     *
     * @return Provides list of Strings generated from main meetingcontroller
     * Map. Each row represents each object's params concatenated gaplessly
     * to next one. So every slight change will lead to difference, which will
     * be recognised when creating MD5 hash. This data is not intended to be
     * used anywhere else than Hash generating
     *
     */
    public List<String> provideDataForHashGenerator() {
        List<String> serializeFriendlyData = new ArrayList<>();
        // MEETING CENTRES
        for (Map.Entry<String, MeetingCentre> entry : meetingCentres.entrySet()) {
            String mcName = entry.getValue().getName();
            String mcCode = entry.getValue().getCode();
            String mcDescription = entry.getValue().getDescription();
            serializeFriendlyData.add(mcName + mcCode + mcDescription);
        }
        // MEETING ROOMS
        for (Map.Entry<String, MeetingCentre> entry : meetingCentres.entrySet()) {
            Map<String, MeetingRoom> tmp = entry.getValue().getMeetingRooms();
            for (Map.Entry<String, MeetingRoom> innerEntry : tmp.entrySet()) {
                String mrName = innerEntry.getValue().getName();
                String mrCode = innerEntry.getValue().getCode();
                String mrDescription = innerEntry.getValue().getDescription();
                String mrCapacity = innerEntry.getValue().getCapacity() + "";
                String mrHasVideoConf = Convertors.convertBooleanToWord(innerEntry.getValue().HasVideoConference());
                String mrMc = innerEntry.getValue().getMeetingCentre().getCode();
                serializeFriendlyData.add(mrName + mrCode + mrDescription + mrCapacity + mrHasVideoConf + mrMc);
            }
        }
        // RESERVATIONS
        for (Map.Entry<String, MeetingCentre> entry : meetingCentres.entrySet()) {
            Map<String, MeetingRoom> tmp = entry.getValue().getMeetingRooms();
            for (Map.Entry<String, MeetingRoom> innerEntry : tmp.entrySet()) {
                List<Reservation> reservations = innerEntry.getValue().getReservations();
                for (Reservation reservation : reservations) {
                    serializeFriendlyData.add(reservation.getMeetingRoom().getCode());
                    serializeFriendlyData.add(reservation.getDate().toString());
                    serializeFriendlyData.add(reservation.getTimeFrom());
                    serializeFriendlyData.add(reservation.getTimeTo());
                    serializeFriendlyData.add(reservation.getExpectedPersonCount()+"");
                    serializeFriendlyData.add(reservation.getCustomer());
                    serializeFriendlyData.add(reservation.isNeedVideoConference()+"");
                    serializeFriendlyData.add(reservation.getNote());
                }
            }
        }
        return serializeFriendlyData;
    }





    public Map<String, MeetingCentre> getMeetingCentres() {
        return meetingCentres;
    }

    public void setMeetingCentres(Map<String, MeetingCentre> meetingCentres) {
        this.meetingCentres = meetingCentres;
    }
}
