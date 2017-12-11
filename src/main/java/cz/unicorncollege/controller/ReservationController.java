package cz.unicorncollege.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.unicorncollege.bt.model.MeetingCentre;
import cz.unicorncollege.bt.model.MeetingRoom;
import cz.unicorncollege.bt.model.Reservation;
import cz.unicorncollege.bt.model.ReservationConflictType;
import cz.unicorncollege.bt.utils.Choices;
import cz.unicorncollege.bt.utils.Convertors;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.lang3.time.DateUtils;

public class ReservationController {

    private MeetingController meetingController;
    private MeetingCentre actualMeetingCentre;
    private MeetingRoom actualMeetingRoom;
    private Date actualDate;

    //In my implementation you can create reservation at least n hours before its begin
    public static final int MINIMAL_TIME_MARGIN = 2; //Hours

    /**
     * Constructor for ReservationController class
     *
     * @param meetingController loaded data of centers and its rooms
     */
    public ReservationController(MeetingController meetingController) {
        this.meetingController = meetingController;
        //Original get date included day time, this method will exclude time of day
        this.actualDate = DateUtils.truncate(new Date(), java.util.Calendar.DAY_OF_MONTH);
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
        //Let's assume for now, is in conflict
        String[] times = verifyAndGetTimePeriodForReservation();

        String expectedPersonCountString;
        Integer expectedPersonCount;

        String customer;

        String videoConfString;
        Boolean videoConf;

        String note;

        if (!times[0].equals("!cancel")) {
            expectedPersonCountString = retrieveExpectedPersonCount();
            if (!expectedPersonCountString.equals("!cancel")) {
                expectedPersonCount = Integer.parseInt(expectedPersonCountString);
                String tempCustomer = retrieveCustomer();
                if (!tempCustomer.equals("!cancel")) {
                    customer = tempCustomer;
                    videoConfString = retrieveVideoConferenceRequirment();
                    if (!videoConfString.equals("!cancel")) {
                        videoConf = Convertors.convertWordToBoolean(videoConfString);
                        String tempNote = retrieveNote();
                        if (!tempNote.equals("!cancel")) {
                            note = tempNote;
                            Reservation r = new Reservation(actualMeetingRoom, actualDate, times[0], times[1], expectedPersonCount, customer, videoConf, note);
                            actualMeetingRoom.getReservations().add(r);
                            System.out.println("Reservation successfully added :)");
                        }
                    }
                }
            }
        }

    }

    private String retrieveNote() {
        String note = Choices.getInput("Enter note if necessary: ");
        return note;
    }

    private String retrieveVideoConferenceRequirment() {
        String videoConfString;
        do {
            videoConfString = Choices.getInput("Do you require video conference possiblity? (Y/N) ");
            if (videoConfString.equals("!cancel")) {
                return videoConfString;
            }
        } while (videoConfString.isEmpty() || verifyValidityOfVideoConference(videoConfString));
        return videoConfString;
    }

    private boolean verifyValidityOfVideoConference(String booleanValue) {
        if (!actualMeetingRoom.HasVideoConference() && Convertors.convertWordToBoolean(booleanValue)) {
            System.out.println("(!) This room does not support video conferences");
            System.out.println("(i) You may either do not require video conference or select other room");
            return true;
        }
        return false;
    }

    private String retrieveCustomer() {
        String customer;
        do {
            customer = Choices.getInput("Enter name of customer: ");
            if (customer.equals("!cancel")) {
                return customer;
            }
        } while (customer.isEmpty());
        return customer;
    }

    private String retrieveExpectedPersonCount() {
        String countString;
        do {
            countString = Choices.getInput("Enter count of persons using this reservation: ");
            if (countString.equals("!cancel")) {
                System.out.println("(i) Reservation add wizard stopped");
                return countString;
            }
        } while (countString.isEmpty() || !verifyExpectedPersonCount(countString));
        return countString;
    }

    private boolean verifyExpectedPersonCount(String gainedInput) {
        Integer result;
        try {
            result = Integer.parseInt(gainedInput);
        } catch (NumberFormatException nfe) {
            System.out.println("(!) Only numeric values are accepted");
            return false;
        }
        return result <= actualMeetingRoom.getCapacity() && result >= 1;
    }

    private String retrieveTime(String kind) {
        String time;
        do {
            time = Choices.getInput("Reservation " + kind + " at: ");
            if (time.equals("!cancel")) {
                return time;
            }
        } while (time.isEmpty() || Convertors.convertTimeStringToMinutesInt(time) == -1);
        return time;
    }

    private String[] verifyAndGetTimePeriodForReservation() {

        ReservationConflictType status = ReservationConflictType.BOTH;

        System.out.println("Add reservation wizard started. Type anytime !cancel to stop it");
        String[] blankResult = {"!cancel"};
        String timeFrom = "";
        String timeTo = "";

        while (status != ReservationConflictType.NONE) {
            if (status != ReservationConflictType.NONE) {
                do {
                    timeFrom = retrieveTime("begins");
                } while (timeFrom.isEmpty());
            }
            if (timeFrom.equals("!cancel")) {
                System.out.println("(i) Reservation add wizard stopped");
                break;
            } else {
                if (status != ReservationConflictType.NONE) {
                    do {
                        timeTo = retrieveTime("ends");
                    } while (timeTo.isEmpty());
                }
                if (timeTo.equals("!cancel")) {
                    System.out.println("(i) Reservation add wizard stopped");
                    break;
                }
            }
            if (areGivenTimesValid(timeFrom, timeTo)) {
                status = isGivenReservationTimeAvailable(timeFrom, timeTo);
                switch (status) {
                    case INITIAL:
                        System.out.println("(i) End of other reservation is overlapping with your one");
                        break;
                    case FINISH:
                        System.out.println("(i) Beginning of other reservation is overlapping with your one");
                        break;
                    case BOTH:
                        System.out.println("(i) Whole other reservation is overlapping with your one");
                        break;
                    case NONE:
                        System.out.println("(i) This time is available");
                        String[] results = new String[2];
                        results[0] = timeFrom;
                        results[1] = timeTo;
                        return results;
                }
            }
        }
        return blankResult;
    }

    private void deleteReservation() {
        System.out.println("You may exit by typing !cancel instead of number");
        if (!actualMeetingRoom.getReservationsWithIndexes(actualDate).isEmpty()) {
            Map<Integer, Reservation> availableReservations = actualMeetingRoom.getReservationsWithIndexes(actualDate);
            Map<Integer, Integer> choiceToId = new LinkedHashMap<>();
            List<String> choices = new ArrayList<>();

            int choiceIndex = 0;
            for (Map.Entry<Integer, Reservation> entry : availableReservations.entrySet()) {
                Integer key = entry.getKey();
                Reservation value = entry.getValue();
                choiceToId.put(choiceIndex, key);
                choices.add("DATE " + getFormattedDate() + " FROM " + value.getTimeFrom() + " TO " + value.getTimeTo());
                choiceIndex++;
            }
            Choices.listChoices(choices);

            Integer reservationChoice = null;
            do {
                // get the choice as string to parse it to integer later
                String chosenOption = Choices.getInput("Choose reservation number to erase: ");
                try {
                    if (chosenOption.equals("!cancel")) {
                        System.out.println("(i) You were redirected back to reservation wizard menu");
                        return;
                    } else {
                        reservationChoice = Integer.parseInt(chosenOption) - 1;
                    }
                    if (reservationChoice >= 0 && reservationChoice < choices.size()) {
                    } else {
                        System.out.println("(!) Invalid choice, type any number from 1 to " + choices.size());
                        reservationChoice = null;
                    }
                } catch (NumberFormatException nfe) {
                    System.out.println("(!) You must enter numeric value");
                }
            } while (reservationChoice == null);

            String eraseDecision = Choices.getInput("Are you sure, you want to erase this reservation? (type true,y,yes,a to confirm) ");
            if (Convertors.convertWordToBoolean(eraseDecision)) {
                int idForErase = choiceToId.get(reservationChoice);
                actualMeetingRoom.getReservations().remove(idForErase);
                System.out.println("This reservation was removed");
            } else {
                System.out.println("No data was changed");
            }
        } else {
            System.out.println("(i) Meeting room " + actualMeetingRoom.getName() + " has no reservation for this date: " + getFormattedDate());
        }
    }

    private void changeDate() {
        String dateString = Choices.getInput("Type date as (DD.MM.YYYY)");
        Date newDate = Convertors.convertStringToDate(dateString);

        if (newDate != null) {
            actualDate = newDate;
            System.out.println(actualDate);
        }

        System.out.println(getActualData());
        listReservationsByDate(actualDate, false);

    }

    private void getItemsToShow() {
        listReservationsByDate(actualDate, false);

        List<String> choices = new ArrayList<>();
        choices.add("Show room parameters");
        choices.add("List reservations again");
        choices.add("List reservations againg with details");
        choices.add("Add New Reservation (UNDER TEST)");
        choices.add("Edit Reservations (N/A)");
        choices.add("Delete Reservation");
        choices.add("Change Date");
        choices.add("Exit");

        while (true) {
            switch (Choices.getChoice("Select an option: ", choices)) {
                case 1:
                    actualMeetingRoom.printRoomParams();
                    break;
                case 2:
                    listReservationsByDate(actualDate, false);
                    break;
                case 3:
                    listReservationsByDate(actualDate, true);
                    break;
                case 4:
                    addNewReservation();
                    break;
                case 5:
                    editReservation();
                    break;
                case 6:
                    deleteReservation();
                    break;
                case 7:
                    changeDate();
                    break;
                case 8:
                    return;
            }
        }
    }

    private void listReservationsByDate(Date date, boolean moreDetails) {
        // list reservations
        List<Reservation> list = actualMeetingRoom.getSortedReservationsByDate(date);
        if (list != null && list.size() > 0) {
            System.out.println("");
            System.out.println("Reservations for " + getActualData());
            for (Reservation reserv : list) {
                System.out.println(reserv.getFormattedDate() + " FROM " + reserv.getTimeFrom() + " TO " + reserv.getTimeTo());
                if (moreDetails) {
                    System.out.println("-> " + "Expected person count is: " + reserv.getExpectedPersonCount());
                    System.out.println("-> " + "Customer name: " + reserv.getCustomer());
                    System.out.println("-> " + "Customer needs video conference? " + Convertors.convertBooleanToWord(reserv.isNeedVideoConference()));
                    System.out.println("-> " + "Note: " + reserv.getNote());
                }
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

    private boolean areGivenTimesValid(String t1, String t2) {
        int time1 = Convertors.convertTimeStringToMinutesInt(t1);
        int time2 = Convertors.convertTimeStringToMinutesInt(t2);

        if (time1 >= time2) {
            System.out.println("(!) You entered times in inverted order");
            return false;
        } else if (time2 - time1 < 15) {
            System.out.println("(!) Shortest possible reservation is at least 15 minutes");
            return false;
        } else {
            return true;
        }
    }

    /**
     * This method takes initial and finish time of new reservation and verifies
     * if wanted time is available. Available time is time, where no other
     * reservation is planned
     *
     * @param t1 Initial time as minutes from 00:00
     * @param t2 Finish time as minutes from 00:00
     *
     * @return true if reservation makes no conflicts, otherwise false
     */
    private ReservationConflictType isGivenReservationTimeAvailable(String t1, String t2) {
        int newInitialTime = Convertors.convertTimeStringToMinutesInt(t1);
        int newFinishTime = Convertors.convertTimeStringToMinutesInt(t2);
        for (Reservation r : actualMeetingRoom.getSortedReservationsByDate(actualDate)) {
            int existingInitialTime = Convertors.convertTimeStringToMinutesInt(r.getTimeFrom());
            int existingFinishTime = Convertors.convertTimeStringToMinutesInt(r.getTimeTo());
            //Does initial time of already saved reservation belong to new Reservation?
            boolean existingReservationOverlapAtItsBegin = (newInitialTime >= existingInitialTime)
                    && (newInitialTime <= existingFinishTime);
            //Does finish time of already saved reservation belong to new Reservation?
            boolean existingReservationOverlapAtItsEnd = (newFinishTime >= existingInitialTime)
                    && (newFinishTime <= existingFinishTime);
            //If at least one of those timestamps of existing reservation belong to new one, it must be conflict!
            if (existingReservationOverlapAtItsBegin && !existingReservationOverlapAtItsEnd) {
                //Only finish time of reservation is overlapping, inform other method about this and stop for cycle
                return ReservationConflictType.FINISH;
            } else if (!existingReservationOverlapAtItsBegin && existingReservationOverlapAtItsEnd) {
                //Only initial time of reservation is overlapping, inform other method about this and stop for cycle
                return ReservationConflictType.INITIAL;
            } else if (existingReservationOverlapAtItsBegin && existingReservationOverlapAtItsEnd) {
                //Both times of new reservation failed
                return ReservationConflictType.BOTH;
            }
        }
        //You were lucky, no conflict was found :)
        return ReservationConflictType.NONE;
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

}
