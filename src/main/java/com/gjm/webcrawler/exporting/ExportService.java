package com.gjm.webcrawler.exporting;

import com.gjm.webcrawler.parsing.PageLink;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExportService {
    public String getFileContent(List<PageLink> links) {
        StringBuilder fileContent = new StringBuilder();

        fileContent.append("url;pageTitle\n");
        for(PageLink pageLink : links) {
            fileContent.append(pageLink.getUrl())
                    .append(';')
                    .append(pageLink.getPageTitle())
                    .append('\n');
        }

        return fileContent.toString();
    }

    public HttpHeaders getHeaders(String path) {
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + path);
        httpHeaders.add("Cache-Control", "no-cache, no-store, must-revalidate");
        httpHeaders.add("Pragma", "no-cache");
        httpHeaders.add("Expires", "0");

        return httpHeaders;
    }

    public MediaType getContentType() {
        return MediaType.parseMediaType("application/octet-stream");
    }
}
