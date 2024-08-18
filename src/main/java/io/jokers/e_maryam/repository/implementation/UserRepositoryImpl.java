package io.jokers.e_maryam.repository.implementation;

import io.jokers.e_maryam.domain.Role;
import io.jokers.e_maryam.domain.UserPrincipal;
import io.jokers.e_maryam.domain.Users;
import io.jokers.e_maryam.dto.UserDTO;
import io.jokers.e_maryam.exception.ApiException;
import io.jokers.e_maryam.repository.RoleRepository;
import io.jokers.e_maryam.repository.UserRepository;
import io.jokers.e_maryam.rowmapper.UserRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.*;

import static io.jokers.e_maryam.enumeration.RoleType.*;
import static io.jokers.e_maryam.enumeration.VerificationType.*;
import static io.jokers.e_maryam.query.UserQuery.*;
import static java.lang.System.*;
import static java.util.Map.*;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.lang3.time.DateUtils.addDays;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserRepositoryImpl implements UserRepository<Users>, UserDetailsService {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final RoleRepository<Role> roleRepository;
    private final BCryptPasswordEncoder encoder;
    private final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";

    @Override
    public Users create(Users user) {
        // Check the email is unique
        if(getEmailCount(user.getEmail().trim().toLowerCase()) > 0)
            throw new ApiException("Email already in use. Please use a different email and try again");
        // Save new user
        try{
            KeyHolder holder = new GeneratedKeyHolder();
            SqlParameterSource parameters = getSqlParameterSource(user);
            jdbcTemplate.update(INSERT_USER_QUERY ,parameters, holder);
            user.setId(requireNonNull(holder.getKey()).longValue());
            // Add role to the user
            roleRepository.addRoleToUser(user.getId(), ROLE_USER.name());
            // Send verification URL
            String verificationUrl = getVerificationUrl(UUID.randomUUID().toString(), ACCOUNT.getType());
            // Save URL in verification table
            jdbcTemplate.update(INSERT_ACCOUNT_VERIFICATION_URL_QUERY, of("userId",user.getId(), "url", verificationUrl));
            // Send email to user with verification URL
            //emailService.sendVerificationUrl(user.getFirstName(), user.getEmail(), verificationUrl, ACCOUNT);
            user.setEnabled(false);
            user.setNotLocked(true);
            // Return the newly created user
            // If any errors, throw exception with proper message

            return user;
        } catch (Exception e){
            log.error(e.getMessage());
            throw new ApiException("An error occured. Please try again");
        }
    }

    public String getVerificationUrl(String key, String type){
        return ServletUriComponentsBuilder.fromCurrentContextPath().path("/user/verify/" + type + "/" + key).toUriString();
    }

    @Override
    public Collection<Users> list(int page, int pageSize) {
        return List.of();
    }

    @Override
    public Users get(Long id) {
        return null;
    }

    @Override
    public Users update(Users data) {
        return null;
    }

    @Override
    public Boolean delete(Long id) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users user = getUserByEmail(email);
        if (user == null) {
            log.error("User not found in the database");
            throw new UsernameNotFoundException("User not found in the database");
        }else {
            log.info("User found in the database : {} ",email);
            return new UserPrincipal(user, roleRepository.getRoleByUserId(user.getId()).getPermission());
        }
    }

    @Override
    public Users getUserByEmail(String email) {
        try {
            return jdbcTemplate.queryForObject(SELECT_USER_BY_EMAIL_QUERY, of("email",email), new UserRowMapper());
        }catch (EmptyResultDataAccessException e){
            log.error(e.getMessage());
            throw new ApiException("No User found by email : " + email);
        }catch (Exception e){
            log.error(e.getMessage());
            throw new ApiException("An error occured. Please try again");
        }
    }

    @Override
    public void sendVerificationCode(UserDTO userDTO) {
        String expirationDate = DateFormatUtils.format(addDays(new Date(),1), DATE_FORMAT);
        String verificationCode = RandomStringUtils.randomAlphabetic(8).toUpperCase();
        try {
           jdbcTemplate.update(DELETE_VERIFICATION_CODE_BY_USER_ID_QUERY, of("userId",userDTO.getId()));
           jdbcTemplate.update(INSERT_VERIFICATION_CODE_QUERY, of("userId", userDTO.getId(), "code", verificationCode, "expiration_date", expirationDate));
           log.info("Verification Code sent : {}", verificationCode);
           //sendSMS(userDTO.getPhone(), "From : Secure Maryam\n Verification code : " + verificationCode);
        } catch (EmptyResultDataAccessException e) {
            log.error(e.getMessage());
            throw new ApiException("");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ApiException("An error occurred. Please try again");
        }
    }

    @Override
    public Users verifyCode(String email, String code) {
        if (Boolean.TRUE.equals(isVerificationCodeExpired(code)))
            throw new ApiException("This code has expired. Please login again.");
        try {
            Users userByCode = jdbcTemplate.queryForObject(SELECT_USER_BY_USER_CODE_QUERY, of("code", code), new UserRowMapper());
            Users userByEmail = jdbcTemplate.queryForObject(SELECT_USER_BY_EMAIL_QUERY, of("email", email), new UserRowMapper());
            if (requireNonNull(userByCode).getEmail().equalsIgnoreCase(requireNonNull(userByEmail).getEmail())){
                jdbcTemplate.update(DELETE_CODE, of("code",code));
                return userByCode;
            }else {
                throw new ApiException("Code is invalid. Please try again !!!");
            }
        } catch (EmptyResultDataAccessException exception) {
            log.error(exception.getMessage());
            throw new ApiException("Could not find record.");
        } catch (Exception exception){
            log.error(exception.getMessage());
            throw new ApiException("An error occurred. Please try again !!!");
        }
    }

    private Boolean isVerificationCodeExpired(String code) {
        try {
            return jdbcTemplate.queryForObject(SELECT_EXPIRATION_CODE_QUERY, of("code", code), Boolean.class);
        } catch (EmptyResultDataAccessException e){
            log.error(e.getMessage());
            throw new ApiException("This code is not valid. Please log in again");
        } catch (Exception e){
            log.error(e.getMessage());
            throw new ApiException("An error occurred. Please try again !!!");
        }
    }

    private Integer getEmailCount(String email) {
        return jdbcTemplate.queryForObject(COUNT_USER_EMAIL_QUERY, of("email",email), Integer.class);
    }

    private SqlParameterSource getSqlParameterSource(Users user) {
        return new MapSqlParameterSource()
                .addValue("firstName", user.getFirstName())
                .addValue("lastName", user.getLastName())
                .addValue("email", user.getEmail())
                .addValue("password", encoder.encode(user.getPassword()));
    }

}
