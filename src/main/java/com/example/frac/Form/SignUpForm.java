package com.example.frac.Form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpForm {
    @NotBlank(message = "아이디는 필수입니다.")
    @Size(min = 3, max = 20, message = "아이디는 3~20자여야 합니다.")
    public String username;
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자입니다.")

    public String password;
}
