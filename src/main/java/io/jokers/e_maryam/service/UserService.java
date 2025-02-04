package io.jokers.e_maryam.service;

import io.jokers.e_maryam.domain.Users;
import io.jokers.e_maryam.dto.UserDTO;

public interface UserService {

    UserDTO createUser(Users user);
    UserDTO getUserByEmail(String email);
    void sendVerificationCode(UserDTO userDTO);
    UserDTO verifyCode(String email, String code);
    void resetPassword(String email);
    UserDTO verifyPasswordKey(String key);
    void renewPassword(String key, String password, String confirmPassword);
    UserDTO verifyAccountKey(String key);
}
