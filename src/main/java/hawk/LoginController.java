package hawk;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String index(Model model) {
        model.addAttribute("title", "Login title");
        return "login";
    }

}