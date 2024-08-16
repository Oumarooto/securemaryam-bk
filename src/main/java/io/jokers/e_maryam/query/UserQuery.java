package io.jokers.e_maryam.query;

/**

 */
public class UserQuery {

    public static final String INSERT_USER_URL_QUERY = "INSERT INTO Users (first_name, last_name, email, password) VALUES (:firstName, :lastName, :email, :password)";
    public static final String COUNT_USER_EMAIL_URL_QUERY = "SELECT COUNT(*) FROM Users WHERE email = :email";
    public static final String INSERT_ACCOUNT_VERIFICATION_URL_QUERY = "INSERT INTO AccountVerifications (user_id, url) VALUES (:userId, :url) ";
    public static final String  SELECT_USER_BY_EMAIL_URL_QUERY = "SELECT * FROM Users WHERE email = :email";
}
