package com.udacity.superduperdrive.cloudstorage.service;


import com.udacity.superduperdrive.cloudstorage.dto.SecrectCredentialDto;
import com.udacity.superduperdrive.cloudstorage.dto.UserChangePasswordDto;
import com.udacity.superduperdrive.cloudstorage.helper.UtilityMethod;
import com.udacity.superduperdrive.cloudstorage.mapper.UserMapper;
import com.udacity.superduperdrive.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {
    private final UserMapper userMapper;
    private final HashService hashService;

    public UserService(UserMapper userMapper, HashService hashService) {
        this.userMapper = userMapper;
        this.hashService = hashService;
    }

    public boolean isUsernameExisted(String username) {
        return !(userMapper.getUserByUsername(username.trim().toLowerCase()) == null);
    }

    private void FormatUser(User user){
        user.setFirstName(UtilityMethod.IsStringNullOrEmpty(user.getFirstName(), false) ? user.getFirstName().trim() : user.getFirstName());
        user.setLastName(UtilityMethod.IsStringNullOrEmpty(user.getLastName(), false) ? user.getLastName().trim() : user.getLastName());
        user.setUsername(UtilityMethod.IsStringNullOrEmpty(user.getUsername(), false) ? user.getUsername().trim().toLowerCase() : user.getUsername());
    }

    public int createUser(User user) {
        var secret = new SecrectCredentialDto(user.getPassword());
        FormatUser(user);
        return userMapper.insert(new User(user.getUsername(), secret.getSalt(), hashService.getHashedValue(user.getPassword(), secret.getSalt()), user.getFirstName(), user.getLastName()));
    }

    public boolean changeUserPassword(UserChangePasswordDto userChangePasswordDto) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(userChangePasswordDto.getNewPassword(), encodedSalt);
        return userMapper.changePassword(userChangePasswordDto.getUsername(), hashedPassword, encodedSalt);
    }

    public User getUser(String username) {
        return userMapper.getUserByUsername(username);
    }

    public Integer getUserIdByUserName(String username) {
        return userMapper.getUserIdByUsername(username);
    }
}