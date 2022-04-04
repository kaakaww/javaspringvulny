package hawk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HiddenController {

    @GetMapping("/hidden")
    public String index(Model model) {
        model.addAttribute("title", "Hidden Page");
        return "hidden";
    }

    @GetMapping("/hidden/hidden2")
    public String jwtAuth(Model model) {
        model.addAttribute("title", "Rando hidden page");
        return "hidden2";
    }
}
