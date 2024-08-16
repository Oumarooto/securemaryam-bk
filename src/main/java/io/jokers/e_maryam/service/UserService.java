package io.jokers.e_maryam.service;

import io.jokers.e_maryam.domain.Users;
import io.jokers.e_maryam.dto.UserDTO;

public interface UserService {

    UserDTO createUser(Users user);
    UserDTO getUserByEmail(String email);

}
