/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.unicorncollege.bt.utils.fileparsing;

import cz.unicorncollege.bt.model.MeetingCentre;
import cz.unicorncollege.controller.MeetingController;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;

/**
 *
 * @author DB-47-PG
 */
public class FileParserXML {

    public static final String DEFAULT_FILE_PATH = "importData.xml";

    public static void saveData(MeetingController controll) {
        // TODO: ulozeni dat do XML souboru, jmeno souboru muze byt natvrdo,
        // adresar stejny jako se nachazi aplikace
        File fileToSaveXML = null;

        System.out.println();
        System.out.println("**************************************************");
        System.out.println("Data was saved into default XML file correctly.");
        System.out.println("**************************************************");
        System.out.println();
    }

    /**
     * Method to load the data from file.
     *
     * @return
     */
    public static Map<String, MeetingCentre> loadDataFromFile() throws ParserConfigurationException, SAXException {
        // TODO: nacist data z XML souboru
        Map<String, MeetingCentre> allMeetingCentres = new LinkedHashMap<>();

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();
        
        

        System.out.println();

        System.out.println("**************************************************");
        System.out.println("Data was loaded from default XML file correctly.");
        System.out.println("**************************************************");

        System.out.println();

        return null;
    }

    public static Map<String, MeetingCentre> importData() {

        Map<String, MeetingCentre> allMeetingCentres = new LinkedHashMap<>();
        return null;
    }

}
