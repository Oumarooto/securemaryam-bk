package io.jokers.e_maryam.dtomapper;


import io.jokers.e_maryam.domain.Role;
import io.jokers.e_maryam.domain.Users;
import io.jokers.e_maryam.dto.UserDTO;
import org.springframework.beans.BeanUtils;

public class UserDTOMapper {

    public static UserDTO fromUser(Users user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    public static UserDTO fromUser(Users user, Role role) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        userDTO.setRoleName(role.getName());
        userDTO.setPermissions(role.getPermission());
        return userDTO;
    }

    public static Users toUser(UserDTO userDTO) {
        Users user = new Users();
        BeanUtils.copyProperties(userDTO, user);
        return user;
    }
}
