package io.jokers.e_maryam.service.implementation;

import io.jokers.e_maryam.domain.Role;
import io.jokers.e_maryam.domain.Users;
import io.jokers.e_maryam.dto.UserDTO;
import io.jokers.e_maryam.repository.RoleRepository;
import io.jokers.e_maryam.repository.UserRepository;
import io.jokers.e_maryam.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static io.jokers.e_maryam.dtomapper.UserDTOMapper.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository<Users> userRepository;
    private final RoleRepository<Role> roleRepository;

    @Override
    public UserDTO createUser(Users user) {
        return mapToUserDTO(userRepository.create(user));
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        return mapToUserDTO(userRepository.getUserByEmail(email));
    }

    @Override
    public void sendVerificationCode(UserDTO userDTO) {
        userRepository.sendVerificationCode(userDTO);
    }

    @Override
    public UserDTO verifyCode(String email, String code) {
        return mapToUserDTO(userRepository.verifyCode(email, code));
    }

    private UserDTO mapToUserDTO(Users user){
        return fromUser(user, roleRepository.getRoleByUserId(user.getId()));
    }
}
