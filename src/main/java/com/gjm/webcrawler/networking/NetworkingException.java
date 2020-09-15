package com.gjm.webcrawler.networking;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class NetworkingException extends RuntimeException {
    public NetworkingException(String message) {
        super(message);
    }
}
