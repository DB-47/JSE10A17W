package cz.unicorncollege.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.unicorncollege.bt.model.MeetingCentre;
import cz.unicorncollege.bt.model.MeetingRoom;
import cz.unicorncollege.bt.model.Reservation;
import cz.unicorncollege.bt.utils.Choices;
import cz.unicorncollege.bt.utils.Convertors;
import java.util.Map;

public class ReservationController {

    private MeetingController meetingController;
    private MeetingCentre actualMeetingCentre;
    private MeetingRoom actualMeetingRoom;
    private Date actualDate;

    /**
     * Constructor for ReservationController class
     *
     * @param meetingController loaded data of centers and its rooms
     */
    public ReservationController(MeetingController meetingController) {
        this.meetingController = meetingController;
        this.actualDate = new Date();
    }

    /**
     * Method to show options for reservations. Namely select meeting centre and
     * room and after that jump into reservation menu of selected meeting room
     *
     * So far ugly implementation :(
     */
    public void showReservationMenu() {

        List<String> choices = new ArrayList<>();
        List<String> availableMeetingCentres = new ArrayList<>();
        List<String> availableMeetingRooms = new ArrayList<>();

        // let them choose one of the loaded meeting centres 
        for (Map.Entry<String, MeetingCentre> en : meetingController.getMeetingCentres().entrySet()) {
            String key = en.getKey();
            MeetingCentre value = en.getValue();
            choices.add(key + " - " + value.getName());
            availableMeetingCentres.add(key);
        }

        if (availableMeetingCentres.isEmpty()) {
            System.out.println("(i) There are no Meeting centres");
            return;
        }

        System.out.println("Type !cancel anytime to abort reservation wizard");

        System.out.println("Available Meeting Centres");
        Choices.listChoices(choices);
        //SELECT MC
        Integer centreChoice = null;
        do {
            // get the choice as string to parse it to integer later
            String chosenOption = Choices.getInput("Choose the Meeting Centre: ");
            try {
                if (chosenOption.equals("!cancel")) {
                    System.out.println("(i) You were redirected back to main menu");
                    return;
                } else {
                    centreChoice = Integer.parseInt(chosenOption) - 1;
                }
                if (centreChoice >= 0 && centreChoice < availableMeetingCentres.size()) {
                    String mcCode = availableMeetingCentres.get(centreChoice);
                    actualMeetingCentre = meetingController.getMeetingCentres().get(mcCode);
                } else {
                    System.out.println("(!) Invalid choice, type any number from 1 to " + availableMeetingCentres.size());
                    centreChoice = null;
                }
            } catch (NumberFormatException nfe) {
                System.out.println("(!) You must enter numeric value");
            }
        } while (centreChoice == null);

        choices.clear();
        //Select MR

        Map<String, MeetingRoom> roomsOfActualMeetingCentre = meetingController.getMeetingCentres().get(actualMeetingCentre.getCode()).getMeetingRooms();

        for (Map.Entry<String, MeetingRoom> entry : roomsOfActualMeetingCentre.entrySet()) {
            String key = entry.getKey();
            MeetingRoom value = entry.getValue();
            choices.add(key + " - " + value.getName());
            availableMeetingRooms.add(key);
        }

        if (availableMeetingRooms.isEmpty()) {
            actualMeetingCentre = null;
            System.out.println("(i) This meeting centre has no meeting rooms");
            return;
        }

        System.out.println("Available Meeting Rooms");
        Choices.listChoices(choices);

        Integer roomChoice = null;
        do {
            String chosenOption = Choices.getInput("Choose the room: ");
            try {
                if (chosenOption.equals("!cancel")) {
                    actualMeetingCentre = null;
                    System.out.println("(i) You were redirected back to main menu");
                    return;
                }
                roomChoice = Integer.parseInt(chosenOption) - 1;
                if (roomChoice >= 0 && roomChoice < availableMeetingRooms.size()) {
                    String mrCode = availableMeetingRooms.get(roomChoice);
                    actualMeetingRoom = meetingController.getMeetingCentres().get(actualMeetingCentre.getCode()).getMeetingRooms().get(mrCode);
                } else {
                    System.out.println("(!) Invalid choice, type any number from 1 to " + availableMeetingRooms.size());
                    roomChoice = null;
                }
            } catch (NumberFormatException nfe) {
                System.out.println("(!) You must enter numeric value");
            }
        } while (roomChoice == null);

        choices.clear();

        System.out.println(getCentreAndRoomNames());
        getItemsToShow();
    }

    private void editReservation() {
        // TODO list reservation as choices, after chosen reservation edit all
        // relevant attributes
    }

    private void addNewReservation() {
        // TODO enter data one by one, add new reservation object to the actual
        // room, than inform about successful adding
    }

    private void deleteReservation() {
        // TODO list all reservations as choices and let enter item for
        // deletion, delete it and inform about successful deletion
    }

    private void changeDate() {
        // TODO let them enter new date in format YYYY-MM-DD, change the actual
        // date, list actual reservations on this date and menu by
        // getItemsToShow()
        String dateString = Choices.getInput("Type date as (DD.MM.YYYY)");
        Date newDate = Convertors.convertStringToDate(dateString);

        if (newDate != null) {
            actualDate = newDate;
            System.out.println(actualDate);
        }

        System.out.println(getActualData());
        listReservationsByDate(actualDate);

    }

    private void getItemsToShow() {
        listReservationsByDate(actualDate);

        List<String> choices = new ArrayList<>();
        choices.add("Edit Reservations");
        choices.add("Add New Reservation");
        choices.add("Delete Reservation");
        choices.add("Change Date");
        choices.add("Exit");

        while (true) {
            switch (Choices.getChoice("Select an option: ", choices)) {
                case 1:
                    editReservation();
                    break;
                case 2:
                    addFewTestReservations();
                    System.out.println("Some test reservations added");
                    break;
                case 3:
                    deleteReservation();
                    break;
                case 4:
                    changeDate();
                    break;
                case 5:
                    return;
            }
        }
    }

    private void listReservationsByDate(Date date) {
        // list reservations
        List<Reservation> list = actualMeetingRoom.getSortedReservationsByDate(date);
        if (list != null && list.size() > 0) {
            System.out.println("");
            System.out.println("Reservations for " + getActualData());
            for (Reservation reserv : list) {
                System.out.println(reserv.getFormattedDate() + " FROM " + reserv.getTimeFrom() + " TO " + reserv.getTimeTo());
            }
            System.out.println("");
        } else {
            System.out.println("");
            System.out.println("There are no reservation for " + getActualData());
            System.out.println("");
        }
    }

    /**
     * Method to get formatted actual date
     *
     * @return
     */
    private String getFormattedDate() {
        //Modified to standard DD.MM.RRRR czech date format
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        return sdf.format(actualDate);
    }
    /**
     * This method takes initial and finish time of new reservation and verifies
     * if wanted time is available. Available time is time, where no other reservation
     * is planned
     * 
     * @param t1 Initial time as String
     * @param t2 Finish time as String
     * 
     * @return true if reservation makes no conflicts, else false
     */
    private boolean verifyNewReservationTimeAvailabilty(String t1, String t2){
        
        return false;

    }

    /**
     * Method to get actual name of place - meeteng center and room
     *
     * @return
     */
    private String getCentreAndRoomNames() {
        return "MC: " + actualMeetingCentre.getName() + " , MR: " + actualMeetingRoom.getName();
    }

    /**
     * Method to get actual state - MC, MR, Date
     *
     * @return
     */
    private String getActualData() {
        return getCentreAndRoomNames() + ", Date: " + getFormattedDate();
    }

    //This will add few reservations into system until add wizard will be implemented
    private void addFewTestReservations() {
        Reservation r3 = new Reservation(actualMeetingRoom, actualDate, "17:15", "17:45", 8, "DL-17", false, "Third");
        Reservation r4 = new Reservation(actualMeetingRoom, actualDate, "19:00", "22:00", 11, "Magor", false, "Fourth");
        Reservation r1 = new Reservation(actualMeetingRoom, actualDate, "10:30", "11:00", 5, "DB-47", true, "First");
        Reservation r2 = new Reservation(actualMeetingRoom, actualDate, "14:30", "15:00", 2, "DK-44", false, "Second");

        actualMeetingRoom.getReservations().add(r1);
        actualMeetingRoom.getReservations().add(r2);
        actualMeetingRoom.getReservations().add(r3);
        actualMeetingRoom.getReservations().add(r4);
    }
}
