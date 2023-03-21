package com.udacity.superduperdrive.cloudstorage.helper;

import com.udacity.superduperdrive.cloudstorage.service.EncryptionService;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Base64;

@Component
public class UtilityMethod {
    public static boolean IsStringNullOrEmpty(String str, boolean isBlanked){
        if (str == null)
            return true;
        if (str.isEmpty())
            return true;
        if (isBlanked)
            if (str.isBlank())
                return true;

        return false;
    }

    public static boolean IsStringNullOrEmpty(String str){
        return IsStringNullOrEmpty(str, true);
    }

    public static String GenerateSalt(){
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }

    public static String EncryptPasswordWithSalt(String password, String salt){
        EncryptionService encryptionService = new EncryptionService();
        return encryptionService.encryptValue(password, salt);
    }

    public static String DecryptPasswordWithSalt(String encryptedPassword, String salt){
        EncryptionService encryptionService = new EncryptionService();
        return encryptionService.decryptValue(encryptedPassword, salt);
    }
}
