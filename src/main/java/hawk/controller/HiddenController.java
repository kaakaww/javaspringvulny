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

    @GetMapping("/hidden/{id}")
    public String jwtAuth(Model model, @PathVariable("id") String id) {
        String title = "Random hidden page : " + id;
        model.addAttribute("title", title);
        return "hidden2";
    }
}
