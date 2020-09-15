package com.gjm.webcrawler.parsing;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ParsingException extends RuntimeException {
    public ParsingException(String message) {
        super(message);
    }
}
