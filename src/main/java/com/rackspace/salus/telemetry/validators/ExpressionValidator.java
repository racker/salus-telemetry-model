package com.rackspace.salus.telemetry.validators;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.rackspace.salus.telemetry.entities.EventEngineTaskParameters.Comparator;
import com.rackspace.salus.telemetry.validators.ExpressionValidator.ComparatorValidation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

/**
 * Validates on object creation whether the validator is one of the four acceptable options that we allow
 */
public class ExpressionValidator implements ConstraintValidator<ComparatorValidation, String> {

  @Override
  public boolean isValid(String comparator, ConstraintValidatorContext context) {
    return Comparator.valid(comparator);
  }

  @Target({FIELD, ANNOTATION_TYPE}) // class level constraint
  @Retention(RUNTIME)
  @Constraint(validatedBy = ExpressionValidator.class) // validator
  @Documented
  public @interface ComparatorValidation {
    String message() default "Valid comparators are: "
        + "==, !=, >, >=, <, <=, =~, !~"; // default error message

    Class<?>[] groups() default {}; // required

    Class<? extends Payload>[] payload() default {}; // required
  }
}
