package com.gjm.webcrawler.parsing;

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
        PageData pageData = parseService.parseWebPage(url);

        model.addAttribute("content", pageData.getHtmlContent());
        model.addAttribute("title", pageData.getTitle());
        model.addAttribute("links", pageData.getLinks());
        return "result";
    }
}
