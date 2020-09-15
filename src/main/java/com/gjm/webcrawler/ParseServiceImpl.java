package com.gjm.webcrawler;

import javafx.util.Pair;
import org.springframework.stereotype.Service;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ParseServiceImpl implements ParseService {
    private static final Pattern TITLE_PATTERN = Pattern.compile("<title>(.*?)</title>");

    @Override
    public Pair<String, String> parseWebPage(String url) {
        String content = "", title;
        try(BufferedInputStream inputStream = new BufferedInputStream(new URL(url).openStream())) {
            content = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch(IOException e) {
            e.printStackTrace();
        }
        Matcher matcher = TITLE_PATTERN.matcher(content);
        title = matcher.find() ? matcher.group(1) : "No title found";

        return new Pair<>(title, content);
    }
}
