package cz.unicorncollege.bt.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Reservation implements Serializable{
	private MeetingRoom meetingRoom;
	private Date date;
	private String timeFrom;
	private String timeTo;
	private int expectedPersonCount;
	private String customer;
	private boolean needVideoConference;
	private String note;

    public Reservation(MeetingRoom meetingRoom, Date date, String timeFrom, String timeTo, int expectedPersonCount, String customer, boolean needVideoConference, String note) {
        this.meetingRoom = meetingRoom;
        this.date = date;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.expectedPersonCount = expectedPersonCount;
        this.customer = customer;
        this.needVideoConference = needVideoConference;
        this.note = note;
    }
          

	public MeetingRoom getMeetingRoom() {
		return meetingRoom;
	}

	public void setMeetingRoom(MeetingRoom meetingRoom) {
		this.meetingRoom = meetingRoom;
	}

	public Date getDate() {
		return date;
	}

	public String getFormattedDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		return sdf.format(date);
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTimeFrom() {
		return timeFrom;
	}

	public void setTimeFrom(String timeFrom) {
		this.timeFrom = timeFrom;
	}

	public String getTimeTo() {
		return timeTo;
	}

	public void setTimeTo(String timeTo) {
		this.timeTo = timeTo;
	}

	public int getExpectedPersonCount() {
		return expectedPersonCount;
	}

	public void setExpectedPersonCount(int expectedPersonCount) {
		this.expectedPersonCount = expectedPersonCount;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public boolean isNeedVideoConference() {
		return needVideoConference;
	}

	public void setNeedVideoConference(boolean needVideoConference) {
		this.needVideoConference = needVideoConference;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
}
