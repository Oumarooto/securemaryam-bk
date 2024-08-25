package io.jokers.e_maryam.utils;

import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jokers.e_maryam.domain.HttpResponse;
import io.jokers.e_maryam.exception.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.OutputStream;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
public class ExceptionUtils {

    public static void processError(HttpServletRequest request,
                                    HttpServletResponse response,
                                    Exception exception){

        if (exception instanceof ApiException || exception instanceof DisabledException
                || exception instanceof LockedException || exception instanceof InvalidClaimException
                || exception instanceof TokenExpiredException || exception instanceof BadCredentialsException
                || exception instanceof UsernameNotFoundException || exception instanceof AccountExpiredException
                || exception instanceof CredentialsExpiredException || exception instanceof InsufficientAuthenticationException
                || exception instanceof AccessDeniedException){
            HttpResponse httpResponse = getHttpResponse(response, exception.getMessage(), BAD_REQUEST);
            writeResponse(response, httpResponse);
        }else {
            HttpResponse httpResponse = getHttpResponse(response, "An error occurred. Please try again !!!", INTERNAL_SERVER_ERROR);
            writeResponse(response, httpResponse);
        }

    }

    private static void writeResponse(HttpServletResponse response,
                                      HttpResponse httpResponse) {
        OutputStream out;
        try{
            out = response.getOutputStream();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(out, httpResponse);
            out.flush();
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    private static HttpResponse getHttpResponse(HttpServletResponse response,
                                                String message,
                                                HttpStatus httpStatus){
        HttpResponse httpResponse = HttpResponse.builder()
                .timeStamp(now().toString())
                .reason(message)
                .status(httpStatus)
                .statusCode(httpStatus.value())
                .build();
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setStatus(httpStatus.value());
        return httpResponse;
    }

}
