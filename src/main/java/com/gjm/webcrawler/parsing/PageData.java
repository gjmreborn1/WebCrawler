package com.gjm.webcrawler.parsing;

import lombok.Data;

import java.util.List;

@Data
public class PageData {
    private String htmlContent;
    private String title;
    private String origin;
    private List<PageLink> links;
}
