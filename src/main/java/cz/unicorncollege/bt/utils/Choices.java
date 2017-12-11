package cz.unicorncollege.bt.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

/**
 * Class with methods for listing choices for console UI menus and getting
 * input from user
 * 
 * @author UCL, DB-47
 */
public class Choices {

    public static void listChoices(List<String> choices) {
        for (int i = 0; i < choices.size(); i++) {
            System.out.println(" " + (i+1) + " - " + choices.get(i));
        }
    }

    /**
     * Method to get the user choice from some list of options.
     *
     * @param choiceText String - Information text about options.
     * @param choices List - list of options given to the user.
     * @return int - choosen option.
     */
    public static int getChoice(String choiceText, List<String> choices) {

        System.out.println(choiceText);
        for (int i = 0; i < choices.size(); i++) {
            System.out.println("  " + (i + 1) + " - " + choices.get(i));
        }

        System.out.print("> ");
        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));

        try {
            return Integer.parseInt(r.readLine().trim());
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * Method to get the response from the user, typicaly some text or another
     * data to fill in some object
     *
     * @param choiceText String - Info about what to enter.
     * @return String - user's answer.
     */
    public static String getInput(String choiceText) {
        String result = null;
        System.out.print(choiceText);

        BufferedReader r = new BufferedReader(new InputStreamReader(System.in));
        try {
            result = r.readLine().trim();
        } catch (Exception e) {
        }

        return result;
    }
    
  
}
