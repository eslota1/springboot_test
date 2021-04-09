package com.example.springboot;

import com.example.utils.PostLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PostHistoryController {

    @Autowired
    private Environment env;

    @RequestMapping(value = "/")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/api")
    public String postString(@RequestParam("post_input_text") String inputTest,
                             Model model) {
        model.addAttribute("title", "Post Page");
        System.out.println(inputTest);
        String pathToLoggFile = env.getProperty("post.log.file");
        System.out.println(pathToLoggFile);
        PostLogger pl = new PostLogger(pathToLoggFile);
        pl.writeLine(inputTest);
        return "index";
    }


    @RequestMapping(value = "/history")
    public String getHistoryString(Model model) {
        String pathToLoggFile = env.getProperty("post.log.file");
        PostLogger pl = new PostLogger(pathToLoggFile);
        String history = pl.readHistory();
        model.addAttribute("history", history);
        model.addAttribute("newLineChar", '\n');
        return "history";
    }

    @RequestMapping(value = "/delete")
    public String deletePostString(@RequestParam("post_text") String deleteText, Model model) {
        model.addAttribute("title", "Delete Page");
        System.out.println(deleteText);
        String pathToLoggFile = env.getProperty("post.log.file");
        PostLogger pl = new PostLogger(pathToLoggFile);
        boolean deleted = pl.deleteInputText(deleteText);
        model.addAttribute("deleted", deleted);
        if (!deleteText.isEmpty()) model.addAttribute("deleteAttempted", true);
        System.out.println("deleted: " + deleted);
        return "delete";
    }

    @RequestMapping(value = "/search")
    public String searchPostString(@RequestParam("posts_text") String searchText, Model model) {
        model.addAttribute("title", "Search Page");
        System.out.println(searchText);
        String pathToLoggFile = env.getProperty("post.log.file");
        PostLogger pl = new PostLogger(pathToLoggFile);
        String foundline = pl.searchInputText(searchText);
        model.addAttribute("foundline", foundline);
        if (!searchText.isEmpty()) model.addAttribute("searchAttempted", true);
        return "search";
    }
}
