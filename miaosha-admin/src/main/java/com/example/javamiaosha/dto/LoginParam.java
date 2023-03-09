package com.example.javamiaosha.dto;

import com.example.javamiaosha.validator.IsMobile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class LoginParam {

    @NotNull
    @IsMobile
    private String mobile;
    @NotNull
    @Length(min = 32)
    private String password;
}
