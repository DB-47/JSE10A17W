package cz.unicorncollege.bt.model;

import cz.unicorncollege.bt.utils.Convertors;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * This class represents meeting room. Meeting room is a object that belongs to
 * one Meeting Centre and Meeting Centre can have 0..N meeting rooms.
 *
 * @author UCL, DB-47
 */
public class MeetingRoom extends MeetingObject implements Serializable {

    private int capacity;
    private boolean hasVideoConference;
    private MeetingCentre meetingCentre;
    //Variables for third part
    private List<Reservation> reservations;

    /**
     * Creates meeting room with given parameters
     *
     * @param capacity How many persons can use this room at the moment.
     * @param code Unique code of meeting room.
     * @param description Gives additional info about meeting room
     * @param hasVideoConference Is there possibility to use this room for video
     * conferences?
     * @param name Name of meeting room. Does not have to be unique, but it
     * should be
     * @param meetingCentre Meeting centre object, to which this room belongs
     *
     */
    public MeetingRoom(int capacity, boolean hasVideoConference, MeetingCentre meetingCentre, String name, String code, String description, List<Reservation> reservations) {
        super(name, code, description);
        this.capacity = capacity;
        this.hasVideoConference = hasVideoConference;
        this.meetingCentre = meetingCentre;
        //Third part
        this.reservations = new ArrayList<>(reservations);
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean HasVideoConference() {
        return hasVideoConference;
    }

    public void setHasVideoConference(boolean hasVideoConference) {
        this.hasVideoConference = hasVideoConference;
    }

    public MeetingCentre getMeetingCentre() {
        return meetingCentre;
    }

    public void setMeetingCentre(MeetingCentre meetingCentre) {
        this.meetingCentre = meetingCentre;
    }

    /**
     * Writes into console all parameters of meeting room
     */
    public void printRoomParams() {
        System.out.println(name);
        System.out.println("-> Description: " + description);
        System.out.println("-> Capacity is: " + capacity);
        System.out.println("-> Room supports video conferences: " + Convertors.convertBooleanToWord(hasVideoConference));
    }

    //Methods for third part
    public List<Reservation> getReservations() {
        return reservations;
    }

    /**
     * Modified getter to get sorted reservations only within given date
     *
     * @param getSortedReservationsByDate Date filter for retrieving
     * reservations
     *
     * @return List of reservations for given date sorted by initial time
     */
    public List<Reservation> getSortedReservationsByDate(Date getSortedReservationsByDate) {
        //I am too lazy to implement sorting, let it TreeMap do it for me :)
        Map<Integer, Reservation> sortedReservations = new TreeMap<>();
        List<Reservation> sortedReservationsAsList = new ArrayList<>();

        for (Reservation reservation : reservations) {
            if (reservation.getDate().equals(getSortedReservationsByDate)) {
                Integer startTime = Convertors.convertTimeStringToMinutesInt(reservation.getTimeFrom());
                sortedReservations.put(startTime, reservation);
            }
        }
        for (Map.Entry<Integer, Reservation> entry : sortedReservations.entrySet()) {
            Integer key = entry.getKey();
            Reservation value = entry.getValue();
            sortedReservationsAsList.add(value);
        }

        return sortedReservationsAsList;
    }

    /**
     * Modified getter to get sorted reservations only within given date, but
     * with excluding one reservation (typically editted one), which we do not
     * want to consider as conflicting one. Your edited reservation cannot
     * conflict with "itself"
     *
     * @param getSortedReservationsByDate Date filter for retrieving
     * reservations
     * @param excludedReservationId if of resevation for exclude
     *
     * @return List of reservations for given date sorted by initial time
     */
    public List<Reservation> getSortedReservationsByDate(Date getSortedReservationsByDate, int excludedReservationId) {
        //I am too lazy to implement sorting, let it TreeMap do it for me :)
        Map<Integer, Reservation> sortedReservations = new TreeMap<>();
        List<Reservation> sortedReservationsAsList = new ArrayList<>();

        for (int i = 0; i < reservations.size(); i++) {
            if (reservations.get(i).getDate().equals(getSortedReservationsByDate) && i != excludedReservationId) {
                Integer startTime = Convertors.convertTimeStringToMinutesInt(reservations.get(i).getTimeFrom());
                sortedReservations.put(startTime, reservations.get(i));
            }
        }

        for (Map.Entry<Integer, Reservation> entry : sortedReservations.entrySet()) {
            Integer key = entry.getKey();
            Reservation value = entry.getValue();
            sortedReservationsAsList.add(value);
        }

        return sortedReservationsAsList;
    }

    /**
     * Method provides all reservations of such meeting room structured
     * chronologically by reservation date and initial time
     *
     * @return Map containing keys as dates and reservation lists, with all
     * reservations for such date. Both dates and times are sorted
     *
     */
    public Map<Date, List<Reservation>> retrieveReservationSortedByDateAndByTime() {
        Map<Date, List<Reservation>> allSortedReservations = new LinkedHashMap<>();
        //Let's get first all dates of reservations
        //Sort them
        //Get for each date it's reservations
        Set<Date> sortedDates = new TreeSet<>();
        for (Reservation r : reservations) {
            sortedDates.add(r.getDate());
        }
        for (Date sortedDate : sortedDates) {
            allSortedReservations.put(sortedDate, new ArrayList<Reservation>());
            List<Reservation> thisDateReservations = getSortedReservationsByDate(sortedDate);
            for (Reservation thisDateReservation : thisDateReservations) {
                allSortedReservations.get(sortedDate).add(thisDateReservation);
            }
        }
        return allSortedReservations;
    }

    /**
     * This is modified getter, which will retrieve all reservation within given
     * date. This getter also return reservation with its index in main List
     * with reservation, so external command can easily delete such reservation
     *
     * @param getReservationsWithIndexByDate Date, for which reservations will
     * be retrieved
     *
     * @return Returns map with integer key and reservation instance as value
     * This key tells, where certain reservation is stored in main reservation
     * List of MeetingRoom
     */
    public Map<Integer, Reservation> getReservationsWithIndexes(Date getReservationsWithIndexByDate) {
        Map<Integer, Reservation> collectedReservations = new HashMap<>();
        for (int i = 0; i < reservations.size(); i++) {
            if (reservations.get(i).getDate().equals(getReservationsWithIndexByDate)) {
                collectedReservations.put(i, reservations.get(i));
            }
        }
        return collectedReservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

}
