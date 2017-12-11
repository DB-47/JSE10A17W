package cz.unicorncollege.bt.model;

import java.io.Serializable;
import java.util.Map;

public class MeetingCentre  extends MeetingObject implements Serializable{
	private Map<String, MeetingRoom> meetingRooms;

    public MeetingCentre(Map<String, MeetingRoom> meetingRooms, String name, String code, String description) {
        super(name, code, description);
        this.meetingRooms = meetingRooms;
    }

	public Map<String, MeetingRoom> getMeetingRooms() {
		return meetingRooms;
	}

	public void setMeetingRooms(Map<String, MeetingRoom> meetingRooms) {
		this.meetingRooms = meetingRooms;
	}
}
