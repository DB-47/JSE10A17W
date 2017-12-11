package cz.unicorncollege.bt.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;

/**
 * 
 * This class provides hashing functions, to get unique hash for object. 
 * In initial version are available default Java Hash generator 
 * All methods outputs their hashes as strings, so it depends on you
 * which method you will use. Just keep on mind to use one method at same
 * time. It is not good idea to generate MD5 hash on initial state of object
 * and later compare with Java Hash :)
 * 
 * 
 *
 * @author DB-47-PG
 */
public class HashGenerators {

    /**
     * This method creates from serializable object its MD5 hash fingerprint.
     *
     * @param object Any serializable object
     * 
     * @return 128 bit (32 char String) MD5 hash
     */
    public static String getMD5(Object object) {
        
        ObjectOutputStream oos = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos); 
            oos.writeObject(object);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(baos.toByteArray());
            return DatatypeConverter.printHexBinary(thedigest);
        } catch (IOException | NoSuchAlgorithmException ex) {
            Logger.getLogger(HashGenerators.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(oos != null){
                oos.close();
                }
            } catch (IOException ex) {
                Logger.getLogger(HashGenerators.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
        }
    
    /**
     * This method creates default int hash from built in java method .hashCode
     * 
     * @param object Any object
     *
     * @return Int hash converted to String
     */
    public static String getJavaHash(Object object){
       String javaHash = object.hashCode() + "";
       return javaHash;
    }

    }
