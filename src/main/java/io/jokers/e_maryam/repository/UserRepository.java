package io.jokers.e_maryam.repository;

import io.jokers.e_maryam.domain.Users;
import io.jokers.e_maryam.dto.UserDTO;

import java.util.Collection;

public interface UserRepository<T extends Users>{

    /* Basic CRUD Operations */
    T create(T data);
    Collection<T> list(int page, int pageSize);
    T get(Long id);
    T update(T data);
    Boolean delete(Long id);

    /* More complex Operations */
    String getVerificationUrl(String key, String type);
    Users getUserByEmail(String email);
    void sendVerificationCode(UserDTO userDTO);

    Users verifyCode(String email, String code);
}
