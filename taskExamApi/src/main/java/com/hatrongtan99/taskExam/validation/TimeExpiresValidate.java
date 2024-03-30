package com.hatrongtan99.taskExam.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TimeExpiresValidate implements ConstraintValidator<ValidateTimeExpires, Integer> {
    @Override
    public void initialize(ValidateTimeExpires validItemCount) {
    }

    @Override
    public boolean isValid(Integer time, ConstraintValidatorContext constraintValidatorContext) {
        return time != null;
    }
}
