package io.jokers.e_maryam.service;

import io.jokers.e_maryam.domain.Role;
import io.jokers.e_maryam.domain.Users;
import io.jokers.e_maryam.dto.UserDTO;

public interface RoleService {

    Role getRoleByUserId(Long userId);
}
