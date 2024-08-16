package io.jokers.e_maryam.repository.implementation;

import io.jokers.e_maryam.domain.Role;
import io.jokers.e_maryam.exception.ApiException;
import io.jokers.e_maryam.repository.RoleRepository;
import io.jokers.e_maryam.rowmapper.RoleRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

import static io.jokers.e_maryam.enumeration.RoleType.*;
import static io.jokers.e_maryam.query.RoleQuery.*;
import static java.util.Map.*;
import static java.util.Objects.*;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RoleRepositoryImpl implements RoleRepository<Role> {


    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Role create(Role data) {
        return null;
    }

    @Override
    public Collection<Role> list(int page, int pageSize) {
        return List.of();
    }

    @Override
    public Role get(Long id) {
        return null;
    }

    @Override
    public Role update(Role data) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }

    @Override
    public void addRoleToUser(Long userId, String roleName) {
        log.info("Adding role {} to user id: {}", roleName, userId);
        try {
            Role role = jdbcTemplate.queryForObject(SELECT_ROLE_BY_NAME_URL_QUERY, of("name", roleName), new RoleRowMapper());
            jdbcTemplate.update(INSERT_ROLE_TO_USER_URL_QUERY, of("userId", userId, "roleId", requireNonNull(role).getId()));
        } catch (EmptyResultDataAccessException e){
            throw new ApiException("No role found by name: " + ROLE_USER.name());
        } catch (Exception e){
            log.error(e.getMessage());
            throw new ApiException("An error occured. Please try again");
        }
    }

    @Override
    public Role getRoleByUserId(Long userId) {
        log.info("Fetching role by user id: {}", userId);
        try {
            return jdbcTemplate.queryForObject(SELECT_ROLE_BY_USER_ID_URL_QUERY, of("user_id", userId), new RoleRowMapper());
        } catch (EmptyResultDataAccessException e){
            throw new ApiException("No role found by name: " + ROLE_USER.name());
        } catch (Exception e){
            log.error(e.getMessage());
            throw new ApiException("An error occured. Please try again");
        }
    }

    @Override
    public Role getRoleByUserEmail(String email) {
        return null;
    }

    @Override
    public void updateUserRole(Long userId, String roleName) {

    }
}
