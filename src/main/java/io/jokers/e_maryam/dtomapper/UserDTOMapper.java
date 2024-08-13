package io.jokers.e_maryam.dtomapper;


import io.jokers.e_maryam.domain.Users;
import io.jokers.e_maryam.dto.UserDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserDTOMapper {

    public static UserDTO fromUser(Users user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        return userDTO;
    }

    public static Users toUser(UserDTO userDTO) {
        Users user = new Users();
        BeanUtils.copyProperties(userDTO, user);
        return user;
    }
}
