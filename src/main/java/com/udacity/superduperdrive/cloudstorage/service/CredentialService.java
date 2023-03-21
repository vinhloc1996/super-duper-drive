package com.udacity.superduperdrive.cloudstorage.service;

import com.udacity.superduperdrive.cloudstorage.dto.SecrectCredentialDto;
import com.udacity.superduperdrive.cloudstorage.helper.UtilityMethod;
import com.udacity.superduperdrive.cloudstorage.mapper.CredentialMapper;
import com.udacity.superduperdrive.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;

    public CredentialService(CredentialMapper credentialMapper) {
        this.credentialMapper = credentialMapper;
    }

    private String formatUsername(String username){
        return (UtilityMethod.IsStringNullOrEmpty(username, false) ? username.trim().toLowerCase() : username);
    }

    public void createCredential(String url, String username, String password, Integer userId){
        var secret = new SecrectCredentialDto(password);
        var newCredential = new Credential(url, formatUsername(username), secret.getSalt(), secret.getEncryptedPassword(), userId);

        credentialMapper.insert(newCredential);
    }

    public List<Credential> getCredentialsByUserId(Integer userId) {
        var a = credentialMapper.getCredentialsByUserId(userId);
        return a;
    }

    public Credential getCredentialById(Integer credentialId) {
        return credentialMapper.getCredentialById(credentialId);
    }

    public void deleteCredential(Integer credentialId){
        credentialMapper.deleteCredential(credentialId);
    }

    public void updateCredential(Integer credentialId, String newUserName, String url, String password){
        var secret = new SecrectCredentialDto(password);
        credentialMapper.updateCredential(credentialId, newUserName, url, secret.getSalt(), secret.getEncryptedPassword());
    }
}