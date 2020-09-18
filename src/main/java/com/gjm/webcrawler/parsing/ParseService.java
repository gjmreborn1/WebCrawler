package com.gjm.webcrawler.parsing;

import com.gjm.webcrawler.networking.HttpService;
import javafx.util.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ParseService {
    private static final Pattern TITLE_PATTERN = Pattern.compile("<title>(.*)</title>");
    private static final Pattern LINK_PATTERN = Pattern.compile("\\s*<a.*href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))", Pattern.CASE_INSENSITIVE);
    private final HttpService httpService;

    public PageData parseWebPage(String url) {
        return parseWebPageInternal(url, true);
    }

    public List<PageLink> getLinks(String origin, String content) {
        Matcher matcher = LINK_PATTERN.matcher(content);
        List<PageLink> links = new ArrayList<>();

        while(matcher.find()) {
            PageLink pageLink = new PageLink();

            String url = matcher.group(2);
            if(url == null || url.startsWith("tel") || url.startsWith("mailto")) {
                continue;
            }
            url = url.replaceAll("\"", "");
            pageLink.setUrl(url);
            if(url.startsWith("/") || !url.startsWith("http")) {
                url = "http://" + origin + "/" + url;
            }
            System.out.println(url);

            // TODO: Refactor

            PageData pageData = parseWebPageInternal(url, false);
            pageLink.setPageTitle(pageData.getTitle());

            links.add(pageLink);
        }
        return links;
    }

    private String parseWebPageTitle(String content) {
        Matcher matcher = TITLE_PATTERN.matcher(content);

        if(matcher.find()) {
            return matcher.group(1);
        } else {
            return "No title found";
        }
    }

    private PageData parseWebPageInternal(String url, boolean withLinks) {
        PageData pageData = new PageData();

        try {
            URL urlObject = new URL(url);
            String content = httpService.fetchWebPageContent(urlObject);
            pageData.setHtmlContent(content);

            String title = parseWebPageTitle(content);
            pageData.setTitle(title);

            String origin = urlObject.getHost();
            pageData.setOrigin(origin);

            List<PageLink> links = withLinks ? getLinks(origin, content) : Collections.emptyList();
            pageData.setLinks(links);

            return pageData;
        } catch(MalformedURLException exc) {
            throw new ParsingException(exc.getMessage());
        }
    }
}
