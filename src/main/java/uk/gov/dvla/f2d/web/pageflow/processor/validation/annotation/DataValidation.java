package uk.gov.dvla.f2d.web.pageflow.processor.validation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * F2D - DataValidation annotation.
 * Supported properties -
 * min - minimum length of field, defaults to 0
 * max - maximum length of field, no defaults
 * regex - Regular Expression for field value to be checked against, defaults to .*
 * notNullOrEmpty - boolean -set to true if field should not be null or empty.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface DataValidation {
    /**
     * minimum length of field, defaults to 0.
     * @return int
     */
    int min() default 0;

    /**
     * minimum length of field, defaults to 0.
     * @return int
     */
    int max();

    /**
     * regex - Regular Expression for field value to be checked against, defaults to .*.
     * @return String
     */
    String regex() default ".*";

    /**
     * notNullOrEmpty - true if field should not be null or empty. Defaults to false.
     * @return boolean
     */
    boolean notNullOrEmpty() default false;
}
