package io.jokers.e_maryam.query;

public class RoleQuery {

    public static final String INSERT_ROLE_TO_USER_URL_QUERY = "INSERT INTO UserRoles (user_id, role_id) VALUES (:userId, :roleId)";
    public static final String SELECT_ROLE_BY_NAME_URL_QUERY = "SELECT * FROM Roles WHERE name = :name";
}
