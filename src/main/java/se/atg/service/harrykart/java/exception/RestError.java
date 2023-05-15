package se.atg.service.harrykart.java.exception;

import org.springframework.http.HttpStatus;

public interface RestError {

    String error();

    HttpStatus httpStatus();

    String description();
}
