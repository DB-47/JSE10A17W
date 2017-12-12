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
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.xml.sax.SAXException;

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
                out.writeStartElement("came");
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
    public static void loadDataFromFile() {
        // TODO: nacist data z XML souboru
        Map<String, MeetingCentre> allMeetingCentres = new LinkedHashMap<>();

        XMLInputFactory factory = XMLInputFactory.newInstance();
        XMLStreamReader xsr = null;
        try {
            xsr = factory.createXMLStreamReader(new FileReader(DEFAULT_FILE_PATH));
            while (xsr.hasNext()) {
                
                
            }
        } catch (FileNotFoundException | XMLStreamException e) {
            System.out.println("(!) Error occoured during file parse: " + e.getMessage());

        } finally {
            try {
                if (xsr != null) {
                    xsr.close();
                }
            } catch (XMLStreamException e) {
                System.out.println("(!) Error during closing file: " + e.getMessage());
            }
        }

        System.out.println();

        System.out.println("**************************************************");
        System.out.println("Data was loaded from default XML file correctly.");
        System.out.println("**************************************************");

        System.out.println();

    }

    public static Map<String, MeetingCentre> importData() {

        Map<String, MeetingCentre> allMeetingCentres = new LinkedHashMap<>();
        return null;
    }

}
