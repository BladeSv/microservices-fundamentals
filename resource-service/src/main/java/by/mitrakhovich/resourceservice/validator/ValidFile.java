package by.mitrakhovich.resourceservice.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Documented
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FileValidator.class)
public @interface ValidFile {
    String message() default "Validation failed or request body is invalid mp3";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
