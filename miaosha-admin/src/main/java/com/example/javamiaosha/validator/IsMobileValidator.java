package com.example.javamiaosha.validator;

import com.example.javamiaosha.utils.ValidatorUtils;
import org.hibernate.validator.cfg.context.Constrainable;
import org.thymeleaf.util.StringUtils;
import org.yaml.snakeyaml.constructor.Construct;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsMobileValidator implements ConstraintValidator<IsMobile,String> {
    private boolean required=false;
    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required=constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(required){
            return ValidatorUtils.isMobile(s);
        }else {
            if(StringUtils.isEmpty(s)){
                return true;
            }else{
                return ValidatorUtils.isMobile(s);
            }
        }
    }
}
