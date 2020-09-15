package com.gjm.webcrawler.parsing;

import com.gjm.webcrawler.networking.HttpService;
import javafx.util.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ParseService {
    private static final Pattern TITLE_PATTERN = Pattern.compile("<title>(.*)</title>");
    private final HttpService httpService;

    public Pair<String, String> parseWebPage(String url) {
        try {
            String content = httpService.fetchWebPageContent(new URL(url));
            String title = parseWebPageTitle(content);

            return new Pair<>(title, content);
        } catch(MalformedURLException exc) {
            throw new ParsingException(exc.getMessage());
        }
    }

    private String parseWebPageTitle(String content) {
        Matcher matcher = TITLE_PATTERN.matcher(content);

        if(matcher.find()) {
            return matcher.group(1);
        } else {
            return "No title found";
        }
    }
}
