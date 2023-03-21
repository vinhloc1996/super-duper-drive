package com.udacity.superduperdrive.cloudstorage.dto;

import com.udacity.superduperdrive.cloudstorage.helper.UtilityMethod;

public class SecrectCredentialDto {
    private String salt;
    private String encryptedPassword;
    private String rawPassword;

    private void generateSecurity(){
        setSalt(UtilityMethod.GenerateSalt());
        setEncryptedPassword(UtilityMethod.EncryptPasswordWithSalt(getRawPassword(), getSalt()));
    }

    public SecrectCredentialDto(String rawPassword) {
        this.rawPassword = rawPassword;
        generateSecurity();
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    private void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

    public String getSalt() {
        return salt;
    }

    private void setSalt(String salt) {
        this.salt = salt;
    }

    private String getRawPassword() {
        return rawPassword;
    }
}
