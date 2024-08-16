package io.jokers.e_maryam.form;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginForm {
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
}
