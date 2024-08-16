package io.jokers.e_maryam.service.implementation;

import io.jokers.e_maryam.domain.Role;
import io.jokers.e_maryam.domain.Users;
import io.jokers.e_maryam.dto.UserDTO;
import io.jokers.e_maryam.dtomapper.UserDTOMapper;
import io.jokers.e_maryam.repository.RoleRepository;
import io.jokers.e_maryam.repository.UserRepository;
import io.jokers.e_maryam.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository<Users> userRepository;
    private final RoleRepository<Role> roleRepository;

    @Override
    public UserDTO createUser(Users user) {
        return UserDTOMapper.fromUser(userRepository.create(user));
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        return UserDTOMapper.fromUser(userRepository.getUserByEmail(email));
    }
}
