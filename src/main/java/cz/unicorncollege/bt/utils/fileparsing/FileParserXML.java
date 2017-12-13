/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.unicorncollege.bt.utils.fileparsing;

import cz.unicorncollege.bt.model.MeetingCentre;
import cz.unicorncollege.bt.model.MeetingRoom;
import cz.unicorncollege.bt.model.Reservation;
import cz.unicorncollege.bt.utils.Convertors;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;

/**
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
                    new OutputStreamWriter(outputStream, "utf-8"));

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
            System.out.println("Data was saved into default XML file correctly.");
            System.out.println("**************************************************");
            System.out.println();
        } catch (XMLStreamException | UnsupportedEncodingException | FileNotFoundException ex) {
            Logger.getLogger(FileParserXML.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Method to load the data from file.
     *
     */
    public static Map<String, MeetingCentre> loadDataFromFile() {
        // TODO: nacist data z XML souboru
        Map<String, MeetingCentre> allMeetingCentres = new LinkedHashMap<>();

        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader xsr = null;

        try {
            xsr = factory.createXMLStreamReader(new FileReader(DEFAULT_FILE_PATH));

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

            String ResFrom = "";
            String ResTo = "";
            String ResExpectedPersonCount = "";
            String ResVideoConferenceNed = "";
            String ResNote = "";

            while (xsr.hasNext()) {

                // Start elementu
                if (xsr.getEventType() == XMLStreamConstants.START_ELEMENT) {
                    actualElement = xsr.getName().getLocalPart();
                    if (actualElement.equalsIgnoreCase("MEETINGCENTER")) {
                        context = "MEETINGCENTER";
                        System.out.println("MC PARSE START");
                    } else if (actualElement.equalsIgnoreCase("MEETINGROOM")) {
                        context = "MEETINGROOM";
                        System.out.println("MR PARSE START");
                    } else if (actualElement.equalsIgnoreCase("RESERVATION")) {
                        context = "RESERVATION";
                    }
                } // načítáme hodnotu elementu
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
                        System.out.println("--> " + MCCode);
                        System.out.println("--> " + MCName);
                        System.out.println("----> " + MRName);
                    } else if (actualElement.equalsIgnoreCase("CODE") && context.equalsIgnoreCase("MEETINGROOM")) {
                        MRCode = xsr.getText();
                        System.out.println("--> " + MCCode);
                        System.out.println("--> " + MCName);
                        System.out.println("----> " + MRCode);
                    } else if (actualElement.equalsIgnoreCase("DESCRIPTION") && context.equalsIgnoreCase("MEETINGROOM")) {
                        MRDescription = xsr.getText();
                        System.out.println("--> " + MCCode);
                        System.out.println("--> " + MCName);
                        System.out.println("----> " + MRDescription);
                    } else if (actualElement.equalsIgnoreCase("CAPACITY") && context.equalsIgnoreCase("MEETINGROOM")) {
                        MRCapacity = xsr.getText();
                        System.out.println("--> " + MCCode);
                        System.out.println("--> " + MCName);
                        System.out.println("----> " + MRCapacity);
                    } else if (actualElement.equalsIgnoreCase("HASVIDEOCONFERENCE") && context.equalsIgnoreCase("MEETINGROOM")) {
                        MRHasVideoConference = xsr.getText();
                        System.out.println("--> " + MCCode);
                        System.out.println("--> " + MCName);
                        System.out.println("----> " + MRHasVideoConference);
                    }

                    actualElement = "";
                    // Konec elementu  
                } else if ((xsr.getEventType() == XMLStreamConstants.END_ELEMENT)) {
                    if (xsr.getName().getLocalPart().equalsIgnoreCase("MEETINGCENTER")) {
                        MeetingCentre retrievedMC = new MeetingCentre(new LinkedHashMap<String, MeetingRoom>(), MCName, MCCode, MCDescription);
                        allMeetingCentres.put(MCCode, retrievedMC);
                        MCName = "";
                        MCCode = "";
                        MCDescription = "";
                        System.out.println("MC PARSE STOP");
                    } else if (xsr.getName().getLocalPart().equalsIgnoreCase("MEETINGROOM")) {
                        
                        MeetingRoom retrievedMr = new MeetingRoom
                        (Integer.parseInt(MRCapacity),
                        Convertors.convertWordToBoolean(MRHasVideoConference),
                        allMeetingCentres.get(MCCode),
                        MRName,
                        MRCode,
                        MRDescription);
                        
                        MRName = "";
                        MRCode = "";
                        MRDescription = "";
                        MRCapacity = "";
                        MRHasVideoConference = "";
                        System.out.println("MR PARSE STOP");
                    }
                }
                xsr.next();
            }
        } catch (FileNotFoundException | XMLStreamException e) {
            System.err.println("Chyba při čtení souboru: " + e.getMessage());
        } finally {
            try {
                if (xsr != null) {
                    xsr.close();
                }
            } catch (XMLStreamException e) {
                System.err.println("Chyba při uzavírání souboru: " + e.getMessage());
            }
        }

        System.out.println(allMeetingCentres);

        System.out.println();

        System.out.println("**************************************************");
        System.out.println("Data was loaded from default XML file correctly.");
        System.out.println("**************************************************");

        System.out.println();
        
        return allMeetingCentres;      
    }

    public static Map<String, MeetingCentre> importData() {

        Map<String, MeetingCentre> allMeetingCentres = new LinkedHashMap<>();
        return null;
    }

}
