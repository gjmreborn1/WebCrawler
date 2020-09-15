package com.gjm.webcrawler;

import javafx.util.Pair;

public interface ParseService {
    Pair<String, String> parseWebPage(String url);
}
