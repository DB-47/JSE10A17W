package cz.unicorncollege.bt.model;

import cz.unicorncollege.bt.utils.Convertors;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
    public MeetingRoom(int capacity, boolean hasVideoConference, MeetingCentre meetingCentre, String name, String code, String description) {
        super(name, code, description);
        this.capacity = capacity;
        this.hasVideoConference = hasVideoConference;
        this.meetingCentre = meetingCentre;
        //Third part
        this.reservations = new ArrayList<>();
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

    //Methods for third part
    public List<Reservation> getReservations() {
        return reservations;
    }

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
//TODO get reservations by date and return sorted reservations by hours
        for (Map.Entry<Integer, Reservation> entry : sortedReservations.entrySet()) {
            Integer key = entry.getKey();
            Reservation value = entry.getValue();
            sortedReservationsAsList.add(value);
        }

        return sortedReservationsAsList;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

}
