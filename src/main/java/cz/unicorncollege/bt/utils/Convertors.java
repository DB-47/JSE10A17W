package cz.unicorncollege.bt.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * This class stores methods used for converting one thing to another thing,
 * which are reused in more places of this program. This is done in order to 
 * obey "Do not repeat yourself" good programming habbit.
 * 
 * For instance there are included methods for converting keywords representing
 * boolean to boolean, etc.
 *
 * @author DB-47
 */
public class Convertors {

    /**
     * This methods parses military time (24 hours) format to integer telling,
     * how many minutes elapsed since 00:00 of the given day.
     *
     * @param time String, containing HH:MM time format
     *
     * @return Minutes since 00:00 of given day. If any expection is caught,
     * value -1 is returned
     */
    public static int convertTimeStringToMinutesInt(String time) {
        String[] timeData = time.split(":");
        if (timeData.length != 2) {
            System.out.println("(!) You entered invalid string");
            return -1;
        } else {
            int hours;
            int minutes;
            try {
                hours = Integer.parseInt(timeData[0]);
                minutes = Integer.parseInt(timeData[1]);
                if (hours <= 23 & minutes <= 59) {
                    return hours * 60 + minutes;
                } else {
                    System.out.println("(!) This time is invalid");
                    return -1;
                }
            } catch (NumberFormatException nfe) {
                System.out.println("(!) Only numeric values as hours and minutes are accepted");
                return -1;
            }
        }
    }

    /**
     * This method generates Date instance from String input, parsed by
     * SimpleDateFormat with Czech date format dd.MM.yyyy
     *
     * @param datumString String representing real life date
     *
     * @return Date instance from provided string and Czech pattern for sdf
     */
    public static Date convertStringToDate(String datumString) {
        return convertStringToDate(datumString, "dd.MM.yyyy");
    }

    /**
     * This method generates Date instance from String input, parsed by
     * SimpleDateFormat with provided date format in second parameter
     *
     * @param datumString String representing real life date
     * @param sdfPattern String representing date format, like dd.MM.yyyy for
     * DMY countries
     *
     * @return Date instance from provided string and pattern for sdf in second
     * string
     */
    public static Date convertStringToDate(String datumString, String sdfPattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(sdfPattern);
        Date datum;
        try {
            datum = sdf.parse(datumString);
        } catch (ParseException ex) {
            System.out.println("(!) Invalid date format");
            return null;
        }
        return datum;
    }
    
    public static String convertDateToString(Date date, String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }
    
    public static String convertDateToString(Date date){
        return convertDateToString(date, "dd.MM.yyyy");
    }

    /**
     * This method converts typical keyword resembling possible true false
     * options as boolean true false values
     *
     * @param keyWord Input to be analysed for T/F value
     *
     * @return Boolean based on given keyword. If there is word not listed in if
     * statements, false is returned
     *
     */
    public static boolean convertWordToBoolean(String keyWord) {
        if (keyWord.equalsIgnoreCase("YES") || keyWord.equalsIgnoreCase("y") || keyWord.equalsIgnoreCase("a") || keyWord.equalsIgnoreCase("true")) {
            return true;
        } else if (keyWord.equalsIgnoreCase("NO") || keyWord.equalsIgnoreCase("n") || keyWord.equalsIgnoreCase("false") || keyWord.equalsIgnoreCase("!cancel")) {
            return false;
        } else {
            return false;
        }
    }

    /**
     * This method converts typical keyword resembling possible true false
     * options as boolean true false values. Here can user define, which word is
     * considered as true (boolean) and false (boolean)
     *
     * @param keyWord Input to be analysed for T/F value
     * @param wordForYes What word will be considered as true
     * @param wordForNo What word will be considered as true
     *
     * @return Boolean based on given keyword. If there is word not listed in if
     * statements, false is returned
     *
     */
    public static boolean convertWordToBoolean(String keyWord, String wordForYes, String wordForNo) {
        if (keyWord.equals(wordForYes)) {
            return true;
        } else if (keyWord.equals(wordForNo)) {
            return false;
        } else {
            return false;
        }
    }

    /**
     * This methods converts boolean value to default keywords resembling
     * boolean value
     *
     * @param bol Boolean value
     *
     * @return Keyword representing boolean
     */
    public static String convertBooleanToWord(boolean bol) {
        return convertBooleanToWord(bol, "YES", "NO");
    }

    /**
     * This methods converts boolean value to default keywords resembling
     * boolean value. This version of methods allows to specify, which words
     * (String) will be returned
     *
     * @param bol Boolean value
     * @param stringForTrue What word will be return if true (boolean) is given
     * @param stringForFalse What word will be return if false (boolean) is
     * given
     *
     * @return One of given String depending on given boolean value
     */
    public static String convertBooleanToWord(boolean bol, String stringForTrue, String stringForFalse) {
        if (bol) {
            return stringForTrue;
        } else {
            return stringForFalse;
        }
    }

}
