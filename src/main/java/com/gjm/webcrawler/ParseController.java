package com.gjm.webcrawler;

import javafx.util.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class ParseController {
    private final ParseService parseService;

    @GetMapping("/parse")
    public String parse(@RequestParam("url") String url, Model model) {
        Pair<String, String> data = parseService.parseWebPage(url);

        model.addAttribute("title", data.getKey());
        model.addAttribute("content", data.getValue());
        return "result";
    }
}
