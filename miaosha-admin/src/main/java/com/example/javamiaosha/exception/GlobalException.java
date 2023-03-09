package com.example.javamiaosha.exception;

import com.example.javamiaosha.dto.RespBeanEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Data
@NotNull
public class GlobalException extends RuntimeException {

    private RespBeanEnum respBeanEnum;
}
