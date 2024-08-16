package io.jokers.e_maryam.query;

public class RoleQuery {

    public static final String INSERT_ROLE_TO_USER_URL_QUERY = "INSERT INTO UserRoles (user_id, role_id) VALUES (:userId, :roleId)";
    public static final String SELECT_ROLE_BY_NAME_URL_QUERY = "SELECT * FROM Roles WHERE name = :name";
    public static final String SELECT_ROLE_BY_USER_ID_URL_QUERY = "SELECT r.id, r.name, r.permission FROM Roles r JOIN UserRoles ur ON ur.role_id = r.id JOIN Users u ON u.id = ur.user_id WHERE u.id = :user_id";
}
