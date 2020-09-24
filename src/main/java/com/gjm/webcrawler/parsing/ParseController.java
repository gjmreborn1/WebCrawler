package com.gjm.webcrawler.parsing;

import com.gjm.webcrawler.exporting.ExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@SessionAttributes("links")
public class ParseController {
    private final ParseService parseService;
    private final ExportService exportService;

    @ModelAttribute("links")
    public List<PageLink> getPageLinks() {
        return new ArrayList<>();
    }

    @GetMapping("/parse")
    public String parse(@RequestParam("url") String url,
                        @ModelAttribute("links") List<PageLink> pageLinks, Model model) {
        PageData pageData = parseService.parseWebPage(url);

        model.addAttribute("content", pageData.getHtmlContent());
        model.addAttribute("title", pageData.getTitle());
        pageLinks.clear();
        pageLinks.addAll(pageData.getLinks());
        return "result";
    }

    @GetMapping("/export")
    public ResponseEntity<String> export(@RequestParam("path") String path,
                                         @ModelAttribute("links") List<PageLink> links) {
        String fileContent = exportService.getFileContent(links);

        return ResponseEntity.ok()
                .headers(exportService.getHeaders(path))
                .contentLength(fileContent.length())
                .contentType(exportService.getContentType())
                .body(fileContent);
    }
}
