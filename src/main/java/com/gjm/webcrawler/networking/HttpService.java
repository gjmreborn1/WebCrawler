package com.gjm.webcrawler.networking;

import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
public class HttpService {
    public String fetchWebPageContent(URL url) {
        try(BufferedInputStream inputStream = new BufferedInputStream(url.openStream())) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch(IOException exc) {
            throw new NetworkingException(exc.getMessage());
        }
    }
}
