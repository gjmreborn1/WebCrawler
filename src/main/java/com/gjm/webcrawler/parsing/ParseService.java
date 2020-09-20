package com.gjm.webcrawler.parsing;

import com.gjm.webcrawler.networking.HttpService;
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
            if(!isUrlValid(url)) {
                continue;
            }
            url = makeUrlUserFriendly(url);
            pageLink.setUrl(url);
            url = makeUrlFull(url, origin);

            pageLink.setPageTitle(parseWebPageInternal(url, false).getTitle());
            links.add(pageLink);
        }
        return links;
    }

    private boolean isUrlValid(String url) {
        return !(url == null || url.startsWith("tel") || url.startsWith("mailto"));
    }

    private String makeUrlUserFriendly(String url) {
        return url.replaceAll("\"", "")
                .trim();
    }

    private String makeUrlFull(String url, String origin) {
        if(url.startsWith("//")) {
            return "https:" + url;
        } else if(url.startsWith("/")) {
            return "http://" + origin + "/" + url;
        } else if(!url.startsWith("http")) {
            return "http://" + origin + "/" + url;
        } else {
            return url;
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

            List<PageLink> links = withLinks ?
                    getLinks(origin, content) : Collections.emptyList();
            pageData.setLinks(links);

            return pageData;
        } catch(MalformedURLException exc) {
            throw new ParsingException(exc.getMessage());
        }
    }
}
