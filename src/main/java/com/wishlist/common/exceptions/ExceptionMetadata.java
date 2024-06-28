package com.wishlist.common.exceptions;

import org.springframework.http.HttpStatus;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Target({ ElementType.TYPE })
public @interface ExceptionMetadata {

    HttpStatus httpStatus() default HttpStatus.INTERNAL_SERVER_ERROR;

}
