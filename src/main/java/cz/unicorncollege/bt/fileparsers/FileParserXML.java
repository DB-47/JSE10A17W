/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.unicorncollege.bt.fileparsers;

import cz.unicorncollege.bt.model.MeetingCentre;
import cz.unicorncollege.bt.model.MeetingRoom;
import cz.unicorncollege.bt.model.Reservation;
import cz.unicorncollege.bt.utils.Choices;
import cz.unicorncollege.bt.utils.Convertors;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/**
 * Class for parsing XML files for this app and saving in-app data back into
 * XML file. Both reader and writer are implemented as SAX. SAX is significantly
 * faster than DOM, but much harder to be coded
 * 
 * @author DB-47-PG
 */
public class FileParserXML {

    public static final String DEFAULT_FILE_PATH = "importData.xml";

    public static void saveData(Map<String, MeetingCentre> data) {
        OutputStream outputStream;
        XMLStreamWriter out;
        try {
            outputStream = new FileOutputStream(new File(DEFAULT_FILE_PATH));
            out = XMLOutputFactory.newInstance().createXMLStreamWriter(
                    new OutputStreamWriter(outputStream));

            out.writeStartDocument();
            out.writeStartElement("XA02-04");
            out.writeStartElement("MeetingCentres");
            for (Map.Entry<String, MeetingCentre> entry : data.entrySet()) {
                out.writeStartElement("meetingCenter");
                //
                String key = entry.getKey();
                MeetingCentre value = entry.getValue();
                //
                out.writeStartElement("name");
                out.writeCharacters(value.getName());
                out.writeEndElement();
                //
                out.writeStartElement("code");
                out.writeCharacters(key);
                out.writeEndElement();
                //
                out.writeStartElement("description");
                out.writeCharacters(value.getDescription());
                out.writeEndElement();
                //
                out.writeStartElement("MeetingRooms");
                for (Map.Entry<String, MeetingRoom> innerEntry : value.getMeetingRooms().entrySet()) {
                    //
                    out.writeStartElement("meetingRoom");
                    String innerKey = innerEntry.getKey();
                    MeetingRoom innerValue = innerEntry.getValue();
                    //
                    out.writeStartElement("name");
                    out.writeCharacters(innerValue.getName());
                    out.writeEndElement();
                    //
                    out.writeStartElement("code");
                    out.writeCharacters(innerKey);
                    out.writeEndElement();
                    //
                    out.writeStartElement("description");
                    out.writeCharacters(innerValue.getDescription());
                    out.writeEndElement();
                    //
                    out.writeStartElement("capacity");
                    out.writeCharacters(innerValue.getCapacity() + "");
                    out.writeEndElement();
                    //
                    out.writeStartElement("hasVideoConference");
                    out.writeCharacters(Convertors.convertBooleanToWord(innerValue.HasVideoConference()));
                    out.writeEndElement();
                    //
                    out.writeStartElement("Reservations");
                    for (Reservation r : innerValue.getReservations()) {
                        out.writeStartElement("reservation");
                        //
                        out.writeStartElement("date");
                        out.writeCharacters(r.getFormattedDate());
                        out.writeEndElement();
                        //
                        out.writeStartElement("from");
                        out.writeCharacters(r.getTimeFrom());
                        out.writeEndElement();
                        //
                        out.writeStartElement("to");
                        out.writeCharacters(r.getTimeTo());
                        out.writeEndElement();
                        //
                        out.writeStartElement("expectedPersonsCount");
                        out.writeCharacters(r.getExpectedPersonCount() + "");
                        out.writeEndElement();
                        //
                        out.writeStartElement("customer");
                        out.writeCharacters(r.getCustomer() + "");
                        out.writeEndElement();
                        //
                        out.writeStartElement("videoConference");
                        out.writeCharacters(Convertors.convertBooleanToWord(r.isNeedVideoConference()));
                        out.writeEndElement();
                        //
                        out.writeStartElement("note");
                        out.writeCharacters(r.getNote());
                        out.writeEndElement();
                        //
                        out.writeEndElement();
                    }
                    out.writeEndElement();
                    //
                    out.writeEndElement();
                }
                out.writeEndElement();
                //
                out.writeEndElement();
            }
            out.writeEndElement();
            out.writeEndElement();
            out.writeEndDocument();

            out.flush();
            out.close();

            System.out.println();
            System.out.println("**************************************************");
            System.out.println("-> Data was saved into default XML file correctly.");
            System.out.println("**************************************************");
            System.out.println();
        } catch (XMLStreamException | FileNotFoundException ex) {
            System.out.println();
            System.out.println("**************************************************");
            System.out.println("-> (!) Problems occoured during saving XML...");
            System.out.println("-> Error details " + ex.getMessage());
            System.out.println("**************************************************");
            System.out.println();
        }
    }

    /**
     * Method to load the data from file.
     *
     * @return
     */
    public static Map<String, MeetingCentre> loadDataFromFile(String fileName) {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader xsr = null;

        Map<String, MeetingCentre> allMeetingCentres = new LinkedHashMap<>();
        List<MeetingRoom> fetchedMeetingRooms = new ArrayList<>();
        List<Reservation> fetchedReservations = new ArrayList<>();

        try {
            xsr = factory.createXMLStreamReader(new FileReader(fileName));

            String actualElement = "";
            String context = "";

            String MCName = "";
            String MCCode = "";
            String MCDescription = "";

            String MRName = "";
            String MRCode = "";
            String MRDescription = "";
            String MRCapacity = "";
            String MRHasVideoConference = "";

            String ResDate = "";
            String ResFrom = "";
            String ResTo = "";
            String ResExpectedPersonCount = "";
            String ResCustomerName = "";
            String ResVideoConferenceNed = "";
            String ResNote = "";

            while (xsr.hasNext()) {

                // Start elementu
                if (xsr.getEventType() == XMLStreamConstants.START_ELEMENT) {
                    actualElement = xsr.getName().getLocalPart();
                    if (actualElement.equalsIgnoreCase("MEETINGCENTER")) {
                        context = "MEETINGCENTER";
                    } else if (actualElement.equalsIgnoreCase("MEETINGROOM")) {
                        context = "MEETINGROOM";
                    } else if (actualElement.equalsIgnoreCase("RESERVATION")) {
                        context = "RESERVATION";
                    }
                } // Načteme hodnotu elementu
                else if (xsr.getEventType() == XMLStreamConstants.CHARACTERS) {

                    if (actualElement.equalsIgnoreCase("NAME") && context.equalsIgnoreCase("MEETINGCENTER")) {
                        MCName = xsr.getText();
                    } else if (actualElement.equalsIgnoreCase("CODE") && context.equalsIgnoreCase("MEETINGCENTER")) {
                        MCCode = xsr.getText();
                    } else if (actualElement.equalsIgnoreCase("DESCRIPTION") && context.equalsIgnoreCase("MEETINGCENTER")) {
                        MCDescription = xsr.getText();
                    }

                    if (actualElement.equalsIgnoreCase("NAME") && context.equalsIgnoreCase("MEETINGROOM")) {
                        MRName = xsr.getText();
                    } else if (actualElement.equalsIgnoreCase("CODE") && context.equalsIgnoreCase("MEETINGROOM")) {
                        MRCode = xsr.getText();
                    } else if (actualElement.equalsIgnoreCase("DESCRIPTION") && context.equalsIgnoreCase("MEETINGROOM")) {
                        MRDescription = xsr.getText();
                    } else if (actualElement.equalsIgnoreCase("CAPACITY") && context.equalsIgnoreCase("MEETINGROOM")) {
                        MRCapacity = xsr.getText();
                    } else if (actualElement.equalsIgnoreCase("HASVIDEOCONFERENCE") && context.equalsIgnoreCase("MEETINGROOM")) {
                        MRHasVideoConference = xsr.getText();
                    }

                    if (actualElement.equalsIgnoreCase("DATE") && context.equalsIgnoreCase("RESERVATION")) {
                        ResDate = xsr.getText();
                    } else if (actualElement.equalsIgnoreCase("FROM") && context.equalsIgnoreCase("RESERVATION")) {
                        ResFrom = xsr.getText();
                    } else if (actualElement.equalsIgnoreCase("TO") && context.equalsIgnoreCase("RESERVATION")) {
                        ResTo = xsr.getText();
                    } else if (actualElement.equalsIgnoreCase("EXPECTEDPERSONSCOUNT") && context.equalsIgnoreCase("RESERVATION")) {
                        ResExpectedPersonCount = xsr.getText();
                    } else if (actualElement.equalsIgnoreCase("CUSTOMER") && context.equalsIgnoreCase("RESERVATION")) {
                        ResCustomerName = xsr.getText();
                    } else if (actualElement.equalsIgnoreCase("VIDEOCONFERENCE") && context.equalsIgnoreCase("RESERVATION")) {
                        ResVideoConferenceNed = xsr.getText();
                    } else if (actualElement.equalsIgnoreCase("NOTE") && context.equalsIgnoreCase("RESERVATION")) {
                        ResNote = xsr.getText();
                    }

                    actualElement = "";
                    // Konec elementu  
                } else if ((xsr.getEventType() == XMLStreamConstants.END_ELEMENT)) {
                    if (xsr.getName().getLocalPart().equalsIgnoreCase("MEETINGCENTER")) {
                        MeetingCentre mc = new MeetingCentre(new LinkedHashMap<String, MeetingRoom>(), MCName, MCCode, MCDescription);
                        for (MeetingRoom mr : fetchedMeetingRooms) {
                            mr.setMeetingCentre(mc);
                            mc.getMeetingRooms().put(mr.getCode(), mr);
                        }
                        allMeetingCentres.put(MCCode, mc);
                        fetchedMeetingRooms.clear();
                    } else if (xsr.getName().getLocalPart().equalsIgnoreCase("MEETINGROOM")) {
                        MeetingRoom mr = new MeetingRoom(Integer.parseInt(MRCapacity), Convertors.convertWordToBoolean(MRHasVideoConference), null, MRName, MRCode, MRDescription, fetchedReservations);
                        for (Reservation r : mr.getReservations()) {
                            r.setMeetingRoom(mr);
                            r.toString();
                        }
                        fetchedMeetingRooms.add(mr);
                        fetchedReservations.clear();
                    } else if (xsr.getName().getLocalPart().equalsIgnoreCase("RESERVATION")) {
                        Reservation r = new Reservation(null, Convertors.convertStringToDate(ResDate), ResFrom, ResTo, Integer.parseInt(ResExpectedPersonCount), ResCustomerName, Convertors.convertWordToBoolean(ResVideoConferenceNed), ResNote);
                        fetchedReservations.add(r);
                    }
                }
                xsr.next();
            }
            System.out.println("**************************************************");
            System.out.println("-> Data was loaded from XML file correctly.");
            System.out.println("**************************************************");
            System.out.println();
        } catch (FileNotFoundException | XMLStreamException e) {
            System.out.println("**************************************************");
            System.out.println("-> (!) Default file missing or is corrupted...");
            System.out.println("-> Error details " + e.getMessage());
            System.out.println("**************************************************");
        } finally {
            try {
                if (xsr != null) {
                    xsr.close();
                }
            } catch (XMLStreamException e) {
                System.out.println("**************************************************");
                System.out.println("-> (!) Error occoured during file closing...");
                System.out.println("-> Error details " + e.getMessage());
                System.out.println("**************************************************");
            }
        }
        return allMeetingCentres;
    }
    
    public static Map<String, MeetingCentre> loadDataFromFile() {
       return loadDataFromFile(DEFAULT_FILE_PATH);
    }

    public static Map<String, MeetingCentre> importData() {
        Map<String, MeetingCentre> allMeetingCentres = new LinkedHashMap<>();

        System.out.println("**************************************************");
        System.out.println("-> (i) Import will lead to loss of temporary in app data");
        System.out.println("-> (i) XML import supports whole data structure of app");
        System.out.println("-> Proceed with caution...");
        System.out.println("-> (i) You can type !cancel in case you triggered this accidentally");
        System.out.println("**************************************************");
        String locationFilter = Choices.getInput("Enter path of imported file: ");
        if (locationFilter.equalsIgnoreCase("!cancel")) {
            System.out.println();
            System.out.println("**************************************************");
            System.out.println("-> (i) You canceled successfully data import");
            System.out.println("**************************************************");
            System.out.println();
        } else {
            allMeetingCentres = loadDataFromFile(locationFilter);
            System.out.println();
            System.out.println("**************************************************");
            System.out.println("-> (i) Data was imported. " + allMeetingCentres.size() + " objects of MeetingCentres was loaded");
            System.out.println("**************************************************");
            System.out.println();
        }

        return allMeetingCentres;
    }

}
