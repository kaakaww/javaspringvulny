package hawk.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("title", "StackHawk Java Vulny Application");
        return "index";
    }

    @GetMapping("/jwt-auth")
    public String jwtAuth(Model model) {
        model.addAttribute("title", "JWT Auth");
        return "jwt-auth";
    }

    @GetMapping("/token-auth")
    public String tokenAuth(Model model) {
        model.addAttribute("title", "Token Auth");
        return "token-auth";
    }

    @GetMapping("/basic-auth")
    public String basicAuth(Model model) {
        model.addAttribute("title", "Basic Auth");
        return "basic-auth";
    }
}