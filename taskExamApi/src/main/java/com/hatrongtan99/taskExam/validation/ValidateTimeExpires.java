package com.hatrongtan99.taskExam.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = TimeExpiresValidate.class)
public @interface ValidateTimeExpires {
    String message() default "Time expires must not be null";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
