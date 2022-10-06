package hawk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HiddenController {

    @GetMapping("/hidden")
    public String index(Model model) {
        model.addAttribute("title", "Hidden Page");
        return "hidden";
    }

    @GetMapping("/hidden/hidden2")
    public String hidden(Model model) {
        model.addAttribute("Rando hidden page");
        return "hidden2";
    }

    @GetMapping("/hidden/cypress")
    public String cypress(Model model) {
        model.addAttribute("title", "Hidden Page, found and tested with cypress tests");
        return "hidden";
    }

    @GetMapping("/hidden/selenium")
    public String selenium(Model model) {
        model.addAttribute("title", "Hidden Page, found and tested with selenium tests");
        return "hidden";
    }

    @GetMapping("/hidden/playwright")
    public String playwright(Model model) {
        model.addAttribute("title", "Hidden Page, found and tested with playwright tests");
        return "hidden";
    }
}
